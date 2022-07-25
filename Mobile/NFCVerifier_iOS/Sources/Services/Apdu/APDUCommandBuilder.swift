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
 *  APDUCommandBuilder is a builder class used to build APDU commands
 */
class APDUCommandBuilder  {
    
    /**
     * Builds the select file command
     * - Parameter p1: Parameter 1 of APDU command
     * - Parameter p2: Parameter 2 of APDU command
     * - Parameter data: Data of APDU command
     * - Parameter le: Expected length
     * - Returns APDUCommand : APDU command to select the file
     */
    static func buildSelectFileCommand(p1:UInt8, p2:UInt8, data:Data,le:UInt8? ) throws -> APDUCommand {
        let cla:UInt8 = 0x00
        let ins:UInt8 = 0xA4
        let lc = UInt8(data.count)
        var command = Data(_: [cla, ins, p1, p2, lc])
        command.append(data)
        if(le != nil) {
            command.append(le!)
        }
        return try APDUCommand(command: command)
   }
    /**
     * Builds the read binary file command
     * - Parameter offset: Offset for the binary read operation as UInt16
     * - Parameter le: Expected length as UInt8
     * - Returns APDUCommand: APDU command to read binary file
     */
    static func buildReadBinaryCommand(offset: UInt16, le: UInt8)throws -> APDUCommand {
        let cla:UInt8 = 0x00
        let ins:UInt8 = 0xB0
        var command = Data(_: [cla, ins])
        withUnsafeBytes(of: offset.bigEndian) {
            command.append(contentsOf: $0)
        }
        command.append(le)
        return try APDUCommand(command: command)
    }
    
    /**
     * Builds the APDU command from bytes
     * - Parameter byte: APDU command in bytes
     * - Returns APDUCommand: APDU command
     */
    static func buildCommand(byte: Data) throws -> APDUCommand {
        return try APDUCommand(command: byte)
    }    
}
