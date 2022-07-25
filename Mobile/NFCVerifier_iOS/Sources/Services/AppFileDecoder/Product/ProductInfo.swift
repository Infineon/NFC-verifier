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
 * Model class to store the product information
 */
class ProductInfo{
    var profileInfo : ProfileType
    var layoutVersion: UInt8
    var modelNumber: String
    var manufactureDate: Date?
    var serialNumber: String
    var productCode:UInt16
    var manufacturerName:String
    var customField : [CustomFieldItem]
    
    /**
     * Initializes the product info instance
     */
    init(profileInfo: ProfileType, layoutVersion: UInt8, modelNumber: String, manufactureDate: Date?, serialNumber: String, productCode:UInt16, manufacturerName:String, customField : [CustomFieldItem]) {   
        self.profileInfo = profileInfo
        self.layoutVersion = layoutVersion
        self.modelNumber = modelNumber
        self.manufactureDate = manufactureDate
        self.serialNumber = serialNumber
        self.productCode = productCode
        self.manufacturerName = manufacturerName
        self.customField = customField
    }
}
