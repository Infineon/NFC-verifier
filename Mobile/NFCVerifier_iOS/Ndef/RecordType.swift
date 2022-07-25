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
 * A Record type can be any NDEF well known record types. E.g Smart poster record, Text Record, etc.
 *
 */
class RecordType {
    private var type:Data    
    
    /**
     * Constructor to set the record type with the give byte array.
     * - Parameter type:  type record type which is of byte array
     */
    public init(type:Data) {
        self.type = type
    }
	
    /**
     * Constructor to set the record type with the give string type.
     * - Parameter type:  type record type of string e.g new RecordType("T");
     */
    public init(type:String)  {      
        self.type = type.data(using: NDEFConstants.DEFAULT_CHARSET )!    
    }
	
    /**
     * Method is to get the record type
     * @return The type of record
     */
    public func getType()->Data {
        return type
    }
	
    public func getTypeAsString() -> String? {        
        return String(data: type, encoding: NDEFConstants.DEFAULT_CHARSET)
    }
}


