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

package com.infineon.ndef.model;


import com.infineon.ndef.NDEFRecord;
import com.infineon.ndef.NFCException;

/**
 * Interface to decode the well known record types
 *
 * @author Infineon Technologies
 */
public interface IRecordDecoder {

    boolean canDecodeRecord(NDEFRecord record);

    AbstractRecord decodeRecord(NDEFRecord record) throws NFCException;

}
