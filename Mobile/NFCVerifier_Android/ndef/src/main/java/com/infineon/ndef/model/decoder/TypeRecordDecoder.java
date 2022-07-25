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
import com.infineon.ndef.model.TypeRecord;

/**
 * Class is to decode the payload byte[] array of type record
 *
 * @author Infineon Technologies
 */
public class TypeRecordDecoder implements RecordPayloadDecoder {

    /**
     * Method to decode the TypeRecord payload byte array into record data structure.
     * @param payload TypeRecord payload byte array
     * @return  Abstract record data structure
     */
    @Override
    public AbstractRecord decodePayload(byte[] payload) {
        String type = new String(payload, 0, payload.length,
                TypeRecord.CONTENT_TYPE_CHARSET);
        return new TypeRecord(type);
    }

}
