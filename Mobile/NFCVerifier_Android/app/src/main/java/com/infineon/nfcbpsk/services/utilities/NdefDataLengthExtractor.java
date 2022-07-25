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
package com.infineon.nfcbpsk.services.utilities;

import com.infineon.ndef.utils.Utils;

/**
 * Extracts the NDEF message length from the NDEF file first read response
 */
public class NdefDataLengthExtractor implements DataLengthExtractor {

    /**
     * Extracts the NDEF message length (First 2 bytes) from the NDEF file fragment
     *
     * @param bytes First fragment of NDEF file
     * @return NDEF message length
     */
    @Override
    public int extractDataLength(byte[] bytes) {
        return Utils.getUINT16(bytes, 0);
    }
}
