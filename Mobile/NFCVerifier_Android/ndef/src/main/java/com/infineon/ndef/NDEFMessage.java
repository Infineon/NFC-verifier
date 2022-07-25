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

import com.infineon.ndef.converter.NDEFMessageDecoder;
import com.infineon.ndef.converter.NDEFMessageEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * NDEF Messages are the basic "transportation" mechanism for NDEF records, with each message containing one or more NDEF Records.
 * This class represents an NFC NDEF records which is a collection of NDEF records and also provides methods to encode and decode NDEF Message. An NDEF Message is a container for one or more NDEF Records.
 *
 * @author Infineon Technologies
 */
public final class NDEFMessage {

    private final NDEFMessageEncoder encoder = NDEFMessageEncoder.instance();
    private List<NDEFRecord> ndefRecords = new ArrayList<>();

    /**
     * Creates a new NDEF message with the list of NDEF records
     *
     * @param ndefRecords : List of NDEF records
     */
    public NDEFMessage(NDEFRecord... ndefRecords) {
        this.ndefRecords.addAll(Arrays.asList(ndefRecords));
    }

    /**
     * Creates a new NDEF Message with {@link NDEFRecord}(s) using the raw byte array data.
     *
     * @param ndefMessage : List of NDEF records in byte array
     */
    public NDEFMessage(byte[] ndefMessage) {
        NDEFMessageDecoder decoder = NDEFMessageDecoder.instance();
        ndefRecords = decoder.decrypt(ndefMessage).getNdefRecords();
    }

    /**
     * Method used to set the  a new  list of NDEF records
     *
     * @param ndefRecords : Adds the NDEF records to record list
     */
    public void setNDEFRecords(NDEFRecord... ndefRecords) {
        this.ndefRecords.addAll(Arrays.asList(ndefRecords));
    }

    /**
     * This method returns the collection of NDEF records available in NDEF Message
     *
     * @return The collection of NDEF records list
     */
    public List<NDEFRecord> getNdefRecords() {
        return Collections.unmodifiableList(ndefRecords);
    }

    /**
     * This method encodes the NDEF Records and returns the raw byte array data
     *
     * @return NDEF Record byte array data
     */
    public byte[] toByteArray() {
        return encoder.encrypt(ndefRecords.toArray(new NDEFRecord[0]));
    }
}
