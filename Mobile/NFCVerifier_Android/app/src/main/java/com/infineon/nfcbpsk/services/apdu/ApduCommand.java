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
 * Stores the Command Application Protocol Data Unit (APDU).
 */
public class ApduCommand {
    /**
     * Constant for case 1 APDU command
     */
    public static final int APDU_CASE_1 = 1;
    /**
     * Constant for case 2 APDU command
     */
    public static final int APDU_CASE_2 = 2;
    /**
     * Constant for case 3 APDU command
     */
    public static final int APDU_CASE_3 = 3;
    /**
     * Constant for case 4 APDU command
     */
    public static final int APDU_CASE_4 = 4;

    /**
     * Array containing command header
     */
    private byte[] mHeader;
    /**
     * Array containing command data
     */
    private byte[] mData;
    /**
     * Value of expected response data length
     */
    private int mLe;

    private boolean forceExtended = false;

    /**
     * Builds an APDU from CLA, INS, P1, P2, Command data and Le byte.
     *
     * @param cla  class byte
     * @param ins  instruction byte
     * @param p1   parameter byte 1
     * @param p2   parameter byte 2
     * @param data command data
     * @param le   expected response data length
     * @throws ApduException if command data cannot be converted into a byte stream.
     */
    public ApduCommand(int cla, int ins, int p1, int p2, Object data, int le) throws ApduException {
        mHeader = new byte[4];
        mHeader[0] = (byte) cla;
        mHeader[1] = (byte) ins;
        mHeader[2] = (byte) p1;
        mHeader[3] = (byte) p2;
        mLe = le;
        mData = ApduUtils.toBytes(data);
    }

    /**
     * Build an APDU command from a byte stream representation.
     *
     * @param command object containing byte stream.
     * @throws ApduException if APDU cannot be build due to syntactical reasons.
     */
    public ApduCommand(Object command) throws ApduException {
        byte[] commandBytes = ApduUtils.toBytes(command);
        int iLength = commandBytes.length;
        int iOffset = 4, iLc = 0;

        // check if valid APDU
        if (iLength < 4) {
            throw new ApduException("APDU command shorter than 4 bytes");
        }

        // copy header
        mHeader = Arrays.copyOf(commandBytes, 4);
        if (iLength == 4) {
            // case 1
            mLe = 0;
        } else {

            // get short Lc or Le
            int iValue = commandBytes[iOffset++] & 0xFF;

            if (iLength == 5) {
                // case 2 short
                mLe = ((iValue - 1) & 0xFF) + 1;
            } else if ((iValue != 0) || (iLength < 7)) {
                // case 3 or 4 short
                if (iLength == 5 + iValue) {
                    // case 3 short
                    iLc = iValue;
                    mLe = 0;
                } else if (iLength == 6 + iValue) {
                    // case 4 short
                    iLc = iValue;
                    mLe = ((commandBytes[iOffset + iValue] - 1) & 0xFF) + 1;
                } else {
                    throw new ApduException("APDU has incorrect Lc byte or data length)");
                }
            } else {
                // get extended Lc or Le
                iValue = ApduUtils.getShort(commandBytes, iOffset);
                iOffset += 2;

                if (iLength == 7) {
                    // case 2 extended
                    mLe = ((iValue - 1) & 0xFFFF) + 1;
                } else if (iLength == 7 + iValue) {
                    // case 3 extended]
                    iLc = iValue;
                    mLe = 0;
                } else if (iLength == 9 + iValue) {
                    // case 4 extended
                    iLc = iValue;
                    mLe = ((ApduUtils.getShort(commandBytes, iOffset + iValue) - 1) & 0xFFFF) + 1;
                } else {
                    throw new ApduException("APDU has incorrect extended Lc or data length)");
                }
                forceExtended = true;
            }
        }

        // copy command data
        mData = new byte[iLc];
        System.arraycopy(commandBytes, iOffset, mData, 0, iLc);
    }

    /**
     * Returns the case of the APDU command.
     *
     * @return APDU case.
     */
    public int getCase() {
        if ((mData == null) || (mData.length == 0)) {
            return (mLe == 0) ? APDU_CASE_1 : APDU_CASE_2;
        }

        return (mLe == 0) ? APDU_CASE_3 : APDU_CASE_4;
    }

    /**
     * Get class byte (CLA).
     *
     * @return class byte.
     */
    public int getCLA() {
        return mHeader[0];
    }

    /**
     * Set class byte (CLA). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param cla new class byte.
     * @return this
     */
    public ApduCommand setCLA(int cla) {
        mHeader[0] = (byte) cla;
        return this;
    }

    /**
     * Get instruction byte (INS).
     *
     * @return instruction byte.
     */
    public int getINS() {
        return mHeader[1];
    }

    /**
     * Set instruction byte (INS). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param ins new class byte.
     * @return this
     */
    public ApduCommand setINS(int ins) {
        mHeader[1] = (byte) ins;
        return this;
    }

    /**
     * Get parameter byte P1.
     *
     * @return P1 byte.
     */
    public int getP1() {
        return mHeader[2];
    }

    /**
     * Set parameter byte P1. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param p1 new P1 byte.
     * @return this
     */
    public ApduCommand setP1(int p1) {
        mHeader[2] = (byte) p1;
        return this;
    }

    /**
     * Get parameter byte P2.
     *
     * @return P2 byte.
     */
    public int getP2() {
        return mHeader[3];
    }

    /**
     * Set parameter byte P2. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param p2 new P2 byte.
     * @return this
     */
    public ApduCommand setP2(int p2) {
        mHeader[3] = (byte) p2;
        return this;
    }

    /**
     * Get command data of APDU.
     *
     * @return byte array containing the command data of APDU.
     */
    public byte[] getHeader() {
        return mHeader;
    }

    /**
     * Set command header of APDU. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param header new command data.
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException if conversion of data object into a byte array fails.
     */
    public ApduCommand setHeader(Object header) throws ApduException {
        mHeader = Arrays.copyOf(ApduUtils.toBytes(header), 4);
        return this;
    }

    /**
     * Get command data of APDU.
     *
     * @return byte array containing the command data of APDU.
     */
    public byte[] getData() {
        return mData;
    }

    /**
     * Set command data of APDU. The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param data new command data.
     * @return reference to 'this' to allow simple concatenation of operations.
     * @throws ApduException if conversion of data object into a byte array fails.
     */
    public ApduCommand setData(Object data) throws ApduException {
        mData = ApduUtils.toBytes(data);
        if (!checkExtendedApdu())
            forceExtended = false;
        return this;
    }

    /**
     * Get expected response data length (Le)
     *
     * @return expected response data length.
     */
    public int getLe() {
        return mLe;
    }

    /**
     * Set expected length (Le). The method returns a reference
     * to 'this' to allow simple concatenation of operations.
     *
     * @param expectedLength expected response data length in bytes.
     * @return reference to 'this' to allow simple concatenation of operations.
     */
    public ApduCommand setLe(int expectedLength) {
        mLe = expectedLength;
        if (!checkExtendedApdu())
            forceExtended = false;
        return this;
    }

    /**
     * Get length of command data (Lc).
     *
     * @return length of command data in bytes.
     */
    public int getLc() {
        return mData.length;
    }

    /**
     * Returns the length of the APDU command in bytes.
     *
     * @return length of the APDU command including the header, data and potential Le byte.
     */
    public int getLength() {
        int iLength, m_iLc = mData.length;

        if (m_iLc == 0) {
            if (mLe == 0) {
                // case 1
                iLength = 4;
            } else if (mLe <= 256) {
                // case 2 short
                iLength = 5;
                if (forceExtended)
                    iLength = 7;
            } else {
                // case 2 extended
                iLength = 7;
            }
        } else {
            if ((m_iLc <= 255) && (mLe <= 256)) {
                // short case 3 or 4
                if (mLe == 0) {
                    // case 3 short
                    iLength = 5 + m_iLc;
                    if (forceExtended)
                        iLength = 7 + m_iLc;
                } else {
                    // case 4 short
                    iLength = 6 + m_iLc;
                    if (forceExtended)
                        iLength = 9 + m_iLc;
                }
            } else {
                if (mLe == 0) {
                    // case 3 extended
                    iLength = 7 + m_iLc;
                } else {
                    // case 4 extended
                    iLength = 9 + m_iLc;
                }
            }
        }

        return iLength;
    }

    /**
     * Returns a byte sequence representation of the APDU command.
     *
     * @return byte array containing the APDU command.
     */
    @SuppressWarnings("UnusedAssignment")
    public byte[] toBytes() {

        byte[] commandBytes = new byte[getLength()];
        int iOffset = 4, iLc = mData.length;

        // set first four header bytes
        System.arraycopy(mHeader, 0, commandBytes, 0, 4);

        // check if short APDU format
        if ((iLc <= 255) && (mLe <= 256) && !forceExtended) {
            // short APDU

            if (iLc > 0) {
                // set Lc byte and copy data
                commandBytes[iOffset++] = (byte) iLc;
                System.arraycopy(mData, 0, commandBytes, iOffset, iLc);
                iOffset += iLc;
            }

            if (mLe > 0) {
                // set Le byte
                commandBytes[iOffset] = (byte) mLe;
                //noinspection UnusedAssignment
                iOffset++;
            }
        } else {
            // extended APDU
            commandBytes[iOffset++] = 0;

            if (iLc > 0) {
                // set extended Lc and copy data
                commandBytes[iOffset++] = (byte) (iLc >> 8);
                commandBytes[iOffset++] = (byte) iLc;
                System.arraycopy(mData, 0, commandBytes, iOffset, iLc);
                iOffset += iLc;
            }

            if (mLe > 0) {
                // set extended Le
                commandBytes[iOffset++] = (byte) (mLe >> 8);
                commandBytes[iOffset] = (byte) mLe;
                iOffset++;
            }
        }

        return commandBytes;
    }

    /**
     * Indicates whether this APDU is of Extended Format or not.
     *
     * @return TRUE for Extended Format.
     * @see ApduCommand#setExtendedFormat(boolean)
     */
    public boolean isExtendedFormat() {
        return forceExtended || checkExtendedApdu();

    }

    /**
     * Allows changing Command APDU format to Extended or Short APDU format with following conditions;
     * <br><b>Force-changing a Natural Extended APDU (Lc > 255 or Le > 256) to Short APDU, will not take effect. It still remains an Extended APDU.</b>
     * <br><b>Force-changing a Case-1 APDU to Extended APDU will not take effect. It still remains a Short APDU.</b>
     *
     * @param isExtended TRUE for Extended form
     * @see ApduCommand#isExtendedFormat()
     */
    public void setExtendedFormat(boolean isExtended) {
        //For case-1: Cant allow to force as extended APDU
        if (getCase() == APDU_CASE_1)
            forceExtended = false;
        else {
            //If real extended APDU; cant allow to make it short APDU
            if (!checkExtendedApdu()) {
                forceExtended = isExtended;
            } else {
                forceExtended = true;
            }
        }
    }

    @NotNull
    @Override
    public String toString() {
        return Utils.toHexString(toBytes());
    }

    /**
     * Append command data to current command.
     *
     * @param data byte stream of command data.
     * @return reference to 'this'. This allows concatenation of append operations within one source code line.
     * @throws ApduException if object cannot be converted in a byte stream.
     */
    public ApduCommand appendData(Object data) throws ApduException {
        byte[] abData = ApduUtils.toBytes(data);
        int iOldLength = mData.length, iNewLength = abData.length;

        // build new data array
        mData = Arrays.copyOf(mData, iOldLength + iNewLength);
        System.arraycopy(abData, 0, mData, iOldLength, iNewLength);

        return this;
    }

    /**
     * Checks if the APDU is an extended APDU
     *
     * @return Returns true if it is an extended APDU, else false
     */
    private boolean checkExtendedApdu() {
        return (mData.length > 255 || mLe > 256);
    }
}
