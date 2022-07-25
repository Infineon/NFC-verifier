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

package com.infineon.ndef.model.decoder;


import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.IconRecord;

/**
 * Class is to decode the payload byte[] array of icon record type
 *
 * @author Infineon Technologies
 */
public class IconPayloadDecoder implements RecordPayloadDecoder {
    private final String mimeType;

    public IconPayloadDecoder(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Method to decode the icon payload record payload byte array into record data structure.
     * @param payload Action record payload byte array
     * @return  Icon record data structure as abstract record
     */
    @Override
    public AbstractRecord decodePayload(byte[] payload) {
        return new IconRecord(mimeType, payload);
    }

}
