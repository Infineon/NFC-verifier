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
 * To decode the NDEF (NFC Data Exchange Format) record which is part of NDEF Message.
  */
class NDEFRecordDecoder {
    
    private static let  instance = NDEFRecordDecoder()
	
    private init(){
        
    }
    
    public static func getInstance()->NDEFRecordDecoder{
        return instance
    }
    
    /**
     * Method to decode the NDEF record and return an abstract Record out of it.
     * - Parameter record: NDEF record that is to be decoded
     * - Returns decoded NDEF record of Abstract Record type
     * - Throws NDEF Exception Throws NFC exception if condition not satisfied
     */
    public func decrypt(record:NDEFRecord) throws ->AbstractRecord? {
        let  decoder = RecordDecoder()
        if (decoder.canDecodeRecord(record: record)) {
            return try decoder.decodeRecord(record: record)
        }
        throw NDEFException.IllegalArgumentException("Unsupported record ");
    }    
}
