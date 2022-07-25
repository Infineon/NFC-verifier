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

import android.content.Context;
import android.util.Log;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.data.logger.TimeLogger;
import com.infineon.nfcbpsk.services.apdu.ApduCommandBuilder;
import com.infineon.nfcbpsk.services.apdu.ApduCommand;
import com.infineon.nfcbpsk.services.apdu.ApduException;
import com.infineon.nfcbpsk.services.apdu.ApduResponse;
import com.infineon.nfcbpsk.services.apdu.MutualAuthException;
import com.infineon.nfcbpsk.data.nfc.NfcChannel;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProductInformationDecoder;
import com.infineon.nfcbpsk.services.appfiledecoder.service.ServiceInformationDecoder;
import com.infineon.ndef.utils.Utils;
import com.infineon.nfcbpsk.services.utilities.DataLengthExtractor;
import com.infineon.nfcbpsk.services.utilities.NdefDataLengthExtractor;

import java.util.Arrays;

/**
 * Defines the APDU commands used for brand verification
 */
public class CommandManager {
    /**
     * Channel to communicate with tag
     */
    final NfcChannel nfcChannel;
    /**
     * Handle of the application context
     */
    final Context context;
    /**
     * Max expected length to the read product and service information.
     * It must be greater than or equals to 128 bytes
     */
    private final int NFC4TC_MLE = 0xE6;

    TimeLogger timeLogger;

    /**
     * Initializes the command manager
     *
     * @param nfcChannel    Channel for communication
     * @param context       Context handle of the requesting activity / application
     */
    public CommandManager(NfcChannel nfcChannel, Context context) {
        this.nfcChannel = nfcChannel;
        this.context = context;
    }

    /**
     * Performs NDEF read operation on the tag.
     * 1. Select the NDEF application
     * 2. Select the CC file
     * 3. Read the CC file
     * 4. Select the NDEF file
     * 5. Read the entire NDEF file
     *
     * @return APDUResponse Read from the Tag
     * @throws ApduException Throws exception in case of errors in executing the operation
     */
    public ApduResponse readNDEFMessage() throws ApduException {

        timeLogger = new TimeLogger();

        //1. Select the NDEF application
        byte[] AID = {(byte) 0xD2, (byte)0x76, (byte)0x00, (byte)0x00, (byte) 0x85, (byte)0x01, (byte)0x00};
        ApduCommand cmdSelectApplication = ApduCommandBuilder.selectFile((byte)0x04,(byte)0x00, AID, (byte)0x00);
        ApduResponse apduResponse = nfcChannel.transmit(cmdSelectApplication);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_select_aid));
        }
        timeLogger.logTime("Step 1-1-1");

        // 2. Select the CC file
        timeLogger.start();
        byte[] data = {(byte) 0xE1, (byte)0x03};
        ApduCommand cmdSelectCC = ApduCommandBuilder.selectFile((byte)0x00, (byte)0x00, data, (byte)0x00);
        apduResponse = nfcChannel.transmit(cmdSelectCC);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_select_cc_file));
        }
        timeLogger.logTime("Step 1-1-2");

        // 3. Read the CC file
        timeLogger.start();
        ApduCommand cmdReadBinaryCC = ApduCommandBuilder.readBinary((short) 0x0000, (byte)0xE6);
        apduResponse = nfcChannel.transmit(cmdReadBinaryCC);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_read_cc_file));
        }
        timeLogger.logTime("Step 1-1-3");

        // 4. Select the NDEF file
        timeLogger.start();
        final byte[] ndefFileID = Arrays.copyOfRange(apduResponse.getData(), 9, 11);
        int maxLE = Utils.getUINT16(apduResponse.getData(), 3);
        if (maxLE > NFC4TC_MLE) {
            maxLE = NFC4TC_MLE;
        }
        ApduCommand cmdSelectNDEF = ApduCommandBuilder.selectFile((byte)0x00, (byte)0x00, ndefFileID, 0);
        apduResponse = nfcChannel.transmit(cmdSelectNDEF);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_select_ndef_file));
        }
        timeLogger.logTime("Step 1-1-4");

        // 5. Read the entire NDEF file
        timeLogger.start();
        apduResponse = this.readBinaryInLoop(false, (short) 0x0000,
                maxLE, new NdefDataLengthExtractor());
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_read_ndef_file));
        }
        timeLogger.logTime("Step 1-1-5");
        return apduResponse;
    }

    /**
     * Performs APDU commands to read the Chip Unique ID from tag
     *
     * @return Chip Unique ID
     * @throws ApduException Throws exception in case of errors in executing the operation
     */
    public ApduResponse getChipUniqueID() throws ApduException {

        // 1. Select the ID_INFO file
        byte[] fileID = {(byte)0x2F, (byte) 0xF7};
        ApduCommand cmdSelectFileIDINFO = ApduCommandBuilder.selectFile((byte)0x00, (byte)0x00, fileID, 0);
        ApduResponse apduResponse = nfcChannel.transmit(cmdSelectFileIDINFO);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_select_id_info_file));
        }

        // 2. Read the Chip Unique ID
        ApduCommand cmdReadBinaryIDINFO = ApduCommandBuilder.readBinary((short) 0x0008, (byte)0x10);
        apduResponse = nfcChannel.transmit(cmdReadBinaryIDINFO);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_read_id_info_file));
        }
        return apduResponse;
    }

    /**
     * Performs APDU commands to get challenge from the tag
     *
     * @return Challenge from the tag
     * @throws ApduException Throws exception in case of errors in executing the operation
     */
    public ApduResponse getChallenge() throws ApduException {
        ApduCommand cmdGetChallenge = ApduCommandBuilder.getChallenge();
        ApduResponse apduResponse = nfcChannel.transmit(cmdGetChallenge);
        if (!apduResponse.isSuccessSW()) {
            throw new ApduException(context.getString(R.string.msg_read_challenge_file));
        }
        return apduResponse;

    }

    /**
     * Performs APDU commands to perform mutual authentication with the tag
     *
     * @param commandData   Mutual authentication command data
     * @return Response of mutual authentication
     * @throws MutualAuthException Throws exception in case of errors in executing the operation
     */
    public ApduResponse mutualAuthenticate(byte[] commandData) throws MutualAuthException {
        ApduCommand cmdMutualAuthenticate;
        ApduResponse apduResponse;
        try {
            cmdMutualAuthenticate = ApduCommandBuilder.mutualAuthenticate((byte)0x00, (byte)0x02, commandData);
            apduResponse = nfcChannel.transmit(cmdMutualAuthenticate);
        } catch (ApduException e) {
            throw new MutualAuthException(context.getString(R.string.msg_mutual_auth_failed));
        }
        if (!apduResponse.isSuccessSW()) {
            throw new MutualAuthException(context.getString(R.string.msg_mutual_auth_failed));
        }
        return apduResponse;
    }

    /**
     * Performs APDU commands to read the product information file
     *
     * @return Product information
     * @throws ApduException Throws exception in case of errors in executing the operation
     */
    public ApduResponse readProductInformation() throws ApduException {
        //Read Product info file SFID and Offset
        short OFFSET_PRODUCT_INFO_WITH_SFID = (short) 0x8100;
        return this.readBinaryInLoop(true, OFFSET_PRODUCT_INFO_WITH_SFID, NFC4TC_MLE,
                new ProductInformationDecoder());
    }

    /**
     * Performs APDU commands to read the service information file
     *
     * @return Service information
     * @throws ApduException Throws exception in case of errors in executing the operation
     */
    public ApduResponse readServiceInformation() throws ApduException {
        //Read Service info file SFID and Offset
        short OFFSET_SERVICE_INFO_WITH_SFID = (short) 0x8200;
        return this.readBinaryInLoop(true, OFFSET_SERVICE_INFO_WITH_SFID, NFC4TC_MLE,
                new ServiceInformationDecoder());
    }

    /**
     * Method to perform ReadBinary command in loop
     *
     * @param isReadWithSFID         set if read with SFID and offset contain the SFID
     * @param offset                 offset for read the binary file
     * @param le                     expected length
     * @param dataLengthExtractor    Extractor to find the data length from the first read response
     * @return return the APDU response
     * @throws ApduException Throws the Exception
     */
    private ApduResponse readBinaryInLoop(boolean isReadWithSFID, short offset, int le,
                                    DataLengthExtractor dataLengthExtractor) throws ApduException {
        ApduResponse apduResponse = new ApduResponse(new byte[0], 0);
        int totalReceivedDataSize = 0;
        int totalBytesToRead = 0;
        do {
            ApduCommand command = ApduCommandBuilder.readBinary(offset, le);
            ApduResponse newApduResponse = nfcChannel.transmit(command);
            apduResponse.appendResponse(newApduResponse, 0);

            if (!newApduResponse.isSuccessSW()) {
                Log.d("DataError", apduResponse.toString());
                break;
            }
            if (totalBytesToRead == 0) {
                totalBytesToRead = dataLengthExtractor.extractDataLength(apduResponse.getData());
            }
            offset = this.calculateOffset(isReadWithSFID, offset, newApduResponse.getData().length);
            totalReceivedDataSize += newApduResponse.getDataLength();
            isReadWithSFID = false;
        } while (totalBytesToRead >= totalReceivedDataSize);
        return apduResponse;
    }

    /**
     * Calculate the offset value for next iteration
     *
     * @param isReadWithSFID defines the reading with SFID or not
     * @param offset         offset of the previous Read file, or 0 if first time read
     * @param length         previous read data length, 0 if first time read
     * @return return the next offset value
     */
    private short calculateOffset(boolean isReadWithSFID, short offset, int length) {
        if (isReadWithSFID) {
            // if Reading with SFID offset first byte is SFID and second byte is offset
            // Shifting offset in first byte and removing SFID because for next time file is selected
            short p2 = (short) (offset << 8);
            return (short) (p2 + length);
        } else {
            return (short) (offset + length);
        }
    }
}
