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

import com.infineon.nfcbpsk.data.logger.LoggerFactory;
import com.infineon.nfcbpsk.data.logger.LoggerType;
import com.infineon.nfcbpsk.data.logger.FileLogger;


/**
 * View model for the recent transaction page
 */
public class RecentTransactionViewModel extends AndroidViewModel {
    public final FileLogger fileLogger;

    /**
     * Default constructor for the about view model class
     * @param application context of the current application
     */
    public RecentTransactionViewModel(@NonNull Application application) {
        super(application);
        fileLogger = (FileLogger) LoggerFactory.getLogger(application, LoggerType.FILE);
    }
}