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

class SmartPosterRecordDecoder : RecordPayloadDecoder{
    func decodePayload(payload: Data) throws -> AbstractRecord {
        let smartPosterRecord  = SmartPosterRecord()
        
        let records:[AbstractRecord] = try NDEFMessageDecoder.decryptor.decrypt(stream: NDEFMessageDecoder.decryptor.decrypt(stream: payload))
        for record in records {
            if record is URIRecord {
                smartPosterRecord.seturiRecord(uriRecord: record as! URIRecord)
            }
            if record is TextRecord {
                smartPosterRecord.addTitleRecord(titleRecord: record as! TextRecord)
            }
            if record is ActionRecord {
                smartPosterRecord.setActionRecord(actionRecord: record as! ActionRecord)
            }
            else {
                smartPosterRecord.addOtherRecord(otherRecord: record)
            }
        }
        return smartPosterRecord        
    }    
}
