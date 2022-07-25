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

package com.infineon.ndef;


import com.infineon.ndef.converter.NDEFRecordDecoder;
import com.infineon.ndef.converter.NDEFRecordEncoder;
import com.infineon.ndef.model.AbstractRecord;
import com.infineon.ndef.model.ActionRecord;
import com.infineon.ndef.model.ExternalTypeRecord;
import com.infineon.ndef.model.SmartPosterRecord;
import com.infineon.ndef.model.TextRecord;
import com.infineon.ndef.model.URIRecord;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;

/**
 * This is the Factory class that provides methods for creation, encode, decode of NDEF records.
 *
 * @author Infineon Technologies
 */
public final class NFCFactory {

    private static final NDEFRecordEncoder recordEncryptor;
    private static final NDEFRecordDecoder recordDecryptor;

    static {
        recordEncryptor = NDEFRecordEncoder.instance();
        recordDecryptor = NDEFRecordDecoder.instance();
    }

    private NFCFactory() {
        
    }

    /**
     * Method is to create URI Record of NDEF record type. The NDEF URI Record is a well-known record type defined by the NFC Forum.
     * It carries a, potentially abbreviated, UTF-8 encoded Internationalized Resource Identifier (IRI).  A uri consists of a prefix and its contents.
     *
     * @param uri Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createURIRecord(String uri) {
        return recordEncryptor.encrypt(new URIRecord(uri));
    }

    /**
     * Method is to create NDEF message with the collection of NDEF Records
     *
     * @param records array of {@link NDEFRecord}(s)
     * @return {@link NDEFMessage}  <br>NDEF message along with {@link NDEFRecord}(s)
     */
    public static NDEFMessage createNDEFMessage(NDEFRecord... records) {
        return new NDEFMessage(records);
    }

    /**
     * Method is to create NDEF message along with {@link NDEFRecord}(s) using the Raw byte array data.
     *
     * @param data Raw byte array data.
     * @return {@link NDEFMessage}  <br>NDEF message along with {@link NDEFRecord}(s)
     */
    public static NDEFMessage createNDEFMessage(byte[] data) {
        return new NDEFMessage(data);
    }

    /**
     * Method is to create smart poster NDEF record. A Smart poster record contains a Text record and a URI record as its payload data.
     * The Smart Poster can also contain actions that will trigger an application in the device.
     *
     * @param title      Content of the Text record.
     * @param charSet    Only supported Charsets are "UTF-8" and "UTF-16BE".
     * @param locale     A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     * @param uri        Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     * @param actionType Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     * @return {@link NDEFRecord} of type Smart poster <i>("Sp")</i>
     */
    public static NDEFRecord createSmartPosterRecord(String title, Charset charSet, Locale locale, String uri, ActionType actionType) {

        Objects.requireNonNull(title, "Invalid title, failed to create smart poster record");
        Objects.requireNonNull(uri, "Invalid URI, failed to create smart poster record");

        TextRecord textRecord = new TextRecord(title);
        if (locale != null) {
            textRecord = new TextRecord(title, locale);
        }
        if (charSet != null && locale != null) {
            textRecord = new TextRecord(title, charSet, locale);
        }
        URIRecord uriRecord = new URIRecord(uri);
        ActionRecord actionRecord = null;
        if (actionType != null) {
            actionRecord = new ActionRecord(actionType);
        }
        return createSmartPosterRecord(textRecord, uriRecord, actionRecord);
    }

    /**
     * Method is to create smart poster NDEF record. A Smart poster record contains a Text record and a URI record as its payload data.
     * The Smart Poster can also contain actions that will trigger an application in the device.
     *
     * @param title      Content of the Text record.
     * @param locale     A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     * @param uri        Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     * @param actionType Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     * @return {@link NDEFRecord} of type Smart poster <i>("Sp")</i>
     */
    public static NDEFRecord createSmartPosterRecord(String title, Locale locale, String uri, ActionType actionType) {

        Objects.requireNonNull(title, "Invalid title, failed to create smart poster record");
        Objects.requireNonNull(uri, "Invalid URI, failed to create smart poster record");

        TextRecord textRecord = new TextRecord(title);
        if (locale != null) {
            textRecord = new TextRecord(title, locale);
        }
        URIRecord uriRecord = new URIRecord(uri);
        ActionRecord actionRecord = null;
        if (actionType != null) {
            actionRecord = new ActionRecord(actionType);
        }
        return createSmartPosterRecord(textRecord, uriRecord, actionRecord);
    }

    /**
     * Method is to create smart poster NDEF record. A Smart poster record contains a Text record and a URI record as its payload data.
     * The Smart Poster can also contain actions that will trigger an application in the device.
     *
     * @param title      Content of the Text record.
     * @param uri        Uniform Resource Identifier (URI). Eg. <i>https://www.company.com/</i>
     * @param actionType Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     * @return {@link NDEFRecord} of type Smart poster <i>("Sp")</i>
     */
    public static NDEFRecord createSmartPosterRecord(String title, String uri, ActionType actionType) {

        Objects.requireNonNull(uri, "Atleast one URI must be present for the Smart poster");

        TextRecord textRecord = null;
        if (title != null && !title.isEmpty()) {
            textRecord = new TextRecord(title);
        }
        URIRecord uriRecord = new URIRecord(uri);
        ActionRecord actionRecord = null;
        if (actionType != null) {
            actionRecord = new ActionRecord(actionType);
        }
        return createSmartPosterRecord(textRecord, uriRecord, actionRecord);
    }

    /**
     * Method to create NDEF smart poster record. An NDEF smart poster record is a record used to store plain (unformatted) text, URI and action Record on an NFC tag.
     * @param textRecord Text record to be added.
     * @param uriRecord URI Record to be added.
     * @param actionRecord Action Record to be added.
     * @return Encrypted form of NDEF record.
     */
    private static NDEFRecord createSmartPosterRecord(TextRecord textRecord, URIRecord uriRecord, ActionRecord actionRecord) {
        SmartPosterRecord smartPoserRecord = new SmartPosterRecord();
        smartPoserRecord.addTitleRecord(textRecord);
        smartPoserRecord.setUriRecord(uriRecord);
        smartPoserRecord.setActionRecord(actionRecord);
        return recordEncryptor.encrypt(smartPoserRecord);
    }

    /**
     * Method to create NDEF text record. An NDEF text record is a record used to store plain (unformatted) text on an NFC tag.
     *
     * @param text    Content of the Text record.
     * @param charSet {@link Charset} only supported Charsets are "UTF-8" and "UTF-16BE".
     * @param locale  : {@link Locale} A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createTextRecord(String text, Charset charSet, Locale locale) {

        Objects.requireNonNull(text, "Invalid text, failed to create text record");

        if (locale != null && charSet != null) {
            return recordEncryptor.encrypt(new TextRecord(text, charSet, locale));
        }
        if (locale != null) {
            return recordEncryptor.encrypt(new TextRecord(text, locale));
        }
        return recordEncryptor.encrypt(new TextRecord(text));
    }

    /**
     * Method to create NDEF text record. An NDEF text record is a record used to store plain (unformatted) text on an NFC tag.
     *
     * @param type  ID of text record
     * @param title Content of the Text record.
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createTextRecord(String type, String title) {
        return recordEncryptor.encrypt(new TextRecord(type, title));
    }

    /**
     * Method to create NDEF text record. An NDEF text record is a record used to store plain (unformatted) text on an NFC tag.
     *
     * @param title Content of the Text record.
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createTextRecord(String title) {
        return recordEncryptor.encrypt(new TextRecord(title));
    }

    /**
     * Method to create NDEF action record.
     *
     * @param actionType Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createActionRecord(ActionType actionType) {
        Objects.requireNonNull(actionType, "Invalid ActionType, failed to create action record");
        return recordEncryptor.encrypt(new ActionRecord(actionType));
    }

    /**
     * Method is to create smart poster NDEF record. A Smart poster record contains a Text record and a URI record as its payload data.
     * The Smart Poster can also contain actions that will trigger an application in the device.
     *
     * @param titleRecord  NDEF record of Type  TEXT 	    ('T').
     * @param uriRecord    NDEF record of Type  URI   	('U').
     * @param actionRecord NDEF record of Type  ACTION    ('act').
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     * @throws NFCException {@link NFCException} Throws NFC exception if condition not satisfied
     */
    public static NDEFRecord createSmartPosterRecord(NDEFRecord titleRecord, NDEFRecord uriRecord, NDEFRecord actionRecord) throws NFCException {

        Objects.requireNonNull(uriRecord, "Invalid URI record, failed to create smart poster record");

        SmartPosterRecord smpRecord = new SmartPosterRecord();

        smpRecord.setUriRecord((URIRecord) recordDecryptor.decrypt(uriRecord));
        //optional record
        if (titleRecord != null) {
            smpRecord.addTitleRecord((TextRecord) recordDecryptor.decrypt(titleRecord));
        }
        //optional record
        if (actionRecord != null) {
            smpRecord.setActionRecord((ActionRecord) recordDecryptor.decrypt(actionRecord));
        }

        return recordEncryptor.encrypt(smpRecord);
    }

    /**
     * Method is to decode the encoded record. Known record types are decoded into instances of their implementation class
     * and can be directly encoded as part of a message.
     *
     * @param ndefRecord Encrypted form of NDEF record [{@link NDEFRecord}].
     * @return {@link AbstractRecord} Decoded form of NDEF record
     * @throws NFCException {@link NFCException} Throws NFC exception if condition not satisfied
     */
    public static AbstractRecord decryptRecord(NDEFRecord ndefRecord) throws NFCException {
        return recordDecryptor.decrypt(ndefRecord);
    }

    /**
     * Method is to encode the record. Known record types are encoded.
     *
     * @param record Decoded form of NDEF record {@link AbstractRecord }.
     * @return {@link NDEFRecord} Encoded form of NDEF record.
     */
    public static NDEFRecord encryptRecord(AbstractRecord record) {
        return recordEncryptor.encrypt(record);
    }

    /**
     * Method is to create an external type record. The payload doesn't need to follow any specific structure.
     *
     * @param data record data bytes
     * @return {@link NDEFRecord} Encrypted form of NDEF record.
     */
    public static NDEFRecord createExternalTypeRecord(byte[] data) {
        return recordEncryptor.encrypt(new ExternalTypeRecord(data));
    }


}
