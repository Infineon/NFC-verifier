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

package com.infineon.nfcbpsk.services.apdu;


import com.infineon.ndef.utils.UtilException;
import com.infineon.ndef.utils.Utils;

/**
 * Contains the utility methods for APDU processing.
 */
public class ApduUtils {

    /**
     * Converts an object to a byte array. The object may either be a byte array, a hex string,
     * an ApduCommand object or an ApduResponse object.
     *
     * @param stream object containing byte stream data.
     * @return byte array containing data stream.
     * @throws ApduException if conversion could not be performed.
     */
    public static byte[] toBytes(Object stream) throws ApduException {
        // check if APDU command
        if (stream instanceof ApduCommand)
            return ((ApduCommand) stream).toBytes();

        // check if APDU response
        if (stream instanceof ApduResponse)
            return ((ApduResponse) stream).toBytes();

        try {
            // try to convert string into byte array
            return Utils.toBytes(stream);
        } catch (UtilException e) {
            // throw exception at end of method
            throw new ApduException(e.getMessage());
        }
    }

    /**
     * Extracts two bytes (short) from a byte array.
     *
     * @param array  array with data
     * @param offset offset of two byte value in data array.
     * @return two byte value from byte array.
     */
    public static int getShort(byte[] array, int offset) {
        return ((array[offset] & 0xFF) << 8) | (array[offset + 1] & 0xFF);
    }
}
