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
 * Provides utility methods
 */
class Utils{
    /**
	 * Flag indicating whether the application is executed in 'debug' mode. 
	 */
    public static var isInDebugMode: Bool {
           #if DEBUG
               return true
           #else
               return false
           #endif
    }
	
    /**
     * Converts the byte array to short
     *  - Parameter data: Byte array (Length >= 2 bytes)
     *  - Returns short value as integer
     */
    static func byteToShort(data:Data) -> Int {
         var value : Int = 0
         for byte in data {
             value = value << 8
             value = value | Int(byte)
         }
         return value
     }
}
