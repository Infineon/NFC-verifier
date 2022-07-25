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
 * Decodes the capability container file received from the tag
 */
class CCFile {
    var data:Data
    var mappingVersion:UInt8?
    var maxLe: Int?
    var ndefFileCtrlData :NDEFFileCtrlTLV?
	
    /**
     * Initializes the instance with the CC payload received from the tag
	 * - Parameter data: CC file payload
     */
    private init(data:Data) {        
        self.data = data
    }
	
    /**
     * Parses the CC file
     *  - Parameter data: CC file payload
     *  - Returns the decoded CC file
     */
    static func parseCCFile(data:Data)  -> (CCFile?  ){        
        if(data.count >= 15){
            let ccFile = CCFile(data: data)
            ccFile.mappingVersion = data[2]
            ccFile.maxLe = Utils.byteToShort(data: Data([data[3], data[4]]))
            let ndefTLVData = data.subdata(in: 7..<15)
            ccFile.ndefFileCtrlData = parseNDEFFileCtrlTLV(fileCtrlData: (ndefTLVData))            
            return ccFile;
        } else {
            return nil
        }
    }
	
    /**
     * Parses the NDEF file control TLV data present in the CC file
     *  - Parameter fileCtrlData: NDEF file control TLV payload
     *  - Returns the parsed file control data
     */
    static func parseNDEFFileCtrlTLV(fileCtrlData:Data) -> NDEFFileCtrlTLV  {      
        let type = fileCtrlData[0]
        let length = fileCtrlData[1]
        let fileID = Data( [fileCtrlData[2], fileCtrlData[3]])
        let fileSize = Utils.byteToShort(data: Data([fileCtrlData[4], fileCtrlData[5]]))
        let readAccessByte = fileCtrlData[6]
        return NDEFFileCtrlTLV(fileID: fileID, fileSize: fileSize, t: type, l: length, readAccessByte: readAccessByte)
    }
    
    /**
     * Validates the CC file data
     * - Parameter onResponseError: Callback method to handle validation errors
     * - Returns true if CC file content is valid, else false
     */
    func validate(onResponseError: @escaping (String)  -> Void) -> Bool {     
        if(mappingVersion == 0x10 || mappingVersion == 0x20) {
            if(ndefFileCtrlData?.t == 0x04 && ndefFileCtrlData?.l == 0x06) {
                if(ndefFileCtrlData?.readAccessByte == 0x00) {
                    return true
                } else {
                    onResponseError("Don't have access to read NDEF")
                    return false
                }
            } else {
                onResponseError("Unsupported Type or Length in CC")
                return false
            }
        } else if (mappingVersion == 0x30) {
            onResponseError("Extended data structure is not supported")
            return false
        } else {
            onResponseError("Unsupported T4T mapping version")
            return false
        }
    }
    
    /**
     * Returns the maximum bytes that can be read out during the first 
	 * read binary operation on NDEF file.
	 * If NDEF file size is less than max supported LE, read only till NDEF file size.
	 * If NDEF file size is more than max supported LE, read with max supported LE.
	 * 
     * - Returns the expected length for the first NDEF read operation
     */
    func getFirstReadLe() -> Int{
        if((ndefFileCtrlData?.fileSize)! >= maxLe!) {            
            return maxLe!            
        } else {
            return (ndefFileCtrlData?.fileSize)!
        }
    }    
}
