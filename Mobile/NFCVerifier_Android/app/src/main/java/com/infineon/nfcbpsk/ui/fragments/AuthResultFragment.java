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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.infineon.nfcbpsk.ui.adapters.InformationAdapter;
import com.infineon.nfcbpsk.databinding.FragmentAuthResultBinding;
import com.infineon.nfcbpsk.ui.viewmodels.AuthenticViewModel;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProductInformationDecoder;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProductInformation;
import com.infineon.nfcbpsk.services.appfiledecoder.product.ProfileType;
import com.infineon.nfcbpsk.services.appfiledecoder.service.ServiceInformationDecoder;
import com.infineon.nfcbpsk.services.appfiledecoder.service.ServiceInformation;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;

/**
 * Fragment view to represent the product and service information page
 */
public class AuthResultFragment extends Fragment {
    /**
     * Product web page URL reference
     */
    final String productWebPageURL;
    FragmentAuthResultBinding authenticFragmentBinding;
    ProductInformation productInformation;
    ServiceInformation serviceInformation;
    private AuthenticViewModel viewModel;

    /**
     * Handler for handle auto open web page
     */
    private Handler handler;
    final Runnable handleAutoOpen = new Runnable() {
        @Override
        public void run() {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productWebPageURL));
                requireActivity().startActivity(browserIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Constructor for the authentic fragment
     * @param productInformation Product information reference
     * @param serviceInformation Service information reference
     * @param productWebPageURL Product web page URL
     */
    public AuthResultFragment(byte[] productInformation, byte[] serviceInformation, String productWebPageURL) {
        try {
            this.productInformation = ProductInformationDecoder.decode(productInformation);
            if (this.productInformation != null  && this.productInformation.getProfileType() != ProfileType.A10) {
                this.serviceInformation = ServiceInformationDecoder.decode(serviceInformation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.productWebPageURL = prepareProductURL(productWebPageURL);
    }

    /**
     * If URL query parameter is already present, append the new parameter else add query parameter
     * @param url String URL
     * @return Updated URL
     */
    private String prepareProductURL(String url) {
        if (url.contains("?")) {
            return url + "&ref=app";
        } else {
            return url + "?ref=app";
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
        authenticFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_auth_result, container, false);
        setupViewModel();
        setupObservers();
        viewModel.prepareInformationList(requireActivity(), productInformation, serviceInformation);
        setupClickEvents();
        handleAutoOpen();
        return authenticFragmentBinding.getRoot();

    }

    /**
     * Method to handle the auto open web logic
     */
    private void handleAutoOpen() {
        PreferenceHelper appPreference = new PreferenceHelper(requireActivity());
        if (appPreference.getAutoOpenPref()) {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(handleAutoOpen, appPreference.getAutoOpenDurationPref() * 1000L);
            }
        }
    }

    /**
     * Called when the Fragment is no longer resumed. This is generally tied to Activity.onPause of the containing Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        stopAutoOpenWebPage();
    }

    /**
     * Method to disable the auto open product webpage handler
     */
    public void stopAutoOpenWebPage() {
        if (handler != null) {
            handler.removeCallbacks(handleAutoOpen);
        }
    }

    /**
     * Method to set the observer for product and service information
     */
    private void setupObservers() {
        viewModel.informationList.observeForever(informationDataModels -> {
            if (informationDataModels != null && informationDataModels.size() > 0) {
                authenticFragmentBinding.rvInformation.setVisibility(View.VISIBLE);
                authenticFragmentBinding.tvError.setVisibility(View.GONE);
                InformationAdapter adapter = new InformationAdapter(informationDataModels);
                authenticFragmentBinding.rvInformation.setLayoutManager(new LinearLayoutManager(getActivity()));
                authenticFragmentBinding.rvInformation.setAdapter(adapter);
            } else {
                authenticFragmentBinding.rvInformation.setVisibility(View.GONE);
                authenticFragmentBinding.tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Sets the view model instance
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthenticViewModel.class);
        authenticFragmentBinding.setLifecycleOwner(this);
        authenticFragmentBinding.setAuthenticateViewModel(viewModel);
        viewModel.productWebPageURL.postValue(productWebPageURL);
    }

    /**
     * Method setup the click events on scan again button and view product webpage button
     */
    private void setupClickEvents() {
        authenticFragmentBinding.btnScanAgain.setOnClickListener(v -> {
            HomeFragment.autoStartScan = true;
            requireActivity().onBackPressed();
        });
        authenticFragmentBinding.btnViewProductWebPage.setOnClickListener(v -> {
            stopAutoOpenWebPage();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productWebPageURL));
            startActivity(browserIntent);
        });
    }
}