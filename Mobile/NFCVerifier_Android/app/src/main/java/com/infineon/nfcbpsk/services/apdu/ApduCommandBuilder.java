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

/**
 *  APDUCommandBuilder is a builder class used to build APDU commands
 */
public class ApduCommandBuilder {
    /**
     * Prepare the select file command
     *
     * @param p1   parameter 1
     * @param p2   parameter 2
     * @param data byte data to pass command
     * @param le   expected output length
     * @return return the ApduCommand
     * @throws ApduException throws APDU exception
     */
    public static ApduCommand selectFile(int p1, int p2, byte[] data, int le) throws ApduException {
        int cla = (byte) 0x00;
        int ins = (byte) 0xA4;
        int lc = data.length;
        return new ApduCommand(cla, ins, p1, p2, data, le);
    }

    /**
     * Prepare the read binary file command
     *
     * @param p1p2 offset value for reading file
     * @param le   Expected output ApduCommand for reading the binary file
     * @return returns the read binary command
     * @throws ApduException throws the APDU exception
     */
    public static ApduCommand readBinary(short p1p2, int le) throws ApduException {
        int cla = (byte) 0x00;
        int ins = (byte) 0xB0;

        int p1 = (byte) ((p1p2 >> 8) & 0xff);
        int p2 = (byte) ((p1p2) & 0xff);

        return new ApduCommand(cla, ins, p1, p2, null, le);
    }

    /**
     * Prepare the get challenge command
     *
     * @return returns the get challenge command
     * @throws ApduException throws the APDU exception
     */
    public static ApduCommand getChallenge() throws ApduException {
        int cla = (byte) 0x00;
        int ins = (byte) 0x84;
        int p1 = (byte) 0x00;
        int p2 = (byte) 0x00;
        int le = (byte) 0x16;
        return new ApduCommand(cla, ins, p1, p2, null, le);
    }

    /**
     * Prepare the mutual authentication command
     *
     * @param p1          Parameter P1 of the mutual authentication command
     * @param p2          Parameter P2 of the mutual authentication command
     * @param commandData Mutual authentication command data
     * @return returns the get challenge command
     * @throws ApduException throws the APDU exception
     */
    public static ApduCommand mutualAuthenticate(int p1, int p2, byte[] commandData)
            throws ApduException {
        int cla = (byte) 0x00;
        int ins = (byte) 0x82;
        int le = (byte) 0x10;
        if (commandData.length != (byte) 0x26) {
            throw new ApduException("Invalid command data length");
        }
        return new ApduCommand(cla, ins, p1, p2, commandData, le);
    }
}
