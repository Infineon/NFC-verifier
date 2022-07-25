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

package com.infineon.nfcbpsk.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infineon.nfcbpsk.BuildConfig;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.ui.adapters.AboutLinksAdapter;
import com.infineon.nfcbpsk.ui.viewmodels.AboutViewModel;
import com.infineon.nfcbpsk.databinding.FragmentAboutBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment view to represent the about page
 */
public class AboutFragment extends Fragment {

    FragmentAboutBinding fragmentAboutBinding;
    AboutViewModel aboutViewModel;

    /**
     * Initializes the fragment with user interface configuration
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAboutBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_about, container, false);
        aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        fragmentAboutBinding.setViewModel(aboutViewModel);
        fragmentAboutBinding.setLifecycleOwner(this);
        aboutViewModel.prepareData(R.raw.about_links);
        setAdapter();
        setBuildName();
        return fragmentAboutBinding.getRoot();
    }

    /**
     * Method to set the build name and version
     */
    private void setBuildName() {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String displayVersion = String.format(getString(R.string.version), versionName, versionCode);
        fragmentAboutBinding.tvVersion.setText(displayVersion);
    }

    /**
     * Method to set the adapter to recycler view
     */
    public void setAdapter() {
        aboutViewModel.jsonString.observeForever(s -> {
            AboutLinksAdapter adapter = new AboutLinksAdapter(getActivity(), aboutViewModel.jsonParsing(s));
            fragmentAboutBinding.rvLinks.setLayoutManager(new LinearLayoutManager(getActivity()));
            fragmentAboutBinding.rvLinks.setAdapter(adapter);
        });
    }


}