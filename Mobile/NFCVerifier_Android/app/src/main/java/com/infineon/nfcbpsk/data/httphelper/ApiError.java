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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model class to store the API error information
 */
public class ApiError {
    public String errorCode;
    public String errorMessage;

    /**
     * Initializes with the API error information
     *
     * @param jsonData         JSON data as string
     * @throws JSONException   Exception thrown if the JSON does not contain the required error info
     */
    public ApiError(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        this.errorCode = jsonObject.getString("ErrorCode");
        this.errorMessage = jsonObject.getString("Error");
    }

}
