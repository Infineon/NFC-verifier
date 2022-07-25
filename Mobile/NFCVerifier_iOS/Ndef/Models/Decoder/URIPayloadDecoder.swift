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

class URIPayloadDecoder : RecordPayloadDecoder {
    func decodePayload(payload: Data) throws -> AbstractRecord {
        var prefix = ""
        if (payload[0] >= URIRecord.ABBRIVIABLE_URIS.count || payload[0] < 0) {
            throw NDEFException.UnknownAbbreviation
        }
        else {
            prefix = URIRecord.ABBRIVIABLE_URIS[Int(payload[0])]
        }
        
        let uri = String(data: payload.suffix(from: 1), encoding: URIRecord.DEFAULT_URI_CHARSET)!
        return URIRecord(uri: prefix + uri)
    }
}
