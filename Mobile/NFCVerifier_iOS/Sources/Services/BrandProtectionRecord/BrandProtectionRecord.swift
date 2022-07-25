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
 * Brand protection external type record 
 */
class BrandProtectionRecord: ExternalTypeRecord {
    
    /**
	 *	Constructor to create NDEF Record. Each Record is made up of header such as the record type, and so forth, and the payload, which contains the content of the message.
     *    - Parameter **tnf**: Record Type Name Format
     *    - Parameter **type**: Record Type
     *    - Parameter **id**: Record ID
     *    - Parameter **payload**: Record Payload Data
     *
     */    
    init(tnf:UInt8, type:Data, id:Data, payload:BrandProtectionPayload) {
        super.init(payload: payload.getPayload())
        self.setTnf(tnf: tnf)
        self.recordType = RecordType(type: type)
    }
    
    /**
     *  Constructor to create NDEF Record. Each record is made up of a header, which contains message about the record, such as the record type, length , and so forth, and the payload, which contains the content of the message.
     *    - Parameter **tnf**: Record Type Name Format
     *    - Parameter **isCuncked**: Specifies whether the data is chunk
     *    - Parameter **type**: Record Type
     *    - Parameter **id**: Record ID
     *    - Parameter **payload**: Record Payload Data
     */
    convenience init(tnf:UInt8, isChuncked:Bool, type:Data, id:Data, payload:BrandProtectionPayload) {        
        self.init(tnf: tnf,type: type, id: id, payload: payload)
    }
}
