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

package com.infineon.ndef.model;

import com.infineon.ndef.ActionType;


/**
 * The NFC Local Type Name for the action is act (0x61 0x63 0x74).
 *
 * @author Infineon Technologies
 */
public class ActionRecord extends AbstractRecord {

    private ActionType actionType;

    /**
     * Constructor to create a NDEF action record.
     *
     * @param actionType The action record type value - Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     */
    public ActionRecord(ActionType actionType) {
        this.actionType = actionType;
        setRecordType(new RecordType("act"));
    }

    public ActionRecord() {
    }

    /**
     * Method to get the supported action type of a record.
     *
     * @return The action record type value - Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     */
    public ActionType getAction() {
        return actionType;
    }

    /**
     * Method to set the action type for a record.
     *
     * @param action Sets the action - Supported Action Type such as <br><i> DO_ACTION</i><br><i>SAVE_LATER</i><br><i>OPEN_FOR_EDITING</i>
     */
    public void setAction(ActionType action) {
        this.actionType = action;
    }


    @Override
    public String toString() {
        return "ActionType: " + actionType;
    }

    /**
     * Method returns true if the record supports action type else returns false.
     *
     * @return Status based on whether has action type or not
     */
    public boolean hasAction() {
        return actionType != null;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
        return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj Object: the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof ActionRecord))
            return false;
        ActionRecord other = (ActionRecord) obj;
        return actionType == other.actionType;
    }

}
