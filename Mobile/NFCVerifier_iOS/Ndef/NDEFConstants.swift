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
 * Consists of all the constants required for NFC NDEF
 */
class NDEFConstants {
  
    /**
     * The MB flag is a 1-bit field that when set indicates the start of an NDEF
     * message
     */
    static let MB:UInt8 = 0x80
    
	/**
     * The ME flag is a 1-bit field that when set indicates the end of an NDEF
     * message . Note, that in case of a chunked payload, the ME flag is set
     * only in the terminating record chunk of that chunked payload.
     */
    static let ME:UInt8 = 0x40;

    /**
     * The CF flag is a 1-bit field indicating that this is either the first
     * record chunk or a middle record chunk of a chunked payload .
     */
    static let CF:UInt8 = 0x20;

    /**
     * The SR flag is a 1-bit field indicating, if set, that the PAYLOAD_LENGTH
     * field is a single octet. This short record layout is intended for compact
     * encapsulation of small payloads which will fit within PAYLOAD fields of
     * size ranging between 0 to 255 octets.
     */
    static let SR:UInt8 = 0x10;

    /**
     * The IL flag is a 1-bit field indicating, if set, that the ID_LENGTH field
     * is present in the header as a single octet. If the IL flag is zero, the
     * ID_LENGTH field is omitted from the record header and the ID field is
     * also omitted from the record.
     */
    static let IL:UInt8 = 0x08;
	
    /**
     * Type Name Format Mask. The TNF field value indicates the structure of the
     * value of the TYPE field.
     */
    static let TNF_MASK:UInt8 = 0x07;
	
    /**
     * Specifies the empty byte array
     */
    static let EMPTY_BYTE_ARRAY:Data  = Data()
	
    /**
     * Specifies the empty NDEF Message byte[] array
     */    
    static let EMPTY_NDEF_MESSAGE = Data([ 0xD0, 0x00, 0x00])
    
    /**
     * An empty Type Name Format byte data
     */
    public static let  TNF_EMPTY:UInt8 = 0;
    
    public static let TNF_UNCHANGED:UInt8 = 6;
	
    /**
     * NFC Forum well-known type [NFC RTD] 0x01
     */
    public static let TNF_WELL_KNOWN:UInt8 = 1;
	
    /**
     * NFC Forum External type [NFC RTD] 0x04
     */
    public static let TNF_EXTERNAL_KNOWN:UInt8 = 4;
	
    /**
     * Media-type RFC 2046 - 0x02
     */
    public static let TNF_MEDIA_TYPE:UInt8 = 2;

    /**
     * Well known UTF type of "UTF-8" charset for record creation
     */
	 
    public static let UTF_8_CHARSET = CharacterSet.init(charactersIn: "UTF-8")
	
    /**
     * Well known default "US-ASCII" charset for record creation
     */
    public static let DEFAULT_CHARSET = String.Encoding.ascii    
}
