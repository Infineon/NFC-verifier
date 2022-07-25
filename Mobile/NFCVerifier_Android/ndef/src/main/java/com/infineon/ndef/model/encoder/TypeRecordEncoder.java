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

import com.infineon.ndef.InvalidURIException;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.TypeRecord;

import java.io.UnsupportedEncodingException;

/**
 * Class is to encode the type record.
 *
 * @author Infineon Technologies
 */
public class TypeRecordEncoder implements RecordPayloadEncoder {

    /**
     * Method to encode the TypeRecord data structure into record payload byte array.
     * @param wellKnownRecord wellKnownRecord TypeRecord
     * @return record payload byte array.
     */
    @Override
    public byte[] encodePayload(AbstractRecord wellKnownRecord) {
        TypeRecord typeRecord = (TypeRecord) wellKnownRecord;
        String type = typeRecord.getType();

        if (type == null || type.isEmpty()) {
            throw new InvalidURIException();
        }

        return getTypeAsBytes(type);
    }

    private byte[] getTypeAsBytes(String type) {
        try {
            return type.getBytes(TypeRecord.CONTENT_TYPE_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
