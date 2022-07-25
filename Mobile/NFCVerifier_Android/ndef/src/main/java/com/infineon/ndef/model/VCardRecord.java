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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * vCard is an electronic business card that contains the contact information of an individual such as the name, address information, 
 * telephone numbers, e-mail address, date of birth and other needed details. It is stored in a VCF format, a standard one for the electronic business cards.
 * @author Infineon Technologies
 *
 */
public class VCardRecord extends AbstractRecord {

	public static final String[] SUPPORTED_TYPES = { "text/vcard",
	"text/x-vcard" };

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private String vCardString;
	private String mimeType;

	/**
	 * Constructor is to create a new VCard record type.
	 * @param mimeType MIME Type value
	 * @param vCardString VCard string
	 */
	public VCardRecord(String mimeType, String vCardString) {
		if (!Arrays.asList(SUPPORTED_TYPES).contains(mimeType)) {
			throw new RuntimeException(
					"Specified MIME type [" + mimeType + "] is not supported");
		}

		setVCardString(vCardString);
		setMimeType(mimeType);

		setRecordType(new RecordType(mimeType));
		setTnf(NDEFConstants.TNF_MEDIA_TYPE);
	}

	/**
	 * Method is to get the plain VCard string
	 * @return VCard string
	 */
	public String getVCardString() {
		return vCardString;
	}

	/**
	 * Method is to set the plain VCard string
	 * @param vCardString Sets VCard string
	 */
	public void setVCardString(String vCardString) {
		this.vCardString = vCardString;
	}

	/**
	 * Method is to get the plain VCard Multipurpose Internet Mail Extensions type
	 * @return MIME Type value
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Method is to set the plain VCard Multipurpose Internet Mail Extensions type
	 * @param mimeType MIME Type value
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
		return "vCard: [" + vCardString + "]";
	}

	/**
	 * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those provided by HashMap.
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((vCardString == null) ? 0 : vCardString.hashCode());
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
		if (!(obj instanceof VCardRecord))
			return false;
		VCardRecord other = (VCardRecord) obj;
		if (vCardString == null) {
			return other.vCardString == null;
		} else if (!vCardString.equals(other.vCardString))
			return false;
		else return other.mimeType.equals(mimeType);
	}

}
