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

import com.infineon.ndef.utils.NDEFConstants;


import java.util.Arrays;

/**
 * The NFC Local Type Name for the action is  (0x73).
 * The Smart Poster Size Record is used to tell the size of the object if the URI references an external entity.
 * It helps in advance capability checks of the reader device to process the object. This record is also an optional.
 *
 * @author Infineon Technologies
 */
public class SizeRecord extends AbstractRecord {

    private byte[] sizeBytes;

    /**
     * Constructor to create a new size record.
     *
     * @param sizeBytes Record Size bytes
     */
    public SizeRecord(byte[] sizeBytes) {
        setSize(sizeBytes);
        setRecordType(new RecordType("s"));
        setTnf(NDEFConstants.TNF_WELL_KNOWN);
    }

    public SizeRecord() {
    }

    /**
     * Method is to get the size of record in bytes[]
     *
     * @return Size in byte array of the record
     */
    public byte[] getSize() {
        return sizeBytes == null ? null : sizeBytes.clone();
    }

    /**
     * Method is to set the size for the size record in bytes[]
     *
     * @param sizeBytes Sets the size in byte array for the record
     */
    public void setSize(byte[] sizeBytes) {
        this.sizeBytes = sizeBytes.clone();
    }


    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (byte b : sizeBytes) {
            builder.append(String.format("%02x", b));
        }
        return "Size: " + builder.toString();
    }

    /**
     * Method returns 'true', if the record size is non-empty else returns 'false'
     *
     * @return State based on whether size of record is empty or not
     */
    public boolean hasSize() {
        return (sizeBytes != null);
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((sizeBytes == null) ? 0 : Arrays.hashCode(sizeBytes));
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
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof byte[]))
            return false;
        return Arrays.equals(sizeBytes, (byte[]) obj);
    }

}
