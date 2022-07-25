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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.databinding.FragmentOptionsBinding;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;
import com.infineon.nfcbpsk.ui.viewmodels.OptionsViewModel;


/**
 * Fragment view to represent the options page
 */
public class OptionsFragment extends Fragment {
    private OptionsViewModel viewModel;

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment OptionsFragment.
     */
    public static OptionsFragment newInstance() {
        return new OptionsFragment();
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
       FragmentOptionsBinding optionsFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_options, container, false);
        viewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
        optionsFragmentBinding.setOptionsViewModel(viewModel);
        optionsFragmentBinding.setLifecycleOwner(this);
        viewModel.appPreference = new PreferenceHelper(requireActivity());
        optionsFragmentBinding.switchAutoOpen.setChecked(viewModel.appPreference.getAutoOpenPref());
        optionsFragmentBinding.switchAutoOpen.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.appPreference.setAutoOpenPref(isChecked));
        viewModel.autoOpenTime.postValue(viewModel.appPreference.getAutoOpenDurationPref());
        return optionsFragmentBinding.getRoot();
    }
}