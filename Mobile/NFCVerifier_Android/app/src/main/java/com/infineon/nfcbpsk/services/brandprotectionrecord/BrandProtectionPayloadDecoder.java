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

package com.infineon.nfcbpsk.services.brandprotectionrecord;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.decoder.RecordPayloadDecoder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Class to decode the payload byte array of an brand protection record type
 */
public class BrandProtectionPayloadDecoder implements RecordPayloadDecoder {

    /**
     * Function to decode the byte array to abstract record
     *
     * @param payload Payload of the brand protection record
     * @return Returns the abstract record
     */
    @Override
    public AbstractRecord decodePayload(byte[] payload) {
        AbstractRecord result = null;

        ByteArrayInputStream bais = new ByteArrayInputStream(payload);

        /*
          Index value for structure indicator
         */
        int OFFSET_FOR_STRUCTURE_INDICATOR = 0;
        byte structureIndicator = payload[OFFSET_FOR_STRUCTURE_INDICATOR];
        /*
          Index value for verification type
         */
        int OFFSET_FOR_VERIFICATION_TYPE = 1;
        byte verificationType = payload[OFFSET_FOR_VERIFICATION_TYPE];
        if (isBlockchainVerificationEnabled(verificationType)) {
            result = parsePayloadCloud(verificationType, structureIndicator, payload);
        }
        return result;
    }

    /**
     * Function to parse the cloud based verification brand protection record
     *
     * @param verificationType   Verification type
     * @param structureIndicator Indicator to define structure
     * @param payload            Payload data
     * @return BrandProtectionRecord
     */
    private BrandProtectionRecord parsePayloadCloud(byte verificationType, byte structureIndicator, byte[] payload) {
        /*
          Index value for url length
         */
        int OFFSET_FOR_URL_LEN = 2;
        int urlLen = payload[OFFSET_FOR_URL_LEN];
        /*
          Index value for url
         */
        int OFFSET_FOR_VERIFICATION_URL = 3;
        byte[] urlByte = Arrays.copyOfRange(payload, OFFSET_FOR_VERIFICATION_URL, (urlLen + OFFSET_FOR_VERIFICATION_URL));
        String url = new String(urlByte, StandardCharsets.UTF_8);
        int keyLabelOffset = OFFSET_FOR_VERIFICATION_URL + urlLen;
        byte[] keyLabel = new byte[2];
        keyLabel[0] = payload[keyLabelOffset];
        keyLabel[1] = payload[keyLabelOffset + 1];
        return new BrandProtectionRecord(payload, structureIndicator, verificationType, url, keyLabel);
    }

    /**
     * Function to check if verification type is cloud based verification
     *
     * @param verificationType Defines the brand protection verification type
     * @return Boolean value is true if cloud verification is enabled
     */
    private boolean isCloudVerificationEnabled(byte verificationType) {
        return verificationType == (byte) 0x01;
    }

    /**
     * Function to check if verification type is PKI
     *
     * @param verificationType Defines the brand protection verification type
     * @return Boolean value is true if PKI verification is enabled
     */
    private boolean isPKIVerificationEnabled(byte verificationType) {
        return verificationType == (byte) 0x02;
    }

    /**
     * Function to check if verification type is blockchain based
     *
     * @param verificationType Defines the brand protection verification type
     * @return Boolean value is true if blockchain verification is enabled
     */
    private boolean isBlockchainVerificationEnabled(byte verificationType) {
        return verificationType == (byte) 0x01;
    }
}


