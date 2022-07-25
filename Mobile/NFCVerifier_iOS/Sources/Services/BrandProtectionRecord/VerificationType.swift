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
 * Defines the brand verification types
 */
enum VerificationType:UInt8 {
    // Cloud Based Verification - 0x01
    case CLOUD = 0x01
    // PKI - 0x02
    case PKI = 0x02
    // BlockChain - 0x04
    case BLOCK_CHAIN = 0x04    
}
