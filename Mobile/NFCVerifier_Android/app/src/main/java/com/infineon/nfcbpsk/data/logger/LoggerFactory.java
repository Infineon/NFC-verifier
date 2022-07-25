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
package com.infineon.nfcbpsk.data.logger;

import android.content.Context;
import com.infineon.nfcbpsk.BuildConfig;

/**
 * Factory class for providing the logger instances
 */
public class LoggerFactory {
    /**
     *  Method returns the logger instance based on the logger type
     * @param context Application context
     * @param loggerType Type of logger
     * @return logger instance
     */
    public static Logger getLogger(Context context, LoggerType loggerType){
        switch (loggerType){
            case FILE:
                if(BuildConfig.LOG_ENABLED){
                    return FileLogger.getFileLogger(context);
                }
                return null;
            case CONSOLE:
                return ConsoleLogger.getConsoleLogger();
        }
        return null;
    }
}
