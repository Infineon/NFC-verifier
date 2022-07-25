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


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * An NDEF text record is a record used to store plain (unformatted) text on an NFC tag.
 * Text record stores a UTF-8 or UTF-16 text content in it. It is associated with a language identifier. Multiple text records with the same language code are not permitted.
 * E.g Text: Hello World! LanguageCode: en EncodingType: UTF8
 *
 * @author Infineon Technologies
 */
public class TextRecord extends AbstractRecord {

    public static final byte LANGUAGE_CODE_MASK = 0x1F;

    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Charset UTF16 = StandardCharsets.UTF_16BE;

    private String text;
    private Charset encoding;
    private Locale locale;

    /**
     * Constructor to create a text record with text ID and plain text value.
     *
     * @param key  ID of text record
     * @param text Content of the Text record Eg. "Welcome"
     */
    public TextRecord(String key, String text) {
        this(text, UTF8, Locale.getDefault());
        setKey(key);
        setRecordType(new RecordType("T"));
    }

    /**
     * Constructor to create a text record with plain text value.
     *
     * @param text Content of the Text record Eg. "Welcome"
     */
    public TextRecord(String text) {
        this(text, UTF8, Locale.getDefault());
        setRecordType(new RecordType("T"));
    }

    /**
     * Constructor to create a text record with plain text value and Locale.
     *
     * @param text   Content of the Text record Eg. "Welcome"
     * @param locale {@link Locale} A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     */
    public TextRecord(String text, Locale locale) {
        this(text, UTF8, locale);
        setRecordType(new RecordType("T"));
    }

    /**
     * Constructor to create a text record with plain text value, Locale and Type of Encoding.
     *
     * @param text     Content of the Text record. Eg. "Welcome"
     * @param encoding {@link Charset} only supported Charsets are "UTF-8" and "UTF-16BE".
     * @param locale   {@link Locale} A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     */
    public TextRecord(String text, Charset encoding, Locale locale) {
        this.encoding = encoding;
        this.text = text;
        this.locale = locale;
        setRecordType(new RecordType("T"));
        if (!encoding.equals(UTF8) && !encoding.equals(UTF16))
            throw new IllegalArgumentException("unsupported encoding. only utf8 and utf16 are allowed.");
    }


    public TextRecord() {
    }

    /**
     * Method is to get the plain text content data of the text record
     *
     * @return returns content of the Text record.
     */
    public String getText() {
        return text;
    }

    /**
     * Method is to set the plain text content data into the text record
     *
     * @param text sets content of text record
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Method is to get the locale of the text record
     *
     * @return A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Method is to set the Locale information into the text record
     *
     * @param locale sets A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Method is to get the supported encoding type of the text record which could be either "UTF-8" and "UTF-16BE".
     *
     * @return returns supported Charsets are "UTF-8" and "UTF-16BE".
     */
    public Charset getEncoding() {
        return encoding;
    }

    /**
     * Method is to set the type of encoding to either "UTF-8" and "UTF-16BE" into the text record
     *
     * @param encoding supported Charsets are "UTF-8" and "UTF-16BE".
     */
    public void setEncoding(Charset encoding) {
        if (!encoding.equals(UTF8) && !encoding.equals(UTF16))
            throw new IllegalArgumentException("unsupported encoding. only utf8 and utf16 are allowed.");

        this.encoding = encoding;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: [");

        if (hasKey())
            sb.append("Key/Id: ").append(getKey()).append(", ");

        sb.append("Text: ").append(text).append(", ");
        sb.append("Locale: ").append(locale.getLanguage()).append(
                locale.getCountry().length() == 0 ? "" : "-" + locale.getCountry());

        sb.append("]");
        return sb.toString();
    }

    /**
     * Method returns 'true', if text content is non-empty for a text record else returns 'false'.
     *
     * @return content of text record
     */
    public boolean hasText() {
        return text != null;
    }

    /**
     * Method returns 'true', if locale information is non-empty for a text record else returns 'false'.
     *
     * @return A Locale object represents a specific geographical, political, or cultural region. An operation that requires a Locale to perform its task is called locale-sensitive and uses the Locale to tailor information for the user. For example, displaying a number is a locale-sensitive operation--the number should be formatted according to the customs/conventions of the user's native country, region, or culture.
     */
    public boolean hasLocale() {
        return locale != null;
    }

    /**
     * Method returns 'true', if encoding type is available for a text record else returns 'false'.
     *
     * @return supported Charsets are "UTF-8" and "UTF-16BE".
     */
    public boolean hasEncoding() {
        return encoding != null;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj Object: the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TextRecord other = (TextRecord) obj;
        if (encoding == null) {
            if (other.encoding != null)
                return false;
        } else if (!encoding.equals(other.encoding))
            return false;
        if (locale == null) {
            if (other.locale != null)
                return false;
        } else if (!locale.equals(other.locale))
            return false;
        if (text == null) {
            return other.text == null;
        } else return text.equals(other.text);
    }
}
