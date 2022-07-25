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


import com.infineon.ndef.converter.NDEFMessageEncoder;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.SmartPosterRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is to encode the smart poster record type.
 *
 * @author Infineon Technologies
 */
public class SmartPosterRecordEncoder implements RecordPayloadEncoder {

    /**
     * Method to encode the SmartPosterRecord data structure into record payload byte array.
     * @param wellKnownRecord wellKnownRecord SmartPosterRecord
     * @return record payload byte array.
     */
    @Override
    public byte[] encodePayload(AbstractRecord wellKnownRecord) {
        SmartPosterRecord myRecord = (SmartPosterRecord) wellKnownRecord;

        List<AbstractRecord> records = new ArrayList<>();

        if (myRecord.getTitleRecords() != null)
            records.addAll(myRecord.getTitleRecords());
        if (myRecord.getUriRecord() != null)
            records.add(myRecord.getUriRecord());
        if (myRecord.getActionRecord() != null)
            records.add(myRecord.getActionRecord());
        return NDEFMessageEncoder.instance().encrypt(records.toArray(new AbstractRecord[0]));
    }

}
