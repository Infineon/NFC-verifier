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

package com.infineon.nfcbpsk.services.brandverification;

import android.app.Activity;
import android.util.Log;

import com.infineon.ndef.NDEFMessage;
import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.NFCException;
import com.infineon.ndef.NFCFactory;
import com.infineon.ndef.converter.NDEFMessageDecoder;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.ExternalTypeRecord;
import com.infineon.ndef.model.URIRecord;
import com.infineon.ndef.utils.UtilException;
import com.infineon.ndef.utils.Utils;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.services.apdu.ApduException;
import com.infineon.nfcbpsk.services.apdu.ApduResponse;
import com.infineon.nfcbpsk.services.apdu.MutualAuthException;
import com.infineon.nfcbpsk.data.nfc.NfcChannel;
import com.infineon.nfcbpsk.data.logger.TimeLogger;
import com.infineon.nfcbpsk.services.apdu.CommandManager;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProductInformationDecoder;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProfileType;
import com.infineon.nfcbpsk.services.brandprotectionrecord.BrandProtectionPayloadDecoder;
import com.infineon.nfcbpsk.services.brandprotectionrecord.BrandProtectionRecord;

import java.util.Arrays;
import java.util.Objects;

import static com.infineon.nfcbpsk.services.brandverification.ErrorResult.TYPE_ERROR;
import static com.infineon.nfcbpsk.services.brandverification.ErrorResult.TYPE_WARNING;

/**
 * Manages the commands execution sequence for performing brand verification
 */
public class BrandVerificationManager {
    /**
     * Handle of the command manager
     */
    private final CommandManager cmdManager;
    /**
     * Handle of the application context
     */
    private final Activity context;
    /**
     * Listener for call back to the calling activity
     */
    private final BrandVerifyEventCallback nfcCallback;
    public TimeLogger totalTimeLogger;

    /**
     * Handle of the time logger
     */
    TimeLogger timeLogger;

    private URIRecord uriRecord;
    private BrandProtectionRecord brandProtectionRecord;
    private MutualAuthGenerateResponse maGenerateResponse;
    private byte[] challenge;
    private byte[] chipID;
    private byte[] mutualAuthCommandData;
    /**
     * Instance of the MutualAuthGenerateEvent interface
     */
    final MutualAuthGenerateEvent mutualAuthGenerateEvent = new MutualAuthGenerateEvent() {
        @Override
        public void onSuccess(MutualAuthGenerateResponse response) {
            try {
                maGenerateResponse = response;
                mutualAuthCommandData = Utils.toByteArray(response.commandData.toUpperCase());
                performBrandVerificationPhase2();
            } catch (UtilException e) {
                ErrorResult errorResult = new ErrorResult(TYPE_WARNING, e.getMessage(),
                        ErrorResult.getTitle(context, Objects.requireNonNull(e.getMessage()),
                                TYPE_WARNING));
                nfcCallback.onError(errorResult);
            }
        }

        @Override
        public void onError(int status, String error) {
            ErrorResult errorResult = new ErrorResult(TYPE_ERROR, error,
                    ErrorResult.getTitle(context, error, TYPE_WARNING));
            nfcCallback.onError(errorResult);
        }
    };

    /**
     * Initializes the brand verification manager
     *
     * @param nfcChannel  Channel for communication
     * @param context     Context handle of the requesting activity / application
     * @param nfcCallback Callback method to handle result
     */
    public BrandVerificationManager(NfcChannel nfcChannel, Activity context,
                                    BrandVerifyEventCallback nfcCallback) {
        this.context = context;
        this.nfcCallback = nfcCallback;
        this.cmdManager = new CommandManager(nfcChannel, context);
    }

    /**
     * Performs brand verification operation - phase 1 which includes the tag communication until
     * reading the challenge and the cloud operation to generate the command data.
     *
     * @throws ApduException APDU exception
     * @throws NFCException NFC exception
     */
    public void performBrandVerification() throws ApduException, NFCException {
        tagOperationPhase1();
        cloudOperationPhase1(mutualAuthGenerateEvent);
    }

    /**
     * Performs brand verification operation - phase 2 which includes the tag communication to
     * perform the mutual authentication command and read product and service information.
     *
     */
    private void performBrandVerificationPhase2() {
        tagOperationPhase2();
    }

    /**
     * Reads the NDEF message from the tag
     *
     * @throws ApduException if there is any exception while executing command or command
     * is not successfully executed
     * @throws NFCException  throws the NFC related exception
     */
    private void tagOperationPhase1() throws ApduException, NFCException {
        timeLogger = new TimeLogger();
        totalTimeLogger = new TimeLogger();

        // Read NDEF message
        ApduResponse ndefMessage = cmdManager.readNDEFMessage();

        // Decode NDEF message
        parseNDEFMessage(ndefMessage);

        // Read ChipID
        ApduResponse chipUniqueIDData = cmdManager.getChipUniqueID();
        chipID = chipUniqueIDData.getData();

        // Read challenge
        ApduResponse apduChallenge = cmdManager.getChallenge();
        challenge = apduChallenge.getData();

        if ((chipID == null) || (challenge == null)) {
            throw new NFCException(context.getResources().getString(R.string.msg_read_challenge_file));
        }

        timeLogger.logTime("Step 1");
        totalTimeLogger.logTime("Total Time");
    }

    /**
     * Perform the mutual authentication command and read product and service information.
     */
    private void tagOperationPhase2() {
        ApduResponse mutualAuthResp;
        boolean serviceInfoFilePresent = false;

        // Perform mutual authentication
        try {
            mutualAuthResp = cmdManager.mutualAuthenticate(mutualAuthCommandData);
        } catch (MutualAuthException e) {
            ErrorResult errorResult = new ErrorResult(TYPE_ERROR, e.getMessage(),
                    ErrorResult.getTitle(context, Objects.requireNonNull(e.getMessage()), TYPE_ERROR));
            nfcCallback.onError(errorResult);
            return;
        }

        timeLogger.logTime("Step 2");
        totalTimeLogger.logTime("Total Time");

        // Read product information file
        timeLogger.start();
        ApduResponse productInfoResponse;
        try {
            productInfoResponse = cmdManager.readProductInformation();
        } catch (ApduException e) {
            ErrorResult errorResult = new ErrorResult(TYPE_WARNING,
                    context.getResources().getString(R.string.msg_read_info_file),
                    ErrorResult.getTitle(context, context.getResources().getString(R.string.msg_read_info_file),
                            TYPE_WARNING));
            nfcCallback.onError(errorResult);
            return;
        }
        Log.i("ProductData", productInfoResponse.toString());
        if (productInfoResponse.isSuccessSW()) {
            if (ProductInformationDecoder.parseProfileType(productInfoResponse.getData()) != ProfileType.A10) {
                serviceInfoFilePresent = true;
            }
        } else {
            ErrorResult errorResult = new ErrorResult(TYPE_WARNING,
                    context.getResources().getString(R.string.msg_bp_record_unable_to_read),
                    ErrorResult.getTitle(context, context.getResources().getString(R.string.msg_bp_record_unable_to_read),
                            TYPE_WARNING));
            nfcCallback.onError(errorResult);
            return;
        }

        // Read service information file
        ApduResponse serviceInfoResponse = null;
        if (serviceInfoFilePresent) {
            try {
                serviceInfoResponse = cmdManager.readServiceInformation();
            } catch (ApduException e) {
                ErrorResult errorResult = new ErrorResult(TYPE_WARNING,
                        context.getResources().getString(R.string.msg_read_info_file),
                        ErrorResult.getTitle(context, context.getResources().getString(R.string.msg_read_info_file),
                                TYPE_WARNING));
                nfcCallback.onError(errorResult);
                return;
            }

            if (!serviceInfoResponse.isSuccessSW()) {
                ErrorResult errorResult = new ErrorResult(TYPE_WARNING,
                        context.getResources().getString(R.string.msg_read_info_file),
                        ErrorResult.getTitle(context, context.getResources().getString(R.string.msg_read_info_file),
                                TYPE_WARNING));
                nfcCallback.onError(errorResult);
                return;
            }
        }

        timeLogger.logTime("Step 3");
        totalTimeLogger.logTime("Total Time");
        totalTimeLogger.stop();
        nfcCallback.onSuccess(productInfoResponse, serviceInfoResponse, uriRecord,
                brandProtectionRecord, maGenerateResponse, mutualAuthResp);
    }

    /**
     * Parses the NDEF message and extracts the brand protection record and URI Record
     *
     * @param ndefMessage APDUResponse of NDEF message read command
     * @throws NFCException NFC related exception
     */
    private void parseNDEFMessage(ApduResponse ndefMessage) throws NFCException {
        // Parse NDEF Message
        try {
            int messageLength = Utils.getUINT16(ndefMessage.getData(), 0);
            byte[] messageBytes = Arrays.copyOfRange(ndefMessage.getData(), 2,
                    messageLength + 2);
            NDEFMessage message = NDEFMessageDecoder.instance().decrypt(messageBytes);
            for (NDEFRecord ndefRecord : message.getNdefRecords()) {
                AbstractRecord abstractRecord = NFCFactory.decryptRecord(ndefRecord);
                if (abstractRecord instanceof URIRecord) {
                    uriRecord = (URIRecord) abstractRecord;
                }
                if (abstractRecord instanceof ExternalTypeRecord) {
                    ExternalTypeRecord record = (ExternalTypeRecord) abstractRecord;
                    brandProtectionRecord = (BrandProtectionRecord)
                            new BrandProtectionPayloadDecoder().decodePayload(record.getData());
                }
            }
        } catch (Exception e) {
            throw new NFCException(context.getResources().getString(R.string.msg_read_ndef_file));
        }
    }

    /**
     * Function to generate the Mutual Auth command data using the cloud service
     *
     * @param mutualAuthGenerateEvent Instance of MutualAuthGenerateEvent to handle the callback
     */
    private void cloudOperationPhase1(MutualAuthGenerateEvent mutualAuthGenerateEvent)
            throws NFCException {
        timeLogger.start();
        if (brandProtectionRecord != null) {
            String hexKeyLabel = Utils.toHexString(brandProtectionRecord.getKeyLabel());
            BrandVerificationService service = new BrandVerificationService(context,
                    brandProtectionRecord.getVerificationURL());
            service.performMutualAuthGeneration(hexKeyLabel, chipID, challenge,
                    mutualAuthGenerateEvent);
        } else {
            throw new NFCException(context.getResources().getString(R.string.msg_bp_record_unable_to_read));
        }
    }
}
