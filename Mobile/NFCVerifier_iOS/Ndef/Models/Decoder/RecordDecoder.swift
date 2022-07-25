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

class RecordDecoder : IRecordDecoder {
    var  payloadDecoder:RecordPayloadDecoder?
    
    func canDecodeRecord(record: NDEFRecord) -> Bool {       
        if(record.tnf == NDEFConstants.TNF_WELL_KNOWN  || record.tnf == NDEFConstants.TNF_EXTERNAL_KNOWN) {            
            payloadDecoder = RecordUtils.getPayloadDecoder(recordType: RecordType (type: record.type) )
            return payloadDecoder != nil           
        } else {
            return false
        }
    }
    
    func decodeRecord(record: NDEFRecord) throws -> AbstractRecord? {
        return try payloadDecoder?.decodePayload(payload: record.payLoad)
    }
}
