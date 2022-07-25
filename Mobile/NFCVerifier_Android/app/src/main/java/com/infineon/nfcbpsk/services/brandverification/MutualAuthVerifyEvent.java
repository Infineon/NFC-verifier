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

/**
 * Callback handler interface for mutual authentication verify response event
 */
public interface MutualAuthVerifyEvent {
    /**
     * Handles the success response with the response data
     *
     * @param response Response from the cloud service
     */
    void onSuccess(MutualAuthVerifyResponse response);

    /**
     * Handles the failure response with error code
     *
     * @param status HTTP status code
     * @param error  Response from the cloud service
     */
    void onError(int status, String error);

}