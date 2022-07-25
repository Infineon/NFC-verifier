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
 * Protocol that defines the NFC communication callback methods
 */
protocol  NFCListener {
    /**
     * Callback method to indicate success and return NFC tag information
     * - Parameter message: Message to be displayed
     * - Parameter productInfoData: Product information as bytes
     * - Parameter serviceInfoData: Service information as bytes
     * - Parameter uriRecord: URI record information
     */
    func onNFCCommunicationSuccess(message:String, productInfoData: Data?, serviceInfoData: Data?, uriRecord: URIRecord?)
    
    /**
     * Callback method to indicate failure
     * - Parameters callbackType: Type of the error
     * - Parameters errorMessage: Error message to be displayed
     */
    func onNFCCommunicationError(callbackType:CallbackType, errorMessage:String)
}

/**
 * Defines the type of error callback
 */
enum CallbackType{
    case Error
    case Warning
}
