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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;

import org.jetbrains.annotations.NotNull;

/**
 * View model for the options view page
 */
public class OptionsViewModel extends AndroidViewModel {

    public final MutableLiveData<Integer> autoOpenTime = new MutableLiveData<>();
    public PreferenceHelper appPreference;

    /**
     * Constructor
     *
     * @param application instance
     */
    public OptionsViewModel(@NonNull @NotNull Application application) {
        super(application);

    }

    /**
     * OnClick method for the increment and decrement button
     *
     * @param v instance of view which is click
     */
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_increment) {
            incrementAutoOpenTime();
        } else if (id == R.id.btn_decrement) {
            decrementAutoOpenTime();
        }
    }

    /**
     * Increment the auto open time by 1 sec
     */
    public void incrementAutoOpenTime() {
        if (appPreference.getAutoOpenDurationPref() < 15) {
            appPreference.setAutoOpenDurationPref(appPreference.getAutoOpenDurationPref() + 1);
            autoOpenTime.postValue(appPreference.getAutoOpenDurationPref());
        }
    }

    /**
     * Decrement the auto open time by 1 second
     */
    public void decrementAutoOpenTime() {
        if (appPreference.getAutoOpenDurationPref() > 3) {
            appPreference.setAutoOpenDurationPref(appPreference.getAutoOpenDurationPref() - 1);
            autoOpenTime.postValue(appPreference.getAutoOpenDurationPref());
        }
    }
}