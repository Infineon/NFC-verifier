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

enum ActionType:UInt8 {
    
    /**
     * Do  the action (send the SMS, launch the browser, make the telephone call)
     */
    case DO_ACTION = 0
    /**
     * Save for later (store the SMS in INBOX, Save the telephone number  in contacts)
     */
    case SAVE_LATER = 1
    /**
     * Open for editing (Open an SMS in the SMS editor, Open the URI in URI editor, Open the telephone number for editing ).
     */
    case OPEN_FOR_EDITING = 2
}
