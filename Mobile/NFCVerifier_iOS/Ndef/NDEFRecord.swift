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
 *  NFC Data Exchange Format (NDEF) Record.
 *   Contains the Following parameters or fields
 *  **Payload Length **       : Regardless of the relationship of the record to other record, the Payload length always indicated the length of the payload encapsulated in this record.
 *  **Payload Type **         : The Payload  Type of a record indicates the kind of data being carried in the payload of that record. This may be used to guide the processing of the payload at the description of the user application.
 *  **Payload Identification**: The optional payload identifier allows user application to identify the payload carried within an NDEF record. By providing a payload identifier. it becomes  possible for other payloads supporting URI-Based Linking technologies to refer to that payload.
 *  **TNF Type Name Format**  : The TNF field value indicates the structure of the value of the Type Field. The TNF field is 3-bit field.
 *  ##Following are the types of TNF supported
 *      
 *      1. Empty : 0x00
 *      2. NFC Forum well-Known type [NFC RTD ] : 0x01
 *      3. Media-Type as defined in RFC 2046 [RFC 2046] : 0x02
 *      4. Absolute URI as Defined In RFC 3986 [RFC 3986] : 0x03
 *      5. NFC Forum external Type [NFC RTD ] : 0x04
 *      6. unknown : 0x05
 *      7. unchanged : 0x06
 *      8. Reserved : 0x07
 */
class NDEFRecord {
    
    /**
     * Type Name Format field. The Type Name Format or TNF Field of NDEF Record is 3-bit Value that describe the record type, and sets the expectation for the Structure and Content of the rest of the record
     */
    var tnf: UInt8
    
    /**
     * The variable length Type field
     */
    var type:Data
    
    /**
     * Identifier meta data
     */
    var id:Data
    /**
     * The actual payLoad
     */
    var payLoad:Data
    
    /**
     * Application data that has been partitioned into multiple chunks each carried in a separate NDEF record, where each of these records except the last has the CF flag set to 1. This facility can be used to carry dynamically generated content for which the pay load size is not known in advance or very large entities that don't fit into a single NDEF record .
     */    
    var isChuncked:Bool = false
    
    /**
     * This constructor is used to create NDEF Record. Each Record is made up of header such as the record type, and so forth, and the payload, which contains the content of the message.
     *    - Parameter tnf: Record Type Name Format
     *    - Parameter type: Record Type
     *    - Parameter id: Record ID
     *    - Parameter payload: Record Payload Data
     */    
    init(tnf:UInt8, type:Data, id:Data, payload:Data) {
        self.tnf = tnf
        self.type = type
        self.id = id
        self.payLoad = payload    
    }
    
    /**
     * This constructor is used to create a NDEF record. Each record is made up of a header, which contains message about the record, such as the record type, length, and so forth, and the payload, which contains the content of the message.
     *    - Parameter tnf: Record Type Name Format
     *    - Parameter isChuncked: Specifies whether the data is chunk
     *    - Parameter type: Record Type
     *    - Parameter Id: Record ID
     *    - Parameter payload: Record Payload Data
     */
    convenience init(tnf:UInt8, isChuncked:Bool, type:Data, id:Data, payload:Data) {        
        self.init(tnf: tnf,type: type, id: id, payload: payload)
        self.isChuncked = isChuncked
    }
}
