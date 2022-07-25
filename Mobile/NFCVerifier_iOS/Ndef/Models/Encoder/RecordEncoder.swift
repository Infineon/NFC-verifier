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

class RecordEncoder : IRecordEncoder {
    var  payloadEncoder:RecordPayloadEncoder?
    
    func canEncodeRecord(record: AbstractRecord) -> Bool {        
        payloadEncoder = RecordUtils.getPayloadEncoder(classs: record)
        if((RecordUtils.getPayloadEncoder(classs: record)) != nil) {
            return true            
        } else {
            return false
        }
    }
    
    func encodeRecord(record: AbstractRecord ) throws -> NDEFRecord {
        let key  = record.getId()
        if(key.count > 255 ){
            throw NDEFException.IllegalArgumentException("Expected record id length <= 255 bytes")
        }
        let payload = try payloadEncoder!.encodePayload(wellKnownRecord: record)
        let type = record.getRecordType()!.getType()
        return NDEFRecord(tnf: NDEFConstants.TNF_WELL_KNOWN, type: type, id: key, payload: payload)
    }
}
