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
package com.infineon.nfcbpsk.data.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class to store and retrieve user preferences in Android
 */
public class PreferenceHelper {
    /**
     * Constant value to save Auto Open web page setting
     */
    private static final String TAG_AUTO_OPEN_LINK = "AUTO_OPEN_LINK";
    /**
     * Constant value to save Auto Open web page time setting
     */
    private static final String   TAG_AUTO_OPEN_DURATION = "AUTO_OPEN_DURATION";
    /**
     * Constant value to save License Agreement Accept or not
     */
    private static final String   TAG_EULA_ACCEPTED_VERSION = "EULA_ACCEPTED_VERSION";
    /**
     * Activity reference
     */
    final Activity activity;
    /**
     * Shared preference
     */
    final SharedPreferences sharedPreferences;

    /**
     * Default auto open time in sec
     */
    final int DEFAULT_AUTO_OPEN_TIME = 3;
    /**
     * Initialize the preference manager
     *
     * @param activity instance of Activity class
     */
    public PreferenceHelper(Activity activity) {
        this.activity = activity;
        /*
          Static Constant for Preferences Id
         */
        String AppPreference = "NFC_VERIFIER_PREF";
        sharedPreferences = activity.getSharedPreferences(AppPreference, Context.MODE_PRIVATE);

    }

    /**
     * Returns whether the license agreement is accepted by user or not
     *
     * @return 0 if not accepted, else the EULA version number (>0)
     */
    public int getLicenseAcceptPref() {
        return sharedPreferences.getInt(TAG_EULA_ACCEPTED_VERSION, 0);
    }

    /**
     * Sets the preference that the license agreement is accepted
     *
     * @param acceptedVersion Version number of the accepted license agreement
     */
    public void setLicenseAcceptPref(int acceptedVersion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_EULA_ACCEPTED_VERSION, acceptedVersion);
        editor.apply();
    }

    /**
     * Get the Auto-Open setting value
     *
     * @return Flag indicating whether auto-open is enabled or disabled
     */
    public boolean getAutoOpenPref() {
        return sharedPreferences.getBoolean(TAG_AUTO_OPEN_LINK, false);
    }

    /**
     * Set Auto-Open preference
     *
     * @param isAutoOpen Flag to enable or disable
     */
    public void setAutoOpenPref(boolean isAutoOpen) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(TAG_AUTO_OPEN_LINK, isAutoOpen);
        editor.apply();
    }

    /**
     * @return Return the Auto-Open-Delay in seconds
     */
    public int getAutoOpenDurationPref() {
        int time = sharedPreferences.getInt(TAG_AUTO_OPEN_DURATION, DEFAULT_AUTO_OPEN_TIME);
        return time == 0 ? DEFAULT_AUTO_OPEN_TIME : time;
    }

    /**
     * Sets the Auto-Open-Delay in seconds
     *
     * @param timeInSec Time in seconds
     */
    public void setAutoOpenDurationPref(int timeInSec) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TAG_AUTO_OPEN_DURATION, timeInSec);
        editor.apply();
    }
}
