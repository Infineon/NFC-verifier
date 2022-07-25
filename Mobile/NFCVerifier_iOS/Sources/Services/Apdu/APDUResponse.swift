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
 * Stores the APDU responses. The response consists of response data (optional) and
 * a status word (mandatory).
 */
class APDUResponse {
	/**
     * Status word indicating successful operation
     */
    public static let SW_SUCCESS: UInt16 = 0x9000
    
    var sw1: UInt8
    var sw2: UInt8
    var data: Data?
    
    /* Initializes the response APDU
     * - Parameter sw1: Response status word 1
     * - Parameter sw2: Response status word 2
     * - Parameter data: Response data without status word
	 */
    init(sw1: UInt8, sw2: UInt8, data: Data?)
    {
        self.sw1 = sw1
        self.sw2 = sw2
        self.data = data
    }
    
    /* Gets the combined status word - SW1 and SW2
     * - Returns: Status word as 2 bytes / UINT16
	 */
    func getSW() -> UInt16 {
        var sw: UInt16 = 0
        sw += UInt16(self.sw1) << 8
        sw += UInt16(self.sw2) << 0
        return sw
    }
    
    /* Gets the status word in hex format
     * - Returns: Status word as hex string
	 */
    func getSWString() -> String {
        return String(format:"%02X ", self.sw1) + String(format:"%02X ", self.sw2)
    }
    
    /* Checks whether the status word indicates SUCCESS
     * - Returns: Flag indicating success (true) or failure (false)
	 */
    func isSuccessSW() -> Bool{
        return (self.getSW() == APDUResponse.SW_SUCCESS)
    }
    
    /* Compares the response status word against the input status word
     * - Parameter sw: Input status word to be compared
     * - Returns: Flag indicating success (true) or failure (false)
	 */
    func checkSW(sw: UInt16) -> Bool{
        return (self.getSW() == sw)
    }
    
   
}
