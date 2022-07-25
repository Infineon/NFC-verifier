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

package com.infineon.ndef.model.encoder;

import com.infineon.ndef.InvalidURIException;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.URIRecord;

import java.io.UnsupportedEncodingException;

/**
 * Class is to encode the URI record type.
 *
 * @author Infineon Technologies
 */
public class URIPayloadEncoder implements RecordPayloadEncoder {

    /**
     * Method to encode the  URIRecord data structure into record payload byte array.
     * @param wellKnownRecord wellKnownRecord  URIRecord
     * @return record payload byte array.
     */
    @Override
    public byte[] encodePayload(AbstractRecord wellKnownRecord) {
        URIRecord uriRecord = (URIRecord) wellKnownRecord;

        if (uriRecord.getUri() == null || uriRecord.getUri().isEmpty()) {
            throw new InvalidURIException();
        }

        String uri = uriRecord.getUri();
        byte[] uriAsBytes = getUriAsBytes(uri);
        int abbreviateIndex = getAbbreviateIndex(uri.toLowerCase());
        if (abbreviateIndex == -1) {
            throw new InvalidURIException();
        }
        int uriCopyOffset = URIRecord.ABBRIVIABLE_URIS[abbreviateIndex].length();
        byte[] payload = new byte[uriAsBytes.length + 1 - uriCopyOffset];
        payload[0] = (byte) abbreviateIndex;
        System.arraycopy(uriAsBytes, uriCopyOffset, payload, 1, uriAsBytes.length - uriCopyOffset);
        return payload;


    }

    private byte[] getUriAsBytes(String uri) {
        try {
            return uri.getBytes(URIRecord.DEFAULT_URI_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    private int getAbbreviateIndex(String uri) {
        int maxLength = 0;
        int abbreviateIndex = 0;
        for (int x = 1; x < URIRecord.ABBRIVIABLE_URIS.length; x++) {

            String prefix = URIRecord.ABBRIVIABLE_URIS[x];

            if (uri.startsWith(prefix) && prefix.length() > maxLength) {
                abbreviateIndex = x;
                maxLength = prefix.length();
            }
        }
        return abbreviateIndex;
    }
}
