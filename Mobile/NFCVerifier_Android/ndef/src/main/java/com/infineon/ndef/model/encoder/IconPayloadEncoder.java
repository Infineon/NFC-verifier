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
import com.infineon.ndef.model.IconRecord;

/**
 * Class is to encode the Icon type record.
 *
 * @author Infineon Technologies
 */
public class IconPayloadEncoder implements RecordPayloadEncoder {


    /**
     * Method to encode the IconRecord data structure into record payload byte array.
     * @param record wellKnownRecord IconRecord from NDEF
     * @return record payload byte array.
     */
    @Override
    public byte[] encodePayload(AbstractRecord record) {
        IconRecord iconRecord = (IconRecord) record;

        if (iconRecord.getImage() == null || iconRecord.getImage().length == 0
                || iconRecord.getMimeType() == null
                || iconRecord.getMimeType().isEmpty()) {
            throw new RuntimeException("Icon record content is empty");
        }

        return iconRecord.getImage();
    }

}
