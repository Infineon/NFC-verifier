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
 * Base class defining the decoders for product and service information received from the tag
 */
class AppFileDecoder {
    
	/**
     * Parses the custom data field
     *  - Parameter payload: Product or service information payload
	 *  - Parameter index: Index of the custom data field
     *  - Returns the decoded custom data in key-value format
     */
    static func parseCustomField(payload: Data, index: inout Int) -> [CustomFieldItem] {
        let range = getCustomFieldRange( byte: payload, index: &index)
        let data = String(bytes: payload.subdata(in: range), encoding: .utf8)!
        let itemArr = data.split(separator: ";")
        var counter = 0
        var customFieldItems = [CustomFieldItem]()
        while(counter<itemArr.count){
            let substring = itemArr[counter].split(separator: ":")
            customFieldItems.append(CustomFieldItem(title: String(substring[0]), value: String(substring[1])))
            counter += 1
        }
        return customFieldItems
    }
	
	/**
     * Returns the range of the custom field in the payload byte array
     *  - Parameter payload: Product or service information payload
	 *  - Parameter index: Index of the custom data field
     *  - Returns the range of the custom data field
     */
    static func getCustomFieldRange(byte:Data, index: inout Int) -> Range<Data.Index>  {
        let data = byte.subdata(in: index..<(index+2))
        let customFieldLen = Utils.byteToShort(data: data)
        let range: Range<Data.Index> = index+2..<(index+2+customFieldLen)
        return range
    }
    
	/**
     * Parses the date fields
     *  - Parameter payload: Product or service information payload
	 *  - Parameter index: Index of the date field
     *  - Returns the decoded date
     */
    static func parseDateBytes(payload: Data, index: inout Int) -> Date? {
        let range :Range<Data.Index>  = index..<index+3
        let data:Data = payload.subdata(in:range)
        index += 3
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyMMdd"
        let date = dateFormatter.date(from: data.hexEncodedString())
        return date
    }    
}
