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
package com.infineon.ndef.converter;


import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.utils.NDEFConstants;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * To encode the NDEF (NFC Data Exchange Format) message.
 * Returns a generator function that encodes ndef Record objects into an NDEF Message octet sequence.
 *
 * @author Infineon Technologies
 */
public final class NDEFMessageEncoder {

	// Short Record Max Length
    private static final int MAX_LENGTH_FOR_SHORT_RECORD = 255;
	//Singleton Object
    private static NDEFMessageEncoder encryptor = new NDEFMessageEncoder();

    private NDEFMessageEncoder() {
    }

    public static NDEFMessageEncoder instance() {
        if (encryptor == null) {
            encryptor = new NDEFMessageEncoder();
        }
        return encryptor;
    }


    /**
     * Method to encode the collection of NDEF records and return as raw byte[] array
     *
     * @param ndefRecords Collection of NDEF records that are to be encoded to raw byte array data
     * @return raw byte[] array data that is encoded
     */
    public byte[] encrypt(NDEFRecord... ndefRecords) {
        if (ndefRecords == null || ndefRecords.length == 0) {
            throw new IllegalArgumentException("Invalid NDEF record Entry...");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte header = (byte) NDEFConstants.MB;
        for (int i = 0; i < ndefRecords.length; i++) {
            NDEFRecord tempRecord = ndefRecords[i];
            if (i == ndefRecords.length - 1) {
                header |= NDEFConstants.ME;
            }
            assembleRecord(stream, header, tempRecord);
            header = 0;
        }
        return stream.toByteArray();
    }

 	/**
     * Method to assemble the records
     * @param header Header of Record
     * @param tempRecord NDEFRecord Object
     */
    private void assembleRecord(ByteArrayOutputStream stream, byte header, NDEFRecord tempRecord) {
        appendHeader(stream, header, tempRecord);
        stream.write(Objects.requireNonNull(tempRecord.getType()).length);
        appendPayloadLength(stream, Objects.requireNonNull(tempRecord.getPayload()).length);
        appendIdLength(stream, Objects.requireNonNull(tempRecord.getId()).length);
        appendBytes(stream, tempRecord.getType());
        appendBytes(stream, tempRecord.getId());
        appendBytes(stream, tempRecord.getPayload());
    }

   	/**
     * Method to append the Record Header
     * @param header Header byte
     * @param ndefRecord NDEFRecord Object
     */
    private void appendHeader(ByteArrayOutputStream baos, byte header, NDEFRecord ndefRecord) {
        header = setShortRecord(header, ndefRecord);
        header = setIdLength(header, ndefRecord);
        header = setTypeNameFormat(header, ndefRecord);
        baos.write(header);
    }

	/**
     *  Method to set Short Record flag in header
     *  @param header Header byte
     *  @param ndefRecord NDEFRecord Object
     *  @return Updated Header
     */
    private byte setShortRecord(byte header, NDEFRecord ndefRecord) {
        if (Objects.requireNonNull(ndefRecord.getPayload()).length <= MAX_LENGTH_FOR_SHORT_RECORD) {
            header |= NDEFConstants.SR;
        }
        return header;
    }



    /**
     * Method to set the length of the if ID is present
     * @param header Header byte
     * @param ndefRecord NDEFRecord Object
     * @return Updated Header
     */
    private byte setIdLength(byte header, NDEFRecord ndefRecord) {
        if (ndefRecord.getId() != null && ndefRecord.getId().length > 0) {
            header |= NDEFConstants.IL;
        }
        return header;
    }

 	/**
     * Method to set Type Name Format header
     * @param header byte
     * @param ndefRecord Object
     * @return Updated Header
     */
    private byte setTypeNameFormat(byte header, NDEFRecord ndefRecord) {
        header |= ndefRecord.getTnf();
        return header;
    }

	/**
     * Method to append the byte array to payload byte stream
     * @param baos Payload byte stream
     * @param bytes Byte array to be append
     */
    private void appendBytes(ByteArrayOutputStream baos, byte[] bytes) {
        baos.write(bytes, 0, bytes.length);
    }

 	/**
     * Method to append the length of the ID field
     * @param baos Payload byte stream
     * @param length Length byte
     */
    private void appendIdLength(ByteArrayOutputStream baos, int length) {
        if (length > 0)
            baos.write(length);
    }

 	/**
     * Appends the payload length
     * @param baos Payload byte stream
     * @param length: Payload length
     */
    private void appendPayloadLength(ByteArrayOutputStream baos, int length) {
        if (length <= MAX_LENGTH_FOR_SHORT_RECORD) {
            baos.write(length);
        } else {
            byte[] payloadLengthArray = new byte[4];
            payloadLengthArray[0] = (byte) (length >>> 24);
            payloadLengthArray[1] = (byte) (length >>> 16);
            payloadLengthArray[2] = (byte) (length >>> 8);
            payloadLengthArray[3] = (byte) (length & 0xff);
            baos.write(payloadLengthArray, 0, payloadLengthArray.length);
        }
    }

    /**
     * Method to encode the collection of abstract records and return as raw byte array
	 *
     * @param records collection of abstract records that are to be encoded to raw bytes[] array
     * @return encoded raw bytes[] array data
     */
    public byte[] encrypt(AbstractRecord... records) {
        return encrypt(Arrays.asList(records));
    }

    /**
     * Method is to encode each individual abstract records and return as raw bytes[] array data
     *
     * @param records collection of abstract records that are to be encoded to raw bytes[]
     * @return encoded raw bytes[] array data
     */
    public byte[] encrypt(Iterable<? extends AbstractRecord> records) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encrypt(records, baos);
        return baos.toByteArray();
    }

    /**
     * Method is to encode each individual abstract records and return as raw bytes[] array data with Byte Array Output Stream
     *
     * @param records collection of abstract records that are to be encoded to raw bytes[]
     * @param baos    Byte Array Output Stream to assemble the record
     */
    public void encrypt(Iterable<? extends AbstractRecord> records, ByteArrayOutputStream baos) {
        byte header = (byte) NDEFConstants.MB;
        for (Iterator<? extends AbstractRecord> it = records.iterator(); it.hasNext(); ) {
            AbstractRecord record = it.next();
            header = setMessageEndIfLastRecord(it, header);

            NDEFRecord ndefRecord = NDEFRecordEncoder.instance().encrypt(record);

            assembleRecord(baos, header, ndefRecord);
            header = 0;
        }

    }

    /**
     * Method is to set message end bit to header byte if record is last
     *
     * @param it Iterator of abstract records
     * @param header Header byte
     * @return updated Header
     */
    private byte setMessageEndIfLastRecord(Iterator<? extends AbstractRecord> it, byte header) {
        if (!it.hasNext()) {
            header |= NDEFConstants.ME;
        }
        return header;
    }
}
