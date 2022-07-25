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
 * NDEF Messages are the basic "transportation" mechanism for NDEF records, with each message containing one or more NDEF Records.
 * This class represents an NFC NDEF records which is a collection of NDEF records and also provides methods to encode and decode NDEF Message. An NDEF Message is a container for one or more NDEF Records.
 *   
 */
final class NDEFMessage {
    var ndefRecords = Array<NDEFRecord>()
    
    /**
     * Creates a new NDEF message with the list of NDEF records
     * - Parameter ndefRecords: List of NDEF records
     */
    init(ndefRecords: Array<NDEFRecord>) {
        self.ndefRecords = ndefRecords        
    }
    
    /**
     * Creates a new NDEF Message with **NDEFRecords** using the raw byte array data.
     * - Parameter ndefMessage: List of NDEF records in byte array
     */
     init(ndefMessage: Data)throws {        
        ndefRecords = try NDEFMessageDecoder.decryptor.decrypt(stream: ndefMessage).getNdefRecords()                
    }
	
    /**
     * Method used to set the  a new  list of NDEF records
     * - Parameter ndefRecords: Adds the NDEF records to record list
     */
    func setNDEFRecords(ndefRecords:Array<NDEFRecord>) {
        self.ndefRecords.append(contentsOf: ndefRecords)
    }
    
    /**
     *  This method returns the collection of NDEF records available in NDEF Message
     *  - Returns the collection of NDEF records list
     */
    func getNdefRecords() -> Array<NDEFRecord> {
        return ndefRecords
    }
    
    /**
     * This method encodes the NDEF Records and returns the raw byte array data
     *  - Returns NDEF record byte array data
     */
    func toByteArray() throws -> Data {
        return try NDEFMessageEncoder.encryptor.encrypt(ndefRecords: ndefRecords)
    }    
}
