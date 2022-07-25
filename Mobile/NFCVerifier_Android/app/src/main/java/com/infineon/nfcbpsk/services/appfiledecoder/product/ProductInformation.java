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
package com.infineon.nfcbpsk.services.appfiledecoder.product;

import com.infineon.nfcbpsk.services.appfiledecoder.CustomFieldItem;

import java.util.ArrayList;
import java.util.Date;


/**
 * Model class to store the product information
 */
public class ProductInformation {
    /**
     * Model number of the product
     */
    public String modelNumber;
    /**
     * Manufacture date of the product
     */
    public Date manufactureDate;
    /**
     * Serial number of the product
     */
    public String serialNumber;
    /**
     * Product code
     */
    public short code;
    /**
     * Name of the manufacturer of the product
     */
    public String manufacturerName;
    /**
     * Custom fields as key-value pairs
     */
    public ArrayList<CustomFieldItem> customFields;
    /**
     * Product profile layout version
     */
    private byte layoutVersion;
    /**
     * Product data profile types
     */
    private ProfileType profileType;
    /**
     * Constructor
     */
    public ProductInformation() {

    }

    /**
     * Getter for product profile layout version
     *
     * @return Product profile layout version
     */
    public byte getLayoutVersion() {
        return layoutVersion;
    }

    /**
     * Setter for product profile layout version
     *
     * @param layoutVersion Product profile layout version
     */
    public void setLayoutVersion(byte layoutVersion) {
        this.layoutVersion = layoutVersion;
    }

    /**
     * Getter for product data profile types
     *
     * @return Product data profile types
     */
    public ProfileType getProfileType() {
        return profileType;
    }

    /**
     * Setter for product data profile type
     *
     * @param profileType Product data profile type
     */
    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    /**
     * Getter for model number of product
     *
     * @return Model number of product
     */
    public String getModelNumber() {
        return modelNumber;
    }

    /**
     * Getter for manufacture date of product
     *
     * @return Manufacture date of product
     */
    public Date getManufactureDate() {
        return manufactureDate;
    }

    /**
     * Getter for serial number of product
     *
     * @return Serial number of product
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Getter for code of product
     *
     * @return code of product
     */
    public short getCode() {
        return code;
    }

    /**
     * Getter for manufacturer name of product
     * @return  manufacturer of product
     */
    public String getManufacturerName() {
        return manufacturerName;
    }

    /**
     * Getter for custom Data array of product
     * @return Custom data array with key and values
     */
    public ArrayList<CustomFieldItem> getCustomFields() {
        return customFields;
    }

}
