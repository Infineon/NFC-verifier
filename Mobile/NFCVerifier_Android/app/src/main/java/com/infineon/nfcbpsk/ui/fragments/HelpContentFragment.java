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

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.databinding.FragmentHelpContentBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment view to represent the help page
 */
public class HelpContentFragment extends Fragment {

    private static final String ARG_PARAM_LINK = "PARAM_LINK";
    private static final String ARG_PARAM_TITLE = "PARAM_TITLE";
    public String title;
    private String link;

    /**
     * Constructor
     */
    public HelpContentFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param link Parameter link to be open .
     * @return A new instance of fragment HelpContentFragment.
     */
    public static HelpContentFragment newInstance(String link, String name) {
        HelpContentFragment fragment = new HelpContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_LINK, link);
        args.putString(ARG_PARAM_TITLE, name);
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
            link = getArguments().getString(ARG_PARAM_LINK);
            title = getArguments().getString(ARG_PARAM_TITLE);
        }
    }

    /**
     * Method called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the view for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHelpContentBinding fragmentWebViewBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_help_content, container, false);
        fragmentWebViewBinding.webView.loadUrl(link);
       fragmentWebViewBinding.webView.setInitialScale(200);

        return fragmentWebViewBinding.getRoot();
    }
}