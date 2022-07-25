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
package com.infineon.nfcbpsk.services.brandverification;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model class to store the response parameters for verify mutual authentication response
 */
public class MutualAuthVerifyResponse {
    /**
     * Version of the API
     */
    public String version;
    /**
     * Verification result
     */
    public String authResult;

    /**
     * Initializes based on the JSON response received from the server
     *
     * @param jsonData          JSON response from verify-ma API
     * @throws JSONException    Exception thrown if the JSON doesn't have required parameters
     */
    public MutualAuthVerifyResponse(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        this.version = jsonObject.getString("Version");
        this.authResult = jsonObject.getString("AuthResult");
    }
}
