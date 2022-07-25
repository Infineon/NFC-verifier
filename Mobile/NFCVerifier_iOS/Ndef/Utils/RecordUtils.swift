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
  * Contains utility methods used for NDEF parsing
  */
class RecordUtils{
    
    /**
     * Method is to get the payload encoder from the class
     * - Parameter class: Record class
     * - Returns  Record payload encoder
     */
    public static func getPayloadEncoder(classs: AbstractRecord) ->RecordPayloadEncoder? {        
        switch(classs) {
            case is URIRecord :
                return URIPayloadEncoder()        
			case is TextRecord:
                return TextRecordEncoder()
			default:
				return nil
        }        
    }
    
    /**
     * Method is to get the payload decoder
     * - Parameter  recordType Type of record
     * - Returns  Record payload decoder
     */
    public static func getPayloadDecoder(recordType: RecordType) -> RecordPayloadDecoder? {
        switch(recordType.getTypeAsString()) {
            case "U":
                return URIPayloadDecoder()          
            default:
                return ExternalTypePayloadDecoder()
        }        
    }
}
