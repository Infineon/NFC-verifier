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
package com.infineon.ndef.utils;


import com.infineon.ndef.model.ActionRecord;
import com.infineon.ndef.model.ExternalTypeRecord;
import com.infineon.ndef.model.IconRecord;
import com.infineon.ndef.model.RecordType;
import com.infineon.ndef.model.SizeRecord;
import com.infineon.ndef.model.SmartPosterRecord;
import com.infineon.ndef.model.TextRecord;
import com.infineon.ndef.model.TypeRecord;
import com.infineon.ndef.model.URIRecord;
import com.infineon.ndef.model.VCardRecord;
import com.infineon.ndef.model.decoder.ActionRecordDecoder;
import com.infineon.ndef.model.decoder.ExternalTypePayloadDecoder;
import com.infineon.ndef.model.decoder.IconPayloadDecoder;
import com.infineon.ndef.model.decoder.RecordPayloadDecoder;
import com.infineon.ndef.model.decoder.SizeRecordDecoder;
import com.infineon.ndef.model.decoder.SmartPosterRecordDecoder;
import com.infineon.ndef.model.decoder.TextPayloadDecoder;
import com.infineon.ndef.model.decoder.TypeRecordDecoder;
import com.infineon.ndef.model.decoder.URIPayloadDecoder;
import com.infineon.ndef.model.decoder.VCardPayloadDecoder;
import com.infineon.ndef.model.encoder.ActionRecordEncoder;
import com.infineon.ndef.model.encoder.ExternalTypeRecordEncoder;
import com.infineon.ndef.model.encoder.IconPayloadEncoder;
import com.infineon.ndef.model.encoder.RecordPayloadEncoder;
import com.infineon.ndef.model.encoder.SizeRecordEncoder;
import com.infineon.ndef.model.encoder.SmartPosterRecordEncoder;
import com.infineon.ndef.model.encoder.TextRecordEncoder;
import com.infineon.ndef.model.encoder.TypeRecordEncoder;
import com.infineon.ndef.model.encoder.URIPayloadEncoder;
import com.infineon.ndef.model.encoder.VCardPayloadEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains utility methods used for NDEF parsing.
 *
 * @author Infineon Technologies
 */
public final class RecordUtils {

    private static final Map<Class<?>, RecordPayloadEncoder> encoderMap = new HashMap<>();
    private static final Map<RecordType, RecordPayloadDecoder> decoderMap = new HashMap<>();

    static {
        encoderMap.put(URIRecord.class, new URIPayloadEncoder());
        encoderMap.put(ActionRecord.class, new ActionRecordEncoder());
        encoderMap.put(TextRecord.class, new TextRecordEncoder());
        encoderMap.put(ExternalTypeRecord.class, new ExternalTypeRecordEncoder());
        encoderMap.put(SmartPosterRecord.class,
                new SmartPosterRecordEncoder());
        encoderMap.put(SizeRecord.class, new SizeRecordEncoder());
        encoderMap.put(TypeRecord.class, new TypeRecordEncoder());
        encoderMap.put(VCardRecord.class, new VCardPayloadEncoder());
        encoderMap.put(IconRecord.class, new IconPayloadEncoder());

        decoderMap.put(new RecordType("U"), new URIPayloadDecoder());
        decoderMap.put(new RecordType("act"), new ActionRecordDecoder());
        decoderMap.put(new RecordType("T"), new TextPayloadDecoder());
        decoderMap.put(new RecordType("Sp"), new SmartPosterRecordDecoder());
        decoderMap.put(new RecordType("s"), new SizeRecordDecoder());
        decoderMap.put(new RecordType("t"), new TypeRecordDecoder());
       
        for (String mimeType : VCardRecord.SUPPORTED_TYPES) {
            decoderMap.put(new RecordType(mimeType),
                    new VCardPayloadDecoder(mimeType));
        }

        for (String mimeType : IconRecord.SUPPORTED_TYPES) {
            decoderMap.put(new RecordType(mimeType),
                    new IconPayloadDecoder(mimeType));
        }
    }

    private RecordUtils() {
      
    }


    /**
     * Method is to get the payload encoder from the class
     *
     * @param clazz Record class
     * @return Record payload encoder
     */
    public static RecordPayloadEncoder getPayloadEncoder(Class<?> clazz) {
        return encoderMap.get(clazz);
    }

    /**
     * Method is to get the payload decoder
     *
     * @param recordType Type of record
     * @return Record payload decoder
     */
    public static RecordPayloadDecoder getPayloadDecoder(
            RecordType recordType) {
        if (decoderMap.containsKey(recordType)) {
            return decoderMap.get(recordType);
        }
        return new ExternalTypePayloadDecoder();
    }

}
