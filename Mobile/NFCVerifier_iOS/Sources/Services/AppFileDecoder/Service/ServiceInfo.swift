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
 * Model class to store the service information
 */
class ServiceInfo {
    var layoutVersion: UInt8
    var purchaseDate: Date?
    var warrantyValidityDate: Date?
    var lastServiceDate: Date?
    var customField : [CustomFieldItem]
    
    /**
     * Initializes the service info instance
     */
    init( layoutVersion: UInt8, purchaseDate: Date?, warrantyValidityDate: Date?, lastServiceDate: Date?, customField : [CustomFieldItem]) {
        self.layoutVersion = layoutVersion
        self.purchaseDate = purchaseDate
        self.warrantyValidityDate = warrantyValidityDate
        self.lastServiceDate = lastServiceDate
        self.customField = customField        
    }
}
