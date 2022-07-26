@ECHO OFF
aws cloudformation deploy --capabilities CAPABILITY_NAMED_IAM --template-file CFLambdaFiles.json --stack-name NFCBPSKDeploymentSourcesTemp %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
aws s3 cp LambdaFiles s3://temp-nfcbpsk-s3-bucket-jar/ --recursive %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
aws cloudformation deploy --capabilities CAPABILITY_NAMED_IAM --template-file CFTables.json --stack-name NFCBPSKDeploymentTables %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
aws dynamodb put-item --table-name Counter --item file://TableData/Counter.json %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
aws dynamodb put-item --table-name Keystore --item file://TableData/Keystore.json %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
aws dynamodb put-item --table-name Product --item file://TableData/Product.json %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 

powershell -Command  "$date = 'nfcbpsk'+(Get-Date).tostring('yyMMddHHmm');(gc CFServerless.json) -replace 'domain-name', $date | Out-File -encoding ASCII TempCFServerless.json" 
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
)


powershell -Command  "$date = 'nfcbpsk-s3-bucket-'+(Get-Date).tostring('yyMMddHHmm'); (gc TempCFServerless.json) -replace 'bucket-name', $date | Out-File -encoding ASCII TempCFServerless.json;(gc CFLandingPage.json) -replace 'bucket-name', $date | Out-File -encoding ASCII TempCFLandingPage.json; (gc LandingPageScript.template) -replace 'bucket-name', $date | Out-File -encoding ASCII TempLandingPageScript.bat" 
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
)

aws cloudformation deploy --capabilities CAPABILITY_NAMED_IAM --template-file TempCFServerless.json --stack-name NFCBPSKDeploymentTemplate %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
powershell -Command  "$output = aws apigateway get-rest-apis  %1 %2 | ConvertFrom-Json; foreach ($element in $output.items) { if($element.name -eq 'brandprotect'){(gc WebData\script-template.js) -replace '{api-id}', $element.id | Out-File -encoding ASCII WebData\script.js  }  }" 
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
)
powershell -Command  "$region = aws configure get region  %1 %2;  (gc WebData\script.js) -replace '{region-code}', $region | Out-File -encoding ASCII WebData\script.js" 
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
)

 
aws cloudformation deploy --capabilities CAPABILITY_NAMED_IAM --template-file TempCFLandingPage.json --stack-name NFCBPSKDeploymentSources %1 %2
IF %ERRORLEVEL% NEQ 0 ( 
   cmd /k
) 
Rem Deleting the temporary files after use
del TempCFServerless.json
del TempCFLandingPage.json

Rem Calling TempLandingPageScript.bat file to setup the landing page 
CMD /c TempLandingPageScript.bat SCRIPT %1 %2
del TempLandingPageScript.bat