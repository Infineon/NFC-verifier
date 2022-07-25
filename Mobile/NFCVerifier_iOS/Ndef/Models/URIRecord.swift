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
 * The NFC Local Type Name for the action is "U" (0x55).
 * URI record is a record that stores the URI information such as a website URL in a tag.
 * URI is the core of the smart poster, and all the records are just associated with metadata about this record.
 * Only one URI record can be present in the smart poster record.
 *
 */
class URIRecord : AbstractRecord {
    
    public static let DEFAULT_URI_CHARSET = Swift.String.Encoding.utf8
    
    private var uri: String
    
    public static let ABBRIVIABLE_URIS = ["","http://www.", "https://www.", "http://", "https://","tel:",
                "mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://",
                "dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://",
                "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:",
                "urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:"]
    /**
     * Constructor is to create a new URI record
     * - Parameter uri: Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     */
    public init(uri: String) {
        self.uri=uri
        super.init()
        self.setRecordType(recordType: RecordType(type: "U"))
    }
    
    /**
     * Method is to get the Uniform Resource Identifier string
     * - Returns  The Uniform Resource Identifier (URI).
     */
    public func getUri()->String {
        return uri
    }
	
    /**
     * Method is to set the Uniform Resource Identifier string onto the record
     * - Parameter uri: sets the Uniform Resource Identifier (URI).
     */
    public func setUri( uri:String) {
        self.uri = uri
    }
}
