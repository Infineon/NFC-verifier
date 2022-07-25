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
 * Each NDEF well known type record is an abstract record. Abstract Record is used to get/set record type.
 * Gets record ID, Gets and sets the key value. Gets and sets the type name format.
 *
 */
class AbstractRecord
{
    var id = Data()
    var recordType:RecordType?
    var recordTnf:UInt8?
    
    /**
     * Method is to get the type of NDEF record
     * - Returns: The type of record
     */
    public func getRecordType()->RecordType? {
        return recordType
    }
    
    /**
     * Method is to set to a specific record type
     * - Parameter recordType: sets the type of record
     */
    func setRecordType(recordType:RecordType) {
        self.recordType = recordType
    }
    
    /**
     * Method is to get the NDEF record ID
     * - Returns The record id
     */
    public func getId()-> Data {
        return id
    }
    
    /**
     * Method is to set the ID for a well known record
     * - Parameter id: sets the record id
     */
    public func setId(id:Data) {
        self.id = id
    }
    
    /**
     * Method is to set the key ID for a NDEF record
     * - Parameter key: sets the key value
     */
    public func setKey(key:String) {
        self.id = key.data(using: NDEFConstants.DEFAULT_CHARSET )!
    }
	
    /**
     * Method is to get the key type of a NDEF record
     * - Returns The key value
     */
    public func getKey()-> String {
        return String(data: id, encoding: NDEFConstants.DEFAULT_CHARSET)!
    }
	
    /**
     * Method is to set the type name format for a record
     * - Parameter tnf: sets the type name format
     */
    public func setTnf(tnf:UInt8) {
        self.recordTnf = tnf
    }
	
    /**
     * Method is to get the type name format of a NDEF record
     * - Returns The type name format
     */
    public func getTnf()->UInt8? {
        return recordTnf
    }
	
    /**
     * Method returns true, if the record has a key ID in it
     * - Returns Status based on key availability
     */
    public func hasKey()->Bool {
        return  id.count > 0;
    }    
}
