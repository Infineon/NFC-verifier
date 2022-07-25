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

package com.infineon.nfcbpsk.ui.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.infineon.nfcbpsk.services.brandverification.ErrorResult;
import com.infineon.nfcbpsk.services.brandverification.BrandVerificationService;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthVerifyEvent;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthGenerateResponse;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthVerifyResponse;
import com.infineon.nfcbpsk.data.logger.LoggerFactory;
import com.infineon.nfcbpsk.data.logger.LoggerType;
import com.infineon.nfcbpsk.data.logger.TimeLogger;
import com.infineon.nfcbpsk.data.logger.FileLogger;
import com.infineon.nfcbpsk.ui.activities.ScanActivity;

import org.jetbrains.annotations.NotNull;


/**
 * View Model for the home page
 */
public class HomeViewModel extends AndroidViewModel {
    public ProgressDialog dialog ;
    public final TimeLogger timeLogger = new TimeLogger();
    public final TimeLogger totalTimeLogger = new TimeLogger();
    public final FileLogger fileLogger = (FileLogger) LoggerFactory.getLogger(getApplication().getApplicationContext(), LoggerType.FILE);
    public final MutableLiveData<ErrorResult> errorResult = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<MutualAuthVerifyResponse> maVerifyResponseMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> productViewPageUrl = new MutableLiveData<>();
    public byte[] productInformationMutableLiveData;
    public byte[] serviceInformationMutableLiveData;

    /**
     * Constructor for the home view model
     * @param application context of application
     */
    public HomeViewModel(@NotNull Application application) {
        super(application);
    }

    /**
     * Method to handle mutual authentication success response
     *
     * @param intent     Intent of return mutual authentication
     * @param activity   Activity instance
     * @param onVerifyMA Callback handler interface for mutual authentication verify response event
     */
    public void handleMutualAuthSuccess(Intent intent, Activity activity, MutualAuthVerifyEvent onVerifyMA) {

        isLoading.postValue(true);
        String bpUrl = intent.getExtras().getString(ScanActivity.RESULT_BRAND_PROTECTION_URI);
        double totalTime = intent.getExtras().getDouble(ScanActivity.RESULT_TIME);
        MutualAuthGenerateResponse mutualAuthGenerateResponse = intent.getExtras().getParcelable(ScanActivity.RESULT_MA_RESPONSE);
        byte[] bytes = intent.getExtras().getByteArray(ScanActivity.RESULT_MA_APDU_RESPONSE);
        productViewPageUrl.postValue(intent.getExtras().getString(ScanActivity.RESULT_URI));
        productInformationMutableLiveData = intent.getExtras().getByteArray(ScanActivity.RESULT_PRODUCT);
        serviceInformationMutableLiveData = intent.getExtras().getByteArray(ScanActivity.RESULT_SERVICE);
        timeLogger.start();
        totalTimeLogger.start();
        totalTimeLogger.addPreviousTime(totalTime);
        mutualAuthVerify(bpUrl, mutualAuthGenerateResponse, bytes, activity, onVerifyMA);
    }

    /**
     * Method perform mutual authentication verification
     *
     * @param url                URL to perform mutual authentication verification
     * @param maGenerateResponse Response of generate mutual authentication command
     * @param bytes              APDU bytes data response of mutual authentication command
     * @param activity           Activity instance
     * @param onVerifyMA         Callback for mutual authentication verification
     */
    private void mutualAuthVerify(String url, MutualAuthGenerateResponse maGenerateResponse, byte[] bytes, Activity activity, MutualAuthVerifyEvent onVerifyMA) {
        BrandVerificationService brandVerificationService = new BrandVerificationService(activity, url);
        brandVerificationService.performMutualAuthVerification(maGenerateResponse.sessionID, bytes, onVerifyMA);
    }
}