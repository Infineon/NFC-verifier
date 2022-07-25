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

package com.infineon.ndef.model.decoder;


import com.infineon.ndef.NFCException;
import com.infineon.ndef.converter.NDEFMessageDecoder;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.ActionRecord;
import com.infineon.ndef.model.SmartPosterRecord;
import com.infineon.ndef.model.TextRecord;
import com.infineon.ndef.model.URIRecord;

import java.util.List;

/**
 * Class is to decode the payload byte[] array of smart poster record type
 *
 * @author Infineon Technologies
 */
public class SmartPosterRecordDecoder implements RecordPayloadDecoder {

    /**
     * Method to decode the SmartPosterRecord payload byte array into record data structure.
     * @param payload SmartPosterRecord payload byte array
     * @return  Abstract record data structure
     * @throws NFCException throws the NFCException
     */
    @Override
    public AbstractRecord decodePayload(byte[] payload) throws NFCException {
        SmartPosterRecord smartPosterRecord = new SmartPosterRecord();
        NDEFMessageDecoder decryptor = NDEFMessageDecoder.instance();

        List<AbstractRecord> records = decryptor.decryptToRecords(decryptor.decrypt(payload));

        for (AbstractRecord record : records) {
            if (record instanceof URIRecord) {
                smartPosterRecord.setUriRecord((URIRecord) record);
            } else if (record instanceof TextRecord) {
                smartPosterRecord.addTitleRecord((TextRecord) record);
            } else if (record instanceof ActionRecord) {
                smartPosterRecord.setActionRecord((ActionRecord) record);
            } else {
                smartPosterRecord.addOtherRecords(record);

            }
        }
        return smartPosterRecord;
    }

}
