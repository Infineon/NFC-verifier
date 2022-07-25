/*
 * Copyright 2022 Infineon Technologies AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import Foundation

/**
 * Manages the commands execution sequence for performing brand verification
 */
class BrandVerificationManager {
	// Value must be greater than or equal to 128 byte to get product and service info 
	// during the first read operation
    static let NFC4TC_MLE:UInt8 = 0xE6
	
    static let BP_OFFSET_PRODUCT_FILE:UInt16 = 0x8100
    static let BP_OFFSET_SERVICE_FILE:UInt16 =  0x8200
    static let SUCCESS_DATA = Data([0x90,0x00])
    
	static let timeLogger = TimeLogger()
    static let totalTimeLogger = TimeLogger()
	static let logger:FileLogger = LoggerFactory.getLogger(logger: .File) as! FileLogger
	
    static var productInfoData:Data?
    static var serviceInfoData:Data?
    static var nfcEventCallback: NFCListener?
    static var bpPayload: BrandProtectionPayload?
    static var uriRecord: URIRecord?
    static var authenticationData: Data?
    static var maResponse: MutAuthGenerationResponse?
    static var nfcChannel: NFCChannel?

    /**
     * Initializes the instance with NFC handlers
     *  - Parameter nfcChannel: NFC handler to be used for communication
	 *  - Parameter nfcEventCallback: Callback method for handling NFC events
     */
    static func performBrandVerification(nfcChannel: NFCChannel?, nfcEventCallback: NFCListener?) {
        self.nfcChannel = nfcChannel
        self.nfcEventCallback = nfcEventCallback
		
        //******** TAG: READ NDEF Message **************
        handleReadNDEFMessage()
    }
	
    //******** TAG Step 1: READ NDEF Message **************
    /**
     * Triggers the NDEF message read operation and handles the response
     */
    static func handleReadNDEFMessage() {
        do {            
            logger.resetLog()
            timeLogger.start()
            totalTimeLogger.start()
            try readNDEFMessage(
                onResponseMessage: { ndefMessage in                    
                    if(ndefMessage.getNdefRecords().count > 1 ){
                        parseNDEFMessage(ndefMessage)
                    } else {
                        //******** TAG: DID NOT Find Two NDEF Records **************
                        nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_NDEF_MESSAGE)
                    }
                                         
                }, onResponseError: { errorMessage in
                    //******** TAG: READ NDEF Error Message **************
                    nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: errorMessage)                   
                })        
        }
        catch {
            //******** TAG: READ NDEF Exception Message **************
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_NDEF_MESSAGE)
        }
    }
    
    /**
     * Parses the records in the NDEF message and extracts the brand protection record.
	 * If brand protection record is available, triggers the next operation to read the 
	 * chip ID from the tag.
	 * 
     *  - Parameter ndefMessage: NDEF message from the tag
     */
    static func parseNDEFMessage(_ ndefMessage: NDEFMessage) {
        do {
            var i = 0
            bpPayload = nil
            while i<ndefMessage.getNdefRecords().count {
                let abstractRecord = try NFCFactory.decryptRecord(ndefRecord: ndefMessage.getNdefRecords()[i])
                if abstractRecord is URIRecord {
                    uriRecord = (abstractRecord as! URIRecord)
                }
                if abstractRecord is ExternalTypeRecord {
                    let record = abstractRecord as! ExternalTypeRecord
                    let bpLocalPayload = try BrandProtectionPayload(payload: record.getData()!)
                    if( bpLocalPayload.keyLabel != 0 ) {
                        bpPayload = bpLocalPayload
                    }
                }
                i += 1
            }
            if(bpPayload != nil ){
				// Brand protection record is available, 
				// therefore perform the next step for brand verification: Read chip ID
                getChipUniqueID()
            } else {
                nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_PARSING_NDEF_RECORD)
            }
        }catch {
            //******** TAG: Unable To Parse NDEF Records **************
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_PARSING_NDEF_RECORD)
        }
    }
        
    //******** TAG Step 2: READ Chip Unique ID From TAG **************
    /**
     * Reads the chip unique ID from the tag. 
     */
    static func getChipUniqueID() {        
        //******** TAG: Get ChipUniqueID **************
        do {
            let CMD_SELECT_IDINFO = try APDUCommandBuilder.buildSelectFileCommand(p1:0x00, p2:0x00, data: Data(_:[0x2F, 0xF7]), le: 0x00)
            let CMD_READ_CHIPID = try APDUCommandBuilder.buildReadBinaryCommand(offset: 0x0008, le: 0x10)
            
			// Select the EF.ID_INFO File
            nfcChannel?.Transmit(command: CMD_SELECT_IDINFO, onResponseEvent: { apduResponse in
               if(apduResponse.isSuccessSW()) {
                  
				  // Read the EF.ID_INFO File
                  nfcChannel?.Transmit(command: CMD_READ_CHIPID, onResponseEvent: { apduResponse in
                       if(apduResponse.isSuccessSW()){
						   
						   // Chip ID is read successfully. Proceed with the next step
						   // of brand verification: Get challenge from the tag
                           getChallenge(chipID: apduResponse.data!)                       
                       }
                       else {
                           //******** TAG: Get ChipUniqueID  Error **************
                            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_COMMAND_READ_CHIP_ID)
                       }                        
                    })
               }
               else {
                   //******** TAG: Get ChipUniqueID  Error **************
                    nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_COMMAND_SELECT_CHIP_ID)
               }                
            })      
        } catch {
            //******** TAG: READ Chip Unique ID Exception  **************
             nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_CHIP_ID_MESSAGE)
        }        
    }
        
    //******** TAG Step 3: Get Challenge From TAG **************
    /**
     * Reads the challenge from the tag. 
	 * - Parameter chipID: Chip unique ID from the tag
     */
    static func getChallenge(chipID: Data) {
        //******** TAG: Get Challenge **************
        do {
            let CMD_GET_CHALLENGE = try APDUCommandBuilder.buildCommand(byte: Data(_: [0x00, 0x84, 0x00, 0x00, 0x16]))
            // Command To Get Challenge
            nfcChannel?.Transmit(command: CMD_GET_CHALLENGE, onResponseEvent: { apduResponse in
               if(apduResponse.isSuccessSW()){
                   //******** Cloud: Get Challenge  **************
                   timeLogger.log(tag: "Step 1 Time")
                   totalTimeLogger.log(tag: "Total Time")
                   timeLogger.start()
				   
				   // Challenge from tag is obtained successfully. Proceed with the next step
				   // of brand verification: Generate mutual auth command data in cloud
                   mutualAuthGenerateAPIRequest(chipID: chipID, challenge: apduResponse.data!)
               }
               else {
                   //******** TAG: Get Challenge Error **************
                   nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_COMMAND_GET_CHALLENGE)
               }                
            })
        } catch {
            //******** TAG: Get Challenge Exception  **************
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_CHALLENGE_MESSAGE)
        }
    }
    
    //******** CLOUD Step 4: MA Generate API Request **************
    /**
     * Generates the mutual authentication command data by communicating with 
     * the brand verification cloud service.	 
	 * - Parameter chipID: Chip unique ID from the tag
	 * - Parameter challenge: Challenge received from the tag
     */
    static func mutualAuthGenerateAPIRequest(chipID: Data, challenge: Data ){
        BrandVerificationService.mutAuthGenerateAPI(verificationUrl: bpPayload!.getVerificationURL(), keyLabel: (bpPayload?.keyLabel.data.hexEncodedString())!,  chipID: chipID,challenge: challenge, onSuccess: { (apiData) in
            maResponse = apiData
			
			// Mutual authentication command data is obtained successfully. 
			// Proceed with the next step of brand verification: Mutual authentication
			// with the tag
            mutualAuth()
        }, onFailure: { error in
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Warning, errorMessage: error)
        })        
    }
    
    //******** TAG Step 5: Handle the mutual authentication  **************
    /**
     * Performs mutual authentication with the tag 
     */
    static func mutualAuth() {
        do {
            //
            //******** TAG: Mutual authentication **************
            
            // Construct mutual authenticate command using the command data 
			// received from the cloud service
            var mutualAuthCommand = Data(_:[0x00,0x82,0x00,0x02,0x26])
            mutualAuthCommand.append(maResponse!.CommandData.hexDecodedData())
			// Append the expected length
            mutualAuthCommand.append(0x10)
            let mutualAuthCommandAPDU = try APDUCommandBuilder.buildCommand(byte: mutualAuthCommand)
            nfcChannel!.Transmit(command: mutualAuthCommandAPDU, onResponseEvent: { apduResponse in
               if(apduResponse.isSuccessSW()){
                   authenticationData = apduResponse.data;
                   timeLogger.log(tag: "Step 2 Time")
                   totalTimeLogger.log(tag: "Total Time")
				   
				   // Mutual authentication is performed successfully with the tag. 
				   // The response data will be verified in the later steps with the cloud service.
				   // Proceed with the next step to read product information file
                   readProductInfo()
               }
               else {
                   //******** TAG: Mutual authentication Error  **************
                   nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_COMMAND_MUTUAL_AUTH)
               }                
            })            
        } catch {
            //******** TAG: Execution of MA command Exception  **************
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_MA)
        }
    }
	
	//******** TAG Step 6: Read the Product Information from TAG  **************
	/**
     * Reads the product information file from the tag 
     */
    static func readProductInfo() {
        //******** TAG: READ PRODUCT INFORMATION **************

        timeLogger.start()
        do {
            try self.nfcChannel!.readBinary(readWithSFID: true,le: NFC4TC_MLE, totalBytesToRead: 0, prevReceivedData: Data(), offset: BP_OFFSET_PRODUCT_FILE, onSuccess:  { productData in
                productInfoData = productData
                var index = 0;
				
				// Product information is read successfully. 
				// Proceed with the next step:
				// If the tag has service information file (Profiles B10 and B20),
				// read the service information file, else skip reading service information
                if(productInfoData != nil &&  ProductInfoDecoder.parseProductProfileType(payload: productInfoData!, index: &index) != ProfileType.A10) {
                   readServiceInfo()
                } else {                           
                   nfcEventCallback?.onNFCCommunicationSuccess(message: "", productInfoData: productInfoData, serviceInfoData: serviceInfoData, uriRecord: uriRecord )
                }
            } ,onError: { error in
                //******** TAG: READ PRODUCT INFORMATION ERROR **************
                nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: error)                                                           
            }, calculateRemainingBytes:ProductInfoDecoder.calculateProductInfoSize)
        }catch {
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_SERVICE_INFO)
        }          
    }
	
    //******** TAG Step 7: Read the Service Information from TAG  **************
    /**
     * Reads the service information file from the tag 
     */
	static func readServiceInfo() {
        
        do {
            try self.nfcChannel!.readBinary(readWithSFID: true,le: NFC4TC_MLE, totalBytesToRead: 0, prevReceivedData: Data(), offset: BP_OFFSET_SERVICE_FILE, onSuccess:  { serviceByteData in
                serviceInfoData = serviceByteData
				
				// Product information is read successfully. 
				// Proceed with the next step: Verify the mutual auth response data
				// with the cloud service. 
				// Since this operation doesn't require the user to keep scanning
				// the tag, indicate in the UI that NFC operation is complete 
				// and verifying in the cloud				
                nfcEventCallback?.onNFCCommunicationSuccess(message: "",productInfoData: productInfoData!, serviceInfoData: serviceInfoData,uriRecord: uriRecord )                
            } ,onError: { error in
                //******** TAG: READ SERVICE INFORMATION ERROR **************
                nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: error)                                                      
            }, calculateRemainingBytes:ServiceInfoDecoder.calculateServiceInfoSize)

        }catch {
            nfcEventCallback?.onNFCCommunicationError(callbackType: .Error, errorMessage: ERR_MSG_READ_SERVICE_INFO)
        }
    }
    
    //******** TAG Step 8: Mutual authentication response data verification **************
    /**
     * Verifies the mutual authentication response data by communicating with 
     * the brand verification cloud service.	 
	 * - Parameter nfcEventCallback: NFC event handler callback
     */
    static func mutualAuthVerifyAPIRequest(nfcEventCallback: @escaping (Bool,String) -> Void) {
        var authCommandData = authenticationData!
        authCommandData.append(Data(SUCCESS_DATA))
        BrandVerificationService.mutAuthVerifyAPI(verificationUrl: bpPayload!.getVerificationURL(), mutualAuthResponse:authCommandData, sessionID : maResponse!.SessionID, onSuccess: { (verifyData) in
            nfcEventCallback(true, verifyData.AuthResult)
        }, onFailure: { error in
            nfcEventCallback(false, error)
        })
    }
    
    /**
     * Reads the NDEF message from the tag.	 
	 * - Parameter onResponseMessage: Chip unique ID from the tag
     */
    static func readNDEFMessage(onResponseMessage: @escaping (NDEFMessage)  -> Void, onResponseError: @escaping (String)  -> Void) throws {
        
        // Command to select NDEF application (with AID of T4T mapping version-1 application)
        let CMD_SELECT  = try APDUCommandBuilder.buildSelectFileCommand(p1:0x04,p2:0x00,data: Data(_:[ 0xD2,  0x76,  0x00,  0x00,  0x85,  0x01,  0x00]), le: 0x00)
        
        // Command to select CC file
        let CMD_SELECT_CC = try APDUCommandBuilder.buildSelectFileCommand(p1: 0x00, p2: 0x00 ,data: Data(_:[0xE1,  0x03]),le: nil)
        
        // Command to read CC file (15 bytes)
        let CMD_READ_BINARY = try APDUCommandBuilder.buildReadBinaryCommand(offset: 0x0000, le: 0x0F)
		
		// Transmit the commands
        nfcChannel?.Transmit(command: CMD_SELECT, onResponseEvent: { apduResponse in            		
            if(apduResponse.isSuccessSW()){                
			
                nfcChannel!.Transmit(command: CMD_SELECT_CC, onResponseEvent: { apduResponse in
                   if(apduResponse.isSuccessSW()) {                 
				   
                        nfcChannel!.Transmit(command: CMD_READ_BINARY, onResponseEvent: { apduResponse in
                            if(apduResponse.isSuccessSW()){
							
                             do {
                                 // CC file is read successfully. Parse the CC file. 
                                 let ccFile = CCFile.parseCCFile(data: apduResponse.data!)
                                 
                                 if(ccFile!.validate(onResponseError:onResponseError)) {
                                    
									// Command to select the NDEF file
                                    let CMD_SELECT_NDEF = try APDUCommandBuilder.buildSelectFileCommand(p1:0x00,p2:0x00,data: Data(_:(ccFile?.ndefFileCtrlData?.fileID)!), le: nil)

                                    nfcChannel!.Transmit(command: CMD_SELECT_NDEF, onResponseEvent: { apduResponse in
                                        if(apduResponse.isSuccessSW()) {
                                            
                                             do {
												 // Read the NDEF file in a loop
                                                 try nfcChannel?.readBinary(readWithSFID: false, le: UInt8((ccFile?.maxLe)!),totalBytesToRead: 0 , prevReceivedData: Data(), offset: 0x0000,onSuccess: {  ndefData in
                                                    
													// Parse the NDEF message
                                                    do{
                                                        let ndefMessageLength = Utils.byteToShort(data: (ndefData.prefix(2)))
                                                        let ndefBytes = ndefData.subdata(in: 2..<ndefMessageLength+2)
                                                        let bpRecordMessage = try NDEFMessageDecoder.decryptor.decrypt(stream: ndefBytes)
                                                        onResponseMessage(bpRecordMessage)
                                                    }
                                                    catch {
                                                        onResponseError(ERR_MSG_EXCEPTION_NDEF_PARSING)
                                                    }
                                                 },onError: {  error in
                                                     onResponseError(ERR_MSG_COMMAND_READ_NDEF_FILE)
                                                 },calculateRemainingBytes: extractNDEFMessageSize)
                                             } catch {
                                                 onResponseError(ERR_MSG_COMMAND_READ_NDEF_FILE)
                                             }                                                        
                                        } else {
                                            onResponseError(ERR_MSG_COMMAND_SELECT_NDEF_FILE)
                                        }
                                     })
                                 }
                             } catch {
                                 onResponseError(ERR_MSG_COMMAND_SELECT_NDEF_FILE)
                             }
                            } else {
                                onResponseError(ERR_MSG_COMMAND_READ_CC_FILE)
                            }
                       })                       
                   } else {
                       onResponseError(ERR_MSG_COMMAND_SELECT_CC_FILE)                       
                   }
               })                
            } else {
                onResponseError(ERR_MSG_COMMAND_SELECT_CMD_FILE)                
            }
        })
    }
   
    /**
     * Extracts the NDEF file length from the NDEF message (First 2 bytes)
	 * - Parameter bytes: First fragment of the NDEF message
     */
    static func extractNDEFMessageSize(bytes:Data)-> Int{        
        let ndefLength = Utils.byteToShort(data: bytes.subdata(in: 0..<2))
		// Includes 2 bytes used for NLEN
        return ndefLength+2
    }    
}
