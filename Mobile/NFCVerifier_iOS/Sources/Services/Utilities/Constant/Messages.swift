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
 * Stores the messages that are displayed in the UI
 */
 
let MSG_HOLD_TAG = "Hold your iPhone near the item to learn more about it."
let MSG_READING =  "Authenticating, Please wait"

// Error in reading NDEF message
let ERR_MSG_COMMAND_READ_NDEF_FILE = "Failed to read NDEF message"
let ERR_MSG_COMMAND_SELECT_NDEF_FILE = "Failed to select NDEF file"
let ERR_MSG_COMMAND_READ_CC_FILE = "Failed to read CC file"
let ERR_MSG_COMMAND_SELECT_CC_FILE = "Failed to select CC file"
let ERR_MSG_COMMAND_SELECT_CMD_FILE = "Failed to select application"

let ERR_MSG_EXCEPTION_NDEF_PARSING = "Failed to parse NDEF message"

// Error in reading chip unique ID
let ERR_MSG_COMMAND_READ_CHIP_ID = "Failed to read chip ID"
let ERR_MSG_COMMAND_SELECT_CHIP_ID = "Failed to select EF.ID_INFO file"

// Error in getting challenge
let ERR_MSG_COMMAND_GET_CHALLENGE = "Failed to get challenge"

// Error in mutual authentication
let ERR_MSG_COMMAND_MUTUAL_AUTH = "Brand verification not successful!"

// Error in processing cloud service response
let ERR_MSG_MA_GENERATE_JSON_PARSING = "Failed to generate authenticate command"
let ERR_MSG_MA_VERIFY_PARSING = "Failed to process verify-ma response"

let ERR_MSG_READ_NDEF_MESSAGE = "Failed to parse NDEF message"
let ERR_MSG_READ_CHIP_ID_MESSAGE = "Failed to read chip ID"
let ERR_MSG_READ_CHALLENGE_MESSAGE  =  "Failed to get challenge"
let ERR_MSG_READ_MA = "Brand verification not successful!"
let ERR_MSG_READ_SERVICE_INFO = "Failed to read service information"
let ERR_MSG_PRODUCT_NOT_FOUND = "Failed to read product information"
let ERR_MSG_READ_PRODUCT_INFO = "Failed to read product information"

let ERR_MESG_PARSING_BP_RECORD = "Failed to parse brand protection record"
let ERR_MSG_PARSING_NDEF_RECORD = "Failed to parse NDEF record"

let ERR_MSG_UNSUPORTED_TAG = "Unsupported tag"
let ERR_MSG_REST_RESPONSE_500 = "The server encountered an unexpected condition which prevented it from fulfilling the request."
let ERR_MSG_REST_RESPONSE_403 = "Unauthorized request. The client does not have access rights to the content."
let ERR_MSG_VER_LIMIT_EXCEEDED = "Daily brand-verification limit exceeded for this key"

let ERR_MSG_UNSUPORT_NFC = "NFC is not supported by this device"
let EULA_FILE_NAME = "EndUserLicense"

let TRANSACTION_LOG_INITIAL_TEXT = "Share Transaction Log\n"
let ERR_TRANSACTION_LOG_NOT_AVAILBALE = "Transaction log not available"
let ERR_MSG_VER_URL = "Please check verification URL "
