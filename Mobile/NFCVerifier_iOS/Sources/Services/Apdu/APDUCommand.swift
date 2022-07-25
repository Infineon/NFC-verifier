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
 * Stores the command APDU
 */
class APDUCommand {
    
    private let OFFSET_FOR_CLA = 0
    private let OFFSET_FOR_INS = 1
    private let OFFSET_FOR_P1 = 2
    private let OFFSET_FOR_P2 = 3
    private let OFFSET_FOR_LC_OR_LE = 4
    
    var command: Data
    //Class byte (CLA)
    var cla: UInt8
    //Instruction byte (INS)
    var ins: UInt8
    //Parameter byte 1 (P1)   
    var p1: UInt8
    //Parameter byte 2 (P2)   	
    var p2: UInt8
    // Data field length (Lc)
    var lc:UInt8?    
    // Data field: Optional.
    var data: Data?    
    // Expected Response Length (Le)
    var le:UInt8?
    
    /*
     * Initializes the class with the APDU input
     * - Parameter command: Command APDU
	 */
    init(command: Data) throws
    {
        if(command.count < 4) {
            fatalError("APDU length must not be less than 4")
        }
        var iOffset = OFFSET_FOR_LC_OR_LE
        self.command = command

        self.cla = command[OFFSET_FOR_CLA]
        self.ins = command[OFFSET_FOR_INS]
        self.p1 = command[OFFSET_FOR_P1]
        self.p2 = command[OFFSET_FOR_P2]
        
        if(command.count == 4) {  // Case 1            
            le = 0x00
        } else {
            // get Short Lc or Le
            
            let iValue = command[iOffset]
            iOffset += 1
            if(command.count == 5) { // case 2
                self.le = iValue
                
            } else if (iValue != 0 ||  command.count < 7 ) { // case  3 or 4 short
                
                if(command.count == 5 + iValue ) { //case 3 short
                    self.lc = iValue
                    self.le = 0
                    
                }
                else  if(command.count == 6 + iValue) { // case 4 short
                    self.lc = iValue
                    self.le = (command[iOffset + Int(iValue)])
                    
                } else {
                    throw APDUException.KnownException("APDU has an Incorrect Lc Byte or Data length ")
                }
                let partData = command.suffix(from: iOffset)
                self.data = partData.prefix(Int(self.lc!))
                
            } else {
                // Handle Extended Lc / Le. Not implemented in this version.
            }                        
        }                  
    }    
}
