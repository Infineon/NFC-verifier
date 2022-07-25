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


import com.infineon.ndef.utils.NDEFConstants;


import java.util.Arrays;

/**
 * The NDEF Icon record is for image support in NFC applications. A Smart Poster may include an icon by including one or
 * several MIME image records, depending on the NFC compliant device capability. The Icon record is an optional feature.
 * Multiple Smart Poster Icon Records can be added under the Smart Poster.
 * @author Infineon Technologies
 *
 */
public class IconRecord extends AbstractRecord {
    public static final String[] SUPPORTED_TYPES = {"image/jpeg",
            "image/png"};
    private byte[] image;
    private String mimeType;

    /**
     * Constructor to create an icon record with multipurpose internet mail extensions and image bytes stream
     *
     * @param mimeType MIME Type
     * @param image    Image data byte stream
     */
    public IconRecord(String mimeType, byte[] image) {
        if (!Arrays.asList(SUPPORTED_TYPES).contains(mimeType)) {
            throw new RuntimeException(
                    "Specified MIME type [" + mimeType + "] is not supported");
        }

        setImage(image);
        setMimeType(mimeType);

        setRecordType(new RecordType(mimeType));
        setTnf(NDEFConstants.TNF_MEDIA_TYPE);
    }

    /**
     * Method is to get the image byte stream from the record
     *
     * @return returns the Image data byte stream
     */
    public byte[] getImage() {
        return image == null ? null : image.clone();
    }

    /**
     * Method is to set the image byte stream
     *
     * @param image sets the image
     */
    public void setImage(byte[] image) {
        this.image = image.clone();
    }

    /**
     * Method is to get the multipurpose internet mail extensions value
     *
     * @return The MIME Type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Method is to set the multipurpose internet mail extensions value
     *
     * @param mimeType sets the MIME Type
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Icon: [" + Arrays.toString(image) + "]";
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((image == null) ? 0 : Arrays.hashCode(image));
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
        if (!(obj instanceof IconRecord))
            return false;
        IconRecord other = (IconRecord) obj;
        if (!other.mimeType.equals(mimeType))
            return false;
        return other.image == image;
    }

}
