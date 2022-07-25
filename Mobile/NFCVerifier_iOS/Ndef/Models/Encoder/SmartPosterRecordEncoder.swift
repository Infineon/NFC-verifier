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

class SmartPosterRecordEncoder: RecordPayloadEncoder {
    func encodePayload(wellKnownRecord: AbstractRecord) throws -> Data {
        let myRecord:SmartPosterRecord = wellKnownRecord as! SmartPosterRecord
        var records:[NDEFRecord] = []
        if(myRecord.getTitleRecord() != nil) {
            for record in myRecord.getTitleRecord()! {
                let newRecord = try NDEFRecordEncoder.instance.encrypt(record: record)
                records.append(newRecord)
            }
        }
        if let record = myRecord.getUriRecord() {
            let newRecord = try NDEFRecordEncoder.instance.encrypt(record: record)
            records.append(newRecord)
        }
        if let actionRecords = myRecord.getActionRecord() {
            let newRecord = try NDEFRecordEncoder.instance.encrypt(record: actionRecords)
            records.append(newRecord)
        }       
        return try NDEFMessageEncoder.encryptor.encrypt(ndefRecords: records)
    }
}
