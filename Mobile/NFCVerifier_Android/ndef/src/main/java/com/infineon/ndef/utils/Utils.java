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

package com.infineon.ndef.utils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;


/**
 * Utility class for string related manipulations.
 */
public class Utils {
    /**
     * Array containing hex digits for toString conversions of byte arrays
     */
    private static final char[] HEX_DIGIT =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
            };

    /**
     * Default delimiter
     */
    private static final String SPACE = " ";

    /**
     * Logger instance of for all library packages
     */
    private static final Logger sLogger = Logger.getLogger("com.infineon.tools");


    /**
     * Private default constructor - prevents instantiation.
     */
    private Utils() {
        // do nothing
    }

    /**
     * Transforms a fraction of a byte array into an ASCII string.
     *
     * @param abyArray array containing ASCII string
     * @param iOffset  offset of string in byte array
     * @param iLength  length of ASCII string
     * @return ASCII string representation of byte array fraction
     */
    public static String toString(byte[] abyArray, int iOffset, int iLength) {
        StringBuilder oDesc = new StringBuilder(iLength);
        oDesc.setLength(iLength);

        for (int i = 0; i < iLength; i++)
            oDesc.setCharAt(i, (char) abyArray[iOffset++]);

        return oDesc.toString();
    }

    /**
     * Convert the content of a byte array into a hex string with space between each byte.
     *
     * @param value byte array to be converted.
     * @return hex string representation of byte array.
     */
    public static String toHexString(byte[] value) {
        return toHexString(value, 0, value.length, SPACE, false, null);
    }

    /**
     * Convert a byte array into a hex string. The formatting of the string may be influenced
     * by a delimiter string which is inserted before each byte. For the first byte, all
     * delimiter characters before ',', '.', ':', ';' or ' ' are skipped.
     *
     * @param value     byte array to be converted.
     * @param offset    offset of data within byte array
     * @param length    of data to be converted
     * @param delimiter delimiter string like ", " or " ", ", 0x", ":", ", (byte)0x"
     * @return resulting hex string
     */
    public static String toHexString(byte[] value, int offset, int length, String delimiter, boolean isByteFormatted, String format) {
        int i;

        // create string buffer of required size
        StringBuilder strValue = new StringBuilder(length * (2 + delimiter.length()));

        // go backwards until dedicated delimiter is found
        for (i = delimiter.length(); i > 0; i--) {
            if (isDedicatedDelimiter(delimiter.charAt(i - 1)))
                break;
        }

        if (i < delimiter.length()) {
            // append first delimiter string portion
            strValue.append(delimiter.substring(i));
        }

        // transform byte by byte
        for (i = 0; i < length; i++, offset++) {
            // append hex value
            strValue.append(HEX_DIGIT[(value[offset] >> 4) & 0xF]).append(HEX_DIGIT[value[offset] & 0xF]);
            if (isByteFormatted && ((i + 1) % 8 == 0)) {
                strValue.append(format);
            }
            // append delimiter if not last value
            if (i < (length - 1))
                strValue.append(delimiter);
        }

        // return resulting string
        return strValue.toString();
    }

    /**
     * Utility method to convert a variety of objects into a byte array. The supported
     * reference types are:
     * <ul><li>null is converted into an empty byte array</li>
     * <li>byte[] is returned unaltered</li>
     * <li>Integer is returned as byte array of 4 bytes with MSB first</li>
     * <li>Short is returned as byte array of 2 bytes with MSB first</li>
     * <li>Byte is returned as byte array of 1 byte</li>
     * <li>hex string is converted into its byte array representation</li>
     * <li>for any other object the <code>toString()</code> method is called and
     * the result is treated as hex string</li></ul>
     * Hex strings are accepted in various input formats e.g. "ABCDEF", "AB cd EF",
     * "0xab:0xc:0xde", "ab, C, DE". ASCII strings may be included if surrounded by
     * hyphens, e.g 'My String'.
     *
     * @param stream object to be converted into a byte array.
     * @return byte array representation of stream object.
     * @throws UtilException if conversion into a byte array fails.
     */
    public static byte[] toBytes(Object stream) throws UtilException {
        // check if null reference
        if (stream == null)
            return new byte[0];

        // check if already byte array
        if (stream instanceof byte[])
            return (byte[]) stream;

        // check if integer value
        if (stream instanceof Integer)
            return toBytes((Integer) stream, 4);

        if (stream instanceof Double)
            return toBytes(((Double) stream).intValue(), 4);

        // check if short value
        if (stream instanceof Short)
            return toBytes(((Short) stream).intValue(), 2);

        // check if byte value
        if (stream instanceof Byte)
            return toBytes(((Byte) stream).intValue(), 1);

        // check if String
        if (stream instanceof String)
            return toByteArray((String) stream);

        // try to convert object into string and from there to byte array
        return toByteArray(stream.toString());
    }

    /**
     * Helper function to convert a primitive value into a byte array.
     *
     * @param value  integer value to be converted.
     * @param length length of resulting array.
     * @return byte array representation of integer value (MSB first).
     */
    public static byte[] toBytes(int value, int length) {
        byte[] abValue = new byte[length];

        while (length > 0) {
            abValue[--length] = (byte) value;
            value >>= 8;
        }

        return abValue;
    }

    /**
     * Converts a hex string into a byte array. The hex string may have various formats
     * e.g. "ABCDEF", "AB cd EF", "0xab:0xc:0xde", "ab, C, DE". ASCII strings may be included
     * if surrounded by hyphens, e.g 'My String'.
     *
     * @param data hex string to be converted
     * @return byte array with converted hex string
     * @throws UtilException if conversion fails for syntactical reasons.
     */
    public static byte[] toByteArray(String data) throws UtilException {
        int i, iOffset, iLength = data.length();
        byte[] abyValue = new byte[iLength];
        boolean bOddNibbleCountAllowed = false;

        for (i = 0, iOffset = 0; i < iLength; i++) {
            char c = data.charAt(i);
            int iValue = -1;

            if ((c >= '0') && (c <= '9')) {
                iValue = c - '0';
            } else if ((c >= 'A') && (c <= 'F')) {
                iValue = c - 'A' + 10;
            } else if ((c >= 'a') && (c <= 'f')) {
                iValue = c - 'a' + 10;
            } else if (((c == 'x') || (c == 'X')) && ((iOffset & 1) == 1)) {
                if (abyValue[iOffset >> 1] == 0) {
                    bOddNibbleCountAllowed = true;

                    // ignore 0x..
                    iOffset--;
                } else {
                    // x but not 0x found
                    throw new UtilException("Illegal character in hex string");
                }
            } else if (c >= 'A') {
                // character cannot be delimiter
                throw new UtilException("Illegal character in hex string");
            } else if (c == '\'') {
                // read ASCII values
                for (i++; i < iLength; i++) {
                    c = data.charAt(i);
                    if (c == '\'')
                        break;

                    abyValue[iOffset >> 1] = (byte) c;
                    iOffset += 2;
                }

                if (((iOffset & 1) != 0) || (c != '\'')) {
                    // character cannot be start of ASCII string
                    throw new UtilException("Illegal character in hex string");
                }
            } else if ((iOffset & 1) == 1) {
                if (!bOddNibbleCountAllowed && isDedicatedDelimiter(c))
                    bOddNibbleCountAllowed = true;

                if (bOddNibbleCountAllowed) {
                    // delimiter found, so just one nibble specified (e.g. 0xA:0xB...)
                    iOffset++;
                }
            }

            if (iValue >= 0) {
                abyValue[iOffset >> 1] = (byte) ((abyValue[iOffset >> 1] << 4) | iValue);
                iOffset++;
            }
        }

        if (!bOddNibbleCountAllowed && ((iOffset & 1) != 0)) {
            throw new UtilException("Hex string has odd nibble count");
        }

        // calculate length of stream
        iLength = (iOffset + 1) >> 1;

        return Arrays.copyOf(abyValue, iLength);
    }

    /**
     * Check if character is a dedicated delimiter character
     *
     * @param c character to be checked.
     * @return true if dedicated delimiter character
     */
    static private boolean isDedicatedDelimiter(char c) {
        switch (c) {
            case ',':
            case '.':
            case ':':
            case ';':
                return true;
        }

        return (c <= ' ');
    }

    /**
     * Returns byte[] format for given int. Only minimum number of required bytes returned
     *
     * @param intValue integer value
     * @return byte array
     */
    public static byte[] getBytes(int intValue) {
        byte[] abArray;
        if ((intValue & 0xFF) == intValue)
            abArray = Utils.toBytes(intValue, 1);
        else if ((intValue & 0xFFFF) == intValue)
            abArray = Utils.toBytes(intValue, 2);
        else if ((intValue & 0xFFFFFF) == intValue)
            abArray = Utils.toBytes(intValue, 3);
        else
            abArray = Utils.toBytes(intValue, 4);
        return abArray;
    }


    /**
     * Converts given hex string into integer
     *
     * @param value any value
     * @return integer
     * @throws UtilException If there is utility related failure
     */
    public static int toInteger(Object value) throws UtilException {
        if (value instanceof Integer)
            return (Integer) value;
        else if (value instanceof Double)
            return ((Double) value).intValue();
        else {
            byte[] bytes = toBytes(value);
            byte[] bts = new byte[4];
            if (bytes.length < 4)
                System.arraycopy(bytes, 0, bts, bts.length - bytes.length, bytes.length);
            else
                bts = bytes;
            return getIntFromArray(bts, 0);
        }
    }

    /**
     * Extracts integer from given byte array
     *
     * @param paramArrayOfByte byte array from which the integer has to be extracted
     * @param paramInt         offset within the byte array at which the int has to be extracted
     * @return extracted integer
     */
    public static int getIntFromArray(byte[] paramArrayOfByte, int paramInt) {
        return paramArrayOfByte[paramInt] << 24 & 0xFF000000
                | paramArrayOfByte[(paramInt + 1)] << 16 & 0xFF0000
                | paramArrayOfByte[(paramInt + 2)] << 8 & 0xFF00
                | paramArrayOfByte[(paramInt + 3)] & 0xFF;
    }

    /**
     * Gives 2 byte integer
     *
     * @param data   bytes
     * @param offset offset
     * @return 2 byte integer
     */
    public static int getUINT16(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
    }


    /**
     * @param length : required length of byte array.
     * @param bais   : source of byte stream.
     * @return byte array.
     */

    public static byte[] getBytesFromStream(int length, InputStream bais) {
        try {
            byte[] bytes = new byte[length];
            final int read = bais.read(bytes, 0, bytes.length);
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String formatCommand(String tag, String cmd) {
        StringBuilder separator = new StringBuilder("\n");
        for (int i=0;i<tag.length();i++){
            separator.append(" ");
        }
        StringBuilder formattedCommand = new StringBuilder();
        for (int i=0;i<cmd.length();i++){
            if(i%16==0){
                formattedCommand.append(separator);
            }
            formattedCommand.append(" ").append(cmd.charAt(i)).append(cmd.charAt(++i));

        }
        return formattedCommand.toString();
    }


}