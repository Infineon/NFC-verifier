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
 * A Record type can be any NDEF well known record types. E.g Smart poster record, Text Record, etc.
 *
 * @author Infineon Technologies
 */
public class RecordType {

    private final byte[] type;

    /**
     * Constructor to set the record type with the give byte array.
     *
     * @param type record type which is of byte array
     */
    public RecordType(byte[] type) {
        this.type = type.clone();
    }

    /**
     * Constructor to set the record type with the give string type.
     *
     * @param type record type of string e.g new RecordType("T");
     */
    public RecordType(String type) {
        this.type = type.getBytes(NDEFConstants.DEFAULT_CHARSET);
    }

    /**
     * Method is to get the record type
     *
     * @return The type of record
     */
    public byte[] getType() {
        return type == null ? null : type.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(type);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RecordType other = (RecordType) obj;
        return Arrays.equals(type, other.type);
    }
}
