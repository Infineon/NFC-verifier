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

/**
 * Exception class for Utility related exceptions.
 */
public class UtilException extends Exception {

    /**
     * Calls the super constructor with the given message.
     *
     * @param message message for exception
     */
    public UtilException(String message) {
        super(message);
    }

    /**
     * Calls the super constructor with the given message and the exception stack.
     *
     * @param message message for exception
     * @param e       exception stack
     */
    public UtilException(String message, Exception e) {
        super(message, e);
    }
}
