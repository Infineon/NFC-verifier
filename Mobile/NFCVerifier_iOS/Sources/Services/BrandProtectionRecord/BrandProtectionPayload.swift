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
 * Defines structure of BP record payload as defined in NFC brand protection starter kit
 */
class BrandProtectionPayload {
    
    let OFFSET_FOR_STRUCTURE_INDICATOR = 0
    let OFFSET_FOR_VERIFICATION_TYPE = 1
    let OFFSET_FOR_URL_LEN = 2
    let OFFSET_FOR_VERIFICATION_URL = 3
	
    // Holds the payload of Brand Protection data
    private var payload: Data
    // Holds the structure indicator of Brand protection data
    var structreIndicator: UInt8!
    // Defines the Brand verification method  // valid are Cloud, PKI, BlockChain
    private var verificationType:UInt8!
    // Defines the length of verification URL?
    private var verificationURLLen:UInt8!
    // Defines the Verification URL
    private var verificationURL: Data!
    // Defines the Key Label For Brand Verification
    var keyLabel:UInt16 = 0
    
    /**
     * Constructor is used to create the payload of NDEF Brand Protection Record
     *  - Parameter payload: BP record payload in bytes
     */
    init(payload: Data) throws {
        self.payload = payload
        try self.parsePayload()
    }
    
    /**
     *  Method to parse the payload of Brand Protection Record
     */
    func parsePayload() throws {
        if(payload.count > 0) {
            self.structreIndicator = payload[OFFSET_FOR_STRUCTURE_INDICATOR]
            self.verificationType = payload[OFFSET_FOR_VERIFICATION_TYPE]
            if(isCloudVerificationEnabled()) {
                try parsePayloadCloud(offset:OFFSET_FOR_VERIFICATION_URL)
            }
            if(isPKIVerificationEnabled()) {
                // Not supported in this version
            }
            if(isBlockChainVerificationEnabled()) {
                // Not supported in this version
            }
        }
    }
    
    /**
     * Method to parse payload of Cloud verification from given offset value 
	 * - Parameter offset: Start offset of cloud verification payload
     */
    func parsePayloadCloud(offset:Int) throws {      
            self.verificationURLLen = payload[OFFSET_FOR_URL_LEN]
            let subPayload = payload.suffix(from: offset)
            self.verificationURL = subPayload.prefix(Int(self.verificationURLLen))
            
            let keyLabelOffset = offset + Int(self.verificationURLLen)
            if(keyLabelOffset<payload.count) {
              self.keyLabel += UInt16(payload[keyLabelOffset+1]) << 8
              self.keyLabel += UInt16(payload[keyLabelOffset]) << 0
            }
    }
    
    /**
     * Method to return the verification URL as String. UTF8 readable format
     */
    func getVerificationURL() -> String {
        if(verificationURL != nil ) {
            return String(data: verificationURL, encoding: .utf8)!
        }
        return "";
    }
	
    /**
     * Method to return the payload in bytes
     */
    func getPayload() -> Data {
        return payload
    }
	
    /**
     * Returns true if cloud based verification is enabled, else false
     */
    func isCloudVerificationEnabled() -> Bool {
        if (verificationType & (1 << 0) > 0) {
           return true
        }
        else {
            return false
        }
    }
	
    /**
     * Returns true if PKI based verification is enabled, else false
     */
    func isPKIVerificationEnabled() -> Bool {
        if (verificationType & (1 << 1) > 0) {
           return true
        }
        else {
            return false
        }
    }
	
    /**
     * Returns true if blockchain based verification is enabled, else false
     */
    func isBlockChainVerificationEnabled() -> Bool {
        if (verificationType & (1 << 2) > 0) {
           return true
        }
        else {
            return false
        }
    }    
}
