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

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.infineon.nfcbpsk.ui.adapters.LinkModel;
import com.infineon.nfcbpsk.services.utilities.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.infineon.nfcbpsk.R.id.iv_facebook;
import static com.infineon.nfcbpsk.R.id.iv_instagram;
import static com.infineon.nfcbpsk.R.id.iv_linkedin;
import static com.infineon.nfcbpsk.R.id.iv_tweeter;
import static com.infineon.nfcbpsk.R.id.iv_youtube;

/**
 * View model for the about page
 */
public class AboutViewModel extends AndroidViewModel {
    public final MutableLiveData<String> jsonString = new MutableLiveData<>();

    /**
     * Default constructor for the about view model class
      * @param application context of the current application
     */ 
    public AboutViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Prepare the data to be listed
     */
    public void prepareData(int id) {
        String json = Utils.readRawFile(getApplication().getApplicationContext(), id);
        jsonString.postValue(json);
    }

    /**
     * Parse the JSON to show on recycler view
     *
     * @param jsonString String encoded json data
     * @return Returns the LinkModel array list
     */
    public ArrayList<LinkModel> jsonParsing(String jsonString) {
        ArrayList<LinkModel> linkModels = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                linkModels.add(new LinkModel(jsonObject.getString("name"), jsonObject.getString("link"), jsonObject.getInt("type"), jsonObject.getString("open")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return linkModels;
    }

    /**
     * Handle the links of the social media icons
     *
     * @param v View witch click
     */
    public void onClick(View v) {
        String link = "";

        String SOCIAL_TWITTER_LINK = "";
        String SOCIAL_LINKEDIN_LINK = "";
        String SOCIAL_YOUTUBE_LINK = "";
        String SOCIAL_INSTAGRAM_LINK = "";
        String SOCIAL_FB_LINK = "";
        int id = v.getId();
        if (id == iv_facebook) {
            link = SOCIAL_FB_LINK;
        } else if (id == iv_instagram) {
            link = SOCIAL_INSTAGRAM_LINK;
        } else if (id == iv_linkedin) {
            link = SOCIAL_LINKEDIN_LINK;
        } else if (id == iv_tweeter) {
            link = SOCIAL_TWITTER_LINK;
        } else if (id == iv_youtube) {
            link = SOCIAL_YOUTUBE_LINK;
        }
        if (link.trim().length() > 0) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            v.getContext().startActivity(browserIntent);
        }
    }
}
