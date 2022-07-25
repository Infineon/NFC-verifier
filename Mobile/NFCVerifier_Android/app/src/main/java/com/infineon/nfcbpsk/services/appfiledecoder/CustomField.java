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
package com.infineon.nfcbpsk.services.appfiledecoder;

/**
 * Custom data fields in the product or service information
 */
public class CustomField {
    /**
     * Represents the key label
     */
    public final String key;
    /**
     * Represents the value
     */
    public final String value;

    /**
     * Constructor for data logs
     *
     * @param key   Key of the custom data field
     * @param value Value of custom data field
     */
    public CustomField(String key, String value) {
        this.key = key;
        this.value = value;
    }
}