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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The NFC Local Type Name for the action is �t� (0x74).
 * The Smart Poster Type Record is used to declare the MIME type entity if the URI references an external entity.
 * It is used to tell the NFC compliant devices what kind of an object it can expect before it opens the connection.
 * This record type is also an optional feature.
 *
 * @author Infineon Technologies
 */
public class TypeRecord extends AbstractRecord {

    public static final Charset CONTENT_TYPE_CHARSET = StandardCharsets.UTF_8;

    public static final String[] COMMON_TYPES = {"text/plain", "image/jpeg",
            "audio/basic", "video/mpeg", "application/octet-stream",
            "application/postscript", "message/rfc822"};

    private String contentType;

    /**
     * Constructor is to create a type record.
     *
     * @param contentType The text record content with payload data
     */
    public TypeRecord(String contentType) {
        setType(contentType);
        setRecordType(new RecordType("t"));
        setTnf(NDEFConstants.TNF_WELL_KNOWN);
    }

    public TypeRecord() {
    }

    /**
     * Method is to get the content type
     *
     * @return The type content of plain text. Content type could be something like 'image/jpeg', 'video/mpeg', 'text/plain', etc.
     */
    public String getType() {
        return contentType;
    }

    /**
     * Method is to set the content type
     *
     * @param contentType The type content to set. Content type could be something like 'image/jpeg', 'video/mpeg', 'text/plain', etc.
     */
    public void setType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Type: " + contentType;
    }

    /**
     * Method returns 'true', if content type is available in the type record else returns 'false'
     *
     * @return 'true' if content type is non empty else returns 'false'
     */
    public boolean hasType() {
        if (contentType != null) {
            return !contentType.trim().isEmpty();
        }
        return false;
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
                + ((contentType == null) ? 0 : contentType.hashCode());
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
        if (!(obj instanceof String))
            return false;
        return contentType.trim().contentEquals(((String) obj).trim());
    }

}
