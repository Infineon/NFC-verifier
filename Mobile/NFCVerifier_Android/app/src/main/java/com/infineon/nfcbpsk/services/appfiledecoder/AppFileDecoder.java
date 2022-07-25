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
package com.infineon.nfcbpsk.services.appfiledecoder;

import com.infineon.ndef.utils.Utils;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Base class defining the decoders for product and service information received from the tag
 */
public class AppFileDecoder {
    final static String DATE_FORMAT = "yy MM dd";

    /**
     * Parse the date from current position to next 3 bytes
     *
     * @param bais ByteArrayInputStream pointing to the date index
     * @return return the date
     */
    protected static Date parseDateBytes(ByteArrayInputStream bais) {
        byte[] array = new byte[3];
        int index = 0;
        while (index < 3) {
            array[index] = (byte) bais.read();
            index++;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        try {
            String str = Utils.toHexString(array);
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Decodes the custom data fields from stream bytes
     *
     * @param bais ByteArrayInputStream pointing to the custom filed index
     * @return return the array list of CustomFieldItem
     */

    protected static ArrayList<CustomFieldItem> parseCustomFields(ByteArrayInputStream bais) {
        byte[] arrayLen = readBytes(bais, 2);
        int customFieldLength = Utils.getUINT16(arrayLen, 0);
        byte[] customBytes = readBytes(bais, customFieldLength);
        String customDataString = Utils.toString(customBytes, 0, customFieldLength);
        String[] listData = customDataString.split(";");
        ArrayList<CustomFieldItem> customFields = new ArrayList<>();
        for (String filed : listData) {
            String[] subData = filed.split(":");
            customFields.add(new CustomFieldItem(subData[0], subData[1]));
        }
        return customFields;
    }

    /**
     * Reads the next bytes with specified length
     *
     * @param bais   ByteArrayInputStream pointing to the current filed index
     * @param length number of bytes to read
     * @return return the byte array
     */
    protected static byte[] readBytes(ByteArrayInputStream bais, int length) {
        byte[] arrayBytes = new byte[length];
        int index = 0;
        while (index < length) {
            arrayBytes[index] = (byte) bais.read();
            index++;
        }
        return arrayBytes;
    }
}
