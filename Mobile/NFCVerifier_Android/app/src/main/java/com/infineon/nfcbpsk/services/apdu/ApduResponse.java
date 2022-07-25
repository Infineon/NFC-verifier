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

import com.infineon.ndef.utils.Utils;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

/**
 * Stores the APDU responses. The response consists of response data (optional) and
 * a status word (mandatory).
 */
public class ApduResponse {
    /**
     * Status word indicating successful operation
     */
    public static final int SW_NO_ERROR = 0x9000;

    /**
     * Status word indicating condition of use not satisfied
     */
    public static final int SW_CONDITIONS_NOT_SATISFIED = 0x6985;

    /**
     * Byte array containing response data and status word
     */
    private byte[] response;

    /**
     * Command execution time
     */
    private long m_lExecTime;

    /**
     * Build a response from a byte data stream.
     *
     * @param response Byte data stream containing card response
     * @param execTime Command execution time in nanoseconds
     * @throws ApduException if conversion of response object into byte array fails.
     */
    public ApduResponse(Object response, long execTime) throws ApduException {
        this.response = ApduUtils.toBytes(response);
        m_lExecTime = execTime;

        // check if valid response
        if (this.response.length < 2) {
            // build dummy response
            this.response = new byte[2];
        }
    }

    /**
     * Append new response to existing response. This method is useful if the response data
     * has to be concatenated from the content of multiple APDU responses (GET RESPONSE).
     * The status word of the existing response will be replaced by the status word of the
     * new response. The new execution time will be added to the existing execution time to
     * form an overall response time. The method returns a reference to 'this' to allow
     * simple concatenation of operations.
     *
     * @param newResponseObj new response to be appended to existing response.
     * @param execTime       execution time in nanoseconds for the new response fragment
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException in case response fragment cannot be converted into a byte array.
     */
    public ApduResponse appendResponse(Object newResponseObj, long execTime) throws ApduException {
        // build byte array from new response and determine byte length
        byte[] newResponse = ApduUtils.toBytes(newResponseObj);
        int length = newResponse.length;

        // add execution time
        m_lExecTime += execTime;

        if (length >= 2) {
            // append response data and overwrite status word of existing response
            response = Arrays.copyOf(response, response.length + length - 2);
            System.arraycopy(newResponse, 0, response, response.length - length, length);
        }

        return this;
    }

    /**
     * Check if status word is SW_NO_ERROR (9000).
     *
     * @return reference to this. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException if received status word is not 9000.
     */
    public ApduResponse checkOK() throws ApduException {
        return checkSW(SW_NO_ERROR);
    }

    /**
     * Check if status word is equal to the presented value.
     *
     * @param statusWord expected status word. Note that only the lower 16 bits are evaluated.
     * @return reference to 'this'. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException if received status word is not expected.
     */
    public ApduResponse checkSW(int statusWord) throws ApduException {
        if ((statusWord & 0xFFFF) != getSW()) {
            throw new ApduException(String.format("Unexpected status word %02X", getSW()));
        }

        return this;
    }

    /**
     * Check if response data length matches to the presented value.
     *
     * @param length expected length of response data.
     * @return reference to 'this'. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException if received data length does not match to expected value.
     */
    public ApduResponse checkDataLength(int length) throws ApduException {
        if (length != response.length - 2) {
            throw new ApduException(String.format("Unexpected response data length %d", response.length - 2));
        }

        return this;
    }

    /**
     * Check if response data (without status word) matches to the presented response data.
     *
     * @param expectedResponse expected response data without status word.
     * @return reference to 'this'. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException if received response data does not match to expected response data.
     */
    public ApduResponse checkData(Object expectedResponse) throws ApduException {
        byte[] abExpectedResponse = ApduUtils.toBytes(expectedResponse);

        checkDataLength(abExpectedResponse.length);

        if (!Arrays.equals(abExpectedResponse, getData()))
            throw new ApduException("Unexpected response data!");

        return this;
    }

    /**
     * Check if response including status word matches to the presented response.
     *
     * @param expectedResponse expected response including status word.
     * @return reference to 'this'. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException if received response does not match to expected response.
     */
    public ApduResponse checkResponse(Object expectedResponse) throws ApduException {
        byte[] abExpectedResponse = ApduUtils.toBytes(expectedResponse);

        if (abExpectedResponse.length != response.length)
            throw new ApduException(String.format("Unexpected response length %d!", response.length - 2));
        if (!Arrays.equals(abExpectedResponse, response))
            throw new ApduException("Unexpected response!");
        return this;
    }

    /**
     * Perform exhaustive response evaluation.
     *
     * @param dataMask mask for response data (byte-wise AND) or null if no masking required.
     * @param data     expected response data or null if response data shall not be checked.
     * @param swMask   mask for status word (byte-wise AND) or null if no masking required.
     * @param sw       expected status word or concatenation of acceptable status words
     * @return reference to 'this'. This allows to concatenate checks and other operations in one source code line.
     * @throws ApduException If there is APDU or communication related failures
     */
    public ApduResponse check(Object dataMask, Object data, Object swMask, Object sw) throws ApduException {
        byte[] resp = toBytes();

        // check if response must be masked
        if (dataMask != null) {
            byte[] mask = ApduUtils.toBytes(dataMask);
            if (resp.length - 2 != mask.length)
                throw new ApduException("Card in terminal is no valid debug target");
            for (int i = 0; i < mask.length; i++)
                resp[i] &= mask[i];
        }

        // check if status word must be masked
        if (swMask != null) {
            byte[] mask = ApduUtils.toBytes(swMask);
            if (mask.length != 2)
                throw new ApduException("Status word mask must be two bytes long");
            resp[resp.length - 2] &= mask[0];
            resp[resp.length - 1] &= mask[1];
        }

        // check if response data must be checked
        if (data != null) {
            byte[] dataBytes = ApduUtils.toBytes(data);

            if (resp.length - 2 != dataBytes.length)
                throw new ApduException("Card in terminal is no valid debug target");

            for (int i = 0; i < dataBytes.length; i++)
                if (resp[i] != dataBytes[i])
                    throw new ApduException("Card in terminal is no valid debug target");
        }

        // check if status word must be checked
        if (sw != null) {
            boolean match = false;
            byte[] swBytes = ApduUtils.toBytes(sw);

            for (int i = 0; i < swBytes.length - 1; i += 2)
                if ((swBytes[i] == resp[resp.length - 2]) && (swBytes[i + 1] == resp[resp.length - 1])) {
                    match = true;
                    break;
                }

            if (!match)
                throw new ApduException("Card in terminal is no valid debug target");
        }

        return this;
    }

    /**
     * Returns the status word contained in the card response.
     *
     * @return status word as integer (always positive value).
     */
    public int getSW() {
        return ApduUtils.getShort(response, response.length - 2);
    }

    /**
     * Get response data array.
     *
     * @return array containing the response data.
     */
    public byte[] getData() {
        return Arrays.copyOf(response, response.length - 2);
    }

    /**
     * Get length of response data.
     *
     * @return length of response data.
     */
    public int getDataLength() {
        return response.length - 2;
    }

    /**
     * Return byte array representation of response data.
     *
     * @return byte array containing response and status word.
     */
    public byte[] toBytes() {
        return response.clone();
    }

    @NotNull
    @Override
    public String toString() {
        return Utils.toHexString(response);
    }

    /**
     * Return command execution time in nano seconds. Depending on
     * the underlying reader hardware the execution time may contain overhead
     * times like transmission etc.
     *
     * @return Command execution time.
     */
    public long getExecutionTime() {
        return m_lExecTime;
    }

    /**
     * Return true if command executed successfully
     * or it returns the false
     *
     * @return returns the flag to setup command execution status Z
     */
    public boolean isSuccessSW() {
        return getSW() == SW_NO_ERROR;

    }
}
