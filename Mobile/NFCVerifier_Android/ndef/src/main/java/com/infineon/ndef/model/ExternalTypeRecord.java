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
 * A NDEF external type record is a user-defined value, based on rules in the NFC Forum Record Type Definition specification.
 * The payload doesn't need to follow any specific structure like it does in other well known records types.
 *
 * @author Infineon Technologies
 */
public class ExternalTypeRecord extends AbstractRecord {

    private byte[] data;

    /**
     * Constructor to create an external type record with user defined data bytes.
     *
     * @param data External type record data bytes
     */
    public ExternalTypeRecord(byte[] data) {
        this.data = data.clone();
        setRecordType(new RecordType("ET"));
    }

    /**
     * Method is to get the raw record data bytes stream.
     *
     * @return Get external type record data bytes
     */
    public byte[] getData() {
        return data == null ? null : data.clone();
    }

    /**
     * Method is to set the raw record data bytes stream to the record.
     *
     * @param data Set external type record data bytes
     */
    public void setData(byte[] data) {
        this.data = data.clone();
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
                + ((data == null) ? 0 : Arrays.hashCode(data));
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
        ExternalTypeRecord other = (ExternalTypeRecord) obj;
        return Arrays.equals(data, other.data);
    }

}
