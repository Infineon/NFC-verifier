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

/**
 * Throws the NFC related exceptions.
 *
 * @author Infineon Technologies
 */
public class NFCException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * The Constructor to thrown the NFC related exceptions
     * @param s message with exception
     */
    public NFCException(String s) {
        super(s);
    }


    /**
     * Method is to thrown nfc related exception with exception and message
     *
     * @param message NFC exception string
     * @param e       NFC exception
     */
    public NFCException(String message, Exception e) {
        super(message, e);
    }

}
