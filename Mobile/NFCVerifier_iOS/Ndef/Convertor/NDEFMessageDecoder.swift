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
 * To decode the NDEF (NFC Data Exchange Format) message. Returns a generator function that decodes NDEF Records from a file-like,
 * byte-oriented stream or a bytes object given by the stream_or_bytes argument.
 */
final class NDEFMessageDecoder {
    
    // Singleton Object
    static let decryptor = NDEFMessageDecoder()
    
    private init(){}    
    
    /**
     * Method is to decode the stream of input data of NDEF message and return the decoded NDEF Message
     * - Parameter stream: Stream of data
     * - Returns the decrypted record
     */
    func decrypt(stream: Data) throws ->NDEFMessage {

        var records = Array<NDEFRecord>();
        var index = 0
        while (index < stream.count) {
            let header = stream[index]
            let tnf = (header & NDEFConstants.TNF_MASK)
            index += 1
            let typeLength = stream[index]
            index += 1
            let payloadLength = try getPayloadLength(shortRecord: (header & NDEFConstants.SR) != 0, stream: stream,index: &index)
            let idLength = try getIdLength(idLengthPresent: (header & NDEFConstants.IL) != 0,stream: stream,index: &index)
            let chunked:Bool = (header & NDEFConstants.CF) != 0
            let type:Data = getBytes(length: typeLength, stream: stream,index: &index)
            let id:Data = getBytes(length: idLength,stream: stream,index: &index)
            let payLoad:Data = getBytes(length: payloadLength,stream: stream,index: &index)
            if (records.isEmpty && (header & NDEFConstants.MB) == 0) {
                throw NDEFException.IllegalArgumentException("Missing Message Begin record in the NDEF Message")
            }            
            records.append(NDEFRecord(tnf: tnf, isChuncked: chunked, type: type, id: id, payload: payLoad))            
        }
        return  NDEFMessage(ndefRecords: records);
    }
        
    /**
     * Method is to extract the NDEF records from the NDEF message and then decode, return the list of NDEF record
	 * - Parameter stream: Stream of data
     */
    func decrypt(stream: NDEFMessage) throws -> [AbstractRecord] {
        var records:[AbstractRecord] = []
        for record in stream.getNdefRecords() {
            records.append(try NDEFRecordDecoder.getInstance().decrypt(record: record)!)
        }
        return records;
    }
    
     /*
      * Method to parse the length of the ID
      * - Parameter idLengthPresent:  Flag indicating that ID Length field is present in record
      * - Parameter stream: Stream of Data
      * - Parameter index: index of length
      * - returns the length of the ID value
     */
    private func getIdLength(idLengthPresent:Bool, stream:Data, index: inout Int) throws ->UInt8 {
        if (idLengthPresent) {
            let l = stream[index]
            index = index + 1
            return l
        } else {
            return 0
        }
    }
	
    /*
     * Method parse the length of the Payload data
     * - Parameter shortRecord:   Flag indicating that it is a short record
     * - Parameter stream: Stream of Data
     * - Parameter index: index of length field
     * - Returns the length of the payload
     */
    private func getPayloadLength(shortRecord: Bool, stream: Data, index:inout Int) throws ->UInt8 {
        if (shortRecord){
            let l = stream[index]
            index = index + 1
            return l
        }
        else {
            let buffer:Data = stream.subdata(in: 0..<4)
            index = index + 4
            return (((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16)
                    | ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF));
        }
    }
	
    /*
     * Method to return the NDEF message in bytes
     * - Parameter length: Length of the NDEF message
     * - Parameter stream: Stream of Data
     * - Parameter index: Start index
     * - Returns the NDEF message in bytes
     */
    private func getBytes(length: UInt8, stream: Data, index: inout Int)-> Data {
        if(length>0) {
            let bytesArray:Data = stream.subdata(in: index..<index+Int(length))
            index += Int(length)
            return bytesArray
            
        } else {
            return Data()
        }
    }    
}
