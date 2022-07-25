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
import com.infineon.ndef.model.TextRecord;
import com.infineon.ndef.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Class is to decode the payload byte[] array of text record type
 *
 * @author Infineon Technologies
 */
public class TextPayloadDecoder implements RecordPayloadDecoder {


    /**
     * Method to decode the TextRecord payload byte array into record data structure.
     * @param payload TextRecord payload byte array
     * @return  Abstract record data structure
     */
    @Override
    public AbstractRecord decodePayload(byte[] payload) {

        ByteArrayInputStream bais = new ByteArrayInputStream(payload);

        int status = bais.read();
        byte languageCodeLength = (byte) (status & TextRecord.LANGUAGE_CODE_MASK);
        String languageCode = new String(Utils.getBytesFromStream(languageCodeLength, bais));

        byte[] textData = Utils.getBytesFromStream(payload.length - languageCodeLength - 1, bais);
        Charset textEncoding = ((status & 0x80) != 0) ? TextRecord.UTF16 : TextRecord.UTF8;

        try {
            String text = new String(textData, textEncoding.name());
            return new TextRecord(text, textEncoding, new Locale(languageCode));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
