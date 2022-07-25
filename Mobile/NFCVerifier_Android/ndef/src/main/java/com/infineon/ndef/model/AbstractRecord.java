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
package com.infineon.ndef.model;

import java.util.Arrays;

/**
 * Each NDEF well known type record is an abstract record. Abstract Record is used to get/set record type.
 * Gets record ID, Gets and sets the key value. Gets and sets the type name format.
 *
 * @author Infineon Technologies
 */
public abstract class AbstractRecord {

    protected byte[] id = new byte[0];

    private RecordType recordType;

    private byte recordTnf;

    /**
     * Method is to get the type of NDEF record
     *
     * @return The type of record
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Method is to set to a specific record type
     *
     * @param recordType sets the type of record
     */
    protected void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    /**
     * Method is to get the NDEF record ID
     *
     * @return The record id
     */
    public byte[] getId() {
        return id;
    }

    /**
     * Method is to set the ID for a well known record
     *
     * @param id sets the record id
     */
    public void setId(byte[] id) {
        this.id = id;
    }

    /**
     * Method is to get the key type of a NDEF record
     *
     * @return The key value
     */
    public String getKey() {
        return new String(id);
    }

    /**
     * Method is to set the key ID for a NDEF record
     *
     * @param key sets the key value
     */
    public void setKey(String key) {
        this.id = key.getBytes();
    }

    /**
     * Method is to get the type name format of a NDEF record
     *
     * @return The type name format
     */
    public byte getTnf() {
        return recordTnf;
    }

    /**
     * Method is to set the type name format for a record
     *
     * @param tnf sets the type name format
     */
    public void setTnf(byte tnf) {
        this.recordTnf = tnf;
    }

    /**
     * Method returns true, if the record has a key ID in it
     *
     * @return Status based on key availability
     */
    public boolean hasKey() {
        return id != null && id.length > 0;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(id);
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj Object: the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractRecord other = (AbstractRecord) obj;
        return Arrays.equals(id, other.id);
    }

}
