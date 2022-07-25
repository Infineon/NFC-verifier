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
package com.infineon.nfcbpsk.services.appfiledecoder.service;

import com.infineon.nfcbpsk.services.appfiledecoder.CustomFieldItem;

import java.util.ArrayList;
import java.util.Date;


/**
 * Model class to store the service information
 */
public class ServiceInformation {

    /**
     * Product profile layout version
     */
    public byte layoutVersion;
    /**
     * Purchase date of the product
     */
    public Date purchaseDate;
    /**
     * Warranty validity date of the product
     */
    public Date warrantyValidityDate;
    /**
     * Last service date of the product
     */
    public Date lastServiceDate;
    /**
     * Custom data array with key and values
     */
    public ArrayList<CustomFieldItem> customFields;

    /**
     * Getter for product profile layout version
     *
     * @return Product profile layout version
     */
    public byte getLayoutVersion() {
        return layoutVersion;
    }
    /**
     * Getter for purchase date of product
     *
     * @return  Purchase date of product
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Getter for warranty validity date of product
     *
     * @return  Warranty validity date of product
     */
    public Date getWarrantyValidityDate() {
        return warrantyValidityDate;
    }

    /**
     * Getter for last service date of product
     *
     * @return  Last service date of product
     */
    public Date getLastServiceDate() {
        return lastServiceDate;
    }

    /**
     * Getter for custom data field array of product
     *
     * @return Custom data field array with key and values
     */
    public ArrayList<CustomFieldItem> getCustomFields() {
        return customFields;
    }
}
