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

import android.util.Log;
import static com.infineon.ndef.utils.Utils.*;

/**
 * Logger class to log the messages into the console of the IDE
 */
public class ConsoleLogger implements Logger {
    /**
     * Prefix
     */
    final String TAG = "NFCBPSK_";
    /**
     * Logger instance
     */
    static ConsoleLogger consoleLogger;

    private ConsoleLogger() {

    }

    /**
     * Return the instance of Logger
     * @return return the instance of Console Logger
     */
    public static ConsoleLogger getConsoleLogger() {
        if (consoleLogger == null) {
            consoleLogger = new ConsoleLogger();
        }
        return consoleLogger;
    }

    /**
     * Method to print a message
     *
     * @param key   Keyword in which the log to be tagged
     * @param value String message to be logged
     */
    @Override
    public void log(String key, String value) {
        Log.i(TAG + key, value);
    }

    /**
     * Method to print an integer value
     *
     * @param key   Keyword in which the log to be tagged
     * @param value Integer value to be logged
     */
    @Override
    public void log(String key, int value) {
        Log.i(TAG + key, String.valueOf(value));
    }

    /**
     * Method to print an float value
     *
     * @param key   Keyword in which the log to be tagged
     * @param value Float value to be logged
     */
    @Override
    public void log(String key, float value) {
        Log.i(TAG + key, String.valueOf(value));
    }

    /**
     * Method to print an double value
     *
     * @param key   Keyword in which the log to be tagged
     * @param value Double value to be logged
     */
    @Override
    public void log(String key, double value) {
        Log.i(TAG + key, String.valueOf(value));
    }

    /**
     * Method to print an byte array
     *
     * @param key   Keyword in which the log to be tagged
     * @param bytes Byte array to be logged
     */
    @Override
    public void log(String key, byte[] bytes) {
        Log.i(TAG + key, toHexString(bytes));
    }
}
