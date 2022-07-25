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
 * This is the Factory class that provides methods for creation, encode, decode of NDEF records.
 *
 */
public class NFCFactory {
    private static var recordEncryptor = NDEFRecordEncoder.instance
    private static var recordDecryptor = NDEFRecordDecoder.getInstance()
 
	/**
     * Method is to create URI Record of NDEF record type. The NDEF URI Record is a well-known record type defined by the NFC Forum.
     * It carries a, potentially abbreviated, UTF-8 encoded Internationalized Resource Identifier (IRI).  A uri consists of a prefix and its contents.
     * - Parameter uri:   Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     * - Returns:    NDEFRecord Encrypted form of NDEF record.
     */
    static func  createURIRecord(uri:String) throws  ->NDEFRecord {
        return try recordEncryptor.encrypt(record: URIRecord(uri: uri))
    }
    
    /**
     * Method is to decode the encoded record. Known record types are decoded into instances of their implementation class
     * and can be directly encoded as part of a message.
     * - Parameter  ndefRecord:  Encrypted form of NDEF record NDEFRecord.
     * - Parameter  AbstractRecord: Decoded form of NDEF record
     * - Throws  NDEFException  Throws NDEF exception if condition not satisfied
     */
    static func decryptRecord(ndefRecord: NDEFRecord) throws -> AbstractRecord? {
        return try recordDecryptor.decrypt(record: ndefRecord);
    }
    
    /**
     * Method is to create Text Record of NDEF record type. The NDEF Text Record is a well-known record type defined by the NFC Forum.
     * It carries a, potentially abbreviated, UTF-8 encoded Internationalized Resource Identifier (IRI).  A uri consists of a prefix and its contents.
     * - Parameter text: Uniform Resource Identifier (text). Eg. <i>welcome</i>
     * - Returns:    NDEFRecord Encrypted form of NDEF record.
     */
    static func  createTextRecord(text:String) throws  ->NDEFRecord {        
        return try recordEncryptor.encrypt(record: TextRecord(text: text))
    }    
}
