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

/*
 * Model class to store the NDEF file control TLV data which is present in CC file
 */
class NDEFFileCtrlTLV {
       
    var fileID: Data?
    var fileSize: Int?
    var t:UInt8?
    var l:UInt8?
    var readAccessByte:UInt8?
    
    /**
     * Initializes the instance with NDEF file control TLV data
     */
    init(fileID:Data,fileSize:Int,t:UInt8,l:UInt8,readAccessByte:UInt8) {
        self.fileID = fileID
        self.fileSize = fileSize
        self.t = t
        self.l = l
        self.readAccessByte = readAccessByte
    }    
}
