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
package com.infineon.nfcbpsk.services.appfiledecoder.service;

import com.infineon.nfcbpsk.services.utilities.DataLengthExtractor;
import com.infineon.nfcbpsk.services.appfiledecoder.AppFileDecoder;
import com.infineon.ndef.utils.Utils;

import java.io.ByteArrayInputStream;

/**
 * Decodes the service information received from the tag
 */
public class ServiceInformationDecoder extends AppFileDecoder implements DataLengthExtractor {

    /**
     * Static function decodes the service information from bytes and returns the ServiceInformation
     *
     * @param bytes byte representation of service information
     * @return ServiceInformation object. returns null if cant decode the byte Properly
     */
    public static ServiceInformation decode(byte[] bytes) {

        if (bytes.length >= new ServiceInformationDecoder().extractDataLength(bytes)) {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes,
                    0, bytes.length);
            ServiceInformation serviceInformation = new ServiceInformation();
            serviceInformation.layoutVersion = (byte) bais.read();
            serviceInformation.purchaseDate = parseDateBytes(bais);
            serviceInformation.warrantyValidityDate = parseDateBytes(bais);
            serviceInformation.lastServiceDate = parseDateBytes(bais);
            serviceInformation.customFields = parseCustomFields(bais);
            return serviceInformation;
        }
        return null;
    }


    /**
     * Calculate the service information max size available in the byte
     *
     * @param bytesServiceInfo part of byte encoded Service information
     * @return return the service info length
     */
    @Override
    public int extractDataLength(byte[] bytesServiceInfo) {
        try {
            int customFieldOffset = 10;
            int customFieldLength = Utils.getUINT16(bytesServiceInfo, customFieldOffset);
            return (customFieldOffset + 2 + customFieldLength);
        } catch (Exception e) {
            return 0;
        }
    }
}