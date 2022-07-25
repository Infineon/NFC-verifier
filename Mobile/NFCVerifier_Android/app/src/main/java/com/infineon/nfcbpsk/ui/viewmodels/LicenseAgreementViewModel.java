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

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.infineon.nfcbpsk.services.utilities.Utils;

import org.jetbrains.annotations.NotNull;

/**
 * View Model for the license agreement page
 */
public class LicenseAgreementViewModel extends AndroidViewModel {
    public final MutableLiveData<String> licenseString = new MutableLiveData<>();
    /**
     * Default constructor for the license agreement view model class
     * @param application context of the current application
     */
    public LicenseAgreementViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    /**
     * Method to read the license file from the raw folder
     * @param fileID Raw file ID to be read
     */
    public void prepareData(int fileID) {
        String json = Utils.readRawFile(getApplication().getApplicationContext(), fileID);
        licenseString.postValue(json);
    }
}