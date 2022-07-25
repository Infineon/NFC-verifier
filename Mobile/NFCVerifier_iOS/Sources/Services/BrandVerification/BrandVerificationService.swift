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
 * Contains methods for accessing brand verification cloud service
 */
final class BrandVerificationService  {
  
    static let logger:FileLogger = LoggerFactory.getLogger(logger: .File) as! FileLogger

    /**
     * Performs request to generate-ma API to generate mutual authentication command data
     * - Parameter verificationUrl: Base URL of the verification service
     * - Parameter keyLabel: Key label stored in the tag
     * - Parameter challenge: Challenge received from the tag
     * - Parameter onSucess: Callback event to notify success
     * - Parameter onFailure: Callback event to notify failure
     */
    static func mutAuthGenerateAPI(verificationUrl: String, keyLabel: String, chipID:Data, challenge: Data, onSuccess: @escaping (MutAuthGenerationResponse) -> Void, onFailure: @escaping (String) -> Void) {        
	
		self.logger.log(tag: "Cloud service:", value: "Generate mutual-authenticate command")
        self.logger.log(tag: "-->", value: verificationUrl + API_MA_GENERATE)
        let timeLogger  = TimeLogger()
        timeLogger.start()
		
        // HTTP request parameters which will be sent in HTTP request body
        let requestData = MutAuthGenerationRequest(ChipID: chipID.hexEncodedString(), Challenge: challenge.hexEncodedString(), KeyLabel:keyLabel )
		
        self.logger.log(tag: "ChipID:", value: chipID)
        self.logger.log(tag: "Challenge:", value: challenge)
        self.logger.log(tag: "KeyLabel:", value: keyLabel.uppercased())
		
        HTTPRequestHelper.request(urlString: verificationUrl + API_MA_GENERATE, requestType: RequestType.POST, param: requestData) { (status,data) in
            do {
                let response = try JSONDecoder().decode(MutAuthGenerationResponse.self, from: data!)
                
				self.logger.log(tag: "<-- Status code:", value: status)
                self.logger.log(tag: "Command:", value: response.CommandData.data(using: .utf8)!)
                self.logger.log(tag: "Session ID:", value: response.SessionID)
                self.logger.log(tag: "Data: \(data!)", value: "\tExec Time: \(timeLogger.get())\n" )
                
				onSuccess(response)
            } catch {
                do {
                    let response = try JSONDecoder().decode(APIError.self, from: data!)
                    onFailure(response.ErrorMessage!)
                } catch {
                    onFailure(ERR_MSG_MA_GENERATE_JSON_PARSING)
                }
            }           
        } onFailure: {status,error in
            onFailure(error)
        }
    }
	
    /**
     * Performs request to verify-ma API to verify mutual authentication response
     * - Parameter verificationUrl: Base URL of the verification service
	 * - Parameter mutualAuthResponse: Response of the mutual authenticate command received from the tag
     * - Parameter sessionID: Session ID from the generate-ma response
     */
    static func mutAuthVerifyAPI(verificationUrl: String, mutualAuthResponse: Data, sessionID : String, onSuccess: @escaping (MutAuthVerificationResponse) -> Void, onFailure: @escaping (String) -> Void) {
        
        // HTTP request parameters which will be sent in HTTP request body
        let requestData = MutAuthVerificationRequest(MutualAuthResponse: mutualAuthResponse.hexEncodedString() , SessionID: sessionID)
		
        self.logger.log(tag: "Cloud service:", value: "Verify mutual-authenticate response" )
        self.logger.log(tag: "-->", value: verificationUrl + API_MA_VERIFY)
        self.logger.log(tag: "MutualAuthResponse:", value: mutualAuthResponse)
        self.logger.log(tag: "Session ID:", value: sessionID)
        let timeLogger  = TimeLogger()		
        timeLogger.start()
		
        HTTPRequestHelper.request(urlString: verificationUrl + API_MA_VERIFY, requestType: RequestType.POST, param: requestData, onSuccess: { (status,data) in
            do {
                let response = try JSONDecoder().decode(MutAuthVerificationResponse.self, from: data!)
                
				self.logger.log(tag: "<-- Status code:", value: status)
                self.logger.log(tag: "Auth Result:", value: response.AuthResult)
                self.logger.log(tag: "Version:", value: response.Version)
                self.logger.log(tag: "Data: \(data!)", value: "\tExec Time:  \(timeLogger.get())\n" )
				
                onSuccess(response)
            } catch {
                do {
                    let response = try JSONDecoder().decode(APIError.self, from: data!)
                    onFailure(response.ErrorMessage!)
                } catch {
                    onFailure(ERR_MSG_MA_VERIFY_PARSING)
                }
            }
        }, onFailure:  { status, error in
            onFailure(error)
        })
    }    
}

