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

package com.infineon.nfcbpsk.ui.viewmodels;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.services.appfiledecoder.CustomFieldItem;
import com.infineon.nfcbpsk.ui.adapters.InformationAdapter;
import com.infineon.nfcbpsk.services.appfiledecoder.InformationDataModel;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProductInformation;
import com.infineon.nfcbpsk.services.appfiledecoder.service.ServiceInformation;
import com.infineon.nfcbpsk.services.utilities.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * View model for authentic service and product information screen
 */
public class AuthenticViewModel extends ViewModel {
    /**
     * Constant for the string resources of the titles in authentic service and product information screen
     */
    public final int[] ids = {R.array.product_information_titles, R.array.service_information_titles};
    /**
     * Represents the product web page link
     */
    public final MutableLiveData<String> productWebPageURL = new MutableLiveData<>();

    /**
     * Information list
     */
    public final MutableLiveData<ArrayList<InformationDataModel>> informationList = new MutableLiveData<>();

    /**
     * Prepare the information list for the product and service both class
     *
     * @param activity           Current activity
     * @param productInformation Product information
     * @param serviceInformation Service information
     */
    public void prepareInformationList(Activity activity, ProductInformation productInformation, ServiceInformation serviceInformation) {

        String[] mainTitles = activity.getResources().getStringArray(R.array.information_titles);
        ArrayList<InformationDataModel> informationModels = new ArrayList<>();
        for (int counter = 0; counter < mainTitles.length; counter++) {
            String mainTitle = mainTitles[counter];
            if (R.array.product_information_titles == ids[counter] && productInformation != null) {
                informationModels.add(new InformationDataModel(InformationAdapter.VIEW_TYPE_TITLE, mainTitle, null));
                prepareData(activity, productInformation, informationModels, ids[counter]);
            }
            if (R.array.service_information_titles == ids[counter] && serviceInformation != null) {
                informationModels.add(new InformationDataModel(InformationAdapter.VIEW_TYPE_TITLE, mainTitle, null));
                prepareData(activity, serviceInformation, informationModels, ids[counter]);
            }

        }
        informationList.postValue(informationModels);
    }

    /**
     * Provides the logic to convert object property to array list as per string name
     *
     * @param activity          Context of activity
     * @param data              object data of parsing
     * @param informationModels Array list of information model class
     * @param id                Id of string resource
     */
    private void prepareData(Activity activity, Object data, ArrayList<InformationDataModel> informationModels, int id) {

        for (String value : activity.getResources().getStringArray(id)) {
            try {
                if (value.equalsIgnoreCase("customFields")) {
                    ArrayList<CustomFieldItem> propertyValue = (ArrayList<CustomFieldItem>) getValueFromObject(data, value);
                    assert propertyValue != null;
                    for (CustomFieldItem customData : propertyValue) {
                        informationModels.add(new InformationDataModel(InformationAdapter.VIEW_TYPE_DATA, customData.key, customData.value));
                    }
                } else {
                    int stringRes = R.string.class.getField(value).getInt(null);
                    String key = activity.getResources().getString(stringRes);
                    String propertyValue = Objects.requireNonNull(getValueFromObject(data, value)).toString();
                    informationModels.add(new InformationDataModel(InformationAdapter.VIEW_TYPE_DATA, key, propertyValue));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the product and service information class property value
     *
     * @param data  Object of product or service
     * @param value Value or property name
     * @return object of product or service
     * @throws NoSuchMethodException     method not found exception
     * @throws IllegalAccessException    IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     */
    private Object getValueFromObject(Object data, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = data.getClass().getMethod("get" + value.replaceFirst(value.substring(0, 1), value.substring(0, 1).toUpperCase(Locale.ENGLISH)));
        Object propertyValue = method.invoke(data);
        if (propertyValue instanceof Date) {
            return Utils.getDateToString((Date) propertyValue);
        }
        if (propertyValue instanceof Short) {
            short shortValue = (short) propertyValue;
            byte[] arr = new byte[]{(byte) (shortValue >> 8), (byte) (shortValue & 0xFF)};
            return com.infineon.ndef.utils.Utils.toHexString(arr);
        }
        return propertyValue;
    }


}