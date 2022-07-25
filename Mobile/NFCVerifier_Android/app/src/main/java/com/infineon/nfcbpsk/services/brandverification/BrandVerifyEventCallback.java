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

import com.infineon.nfcbpsk.services.apdu.ApduResponse;
import com.infineon.nfcbpsk.services.brandprotectionrecord.BrandProtectionRecord;
import com.infineon.ndef.model.URIRecord;

/**
 * Callback handler to handle the result of brand verification event
 */
public interface BrandVerifyEventCallback {
    /**
     * Method invoked on successful completion of brand verification event
     *
     * @param productAPDUResponse   Product data in APDU form
     * @param serviceAPDUResponse   Service data in APDU form
     * @param uriRecord             URI record
     * @param brandProtectionRecord Brand protection record
     * @param maGenerateResponse    Mutual auth response
     * @param apduResponse          APDU response of mutual auth
     */
    void onSuccess(ApduResponse productAPDUResponse,
                   ApduResponse serviceAPDUResponse,
                   URIRecord uriRecord,
                   BrandProtectionRecord brandProtectionRecord,
                   MutualAuthGenerateResponse maGenerateResponse,
                   ApduResponse apduResponse);

    /**
     * Method invoked on failure
     * @param error Error information
     */
    void onError(ErrorResult error);
}
