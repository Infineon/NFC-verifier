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
import com.infineon.ndef.model.IRecordEncoder;
import com.infineon.ndef.model.encoder.RecordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * To encode the NDEF (NFC Data Exchange Format) record which is part of NDEF Message.
 *
 * @author Infineon Technologies
 */
public final class NDEFRecordEncoder {

    private final List<IRecordEncoder> recordEncryptor = new ArrayList<>();

    private NDEFRecordEncoder() {
        recordEncryptor.add(new RecordEncoder());
    }

    public static NDEFRecordEncoder instance() {
        return new NDEFRecordEncoder();
    }

    /**
     * Method to encode the NDEF record and return as NDEF Record type.
     *
     * @param record An abstract record that is to be encoded as NDEF Record type
     * @return Encoded NDEF record from an abstract record
     */
    public NDEFRecord encrypt(AbstractRecord record) {
        for (IRecordEncoder encoder : recordEncryptor) {
            if (encoder.canEncodeRecord(record)) {
                return encoder.encodeRecord(record);
            }
        }
        throw new IllegalArgumentException("Unsupported record [" + record.getClass().getName() + "]");
    }
}
