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

import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.IRecordEncoder;
import com.infineon.ndef.utils.NDEFConstants;
import com.infineon.ndef.utils.RecordUtils;

/**
 * Class is to encode any well known NDEF record type.
 *
 * @author Infineon Technologies
 */
public class RecordEncoder implements IRecordEncoder {
    RecordPayloadEncoder payloadEncoder;

    /**
     * Method to check record encoding is supported or not
     * @param record AbstractRecord to be check
     * @return flag indicating record encoding is supported or not
     */
    @Override
    public boolean canEncodeRecord(AbstractRecord record) {
        payloadEncoder = RecordUtils.getPayloadEncoder(record.getClass());
        return payloadEncoder != null;
    }


    /**
     * Method to encode the wellKnownRecord data structure into record payload byte array.
     * @param record wellKnownRecord  from NDEF
     * @return NDEFRecord.
     */
    @Override
    public NDEFRecord encodeRecord(AbstractRecord record) {
        byte[] key = record.getId();
        if (key != null && key.length > 255) {
            throw new IllegalArgumentException("Expected record id length <= 255 bytes");
        }

        byte[] payload = payloadEncoder.encodePayload(record);
        byte[] type = record.getRecordType().getType();
        return new NDEFRecord(NDEFConstants.TNF_WELL_KNOWN, type, key, payload);
    }

}
