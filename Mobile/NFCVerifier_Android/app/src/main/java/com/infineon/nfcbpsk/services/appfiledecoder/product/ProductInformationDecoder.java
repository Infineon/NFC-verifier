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

package com.infineon.nfcbpsk.services.appfiledecoder.product;

import com.infineon.nfcbpsk.services.utilities.DataLengthExtractor;
import com.infineon.nfcbpsk.services.appfiledecoder.AppFileDecoder;
import com.infineon.ndef.utils.Utils;

import java.io.ByteArrayInputStream;

/**
 * Decodes the product information received from the tag
 */
public class ProductInformationDecoder extends AppFileDecoder implements DataLengthExtractor {

    /**
     * Max manufacturer name length supported by A10 profile
     */
    private static final int MANUFACTURER_MAX_LEN_A10 = 21;
    /**
     * Max manufacturer name length supported by B10 profile
     */
    private static final int MANUFACTURER_MAX_LEN_B10 = 64;
    /**
     * Max manufacturer name length supported by B20 profile
     */
    private static final int MANUFACTURER_MAX_LEN_B20 = 64;

    /**
     * Static function decodes the product information from bytes and returns the ProductInformation
     *
     * @param bytes byte representation of product information
     * @return ProductInformation object. returns null if cant decode the byte array
     */
    public static ProductInformation decode(byte[] bytes) {


        ProfileType profileType = parseProfileType(bytes);
        if (profileType != null && bytes.length >= new ProductInformationDecoder().extractDataLength(bytes)) {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes,
                    0, bytes.length);
            ProductInformation productInformation = new ProductInformation();
            productInformation.setProfileType(profileType);
            long skip = bais.skip(2);
            productInformation.setLayoutVersion((byte) bais.read());
            productInformation.modelNumber = parseModelNumber(bais);
            productInformation.manufactureDate = parseDateBytes(bais);
            productInformation.serialNumber = parseSeriesNumber(bais);
            productInformation.code = (short) parseProductCode(bais);
            productInformation.manufacturerName = parseManufacturerName(bais, profileType);
            productInformation.customFields = parseCustomFields(bais);
            return productInformation;
        }
        return null;
    }

    /**
     * Parse the manufacturer name
     *
     * @param bais        ByteArrayInputStream pointing to the position of manufacturer offset.
     * @param profileType Profile type encode by byte current byte Stream
     * @return return the manufacturer name
     */
    private static String parseManufacturerName(ByteArrayInputStream bais, ProfileType profileType) {
        int length = bais.read();
        byte[] array = readBytes(bais, length);
        final long skip = bais.skip(getManufacturerLength(profileType) - 1 - length);
        return Utils.toString(array, 0, array.length);
    }

    /**
     * Parse the product code
     *
     * @param bais ByteArrayInputStream pointing to the position of product code offset
     * @return Product code
     */
    private static int parseProductCode(ByteArrayInputStream bais) {
        byte[] array = readBytes(bais, 2);
        return Utils.getUINT16(array, 0);
    }

    /**
     * parse the serial number of product
     *
     * @param bais ByteArrayInputStream pointing to the position of serial number offset
     * @return Serial Number
     */
    private static String parseSeriesNumber(ByteArrayInputStream bais) {
        byte[] array = readBytes(bais, 8);
        return Utils.toString(array, 0, array.length);
    }

    /**
     * Parse the Model number
     *
     * @param bais ByteArrayInputStream pointing to the position of model number offset
     * @return Model number as string
     */
    public static String parseModelNumber(ByteArrayInputStream bais) {
        byte[] array = readBytes(bais, 8);
        return Utils.toString(array, 0, array.length);

    }

    /**
     * Parse the profile type encoded by the byte array
     *
     * @param bais ByteArrayInputStream pointing to the position of profile byte offset
     * @return Enum ProfileType. returns null in case of unsupported profile type
     */
    public static ProfileType parseProfileType(byte[] bais) {
        int productProfileTypeInt = Utils.getUINT16(bais, 0);
        if (productProfileTypeInt == ProfileType.A10.value) {
            return ProfileType.A10;
        }
        if (productProfileTypeInt == ProfileType.B10.value) {
            return ProfileType.B10;
        }
        if (productProfileTypeInt == ProfileType.B20.value) {
            return ProfileType.B20;
        }
        return null;
    }

    /**
     * Returns the manufacturer max length supported by profile type
     *
     * @param profileType Profile type of byte
     * @return return max manufacturer name filed. null in case of unsupported profile type
     */
    private static int getManufacturerLength(ProfileType profileType) {
        switch (profileType) {
            case A10:
                return MANUFACTURER_MAX_LEN_A10;
            case B10:
                return MANUFACTURER_MAX_LEN_B10;
            case B20:
                return MANUFACTURER_MAX_LEN_B20;
            default:
                return 0;

        }
    }

    /**
     * Calculate the product information max size available in the byte
     *
     * @param bytesProductInfo part of byte encoded product information
     * @return return the product info length
     */
    @Override
    public int extractDataLength(byte[] bytesProductInfo) {
        try {
            int manufacturerLength = 0;
            ProfileType profileType = parseProfileType(bytesProductInfo);
            if ( profileType != null)
                manufacturerLength = getManufacturerLength(profileType);
            int customFieldOffset = 24 + manufacturerLength;
            int customFieldLength = Utils.getUINT16(bytesProductInfo, customFieldOffset);
            return (22 + manufacturerLength + 2 + customFieldLength);
        } catch (Exception e){
            return 0;
        }
    }
}

