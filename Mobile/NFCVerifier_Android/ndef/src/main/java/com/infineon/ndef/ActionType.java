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
package com.infineon.ndef;

public enum ActionType {
    /**
     * Do the action (send the SMS, launch the browser, make the telephone call)
     */
    DO_ACTION((byte) 0),
    /**
     * Save for later (store the SMS in INBOX, put the URI in a bookmark, save the telephone number in contacts)
     */
    SAVE_LATER((byte) 1),
    /**
     * Open for editing (open an SMS in the SMS editor, open the URI in an URI editor, open the telephone number for editing).
     */
    OPEN_FOR_EDITING((byte) 2);

    private final byte value;

    /**
     * Constructor for ActionType
     * @param value byte indication the action type
     */
    ActionType(byte value) {
        this.value = value;
    }

    /**
     * Method to return the action value
     * @param value byte value for action type
     * @return ActionType with respect to value
     */
    public static ActionType getActionByValue(byte value) {
        for (ActionType action : ActionType.values()) {
            if (value == action.getValue()) {
                return action;
            }
        }
        throw new IllegalArgumentException("Action with value :" + value + " is not supported");
    }

    /**
     * Getter for the value
     * @return Action type as byte
     */
    public byte getValue() {
        return value;
    }
}