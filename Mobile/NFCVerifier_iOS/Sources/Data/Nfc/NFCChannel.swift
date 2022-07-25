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
import CoreNFC

/*
 * Contains the handlers to transmit ISO7816 commands and to receive responses
 */
public class NFCChannel {
    var tag: NFCISO7816Tag
    var readerSession: NFCTagReaderSession
    let logger:FileLogger = LoggerFactory.getLogger(logger: .File) as! FileLogger
    
    /**
     * Initializes the command handler with CoreNFC handles
     *  - Parameter tag_iso7816: ISO7816 tag handle used for communication with the tag
     *  - Parameter reader_session: NFCTagReaderSession handle used to manage the tag reader session
	 */
    init(tagISO7816: NFCISO7816Tag, readerSession: NFCTagReaderSession) {
        self.tag = tagISO7816
        self.readerSession = readerSession
    }
    
    /** 
	 * Transmits the command APDU to the NFC tag and returns the response through the response event handler
     *  - Parameter command: Command APDU to be transmitted
     *  - Parameter onResponseEvent: Response event handler
     */
    func Transmit(command: APDUCommand, onResponseEvent: @escaping (APDUResponse) -> Void) {
        let commandAPDU = NFCISO7816APDU(data: command.command)
        self.logger.log(tag:getCommandName(command: command.command), value:"")
        self.logger.log(tag: "-->", value: command.command)
        let timeLogger  = TimeLogger()
        timeLogger.start()
        self.tag.sendCommand(apdu: commandAPDU!) { [self] (response: Data, sw1: UInt8, sw2: UInt8, error: Error?)
            in
            if(response.count>0){
                self.logger.log(tag: "<--", value: response)
            }
            let responseAPDU = APDUResponse(sw1: sw1, sw2: sw2, data: response)
            self.logger.log(tag: "SW:", value:"\(responseAPDU.getSWString())\tData: \(response)\tExec Time: \(timeLogger.get())\n")            
            onResponseEvent(responseAPDU)
        }
    }
	
    /**
     *  Method to read binary file from NFC tag without SFID
     *  - Parameter le: Expected length
     *  - Parameter totalBytesToRead: Maximum bytes to read
     *  - Parameter onSuccess: Callback method to indicate success
     *  - Parameter onError: Callback method to indicate failure
     */
    func readBinary(le:UInt8, totalBytesToRead:Int, onSuccess: @escaping (Data) -> Void, onError: @escaping (String) -> Void) throws {        
        try self.readBinary(readWithSFID: false, le:le,totalBytesToRead:totalBytesToRead, prevReceivedData: Data(), offset: 0x00, onSuccess: {  data in
            onSuccess(data)
        }, onError: { error in
            onError(error)
        },calculateRemainingBytes: nil)
    }
  

    /**
     *  Method to read binary file from NFC tag with SFID
     *  - Parameter le: Expected length
     *  - Parameter totalBytesToRead: Maximum bytes to read
     *  - Parameter isReadWithSFID: Flag to indicate that SFID is used
     *  - Parameter prevReceivedData: Data received during previous read operation. Empty in case of first iteration.
     *  - Parameter offset: Offset to be read
     *  - Parameter onSuccess: Callback method to indicate success
     *  - Parameter onError: Callback method to indicate failure
     *  - Parameter calculateRemainingBytes: Method to calculate the remaining bytes to be read
     */
    func readBinary(readWithSFID: Bool,le:UInt8,totalBytesToRead:Int, prevReceivedData: Data, offset: UInt16, onSuccess: @escaping (Data) -> Void, onError: @escaping (String) -> Void, calculateRemainingBytes: ((Data) -> Int)?) throws {
        // if SFID present then build command with SFID else build with offset only
        let command  = try APDUCommandBuilder.buildReadBinaryCommand(offset: offset, le: le)
        Transmit(command: command) { responseAPDU in
            if(responseAPDU.isSuccessSW()) {
                var totalReceivedData = prevReceivedData
                totalReceivedData.append(responseAPDU.data!)
                var newTotalBytesToRead = totalBytesToRead
                if(totalBytesToRead==0) {
                    if(calculateRemainingBytes != nil) {
                        newTotalBytesToRead = calculateRemainingBytes!(totalReceivedData)
                    }
                }
                
                if(totalReceivedData.count < newTotalBytesToRead) {
                    
                    let newOffset = self.calculateOffset(readWithSFID: readWithSFID,offset: offset,data: responseAPDU.data!)
                    do {
                        try self.readBinary(readWithSFID:false,le: le, totalBytesToRead:newTotalBytesToRead, prevReceivedData: totalReceivedData, offset: newOffset, onSuccess: { data in
                            onSuccess(data)
                        }, onError: { error in
                            onError(error)
                        },calculateRemainingBytes: nil)
                    } catch {
                        onError("Read Binary Exception")
                    }
                } else {
                    onSuccess(totalReceivedData)
                }
            } else {
                onError(responseAPDU.getSWString())
            }
        }
    }
    
    /**
     *  Method to calculate the next offset for the next iteration of read binary operation
     *  - Parameter readWithSFID:  Flag to indicate that SFID is used
     *  - Parameter offset: Offset to be read
     *  - Parameter data: Data received during previous read operation
     *  - returns the next offset value for read binary operation
     */
    private func calculateOffset(readWithSFID:Bool, offset: UInt16, data:Data)->UInt16 {
        if(readWithSFID) {
            var parameters = [UInt8]()
            withUnsafeBytes(of: offset.bigEndian) {
                parameters.append(contentsOf: $0)
            }
            let p2 = parameters[1]
            return UInt16(p2 + UInt8(data.count))
        } else {
            return offset + UInt16(data.count)
        }
	}
	
	/**
     * Function returns the name of command which can be used for logging
	 * - Parameter command:  APDU command
	 * - Returns the name of the command as string
     */
    func getCommandName(command:Data) -> String {
        var commandName = ""
        if(command.count >= 4 ) {
            if(command[1] == 0xA4) { //  Select command
                if(command[2] == 0x04) {
                    commandName = "Select File by AID"
                }
                if(command[2] == 0x00) {
                    commandName = "Select File by FID"
                }
            }
            else if(command[1] == 0xB0) {
                commandName = "Read Binary"
            }
            else  if(command[1] == 0x84) {
                commandName = "Get Challenge"
            }
            else  if(command[1] == 0x82) {
                commandName = "Mutual Authenticate"
            } else {
                commandName =  "Unknown"
            }
        } else {
            commandName =  "Unknown"
        }
        return commandName    
	}
}
	

