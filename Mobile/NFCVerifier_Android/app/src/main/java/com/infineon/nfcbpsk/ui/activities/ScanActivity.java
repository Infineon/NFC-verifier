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

package com.infineon.nfcbpsk.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthGenerateResponse;
import com.infineon.nfcbpsk.data.logger.LoggerFactory;
import com.infineon.nfcbpsk.services.brandverification.ErrorResult;
import com.infineon.nfcbpsk.services.brandverification.BrandVerificationManager;
import com.infineon.nfcbpsk.services.brandverification.BrandVerifyEventCallback;
import com.infineon.nfcbpsk.databinding.ActivityScanBinding;
import com.infineon.nfcbpsk.data.logger.FileLogger;
import com.infineon.nfcbpsk.data.logger.LoggerType;
import com.infineon.nfcbpsk.services.apdu.ApduResponse;
import com.infineon.nfcbpsk.data.nfc.NfcChannel;
import com.infineon.nfcbpsk.services.brandprotectionrecord.BrandProtectionRecord;
import com.infineon.ndef.model.URIRecord;

import java.util.Objects;

import static com.infineon.nfcbpsk.services.brandverification.ErrorResult.TYPE_WARNING;

/**
 * Android activity containing user interface elements of the card polling screen
 */
public class ScanActivity extends AppCompatActivity {
    public final static String RESULT_MESSAGE = "RESULT_MSG";
    public final static String RESULT_PRODUCT = "RESULT_PRODUCT";
    public final static String RESULT_SERVICE = "RESULT_SERVICE";
    public final static String RESULT_URI = "RESULT_URI";
    public final static String RESULT_BRAND_PROTECTION_URI = "RESULT_BP_URI";
    public final static String RESULT_MA_RESPONSE = "MA_RESPONSE";
    public final static String RESULT_MA_APDU_RESPONSE = "MA_APDU_RESPONSE";
    public final static String RESULT_TIME = "TIME_TAKEN";
    ActivityScanBinding binding;
    NfcChannel nfcChannel;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    FileLogger fileLogger;
    private BrandVerificationManager brandVerificationManager;
    private boolean autoCancel = true;

    /**
     * Initializes the activity with user interface configuration
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelBtnHandler();

        fileLogger = (FileLogger) LoggerFactory.getLogger(getApplicationContext(), LoggerType.FILE);

        initializeNfcAdapter();
        setSessionTimeoutHandler();
    }

    /**
     * Assigns the cancel button handler to close this activity
     */
    private void setCancelBtnHandler(){
        binding.btnCancelPolling.setOnClickListener(v -> finish());
    }

    /**
     * Sets the session timeout handler which will timeout after the configured time if the
     * tag is not presented
     */
    private void setSessionTimeoutHandler(){
        new Handler().postDelayed(() -> {
            try {
                if (autoCancel) {
                    ErrorResult errorResult = new ErrorResult(TYPE_WARNING, getString(R.string.time_out),
                            ErrorResult.getTitle(this, getString(R.string.time_out), TYPE_WARNING));
                    finishPollingWithError(errorResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 50000);
    }

    /**
     * Setup the NFC adapter to start listening for tags. If NFC is not supported by the device,
     * it will close the activity with an error.
     */
    private void initializeNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            ErrorResult errorResult = new ErrorResult(TYPE_WARNING, getString(R.string.msg_unsupported_nfc),
                    ErrorResult.getTitle(this, getString(R.string.msg_unsupported_nfc), TYPE_WARNING));
            finishPollingWithError(errorResult);
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,
                        this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                Build.VERSION.SDK_INT >= 31?PendingIntent.FLAG_MUTABLE:0);
    }

    /**
     * New intent is received from the platform
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            resolveIntent(intent);
        }
    }

    /**
     * Enables the dispatcher to throw the intent to the foreground activity.
     * If NFC is disabled, display instruction to the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showDialogNfcDisabled();
                return;
            }
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    /**
     * Unregister from the foreground dispatcher when the app not in foreground
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    /**
     * Displays the dialog with instructions to enable NFC
     */
    private void showDialogNfcDisabled() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.message_enable_nfc))
                .setCancelable(false)
                .setPositiveButton(R.string.enabled, (dialog, id) -> {
                    dialog.cancel();
                    showWirelessSettings();
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {

                    ErrorResult errorResult = new ErrorResult(TYPE_WARNING, getString(R.string.nfc_disable),
                            ErrorResult.getTitle(getApplicationContext(), getString(R.string.nfc_disable), TYPE_WARNING));
                    finishPollingWithError(errorResult);
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(getString(R.string.message_nfc_disable));
        alert.show();
    }

    /**
     * Display the wireless settings screen to configure the NFC settings
     */
    private void showWirelessSettings() {
        Toast.makeText(this,
                R.string.msg_to_enabled_nfc,
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    /**
     * Handles the intent and looks for any tag intent. A tag is detected in the field when an intent
     * with tag is received.
     *
     * @param intent Intent received from the platform
     */
    private void resolveIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tagFromIntent != null) {
            IsoDep tagHandle = IsoDep.get(tagFromIntent);
            handleTagDetection(tagHandle);
        }
    }

    /**
     * Performs brand verification activity when the tag is detected
     *
     * @param tagHandle ISO DEP handle of the presented tag
     */
    private void handleTagDetection(IsoDep tagHandle) {
        nfcChannel = new NfcChannel(tagHandle, fileLogger);
        if (nfcChannel.open()) { // tag is connected
            autoCancel = false;
            if (fileLogger != null) {
                fileLogger.resetLog();
            }
            binding.ivLoader.setVisibility(View.VISIBLE);
            binding.ivWaitScan.setVisibility(View.INVISIBLE);
            binding.txtMessage.setText(R.string.authenticating);
            try {
                brandVerificationManager = new BrandVerificationManager(nfcChannel, this, new BrandVerifyEventCallback() {
                    @Override
                    public void onSuccess(ApduResponse productAPDUResponse, ApduResponse servicePDUResponse, URIRecord uriRecord,
                                          BrandProtectionRecord brandProtectionRecord, MutualAuthGenerateResponse maGenerateResponse, ApduResponse apduMAResponse) {
                        nfcChannel.close();
                        prepareSuccessResponse(productAPDUResponse, servicePDUResponse,
                                uriRecord, brandProtectionRecord, maGenerateResponse, apduMAResponse);
                    }

                    @Override
                    public void onError(ErrorResult errorResult) {
                        runOnUiThread(() -> {
                            binding.ivLoader.setVisibility(View.INVISIBLE);
                            finishPollingWithError(errorResult);
                        });
                    }
                });
                brandVerificationManager.performBrandVerification();

            } catch (Exception e) {
                runOnUiThread(() -> {
                    ErrorResult errorResult = new ErrorResult(TYPE_WARNING, e.getMessage(),
                            ErrorResult.getTitle(this, Objects.requireNonNull(e.getMessage()), TYPE_WARNING));
                    finishPollingWithError(errorResult);
                });
            }
        } else {
            binding.ivLoader.setVisibility(View.INVISIBLE);
            ErrorResult errorResult = new ErrorResult(TYPE_WARNING, getString(R.string.msg_unable_connect_tag),
                    ErrorResult.getTitle(this, getString(R.string.msg_unable_connect_tag), TYPE_WARNING));
            finishPollingWithError(errorResult);
        }
    }

    /**
     * Prepare the intent to the caller activity and finish the current activity
     *
     * @param productAPDUResponse   Product data as APDU
     * @param servicePDUResponse    Service data as APDU
     * @param uriRecord             URI record
     * @param brandProtectionRecord Brand protection record
     * @param maGenerateResponse    Mutual auth response
     * @param apduMAResponse        APDU response of mutual auth
     */
    private void prepareSuccessResponse(ApduResponse productAPDUResponse, ApduResponse servicePDUResponse,
                                        URIRecord uriRecord, BrandProtectionRecord brandProtectionRecord,
                                        MutualAuthGenerateResponse maGenerateResponse, ApduResponse apduMAResponse) {
        runOnUiThread(() -> {
            binding.ivLoader.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent();
            if (productAPDUResponse != null) {
                intent1.putExtra(RESULT_PRODUCT, productAPDUResponse.getData());
            }
            if (servicePDUResponse != null) {
                intent1.putExtra(RESULT_SERVICE, servicePDUResponse.getData());
            }
            intent1.putExtra(RESULT_URI, uriRecord.getUri());
            intent1.putExtra(RESULT_BRAND_PROTECTION_URI, brandProtectionRecord.getVerificationURL());
            intent1.putExtra(RESULT_MA_RESPONSE, maGenerateResponse);
            intent1.putExtra(RESULT_MA_APDU_RESPONSE, apduMAResponse.getData());
            intent1.putExtra(RESULT_TIME, brandVerificationManager.totalTimeLogger.getTotalTimeTaken());
            setResult(Activity.RESULT_OK, intent1);
            finish();
        });
    }

    /**
     * When back button is pressed, cancel the tag polling event
     */
    @Override
    public void onBackPressed() {
        ErrorResult errorResult = new ErrorResult(TYPE_WARNING, getString(R.string.nfc_polling_cancel),
                ErrorResult.getTitle(this, getString(R.string.nfc_polling_cancel), TYPE_WARNING));
        finishPollingWithError(errorResult);
    }

    /**
     * Terminates the polling operation and displays an error
     *
     * @param errorResult Error to be displayed
     */
    private void finishPollingWithError(ErrorResult errorResult) {
        if (nfcChannel != null) {
            nfcChannel.close();
        }
        Intent intent = new Intent();
        intent.putExtra(RESULT_MESSAGE, errorResult);
        try {
            brandVerificationManager.totalTimeLogger.stop();
            intent.putExtra(RESULT_TIME, brandVerificationManager.totalTimeLogger.getTotalTimeTaken());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}