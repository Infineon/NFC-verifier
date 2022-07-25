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
 * Class is to encode the payload of an action record
 */
class ActionRecordEncoder: RecordPayloadEncoder {
    func encodePayload(wellKnownRecord: AbstractRecord) throws -> Data {      
        let actionRecord:ActionRecord = wellKnownRecord as! ActionRecord
        if(!actionRecord.hasAction()) {
            throw NDEFException.IllegalArgumentException("Missing Action")
        }
        return Data([actionRecord.getAction()!.rawValue])
    }       
}

