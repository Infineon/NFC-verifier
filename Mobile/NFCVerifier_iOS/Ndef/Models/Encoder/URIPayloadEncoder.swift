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

class URIPayloadEncoder: RecordPayloadEncoder {
    
    func encodePayload(wellKnownRecord: AbstractRecord) throws -> Data {
        
        let uriRecord = wellKnownRecord as! URIRecord

        if(uriRecord.getUri().isEmpty ) {
            throw NDEFException.empty
        }

        let uri = uriRecord.getUri()
        let uriAsBytes = getUriAsBytes(uri: uri)
        let abbreviateIndex = getAbbreviateIndex(uri: uri.lowercased())
        if(abbreviateIndex == -1) {
            throw NDEFException.invalidURI
        }
        let uriCopyOffset = URIRecord.ABBRIVIABLE_URIS[abbreviateIndex].count
        var payload = Data()
        payload.append(UInt8(abbreviateIndex))
        payload.append((uriAsBytes?.suffix(from:uriCopyOffset ))!)
        return payload       
    }
        
    private func getUriAsBytes(uri:String)  -> Data? {
        uri.data(using: URIRecord.DEFAULT_URI_CHARSET)    
    }
    
    private func getAbbreviateIndex(uri:String)->Int {
        var maxLength = 0
        var abbreviateIndex = 0
        var x:Int = 1
        while ( x < URIRecord.ABBRIVIABLE_URIS.count ) {
            let abbreviablePrefix =  URIRecord.ABBRIVIABLE_URIS[x];
            if (uri.hasPrefix(abbreviablePrefix) && abbreviablePrefix.count > maxLength) {
                abbreviateIndex = x
                maxLength = abbreviablePrefix.count
            }
            x += 1
        }
        return abbreviateIndex
    }
}
