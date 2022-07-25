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

package com.infineon.ndef.model.encoder;


import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.VCardRecord;

import java.io.UnsupportedEncodingException;

/**
 * Class is to encode the VCard record type.
 *
 * @author Infineon Technologies
 */
public class VCardPayloadEncoder implements RecordPayloadEncoder {

    /**
     * Method to encode the VCardRecord data structure into record payload byte array.
     * @param record wellKnownRecord VCardRecord
     * @return record payload byte array.
     */
    @Override
    public byte[] encodePayload(AbstractRecord record) {
        VCardRecord vCardRecord = (VCardRecord) record;

        if (vCardRecord.getVCardString() == null
                || vCardRecord.getVCardString().isEmpty()
                || vCardRecord.getMimeType() == null
                || vCardRecord.getMimeType().isEmpty()) {
            throw new RuntimeException("vCard content is empty");
        }

        String vCardString = vCardRecord.getVCardString();

        return getVCardStringAsBytes(vCardString);
    }

    /**
     * Method to convert string to byte array
     * @param vCardString to be convert as byte
     * @return byte form of the string
     */
    private byte[] getVCardStringAsBytes(String vCardString) {
        try {
            return vCardString.getBytes(VCardRecord.DEFAULT_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
