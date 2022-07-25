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

import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.NFCException;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.IRecordDecoder;
import com.infineon.ndef.model.RecordType;
import com.infineon.ndef.utils.NDEFConstants;
import com.infineon.ndef.utils.RecordUtils;

import java.util.Objects;

/**
 * Class is to decode a well known NDEF record type.
 *
 * @author Infineon Technologies
 */
public class RecordDecoder implements IRecordDecoder {

    RecordPayloadDecoder recordPayLoadDecoder;

    /**
     * Method to check record decoding is supported or not
     * @param record NDEFRecord to be check
     * @return flag indicating record decoding is supported or not
     */
    @Override
    public boolean canDecodeRecord(NDEFRecord record) {
        if (record.getTnf() == NDEFConstants.TNF_WELL_KNOWN || record.getTnf() == NDEFConstants.TNF_EXTERNAL) {
            recordPayLoadDecoder = RecordUtils.getPayloadDecoder(new RecordType(Objects.requireNonNull(record.getType())));
            return recordPayLoadDecoder != null;
        }

        return false;
    }

    /**
     * Method to decode the NDEFRecord into record data structure.
     * @param record NDEFRecord record payload
     * @return  Abstract record data structure
     * @throws NFCException throws the NFCException
     */
    @Override
    public AbstractRecord decodeRecord(NDEFRecord record) throws NFCException {
        return recordPayLoadDecoder.decodePayload(record.getPayload());
    }

}
