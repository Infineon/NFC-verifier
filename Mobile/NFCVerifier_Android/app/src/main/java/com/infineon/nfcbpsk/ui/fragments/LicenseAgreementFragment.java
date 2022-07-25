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

import com.infineon.nfcbpsk.BuildConfig;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;
import com.infineon.nfcbpsk.databinding.FragmentLicenseAgreementBinding;
import com.infineon.nfcbpsk.ui.viewmodels.LicenseAgreementViewModel;

/**
 * Fragment view to represent the license agreement page
 */
public class LicenseAgreementFragment extends Fragment {
    private static final String ARG_PARAM_FILE_ID = "PARAM_FILE_ID";
    private static final String ARG_PARAM_IS_FIRST_TIME_OPEN = "PARAM_FIRST_OPEN";
    private static final String ARG_PARAM_FILE_NAME = "PARAM_FILE_NAME";
    private int licenseFileID;
    private boolean isFirstTimeOpen = true;
    public String title;
    private FragmentLicenseAgreementBinding binding;

    /**
     * Constructor for the license and agreement fragment
     * @param licenseID Raw file ID from where license text should be loaded
     * @param isFirstTimeOpen Flag to indicate is LicenseAgreementFragment is visible first time or not
     * @param title Toolbar title
     */
    public static LicenseAgreementFragment newInstance(int licenseID,boolean isFirstTimeOpen,String title) {
        LicenseAgreementFragment fragment = new LicenseAgreementFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_FILE_ID, licenseID);
        args.putBoolean(ARG_PARAM_IS_FIRST_TIME_OPEN, isFirstTimeOpen);
        args.putString(ARG_PARAM_FILE_NAME, title);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment. This is called after onAttach and before onCreateView.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            licenseFileID = getArguments().getInt(ARG_PARAM_FILE_ID);
            title = getArguments().getString(ARG_PARAM_FILE_NAME);
            isFirstTimeOpen = getArguments().getBoolean(ARG_PARAM_IS_FIRST_TIME_OPEN);
        }
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
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_license_agreement, container, false);
        LicenseAgreementViewModel viewModel = new ViewModelProvider(this).get(LicenseAgreementViewModel.class);
        binding.setLicenseAgreementViewModel(viewModel);
        viewModel.prepareData(licenseFileID);
        viewModel.licenseString.observeForever(binding.text::setText);
        setAcceptButtonVisibility();
        setupClickEvent();
        return binding.getRoot();
    }

    /**
     * Method setup the click events on accept button
     */
    public void setupClickEvent() {
        binding.btnAcceptAgreement.setOnClickListener(v -> {
            PreferenceHelper appPreference = new PreferenceHelper(requireActivity());
            appPreference.setLicenseAcceptPref(BuildConfig.VERSION_CODE);
            requireActivity().onBackPressed();

        });
    }

    /**
     * Method to set the accept button visibility
     */
    public void setAcceptButtonVisibility() {
        if(isFirstTimeOpen){
            binding.layoutAcceptButton.setVisibility(View.VISIBLE);
        }
        else {
            binding.layoutAcceptButton.setVisibility(View.GONE);
        }
    }
}