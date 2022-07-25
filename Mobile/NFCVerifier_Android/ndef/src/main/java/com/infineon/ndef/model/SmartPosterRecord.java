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

import java.util.ArrayList;

/**
 * The Smartposter Record type is 'Sp'.
 * The smart poster is the type of record that combines multiple records under it.
 * The concept of the smart poster is built around URI�s that are very powerful and can represent the information from unique identifiers to EPC codes to web addresses to SMS messages to phone calls and beyond.
 * The smart poster record defines a structure that associates a URI with several types of metadata such as action, URI type, size of the content the URI refers to, etc.
 * Apart from the specified record types, other records can also be added under the smart poster record.
 *
 * @author Infineon Technologies
 */
public class SmartPosterRecord extends AbstractRecord {

    /**
     * The Title record for the service (there can be many of these in different languages, but a
     * language MUST NOT be repeated). This record is optional
     */
    private final ArrayList<TextRecord> titleRecords = new ArrayList<>();
    private final ArrayList<AbstractRecord> otherRecords = new ArrayList<>();
    private ActionRecord actionRecord;
    /**
     * This is the core of the Smart Poster, and all other records are just metadata
     * about this record. There MUST be one URI record and there MUST NOT be more than one
     */
    private URIRecord uriRecord;

    /**
     * The Constructor is to create a smart poster record
     */
    public SmartPosterRecord() {
        setRecordType(new RecordType("Sp"));
    }

    /**
     * Method is to get the action record from the smart poster record
     *
     * @return The action record
     */
    public ActionRecord getActionRecord() {
        return actionRecord;
    }

    /**
     * Method is to add an action record to the smart poster record
     *
     * @param actionRecord sets the action record
     */
    public void setActionRecord(ActionRecord actionRecord) {
        this.actionRecord = actionRecord;
    }

    /**
     * Method is to get the URI record from the smart poster record
     *
     * @return The URI record
     */
    public URIRecord getUriRecord() {
        return uriRecord;
    }

    /**
     * Method is to add a URI record to the smart poster record
     *
     * @param uriRecord sets the URI record
     */
    public void setUriRecord(URIRecord uriRecord) {
        this.uriRecord = uriRecord;
    }

    /**
     * Method is to get the Other records from the smart poster record
     *
     * @return The Text record
     */
    public ArrayList<AbstractRecord> getOtherRecords() {
        return otherRecords;
    }

    /**
     * Method is to add a text record to the smart poster record
     *
     * @param otherRecords sets the Other records
     */
    public void addOtherRecords(AbstractRecord otherRecords) {
        this.otherRecords.add(otherRecords);
    }

    /**
     * Method is to get the text record from the smart poster record
     *
     * @return The Text record
     */
    public ArrayList<TextRecord> getTitleRecords() {
        return titleRecords;
    }

    /**
     * Method is to add a text record to the smart poster record
     *
     * @param titleRecord sets the Text record
     */
    public void addTitleRecord(TextRecord titleRecord) {
        boolean duplicateLocale = false;
        for (TextRecord textRecord : titleRecords) {
            if (textRecord.getLocale() == titleRecord.getLocale()) {
                duplicateLocale = true;
                break;
            }
        }
        if (!duplicateLocale) {
            titleRecords.add(titleRecord);
        }
    }


    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((actionRecord == null) ? 0 : actionRecord.hashCode());
        result = prime * result + titleRecords.hashCode();
        result = prime * result + ((uriRecord == null) ? 0 : uriRecord.hashCode());
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
        if (!(obj instanceof SmartPosterRecord))
            return false;
        SmartPosterRecord other = (SmartPosterRecord) obj;
        if (actionRecord == null) {
            if (other.actionRecord != null)
                return false;
        } else if (!actionRecord.equals(other.actionRecord))
            return false;
        if (!titleRecords.equals(other.titleRecords))
            return false;
        if (uriRecord == null) {
            return other.uriRecord == null;
        } else return uriRecord.equals(other.uriRecord);
    }

}
