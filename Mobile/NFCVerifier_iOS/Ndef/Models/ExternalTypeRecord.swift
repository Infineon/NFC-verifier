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
 * A NDEF external type record is a user-defined value, based on rules in the NFC Forum Record Type Definition specification.
 * The payload doesn't need to follow any specific structure like it does in other well known records types.
 */
class ExternalTypeRecord : AbstractRecord {
    private var payload : Data?
    
    /**
     * Constructor to create an external type record with user defined data bytes.
     * - Parameter data: External type record data bytes
     */
    init(payload : Data){
        super.init()
        self.payload = payload
        self.recordTnf = NDEFConstants.TNF_EXTERNAL_KNOWN
        setRecordType(recordType: RecordType(type: "ET"))
    }

    /**
     * Constructor to create an external type record with user defined data bytes.
     * - Parameter data: External type record data bytes
     * - Parameter recordType: record Type
     */
    init(payload: Data, recordType: RecordType){
        super.init()
        self.payload = payload
        self.recordTnf = NDEFConstants.TNF_EXTERNAL_KNOWN
        setRecordType(recordType: recordType)
    }
	
    /**
     * Method is to get the raw record data bytes stream.
     * - Returns external type record data bytes
     */
    public func getData() -> Data? {
        return payload
    }
    
    /**
     * Method is to set the raw record data bytes stream to the record.
     * - Parameter data" Set external type record data bytes
     */
    public func setData(payload: Data) {
        self.payload = payload
    }
}

