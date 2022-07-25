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
import com.infineon.ndef.NFCException;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.decoder.RecordDecoder;

/**
 * To decode the NDEF (NFC Data Exchange Format) record which is part of NDEF Message.
 *
 * @author Infineon Technologies
 */
public class NDEFRecordDecoder {

    public static NDEFRecordDecoder instance() {
        return new NDEFRecordDecoder();
    }

    /**
     * Method to decode the NDEF record and return an abstract Record out of it.
     *
     * @param record NDEF record that is to be decoded
     * @return decoded NDEF record of Abstract Record type
     * @throws NFCException Throws NFC exception if condition not satisfied
     */
    public AbstractRecord decrypt(NDEFRecord record) throws NFCException {
        RecordDecoder decoder = new RecordDecoder();
        if (decoder.canDecodeRecord(record)) {
            return decoder.decodeRecord(record);
        }
        throw new IllegalArgumentException(
                "Unsupported record [" + record.getClass().getName() + "]");
    }
}
