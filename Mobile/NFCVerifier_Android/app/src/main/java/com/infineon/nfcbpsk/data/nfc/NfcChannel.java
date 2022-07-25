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
package com.infineon.nfcbpsk.data.nfc;

import android.nfc.tech.IsoDep;
import com.infineon.nfcbpsk.data.logger.TimeLogger;
import com.infineon.nfcbpsk.data.logger.FileLogger;
import com.infineon.ndef.utils.Utils;
import com.infineon.nfcbpsk.services.apdu.ApduCommand;
import com.infineon.nfcbpsk.services.apdu.ApduException;
import com.infineon.nfcbpsk.services.apdu.ApduResponse;

import java.io.IOException;

/**
 * Provides the communication channel for NFC
 */
public class NfcChannel {
    /**
     * Stores the tag handle
     */
    public final IsoDep nfcTag;
    final FileLogger fileLogger;
    Boolean connected = false;

    /**
     * Initializes the command handler with NFC handle
     *
	 * @param tag IsoDep tag handle used for communication with the tag
	 * @param fileLogger FileLogger to handle the logs
     */
    public NfcChannel(IsoDep tag, FileLogger fileLogger) {
        nfcTag = tag;
        this.fileLogger = fileLogger;
    }

    /**
     * Transmits the command APDU to the NFC tag and receives the APDU response
     *
     * @param command Command APDU to be transmitted
     * @return Byte array of APDU response
     */
    public byte[] transmit(byte[] command) {
        TimeLogger timeLogger = new TimeLogger();
        if (fileLogger != null) {
            fileLogger.log(getCommandName(command), "");
            fileLogger.log("-->", command);
        }
        byte[] resp;
        try {
            resp = nfcTag.transceive(command);
            ApduResponse response = new ApduResponse(resp, 0);
            if (fileLogger != null) {
                if (response.getData() != null && response.getData().length > 0) {
                    fileLogger.log("<--", response.getData());
                }
                fileLogger.log("SW:" + Utils.toHexString(
                        Utils.getBytes(response.getSW())).replace(" ", "")
                        + "   Data: " + response.getDataLength() + " bytes",
                        "  Exec Time:" + timeLogger.getDifferenceInTime() + " ms");
            }
        } catch (IOException | ApduException e) {
            e.printStackTrace();
            if (fileLogger != null) {

                fileLogger.log("sw", e.toString());
            }
            resp = new byte[]{0x00, 0x00};
        }
        return resp;
    }

    /**
     * Transmits APDU command and receives the APDU response
     *
     * @param command APDU command to be transmitted to the tag
     * @return Response from the tag
     */
    public ApduResponse transmit(ApduCommand command) throws ApduException {

        byte[] emptyResponse = {0x00, 0x00};
        ApduResponse response = new ApduResponse(emptyResponse, 0);
        try {
            byte[] resp = transmit(command.toBytes());
            response = new ApduResponse(resp, 0);
        } catch (ApduException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Establishes connection with the tag
     *
     * @return Flag indicating whether tag is connected
     */
    public boolean open() {
        try {
            nfcTag.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Disconnects the tag
     */
    public void close() {
        try {
            nfcTag.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the human readable command name based on the APDU command. Used for logging purpose.
     *
     * @param command APDU command
     * @return Name of the command
     */
    private String getCommandName(byte[] command) {
        String name = "";
        if (command.length >= 4) {
            if (command[1] == (byte) 0xA4) {
                if (command[2] == (byte) 0x04) {
                    name = "Select File by AID";
                }else if (command[2] == (byte) 0x00) {
                    name = "Select File by FID";
                }
            } else if (command[1] == (byte) 0xB0) {
                name = "Read Binary";
            } else if (command[1] == (byte) 0x84) {
                name = "Get Challenge";
            } else if (command[1] == (byte) 0x82) {
                name = "Mutual Authenticate";
            } else {
                name = "Unknown";
            }
        } else {
            name = "Unknown";
        }
        return name;
    }
}
