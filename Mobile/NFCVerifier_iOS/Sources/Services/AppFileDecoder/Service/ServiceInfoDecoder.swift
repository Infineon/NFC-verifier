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
 * Decodes the service information received from the tag
 */
class  ServiceInfoDecoder  : AppFileDecoder {  
	
	/*
	 * Decodes the service information received from the tag
	 * - Parameter payload: Service information payload as bytes
	 * - Returns the decoded service information
	 */
    static func decode(payload: Data) -> ServiceInfo? {       
        if(payload.count >= calculateServiceInfoSize(byte: payload)) {
            var index = 0

            //Layout Version Info
            let layoutVersion = payload[index]
            index += 1
            let purchaseDate = parseDateBytes(payload: payload, index: &index)
            let warrantyValidityDate = parseDateBytes(payload: payload, index: &index)
            let lastServiceDate = parseDateBytes(payload: payload, index: &index)
            let customField = parseCustomField(payload: payload, index: &index)
            return ServiceInfo(layoutVersion: layoutVersion, purchaseDate: purchaseDate, warrantyValidityDate: warrantyValidityDate, lastServiceDate: lastServiceDate, customField: customField)
        }
        else {
            return nil
        }
    }
    
    /**
     * Calculates the length of the service info file. 
     *  - Parameter byte: Partial service information payload read from the first read binary operation
     *  - Returns the length of the service information file
     */
    static func calculateServiceInfoSize(byte:Data)-> Int{
        let customFieldOffset = 10
        let customFieldLen = Utils.byteToShort(data: byte.subdata(in: customFieldOffset..<(customFieldOffset+2)))
        let length = (10+2+customFieldLen)
        return length
    }
}
