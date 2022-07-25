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
 * To encode the NDEF (NFC Data Exchange Format) message.
 * Returns a generator function that encodes NDEF Record objects into an NDEF Message octet sequence.
 */
final class NDEFMessageEncoder {
    
    // Short Record Max Length
    static let  MAX_LENGTH_FOR_SHORT_RECORD = 255
    
    //Singleton Object
    public static let encryptor = NDEFMessageEncoder()
    
    private init(){}
    
    /**
     * Method to encode the collection of NDEF records and return as raw byte array
     * - Parameter ndefRecords: Collection of NDEF records that are to be encoded to raw byte array data
     * - Returns raw byte array data that is encoded
     */
    func encrypt(ndefRecords:Array<NDEFRecord>) throws -> Data  {
        
        if(ndefRecords.count == 0) {
            throw NDEFException.obvious("Invalid NDEF record Entry...")
        }
        var stream = Data()
        var header = NDEFConstants.MB
        var i = 0
        
        while i<ndefRecords.count {
            let tempRecord  = ndefRecords[i]
            if(i == ndefRecords.count-1) {
                header |= NDEFConstants.ME
            }
            let record = assembleRecord(header: header, tempRecord: tempRecord)
            
            stream.append(record)
            
            header=0x00;
            i+=1
        }
        return stream
    }

    /**
     * Method to assemble the records
     * - Parameter header: Header of Record
     * - Parameter tempRecord: NDEFRecord Object
     * - Returns assembled records
     */
    private func assembleRecord(header:UInt8, tempRecord:NDEFRecord)->Data {
        let newheader = appendeHeader(header: header,ndefRecord: tempRecord );
        var stream = Data([UInt8(tempRecord.type.count)])
        stream = appendPayloadLength(stream: stream, length: UInt8(tempRecord.payLoad.count));
        if (tempRecord.id.count > 0) {
            stream = appendIdLength(stream: stream, length: UInt8(tempRecord.id.count));
        }
        stream.append(tempRecord.type)
        stream.append(tempRecord.id)
        stream.append(tempRecord.payLoad)
        var newStream = Data([newheader])
        newStream.append(stream)
        return newStream
    }
	
    /**
     * Method to append the Record Header
     * - Parameter header: Header byte
     * - Parameter ndefRecord: NDEFRecord Object
     * - Returns Updated Header
     */
    private func appendeHeader(header:UInt8, ndefRecord:NDEFRecord)->UInt8 {
        var newheader = header
        newheader = setShortRecord(header: newheader, ndefRecord: ndefRecord);
        newheader = setIdLength(header: newheader, ndefRecord: ndefRecord);
        newheader = setTypeNameFormat(header: newheader, ndefRecord: ndefRecord);
        return newheader        
    }
	
    /**
     *  Method to set Short Record flag in header
     * - Parameter header: Header byte
     * - Parameter ndefRecord: NDEFRecord Object
     * - Returns Updated Header
     */
    private func setShortRecord (header:UInt8, ndefRecord:NDEFRecord)->UInt8 {        
        if (ndefRecord.payLoad.count <= NDEFMessageEncoder.MAX_LENGTH_FOR_SHORT_RECORD) {
            return header | NDEFConstants.SR;
        }
        return header;
    }
    
    /**
     * Method to set the length of the if ID is present
     * - Parameter header: Header byte
     * - Parameter ndefRecord: NDEFRecord Object
     * - Returns Updated Header
     */
    private func setIdLength(header:UInt8, ndefRecord:NDEFRecord)->UInt8 {        
        if (ndefRecord.id.count > 0) {
           return header | NDEFConstants.IL;
        }
        return header;
    }
	
    /**
     * Method to set Type Name Format header
     * - Parameter: Header byte
     * - Parameter: NDEFRecord Object
     * - Returns Updated Header
     */
    private func setTypeNameFormat(header:UInt8,ndefRecord:NDEFRecord)->UInt8 {
        let tnf = ndefRecord.tnf
        let newHeader = header | tnf
        return newHeader
        
    }
    
    /**
     * Appends the payload length
     * - Parameter stream: Payload byte stream
     * - Parameter length: Payload length
     * - Returns Updated Header
     */
    private func appendPayloadLength(stream:Data, length: UInt8)->Data {
        var newStream = stream
        if (length <= NDEFMessageEncoder.MAX_LENGTH_FOR_SHORT_RECORD) {
            newStream.append(length)
        }else {
                var payloadLengthArray: [UInt8] = Array(repeating: 0, count: 4);
                payloadLengthArray[0] = UInt8((length >> 24));
                payloadLengthArray[1] = UInt8((length >> 16));
                payloadLengthArray[2] = UInt8((length >> 8));
                payloadLengthArray[3] = UInt8((length & 255));
                newStream.append(contentsOf: payloadLengthArray)          
        }
        return newStream
    }
    
    /**
     * Method to append the length of the ID field
     * - Parameter stream: Payload byte stream
     * - Parameter length: Length byte
     * - Returns Updated payload
     */
    private func appendIdLength(stream:Data,length:UInt8)->Data {
        var newStream = stream
        newStream.append(length)
        return newStream
    }
}
