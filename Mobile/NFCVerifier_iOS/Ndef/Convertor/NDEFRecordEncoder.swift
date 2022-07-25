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
 * To encode the NDEF (NFC Data Exchange Format) record which is part of NDEF Message.
 */
final class NDEFRecordEncoder {
    
    static let  instance = NDEFRecordEncoder()
    private var recordEncryptor = Array<IRecordEncoder>()

    private init(){
        recordEncryptor.append(RecordEncoder())
    }

    /**
     * Method to encode the NDEF record and return as NDEF Record type.
     * - Parameter record: An abstract record that is to be encoded as NDEF Record type
     * - Returns Encoded NDEF record from an abstract record
     */   
    public func encrypt(record: AbstractRecord) throws -> NDEFRecord {
        var i = 0
        while (i < recordEncryptor.count) {
            
            if (recordEncryptor[i].canEncodeRecord(record: record)) {
                return try recordEncryptor[i].encodeRecord(record: record);
            }
            i += 1
        }
        throw NDEFException.IllegalArgumentException("Unsupported record ");
    }    
}
