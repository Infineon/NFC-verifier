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

import com.infineon.nfcbpsk.BuildConfig;

import java.util.Date;

/**
 * Helper class to log performance using software timer
 */
public class TimeLogger {
    /**
     * Tag to print on console
     */
    final String TAG = "Performance";
    /**
     * Console Logger reference
     */
    final ConsoleLogger consoleLogger;

    /**
     * Previous time reference
     */
    Double previousTime = 0.0;
    /**
     * Start time reference
     */
    Date start;
    /**
     * end time reference
     */
    Date endTime;

    /**
     * Constructor to instance
     */
    public TimeLogger() {

        start();
        consoleLogger = (ConsoleLogger) LoggerFactory.getLogger(null, LoggerType.CONSOLE);
    }

    /**
     * Method to start the logger time
     */
    public void start() {
        start = new Date();
    }

    /**
     * Method to add the previous time
     *
     * @param previousTime previous time
     */
    public void addPreviousTime(Double previousTime) {
        this.previousTime = previousTime;
    }

    /**
     * Method to log the time difference in console
     *
     * @param label label of log Printing
     */
    public void logTime(String label) {
        if (BuildConfig.TIME_LOG_ENABLED) {
            double difference_In_Time = getDifferenceInTime();
            consoleLogger.log(TAG, label + " " + difference_In_Time + " ms");
        }
    }

    /**
     * Method to set the end time to current time
     */
    public void stop() {
        endTime = new Date();
    }

    /**
     * Calculates the difference between start time and current time
     *
     * @return time difference in millisecond
     */
    public double getDifferenceInTime() {
        double difference_In_Time
                = new Date().getTime() - start.getTime();
        double difference_In_Seconds
                = (difference_In_Time
                / 1000)
                % 60;

        return difference_In_Time + previousTime;

    }

    /**
     * Calculates the difference between start and end time
     *
     * @return time difference in millisecond
     */
    public double getTotalTimeTaken() {
        double difference_In_Time
                = endTime.getTime() - start.getTime();
        double difference_In_Seconds
                = (difference_In_Time
                / 1000)
                % 60;
        return difference_In_Time;
    }
}
