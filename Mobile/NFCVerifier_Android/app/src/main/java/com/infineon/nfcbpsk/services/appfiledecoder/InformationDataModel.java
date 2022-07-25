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
 * Model class for product and service information files
 */
public class InformationDataModel {
    /**
     * Type of the view
     */
    public final int type;
    /**
     * Title of the value
     */
    public final String title;
    /**
     * Value of the property
     */
    public final String value;

    /**
     * Creates the information data model class
     *
     * @param type  Type of the view
     * @param title Title of the view
     * @param value Value of the view
     */
    public InformationDataModel(int type, String title, String value) {
        this.type = type;
        this.title = title;
        this.value = value;
    }
}
