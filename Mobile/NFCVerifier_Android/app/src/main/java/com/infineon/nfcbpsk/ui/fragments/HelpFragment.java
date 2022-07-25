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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.ui.adapters.AboutLinksAdapter;
import com.infineon.nfcbpsk.databinding.FragmentHelpBinding;
import com.infineon.nfcbpsk.ui.viewmodels.HelpViewModel;

/**
 * Fragment view to represent the help index page
 */
public class HelpFragment extends Fragment {

    FragmentHelpBinding helpFragmentBinding;
    private HelpViewModel viewModel;

    /**
     * Method to create the instance of the fragment
     * @return New instance of the fragment
     */
    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    /**
     * Method called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        helpFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_help, container, false);
        viewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        setAdapter();
        setupClickEvents();
        viewModel.prepareData(R.raw.help_links);
        return helpFragmentBinding.getRoot();
    }

    /**
     * Method setup the click events on user guide label
     */
    public void setupClickEvents() {
        helpFragmentBinding.tvViewUserManual.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity != null && getString(R.string.github_link).length()>0) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)));
                activity.startActivity(browserIntent);
            }
        });
    }

    /**
     * Set the adapter to recycler view
     */
    public void setAdapter() {
        viewModel.jsonString.observeForever(s -> {
            AboutLinksAdapter adapter = new AboutLinksAdapter(getActivity(), viewModel.jsonParsing(s));
            helpFragmentBinding.rvMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
            helpFragmentBinding.rvMenu.setAdapter(adapter);
        });
    }

}