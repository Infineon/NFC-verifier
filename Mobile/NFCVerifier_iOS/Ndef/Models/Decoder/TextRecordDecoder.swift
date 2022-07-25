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
 * Class is to decode the text record type
 */
public class TextRecordDecoder: RecordPayloadDecoder {
    
    func decodePayload(payload: Data) throws -> AbstractRecord {
        var index = 0
        let status  = payload[index]
        index = index+1
        let languageCodeLength:UInt8 = (status & UInt8(TextRecord.LANGUAGE_CODE_MAX))
        let languageCode = parseLanguageCode(payload: payload,length:languageCodeLength, index:&index)
        let textData = parseTextData(payload: payload,length:(UInt8(payload.count) - languageCodeLength - 1), index: &index)
        var textEncoding:String.Encoding?
    
            if(Int(status & 0x80) != 0) {
                textEncoding =  TextRecord.UTF16BE
                
            } else {
                textEncoding = TextRecord.UTF8
            }
        let text = String(data: textData, encoding: textEncoding!)
        return TextRecord(text: text!, encoding: textEncoding!, locale: Locale(identifier: languageCode!))        
    }
    
    /**
     *   Static function parse the Language Code
     *   - parameter payload Data byte of payload
     *   - Parameter length: Length of the language code
     *   - Parameter index: Defines the current byte position
     *   - Returns the Language Code
     */
    func parseLanguageCode(payload: Data, length:UInt8, index: inout Int) -> String? {
        let range :Range<Data.Index>  = index..<index+Int(length)
        let data = payload.subdata(in:range)
        index += Int(length)
        return String(data: data, encoding: .utf8)
    }
    
    /**
      *  Static function parse the Text Data
      *  - parameter payload: Data byte of payload
      *  - Parameter length: Length of the language code
      *  - Parameter index: Defines the current byte position
      *  - Returns the parsed text data
     */
    func parseTextData(payload: Data, length:UInt8, index: inout Int) -> Data {
        let range :Range<Data.Index>  = index..<index+Int(length)
        let data = payload.subdata(in:range)
        index += Int(length)
        return data
    }
}
