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


import com.infineon.ndef.NDEFMessage;
import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.NFCException;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.utils.NDEFConstants;
import com.infineon.ndef.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * To decode the NDEF (NFC Data Exchange Format) message. Returns a generator function that decodes NDEF Records from a file-like,
 * byte-oriented stream or a bytes object given by the stream_or_bytes argument.
 *
 * @author Infineon Technologies
 */

public final class NDEFMessageDecoder {

    private NDEFMessageDecoder() {
    }
	
	/**
     * Method is to create instance of class
	 * 
	 * @return NDEFMessageDecoder instance
	 */
    public static NDEFMessageDecoder instance() {
        return new NDEFMessageDecoder();
    }

    /**
     * Method is to decode the stream of input data of NDEF message and return the decoded NDEF Message
	 *
     * @param ndefMessage NDEF Message as byte array 
     * @param offset      offset information
     * @param length      length of data to be decrypted
     * @return returns the decrypted record information
     */
    public NDEFMessage decrypt(byte[] ndefMessage, int offset, int length) {
        ByteArrayInputStream bais = new ByteArrayInputStream(ndefMessage,
                offset, length);
        return decrypt(bais);
    }

    /**
     * Method is to decode the stream of input data of NDEF message and return the decoded NDEF Message
     *
     * @param ndefMessage NDEF Message
     * @return returns the decoded NDEF Message
     */
    public NDEFMessage decrypt(byte[] ndefMessage) {
        return decrypt(ndefMessage, 0, ndefMessage.length);
    }

    /**
     * Method is to decode the stream of input data of ndef message and return the decoded NDEF Message
     *
     * @param stream stream of data
     * @return returns the decoded NDEF Message
     */
    public NDEFMessage decrypt(InputStream stream) {

        List<NDEFRecord> records = new ArrayList<>();
        try {
            while (stream.available() > 0) {
                int header = stream.read();
                byte tnf = (byte) (header & NDEFConstants.TNF_MASK);

                int typeLength = stream.read();
                int payloadLength = getPayloadLength(
                        (header & NDEFConstants.SR) != 0, stream);
                int idLength = getIdLength((header & NDEFConstants.IL) != 0,
                        stream);
                boolean chunked = (header & NDEFConstants.CF) != 0;

                byte[] type = Utils.getBytesFromStream(typeLength, stream);
                byte[] id = Utils.getBytesFromStream(idLength, stream);
                byte[] payload = Utils.getBytesFromStream(payloadLength,
                        stream);
                if (records.isEmpty() && (header & NDEFConstants.MB) == 0)
                    throw new IllegalArgumentException(
                            "Missing Message Begin record in the NDEF Message");

                if (stream.available() == 0 && (header & NDEFConstants.ME) == 0)
                    throw new IllegalArgumentException(
                            "Missing Message End record in the NDEF Message");


                records.add(new NDEFRecord(tnf, chunked, type, id, payload));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new NDEFMessage(records.toArray(new NDEFRecord[0]));
    }

 	/**
     * Method to parse the length of the ID
     * @param idLengthPresent Flag indicating that ID Length field is present in record
     * @param bais Stream of Data
     * @return the length of the ID value
     */
    private int getIdLength(boolean idLengthPresent, InputStream bais)
            throws IOException {
        if (idLengthPresent)
            return bais.read();
        else
            return 0;
    }

	/**
     * Method parse the length of the Payload data
     * @param shortRecord Flag indicating that it is a short record
     * @param bais Stream of Data
     * @return the length of the payload
     */
    private int getPayloadLength(boolean shortRecord, InputStream bais)
            throws IOException {
        if (shortRecord)
            return bais.read();
        else {
            byte[] buffer = Utils.getBytesFromStream(4, bais);
            return (((buffer[0] & 0xFF) << 24) | ((buffer[1] & 0xFF) << 16)
                    | ((buffer[2] & 0xFF) << 8) | (buffer[3] & 0xFF));
        }
    }

    /**
     * Method is to take the byte[] array of payload data and decode to a collection of NDEF records
     *
     * @param payload payload byte[] array to be decoded
     * @return The decoded collection of NDEF records
     * @throws NFCException Throws NFC exception if condition not satisfied
     */
    public List<AbstractRecord> decryptToRecords(byte[] payload) throws NFCException {
        return decryptToRecords(decrypt(payload));
    }

    /**
     * Method is to decode the Input stream data passed as parameter and return the list of NDEF records
     *
     * @param in Input stream data to be decoded
     * @return Collection of NDEF records that is decoded
     * @throws NFCException Throws NFC exception if condition not satisfied
     */
    public List<AbstractRecord> decodeToRecords(InputStream in) throws NFCException {
        return decryptToRecords(decrypt(in));
    }

    /**
     * Method is to extract the ndef records from the ndef message and then decode, return the list of NDEF records
     *
     * @param ndefMessage ndefMessage NDEF message to be decoded
     * @return Collection of NDEF records that is decoded
     * @throws NFCException Throws NFC exception if condition not satisfied
     */
    public List<AbstractRecord> decryptToRecords(NDEFMessage ndefMessage) throws NFCException {
        List<AbstractRecord> records = new ArrayList<>();
        List<NDEFRecord> ndefRecords = ndefMessage.getNdefRecords();
        for (NDEFRecord record : ndefRecords) {
            records.add(new NDEFRecordDecoder().decrypt(record));
        }
        return records;
    }

}
