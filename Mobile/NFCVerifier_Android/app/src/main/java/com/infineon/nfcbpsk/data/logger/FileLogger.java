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

import android.annotation.SuppressLint;
import android.content.Context;

import com.infineon.ndef.utils.Utils;
import com.infineon.nfcbpsk.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Logger that logs the output in a file
 */
public class FileLogger implements Logger {

    @SuppressLint("StaticFieldLeak")
    private static FileLogger fileLogger;
    final Context context;
    private final String FILENAME = "logger.txt";
    StringBuilder stringBuilder = new StringBuilder();

    /**
     * Constructor to initialize the logger context
     *
     * @param context Application context
     */
    private FileLogger(Context context) {
        this.context = context;
    }

    /**
     * Method to return the logger handle
     *
     * @param context Application context
     * @return Returns the logger handle
     */
    public static FileLogger getFileLogger(Context context) {
        if (fileLogger == null) {
            fileLogger = new FileLogger(context);
        }
        return fileLogger;
    }

    /**
     * Method to reset the log by clearing the buffer
     */
    public void resetLog() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.dateformat), Locale.US);
        String date = simpleDateFormat.format(new Date());
        logToBuffer(date + "#", false);
    }

    /**
     * Method to log the output to the buffer
     *
     * @param inputToFile String data to be logged
     * @param isAppend Flag to indicate is inputToFile should be append or set to buffer
     */
    private void logToBuffer(String inputToFile, boolean isAppend) {
        if (isAppend) {
            stringBuilder.append("\n").append(inputToFile);
        } else {
            stringBuilder = new StringBuilder(inputToFile);
        }
    }

    /**
     * Method to save the changes to the log file
     */
    public void commitToFile() {
        try {
            if (stringBuilder.toString().length() > 0) {
                FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fileOutputStream.write(stringBuilder.toString().getBytes());
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to print a message
     *
     * @param key   Keyword in which the log to be tagged
     * @param value String message to be logged
     */
    @Override
    public void log(String key, String value) {
        logToBuffer(key + " " + value, true);
    }

    /**
     * Method to print an integer value
     *
     * @param key   Keyword in which the log to be tagged
     * @param value Integer value to be logged
     */
    @Override
    public void log(String key, int value) {
        logToBuffer(key + " " + value, true);
    }

    /**
     * Method to print a float value
     *
     * @param key   Keyword in which the log to be tagged
     * @param value Float value to be logged
     */
    @Override
    public void log(String key, float value) {
        logToBuffer(key + " " + value, true);
    }

    /**
     * Method to print a double value
     * @param key   Keyword in which the log to be tagged
     * @param value Double value to be logged
     */
    @Override
    public void log(String key, double value) {
        logToBuffer(key + " " + value, true);
    }

    /**
     * Method to print a byte array
     *
     * @param key   Keyword in which the log to be tagged
     * @param bytes Byte array to be logged
     */
    @Override
    public void log(String key, byte[] bytes) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < key.length() - 1; i++) {
            space.append(" ");

        }

        logToBuffer(key + "" + Utils.toHexString(bytes, 0, bytes.length, " ", true, "\n" + space), true);
    }

    /**
     * Method to read the data from logger file
     * @return log data from file
     */
    public String readData() {
        try {
            FileInputStream fin = context.openFileInput(FILENAME);
            int a;
            StringBuilder temp = new StringBuilder();
            while ((a = fin.read()) != -1) {
                temp.append((char) a);
            }
            fin.close();
            return temp.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
