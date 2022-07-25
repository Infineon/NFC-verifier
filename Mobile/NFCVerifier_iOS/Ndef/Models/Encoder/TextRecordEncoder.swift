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
 * Class to encode the text record type
 */
class TextRecordEncoder: RecordPayloadEncoder {
    
    func encodePayload(wellKnownRecord: AbstractRecord) throws -> Data {
        let textRecord:TextRecord = wellKnownRecord as! TextRecord;
        if(!textRecord.hasLocale()){
            throw NDEFException.runtimeError("Missing locale")
        }
        if(!textRecord.hasEncoding()){
            throw NDEFException.runtimeError("Missing Encoding")
        }
        if(!textRecord.hasText()){
            throw NDEFException.runtimeError("Missing text")
        }
        let locale = textRecord.getLocale()
        var languageData:Data?
        if(locale?.regionCode?.count == 0) {
            languageData = (locale?.languageCode!.data(using: .utf8))
        } else {
            languageData = ((locale?.languageCode)!+"-"+(locale?.regionCode)!).data(using: .utf8)
        }
        if(languageData!.count > TextRecord.LANGUAGE_CODE_MAX) {
            throw NDEFException.runtimeError("Language code length is Longer. this is not supported")            
        }
		
        let encoding:String.Encoding =  textRecord.getEncoding()!
        let textData:Data = (textRecord.getText()?.data(using: encoding))!
        var status :UInt8 = 0x00
        let isEncoderSame: Int?
        if (TextRecord.UTF16BE == encoding) {
            isEncoderSame = 0x80
        } else
        {
            isEncoderSame = 0x00
        }
        status = UInt8((languageData!.count | isEncoderSame!))
		
        var payload = Data(_:[status])
        payload.append(languageData!)
        payload.append(textData)
        return payload
    }
}
