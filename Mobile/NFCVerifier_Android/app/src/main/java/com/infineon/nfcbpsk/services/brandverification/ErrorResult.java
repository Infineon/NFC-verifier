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
package com.infineon.nfcbpsk.services.brandverification;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.infineon.nfcbpsk.R;

/**
 * Model class for storing internal error. Helps to map the internal error into displayable message.
 */
public class ErrorResult implements Parcelable {
    /**
     * Static constant defines NFC communication failed as error
     */
    public final static int TYPE_ERROR = 1;

    /**
     * Static constant defines NFC communication failed with warning
     */
    public final static int TYPE_WARNING = 2;
    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates
     * instances of your Parcelable class from a Parcel.
     */
    public static final Creator<ErrorResult> CREATOR = new Creator<ErrorResult>() {
        @Override
        public ErrorResult createFromParcel(Parcel in) {
            return new ErrorResult(in);
        }

        @Override
        public ErrorResult[] newArray(int size) {
            return new ErrorResult[size];
        }
    };

    /**
     * Type of the NFC communication failure
     */
    public final int type;
    /**
     * Error message
     */
    public final String message;
    /**
     * Title of error message
     */
    public final String title;

    /**
     * Initializes the class with type, message and title
     * @param type Type of the NFC communication failure
     * @param message Error message
     * @param title Title of error message
     */
    public ErrorResult(int type, String message, String title) {
        this.type = type;
        this.message = message;
        this.title = title;
    }

    /**
     * Initializes the class with Parcel
     * @param in The Parcel in which the object should be written. This value cannot be null.
     */
    protected ErrorResult(Parcel in) {
        type = in.readInt();
        message = in.readString();
        title = in.readString();
    }

    /**
     * Maps the internal error into user message.
     *
     * @param context Application context
     * @param message Internal error message
     * @param type Error type indicator (1 = Error, 2 = Warning)
     * @return User message
     */
    public static String getTitle(Context context, String message, int type) {
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_unsupported_nfc))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_select_aid))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_select_cc_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_select_ndef_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_read_ndef_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_read_cc_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_select_id_info_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_read_id_info_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_read_challenge_file))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_mutual_auth_failed))) {
            return context.getResources().getString(R.string.authentication_failed);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_bp_record_unable_to_read))) {
            return context.getResources().getString(R.string.could_t_verify_tag);
        }
        if (message.equalsIgnoreCase(context.getResources().getString(R.string.msg_unable_connect_tag))) {
            return context.getResources().getString(R.string.unsupported_tag);
        }
        if (type == TYPE_ERROR) {
            return context.getResources().getString(R.string.authentication_failed);
        } else {
            return context.getResources().getString(R.string.could_t_verify_tag);
        }
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable object instance. Value is either 0 or CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest Parcel: The Parcel in which the object should be written. This value cannot be null.
     * @param flags int: Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE. Value is either 0 or a combination of PARCELABLE_WRITE_RETURN_VALUE, and android.os.Parcelable.PARCELABLE_ELIDE_DUPLICATES
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(message);
        dest.writeString(title);
    }
}
