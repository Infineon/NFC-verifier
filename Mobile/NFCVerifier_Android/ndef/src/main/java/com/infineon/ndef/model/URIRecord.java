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


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The NFC Local Type Name for the action is "U"(0x55).
 * URI record is a record that stores the URI information such as a website URL in a tag.
 * URI is the core of the smart poster, and all the records are just associated with metadata about this record.
 * Only one URI record can be present in the smart poster record.
 *
 * @author Infineon Technologies
 */
public class URIRecord extends AbstractRecord {

    public static final Charset DEFAULT_URI_CHARSET = StandardCharsets.UTF_8;
    public static final String[] ABBRIVIABLE_URIS = {
            "", "http://www.", "https://www.", "http://", "https://", "tel:",
            "mailto:", "ftp://anonymous:anonymous@", "ftp://ftp.", "ftps://", "sftp://", "smb://", "nfs://", "ftp://",
            "dav://", "news:", "telnet://", "imap:", "rtsp://", "urn:", "pop:", "sip:", "sips:", "tftp:", "btspp://",
            "btl2cap://", "btgoep://", "tcpobex://", "irdaobex://", "file://", "urn:epc:id:", "urn:epc:tag:",
            "urn:epc:pat:", "urn:epc:raw:", "urn:epc:", "urn:nfc:"};
    private String uri;

    /**
     * Constructor is to create a new URI record
     *
     * @param uri Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     */
    public URIRecord(String uri) {
        this.uri = uri;
        setRecordType(new RecordType("U"));
    }

    /**
     * Method is to get the Uniform Resource Identifier string
     *
     * @return The Uniform Resource Identifier (URI).
     */
    public String getUri() {
        return uri;
    }

    /**
     * Method is to set the Uniform Resource Identifier string onto the record
     *
     * @param uri sets the Uniform Resource Identifier (URI).
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Uri: [" + uri + "]";
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
        if (!(obj instanceof URIRecord))
            return false;
        URIRecord other = (URIRecord) obj;
        if (uri == null) {
            return other.uri == null;
        } else return uri.equals(other.uri);
    }

}
