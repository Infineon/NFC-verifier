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

package com.infineon.ndef;


import com.infineon.ndef.utils.Utils;

/**
 * NFC Data Exchange Format (NDEF) Record.
 * Contains the following parameters or fields
 * <br>
 * <br>
 * <b><i>Payload Length</i></b> :Regardless of the relationship of the record to other record, the payload length always indicates the length of the payload encapsulated in this record.
 * <br>
 * <br>
 * <b><i>Payload Type</i></b> :The payload type of a record indicates the kind of data being carried in the payload of that record. This may be used to guide the processing of the payload at the description of the user application.
 *
 * <br>
 * <br>
 * <b><i>Payload Identification</i></b>:The optional payload identifier allows user application to identify the payload carried within an NDEF record. By providing a payload identifier, it becomes possible for other payloads supporting URI-Based linking technologies to refer to that payload.
 *
 * <br>
 * <br>
 * <b><i>TNF (Type Name Format)</i></b>:The TNF field value indicates the structure of the value of the Type Field. The TNF field is 3-bit field.
 * <br>
 * Following are the types of TNF supported:
 * <br><i>Empty (0x00)</i>
 * <br><i>NFC Forum well-Known type [NFC RTD](0x01)</i>
 * <br><i> Media-Type as defined in RFC 2046 [RFC 2046](0x02)</i>
 * <br><i>Absolute URI as Defined In RFC 3986 [RFC 3986](0x03)</i>
 * <br><i>NFC Forum external Type [NFC RTD](0x04)</i>
 * <br><i>Unknown(0x05)</i>
 * <br><i>Unchanged (0x06)</i>
 * <br><i>Reserved(0x07)</i>
 * <br><br>
 * <b><i>TNF (Type Name Format)</i></b>:Application data that has been partitioned into multiple chunks each carried in a separate
 * NDEF record, where each of these records except the last has the CF flag set to 1. This
 * facility can be used to carry dynamically generated content for which the payload size is
 * not known in advance or very large entities that don't fit into a single NDEF record.
 *
 * @author Infineon Technologies
 */
public final class NDEFRecord {

    /**
     * Type Name Format field. The Type Name Format or TNF Field of NDEF Record is 3-bit Value that describe the record type, and sets the expectation for the Structure and Content of the rest of the record
     */
    private final byte tnf;

    /**
     * The variable length Type field
     */
    private final byte[] type;

    /**
     * Identifier meta data
     */
    private final byte[] id;
    /**
     * The actual payLoad
     */
    private final byte[] payload;

    /**
     * Application data that has been partitioned into multiple chunks each carried in a separate
     * NDEF record, where each of these records except the last has the CF flag set to 1. This
     * facility can be used to carry dynamically generated content for which the payload size is
     * not known in advance or very large entities that don't fit into a single NDEF record.
     */
    private boolean isChunked;

    /**
     * This constructor is used to create NDEF Record. Each Record is made up of header such as the record type, and so forth, and the payload, which contains the content of the message.

     * @param tnf     Record Type Name Format
     * @param type    Record Type
     * @param id      Record ID
     * @param payload Record Payload Data
     */
    public NDEFRecord(byte tnf, byte[] type, byte[] id, byte[] payload) {
        this.tnf = tnf;
        this.type = type;
        this.id = id;
        this.payload = payload;
    }

    /**
     * This constructor is used to create a NDEF record. Each record is made up of a header, which contains message about the record,
     * such as the record type, length, and so forth, and the payload, which contains the content of the message
     *
     * @param tnf     Record Type Name Format
     * @param chunked Specifies whether the data is chunk
     * @param type    Record Type
     * @param id      Record ID
     * @param payload Record Payload Data
     */
    public NDEFRecord(byte tnf, boolean chunked, byte[] type, byte[] id, byte[] payload) {
        this(tnf, type, id, payload);
        this.isChunked = chunked;
    }

    /**
     * This method returns the TNF: Type Name Format Field. The Type Name Format or TNF Field of an NDEF record is a 3-bit value that
     * describes the record type, and sets the expectation for the structure and content of the rest of the record
     *
     * @return Returns the 3-bit TNF.
     */
    public byte getTnf() {
        return tnf;
    }

    /**
     * This method returns the record type
     *
     * @return The variable length Type field.
     */
    public byte[] getType() {
        return type == null ? null : type.clone();
    }

    /**
     * This method returns the record ID(in bytes)
     *
     * @return Returns the variable length ID.
     */
    public byte[] getId() {
        return id == null ? null : id.clone();
    }

    /**
     * Indicates the length (in bytes) of the record payload.
     *
     * @return Returns the variable length payload.
     */
    public byte[] getPayload() {
        return payload == null ? null : payload.clone();
    }

    /**
     * The CF flag indicates if this is the first record chunk or a middle record chunk
     *
     * @return true if record is chunked
     */
    public boolean isChunked() {
        return isChunked;
    }

    @Override
    public String toString() {
        String format = "";
        format += "tnf = " + Utils.toHexString(new byte[]{tnf}) + ",";
        try {
            format += "type = " + (char) Utils.toInteger(type) + ",";
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        format += "payload = " + Utils.toHexString(payload) + ",";
        format += "chunked = " + isChunked + ",";
        format += "id = " + Utils.toHexString(id);
        return "[" + format + "]";
    }
}
