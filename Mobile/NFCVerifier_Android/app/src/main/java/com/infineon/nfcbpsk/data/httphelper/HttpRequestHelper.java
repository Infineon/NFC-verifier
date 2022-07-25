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

import android.app.Activity;

import com.infineon.nfcbpsk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Helper class to perform HTTPS request to the cloud service
 */
public class HttpRequestHelper {
    /**
     * HTTPS URL
     */
    public final String url;

    private final Activity context;

    /**
     * Initializes the handler with the HTTPS URL
     *
     * @param context   Context of the requesting activity
     * @param url       HTTPS URL to which the request has to be made
     */
    public HttpRequestHelper(Activity context, String url) {
        this.url = url;
        this.context = context;
    }

    /**
     * Creates a HTTPS POST request, handles the HTTP response codes and triggers the response
     * callback with the response data
     *
     * @param payload            JSON payload data for the HTTP request body
     * @param onResponseCallback Callback interface to handle the response
     */
    public void postRequest(String methodName, JSONObject payload,
                            HttpResponseEvent onResponseCallback) {
        String finalUrl = url + methodName;
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(finalUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDefaultUseCaches(false);
                conn.setUseCaches(false);
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(payload.toString());
                os.flush();
                os.close();
                String message = conn.getResponseMessage();
                Map<String, List<String>> stringListMap = conn.getHeaderFields();
                int status = conn.getResponseCode();

                if (status == 200 || status == 201) {   // HTTP OK / CREATED / Success
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    onResponseCallback.onSuccess(status, sb.toString());
                } else {    // Error code
                    try {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        String error = jsonObject.getString("Error");
                        onResponseCallback.onError(status, error);
                    } catch (JSONException e) {
                        onResponseCallback.onError(status, message);
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                onResponseCallback.onError(400, context.getResources().getString(R.string.internet_error));
            }
        });
        thread.start();
    }
}
