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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.infineon.nfcbpsk.BuildConfig;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.services.brandverification.ErrorResult;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthVerifyEvent;
import com.infineon.nfcbpsk.services.brandverification.MutualAuthVerifyResponse;
import com.infineon.nfcbpsk.databinding.FragmentHomeBinding;
import com.infineon.nfcbpsk.ui.activities.ScanActivity;
import com.infineon.nfcbpsk.ui.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import static com.infineon.nfcbpsk.services.brandverification.ErrorResult.TYPE_WARNING;
import static java.util.Objects.requireNonNull;

/**
 * Fragment view to represent the home page
 */
public class HomeFragment extends Fragment implements MutualAuthVerifyEvent {
    public static boolean autoStartScan;
    FragmentHomeBinding fragmentHomeBinding;
    private HomeViewModel viewModel;

    /**
     * Activity result handler to handle the result from scanning activity
     */
    final ActivityResultLauncher<Intent> activityStartForNFCPollingResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            nfcPollingResult -> {
                if (nfcPollingResult.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = nfcPollingResult.getData();
                    viewModel.handleMutualAuthSuccess(requireNonNull(intent), getActivity(), HomeFragment.this);
                    autoStartScan = false;
                } else if (nfcPollingResult.getResultCode() == Activity.RESULT_CANCELED) {
                    Intent intent = nfcPollingResult.getData();
                    ErrorResult errorResult = null;
                    if (intent != null) {
                        errorResult = intent.getExtras().getParcelable(ScanActivity.RESULT_MESSAGE);
                    }
                    viewModel.errorResult.postValue(errorResult);
                    if (viewModel.fileLogger != null) {
                        viewModel.fileLogger.commitToFile();
                    }
                }
            });

    /**
     * Constructor
     * @param autoStartScan flag to indicate the start the scanning activity
     */
    public HomeFragment(boolean autoStartScan) {
        HomeFragment.autoStartScan = autoStartScan;
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
        fragmentHomeBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        return fragmentHomeBinding.getRoot();
    }

    /**
     * Called immediately after onCreateView has returned, but before any saved state has been restored in to the view. This gives subclasses a chance to initialize themselves once they know their view hierarchy has been completely created. The fragment's view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by onCreateView.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();
        setupClickEvents();
        if(viewModel.dialog == null){
            viewModel.dialog = ProgressDialog.show(getActivity(), "",
                    getString(R.string.loading), true);
        }
        setupObservers();
        loadDefaults();
        handleAutoTagScanning();
    }

    /**
     * Method to handle automatic launch of scanning activity
     */
    public void handleAutoTagScanning() {
        PreferenceHelper appPreference = new PreferenceHelper(requireActivity());
        if (autoStartScan && appPreference.getLicenseAcceptPref() == BuildConfig.VERSION_CODE) {
            activityStartForNFCPollingResult.launch(new Intent(getContext(), ScanActivity.class));
        }
    }

    /**
     * Setup the view model instance
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding.setLifecycleOwner(this);
        fragmentHomeBinding.setHomeViewModel(viewModel);
    }

    /**
     * Add the observer to display progress icon
     */
    private void setupObservers() {
        //Hide and display the loader on screen
        viewModel.isLoading.observeForever(isLoading -> {
            if (isLoading) {
               viewModel.dialog.show();
            } else {
                viewModel. dialog.dismiss();
            }
        });
    }

    /**
     * Method set the default setting of UI
     */
    private void loadDefaults() {
        viewModel.isLoading.setValue(false);
        viewModel.errorResult.postValue(null);
    }

    /**
     * Set the click event to UI components
     */
    private void setupClickEvents() {
        /*
          Register click for scan again button
         */
        fragmentHomeBinding.btnScanAgain.setOnClickListener(v -> {
            loadDefaults();
            activityStartForNFCPollingResult.launch(new Intent(getContext(), ScanActivity.class));
        });
        /*
          Register Scan and Verify click event
         */
        fragmentHomeBinding.btnScanVerify.setOnClickListener(v -> activityStartForNFCPollingResult.launch(new Intent(getContext(), ScanActivity.class)));
    }

    /**
     * Callback method to handle the success response with the mutual auth verify response data
     *
     * @param response mutual auth verify response data from the cloud service
     */
    @Override
    public void onSuccess(MutualAuthVerifyResponse response) {
        viewModel.timeLogger.logTime(getString(R.string.step4));
        viewModel.totalTimeLogger.logTime(getString(R.string.total_time));
        if (viewModel.fileLogger != null) {
            viewModel.fileLogger.log(getString(R.string.total_time_taken), viewModel.totalTimeLogger.getDifferenceInTime() + " ms");
            viewModel.fileLogger.commitToFile();
        }
        viewModel.maVerifyResponseMutableLiveData.postValue(response);
        viewModel.isLoading.postValue(false);
        AuthResultFragment authResultFragment = new AuthResultFragment(viewModel.productInformationMutableLiveData, viewModel.serviceInformationMutableLiveData, viewModel.productViewPageUrl.getValue());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, authResultFragment).addToBackStack(null)
                .commit();

    }

    /**
     * Callback method to handles the failure response with error code
     *
     * @param status HTTP status code
     * @param error  Response from the mutual auth cloud service
     */
    @Override
    public void onError(int status, String error) {
        viewModel.errorResult.postValue(new ErrorResult(TYPE_WARNING, error, ErrorResult.getTitle(requireActivity(), error, TYPE_WARNING)));
        viewModel.isLoading.postValue(false);
        viewModel.timeLogger.logTime(getString(R.string.step4));
        viewModel.totalTimeLogger.logTime(getString(R.string.total_time));
        if (viewModel.fileLogger != null) {
            viewModel.fileLogger.log(getString(R.string.total_time_taken), viewModel.totalTimeLogger.getDifferenceInTime() + " ms");
            viewModel.fileLogger.commitToFile();
        }
    }


}