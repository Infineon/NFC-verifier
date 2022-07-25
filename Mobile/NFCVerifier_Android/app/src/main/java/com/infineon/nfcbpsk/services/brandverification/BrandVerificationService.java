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
package com.infineon.nfcbpsk.services.brandverification;

import android.app.Activity;

import com.infineon.nfcbpsk.data.httphelper.HttpRequestHelper;
import com.infineon.nfcbpsk.data.httphelper.HttpResponseEvent;
import com.infineon.nfcbpsk.data.httphelper.ApiError;
import com.infineon.nfcbpsk.data.logger.LoggerFactory;
import com.infineon.nfcbpsk.data.logger.LoggerType;
import com.infineon.nfcbpsk.data.logger.TimeLogger;
import com.infineon.nfcbpsk.data.logger.FileLogger;
import com.infineon.ndef.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.infineon.nfcbpsk.data.httphelper.Constants.API_MUTUAL_AUTH_GENERATE;
import static com.infineon.nfcbpsk.data.httphelper.Constants.API_MUTUAL_AUTH_VERIFY;

/**
 * Contains methods for accessing brand verification cloud service
 */
public class BrandVerificationService {
    final FileLogger fileLogger;
    /**
     * Instance of HttpRequest Handler to handle the Http request
     */
    private final HttpRequestHelper httpRequestHandler;

    /**
     * Initializes the brand verification service
     *
     * @param context Requester activity instance
     * @param url     URL endpoint of the brand verification service
     */
    public BrandVerificationService(Activity context, String url) {
        this.httpRequestHandler = new HttpRequestHelper(context, url);
        this.fileLogger = (FileLogger) LoggerFactory.getLogger(context, LoggerType.FILE);
    }

    /**
     * This method performs a generate-ma service request and triggers the callback method when
     * the response is received.
     *
     * Service:     generate-ma
     * Input:       KeyLabel, ChipID and Challenge
     * Output:      SessionID, Mutual-auth command data
     *
     * @param keyLabel     Key-label as string
     * @param chipId       Chip unique ID as byte array
     * @param challenge    Challenge as byte array
     * @param onMutualAuthGenerateEvent Callback method to handle the response
     */
    public void performMutualAuthGeneration(String keyLabel, byte[] chipId, byte[] challenge,
                                            MutualAuthGenerateEvent onMutualAuthGenerateEvent) {
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("ChipID", Utils.toHexString(chipId));
            jsonParam.put("Challenge", Utils.toHexString(challenge));
            jsonParam.put("KeyLabel", keyLabel);
            if (fileLogger != null) {
                fileLogger.log("Cloud service:", "Generate mutual-authenticate command");
                fileLogger.log("-->", httpRequestHandler.url + API_MUTUAL_AUTH_GENERATE);
                fileLogger.log("ChipID:", chipId);
                fileLogger.log("Challenge:", challenge);
                fileLogger.log("KeyLabel:", keyLabel);
            }

            TimeLogger timeLogger = new TimeLogger();
            httpRequestHandler.postRequest(API_MUTUAL_AUTH_GENERATE, jsonParam,
                    new HttpResponseEvent() {
                @Override
                public void onSuccess(int status, String response) {
                    try {
                        MutualAuthGenerateResponse maGenerateResponse =
                                new MutualAuthGenerateResponse(response);
                        if (fileLogger != null) {
                            fileLogger.log("<--Status Code:", status);
                            fileLogger.log("Command:", Utils.formatCommand("Command:",maGenerateResponse.commandData));
                            fileLogger.log("Session Id:", maGenerateResponse.sessionID);
                            fileLogger.log("Data: " + response.getBytes().length + " bytes",
                                    "  Exec Time:" + timeLogger.getDifferenceInTime() + " ms");
                        }
                        onMutualAuthGenerateEvent.onSuccess(maGenerateResponse);
                    } catch (JSONException e) {
                        try {
                            onMutualAuthGenerateEvent.onError(status,
                                    new ApiError(response).errorMessage);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            onMutualAuthGenerateEvent.onError(status, response);
                        }
                    }
                }

                @Override
                public void onError(int status, String error) {
                    onMutualAuthGenerateEvent.onError(status, error);
                }
            });
        } catch (JSONException e) {
            onMutualAuthGenerateEvent.onError(0, "Exception");
        }
    }

    /**
     * This method performs a verify-ma service request and triggers the callback method when
     * the response is received.
     *
     * Service:     verify-ma
     * Input:       SessionID and Mutual-authentication response
     * Output:      Verification result
     *
     * @param sessionID                 SessionID received in the generate-ma request
     * @param mutualAuthResponse        Mutual authentication response received from the token
     * @param onMutualAuthVerifyEvent   Callback method to handle the response
     */
    public void performMutualAuthVerification(String sessionID, byte[] mutualAuthResponse,
                                              MutualAuthVerifyEvent onMutualAuthVerifyEvent) {
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("MutualAuthResponse",
                    Utils.toHexString(mutualAuthResponse) + "9000");
            jsonParam.put("SessionID", sessionID);
            if (fileLogger != null) {
                fileLogger.log("Cloud service:", "Verify mutual-authenticate response");
                fileLogger.log("-->", httpRequestHandler.url + API_MUTUAL_AUTH_VERIFY);
                fileLogger.log("MutualAuthResponse:", mutualAuthResponse);
                fileLogger.log("Session ID:", sessionID);
            }
            TimeLogger timeLogger = new TimeLogger();
            httpRequestHandler.postRequest(API_MUTUAL_AUTH_VERIFY, jsonParam,
                    new HttpResponseEvent() {
                @Override
                public void onSuccess(int status, String response) {
                    try {

                        MutualAuthVerifyResponse maGenerateResponse =
                                new MutualAuthVerifyResponse(response);
                        if (fileLogger != null) {
                            fileLogger.log("<--Status Code:", status);
                            fileLogger.log("Auth Result:", maGenerateResponse.authResult);
                            fileLogger.log("Version:", maGenerateResponse.version);
                            fileLogger.log("Data: " + response.getBytes().length + " bytes",
                                    "  Exec Time:" + timeLogger.getDifferenceInTime() + " ms");
                        }
                        onMutualAuthVerifyEvent.onSuccess(maGenerateResponse);
                    } catch (JSONException e) {
                        try {
                            onMutualAuthVerifyEvent.onError(status,
                                    new ApiError(response).errorMessage);

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            onMutualAuthVerifyEvent.onError(status, response);
                        }
                    }
                }

                @Override
                public void onError(int status, String error) {
                    onMutualAuthVerifyEvent.onError(status, error);
                }
            });
        } catch (JSONException e) {
            onMutualAuthVerifyEvent.onError(0, "Exception");
        }
    }
}
