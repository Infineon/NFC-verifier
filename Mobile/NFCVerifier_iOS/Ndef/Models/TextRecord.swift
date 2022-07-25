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

import Foundation

/**
 * An NDEF text record is a record used to store plain (unformatted) text on an NFC tag
 * Text record stores a UTF-8 or UTF-16 text context in it. It is associated with a language identifier. 
 * Multiple text records with the same language code are not permitted.
 * E.g. Text: Hello World! LanguageCode : en EncodingType:UTF8
 *
 */
class TextRecord : AbstractRecord {
    public static let LANGUAGE_CODE_MAX = 0x1f
    public static let UTF8:String.Encoding = .utf8
    public static let UTF16BE:String.Encoding = .utf16BigEndian
    private var text:String?
    private var encoding:String.Encoding?
    private var locale:Locale?
    
    /**
     * Constructor to create a text record with text ID and plain text value
     *  - Parameter key ID: if text record
     *  - Parameter text: Content of the Text record Eg. "Welcome"
     */
    init(key:String, text:String) {
        self.text = text
        self.encoding = .utf8
        self.locale = Locale.current
        super.init()
        self.setKey(key: key)
        self.setRecordType(recordType: RecordType(type: "T"))
    }
    
    /**
     *Constructor to create a text record with plain text value
     * - Parameter text Content of the Text record eg. "Welcome"
     */
    init(text:String){
        self.text = text
        self.encoding = .utf8
        self.locale = Locale(identifier: "en-IN")
        super.init()
        self.setRecordType(recordType: RecordType(type: "T"))
    }
    
    /**
     * Constructor to create a text record with plain text value, Locale and Type of Encoding.
     * - Parameter text Content of text record. Eg. "welcome"
     * - Parameter encoding only supported charsets are UTF-8 and UTF-16BE
     * - Parameter Locale A Locale object represents a specific geographical, political or cultural region. An operation that requires a Local-sensitive and uses the Locale to tailor information for the user. 
	 * for example, displaying a number is locale sensitive operation -- the number should be formatted according to customer/ convention of the user native country, region or culture.
     *
     */
    init(text:String, encoding:String.Encoding, locale:Locale){
        super.init()
        self.text = text
        self.encoding = encoding
        self.locale = locale
        setRecordType(recordType: RecordType(type: "T"))
    }
    
    /**
     * Method is to get the plain text content data of the text record
     * - Return the content of the text record
     */
    public func getText()-> String? {
        return text
    }
    
    /**
     * Method is to get the locale of the text record
     * - Returns the locale
     */
    public func getLocale()->Locale?{
        return locale
    }
    
    /**
     * Method is to get the supported encoding type of text record which could be either "UTF-8" and "UTF - 16 BE".
     * - Returns the supported charset are "UTF-8" and "UTF-16BE"
     */
    public func getEncoding()-> String.Encoding? {
        return encoding;
    }
    
    /**
     * Method to set the plain text content data into the text record
     * - Parameter text: sets content of text record
     */
    public func setText(text: String) {
        self.text = text
    }
    
    /**
     * Method is to set the type of encoding to either "UTF-16BE" into the text record
     * - Parameter encoding: supported Charsets "UTF-8" and "UTF-16BE"
     */
    public func setEncoding(encoding: String.Encoding) throws
    {
        self.encoding = encoding
    }
    
    /**
     * Method is to set Locale information into the text record
     * - Parameter local sets A Locale Object represents a specific geographical, political or cultural region. An operation that requres a Locale to perform its task is called locale-sensitivity and uses the Locale to trailker information for the user.
     */
    public func setLocale(locale:Locale){
        self.locale = locale
    }
    
    /**
     * Methods return true if text content is non empty for a text record else return false
     *  - Returns flag indicating text content is non empty
     */
    public func hasText() -> Bool {
       return self.text != nil
    }
    
    /**
     * Methods return true if Locale content is non empty for a text record else return false
     *  - Returns flag indicating locale content is non empty
     */
    public func hasLocale() -> Bool {
       return self.locale != nil
    }
    
    /**
     * Methods return true if encoding content is non empty for a text record else return false
     *  - Returns flag indicating encoding content is non empty
     */
    public func hasEncoding() -> Bool {
       return self.encoding != nil
    }
}
