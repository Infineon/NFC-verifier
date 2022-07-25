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

package com.infineon.nfcbpsk.data.httphelper;

/**
 * Defines the constants used for the cloud services
 */
public class Constants {

    /**
     * Cloud API for generating mutual authentication command data
     */
    public static final String API_MUTUAL_AUTH_GENERATE = "/sm/generate-ma";

    /**
     * Cloud API for verifying mutual authentication command response
     */
    public static final String API_MUTUAL_AUTH_VERIFY = "/sm/verify-ma";
}
