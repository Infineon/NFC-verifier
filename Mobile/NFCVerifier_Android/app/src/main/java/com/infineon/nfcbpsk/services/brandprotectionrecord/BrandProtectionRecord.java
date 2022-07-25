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

import com.infineon.ndef.model.ExternalTypeRecord;

public class BrandProtectionRecord extends ExternalTypeRecord {

    /**
     * Structure indicator
     */
    byte structureIndicator;
    /**
     * Verification type (Bits: Cloud-0x01, PKI-0x02, Blockchain-0x04)
     */
    byte verificationType;
    /**
     * Length of verification URL (n)
     */
    byte verificationURLLen;
    /**
     * Verification URL
     */
    String verificationURL;
    /**
     * Key label
     */
    byte[] keyLabel = new byte[2];

    /**
     * Constructor to create an external type record with user defined data bytes.
     *
     * @param data External type record data bytes
     */
    public BrandProtectionRecord(byte[] data) {
        super(data);
    }

    /**
     * Constructor to create an brand protection type record with user defined data bytes.
     *
     * @param data               Byte array of user defined data
     * @param structureIndicator Structure indicator
     * @param verificationType   Verification type (Bits: Cloud-0x01, PKI-0x02, Blockchain-0x04)
     * @param verificationURL    Verification URL
     * @param keyLabel           Key label
     */
    public BrandProtectionRecord(byte[] data, byte structureIndicator, byte verificationType, 
                                 String verificationURL, byte[] keyLabel) {
        super(data);
        this.structureIndicator = structureIndicator;
        this.verificationType = verificationType;
        this.verificationURLLen = (byte) verificationURL.length();
        this.verificationURL = verificationURL;
        this.keyLabel = keyLabel;
    }

    /**
     * Getter for structure indicator
     *
     * @return Value of structure indicator
     */
    public byte getStructureIndicator() {
        return structureIndicator;
    }

    /**
     * Getter for verification type
     *
     * @return Verification type
     */
    public byte getVerificationType() {
        return verificationType;
    }

    /**
     * Getter for length of verification URL length
     *
     * @return Verification URL length
     */
    public byte getVerificationURLLen() {
        return verificationURLLen;
    }

    /**
     * Getter for verification URL
     *
     * @return Verification URL
     */
    public String getVerificationURL() {
        return verificationURL;
    }

    /**
     * Getter for key label
     *
     * @return Key label present in the brand protection record
     */
    public byte[] getKeyLabel() {
        return keyLabel;
    }
}
