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
import com.infineon.nfcbpsk.databinding.FragmentTransactionLogBinding;
import com.infineon.nfcbpsk.data.logger.LoggerFactory;
import com.infineon.nfcbpsk.data.logger.ConsoleLogger;
import com.infineon.nfcbpsk.ui.viewmodels.RecentTransactionViewModel;
import com.infineon.nfcbpsk.data.logger.LoggerType;

/**
 * Fragment view to represent the recent transaction log page
 */
public class RecentTransactionFragment extends Fragment {

    private FragmentTransactionLogBinding binding;
    private RecentTransactionViewModel viewModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RecentTransactionFragment.
     */
    public static RecentTransactionFragment newInstance() {
        return new RecentTransactionFragment();
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
                inflater, R.layout.fragment_transaction_log, container, false);
        viewModel = new ViewModelProvider(this).get(RecentTransactionViewModel.class);
        binding.setRecentTransactionViewModel(viewModel);
        displayTransactionLog();
        setupClickEvent();
        return binding.getRoot();
    }

    /**
     * Method setup the click events on share button
     */
    public void setupClickEvent() {
        binding.btnShare.setOnClickListener(v -> shareTransactionLog());
    }

    /**
     * Method to reads the transaction log from file and display on UI
     */
    private void displayTransactionLog() {
        try {
             String[] log = new String[0];
             if (viewModel.fileLogger != null) {
                 log = viewModel.fileLogger.readData().split("#");
             }
             binding.title.setText(String.format("%s %s", getResources().getString(R.string.transaction_at), log[0]));
             binding.text.setText(log[1]);
             binding.error.setVisibility(View.GONE);
         } catch (Exception e) {
             binding.btnShare.setVisibility(View.GONE);
             binding.error.setVisibility(View.VISIBLE);
             binding.title.setVisibility(View.GONE);
         }
    }

    /**
     * Method to share the transaction log using Android SEND intent
     */
    private void shareTransactionLog() {
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String body = "Share Transaction Log\n"+binding.text.getText().toString();
        String sub =  binding.title.getText().toString();
        myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
        myIntent.putExtra(Intent.EXTRA_TEXT,body);
        // Logs the transaction log in  console
        ConsoleLogger consoleLogger = (ConsoleLogger) LoggerFactory.getLogger(getContext(), LoggerType.CONSOLE);
        if (consoleLogger != null) {
            consoleLogger.log("Log",body);
        }
        startActivity(Intent.createChooser(myIntent, getString(R.string.share_log)));
    }
}