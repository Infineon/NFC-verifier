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
package com.infineon.nfcbpsk.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.infineon.nfcbpsk.BR;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.databinding.ItemAboutLinkBinding;
import com.infineon.nfcbpsk.databinding.ItemInfoTitleBinding;
import com.infineon.nfcbpsk.ui.activities.MainActivity;
import com.infineon.nfcbpsk.ui.fragments.HelpContentFragment;
import com.infineon.nfcbpsk.ui.fragments.LicenseAgreementFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Provides the adapter for about page recycler view data adapter
 */
public class AboutLinksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Constant for title view
     */
    public final static int VIEW_TYPE_TITLE = 1;
    /**
     * Constant for data view
     */
    public static int VIEW_TYPE_DATA = 2;
    /**
     * Constant to view link in browser
     */
    public final static String VIEW_IN_BROWSER = "BROWSER";
    /**
     * Constant to view link in web view
     */
    public static final String VIEW_IN_WEB_VIEW = "WEBVIEW";
    /**
     * Constant to view link as file view
     */    
    public static final String VIEW_IN_FILE_VIEW = "FILEVIEW";

    final ArrayList<LinkModel> linkModels;
    final AppCompatActivity activity;

    /**
     * Constructor
     * @param activity Context of the current activity
     * @param linkModels ArrayList of links
     */
    public AboutLinksAdapter(Activity activity, ArrayList<LinkModel> linkModels) {
        this.linkModels = linkModels;
        this.activity = (AppCompatActivity) activity;
        ((MainActivity) activity).selectedItemPosition = -1;
    }

    /**
     * Return the type of the view to be loaded
     *
     * @param position Position of view
     * @return Type of the view to be loaded
     */
    @Override
    public int getItemViewType(int position) {
        return linkModels.get(position).type;
    }

    /**
     * Creates view holder for android recycler view
     *
     * @param parent   View group
     * @param viewType View type
     * @return View holder for current data
     */
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TITLE) {
            ItemInfoTitleBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_info_title, parent, false);
            return new TitleViewHolder(binding);
        } else {
                ItemAboutLinkBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_about_link, parent, false);
            return new LinkViewHolder(binding);
        }
    }

    /**
     * Method to bind the data view holder of current position
     * @param holder  RecyclerView.ViewHolder
     * @param position Index of the view to be bound
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.bind(linkModels.get(position).name);
        } else {
            LinkViewHolder linkViewHolder = (LinkViewHolder) holder;
            linkViewHolder.bind(linkModels.get(position));

            linkViewHolder.binding.getRoot().setOnClickListener(v -> {

               if (linkModels.get(position).link != null && linkModels.get(position).link.trim().length() > 0) {

                   int previousSelectedMenus = ((MainActivity) activity).selectedItemPosition;
                   notifyItemChanged(previousSelectedMenus);
                   ((MainActivity) activity).selectedItemPosition = holder.getAdapterPosition();
                    linkViewHolder.binding.tvTitle.setTextColor(activity.getResources().getColor(R.color.ocean));
                    if (linkModels.get(position).open.equalsIgnoreCase(VIEW_IN_BROWSER)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkModels.get(position).link));
                        activity.startActivity(browserIntent);
                    } else  if (linkModels.get(position).open.equalsIgnoreCase(VIEW_IN_WEB_VIEW)) {
                        HelpContentFragment webViewFragment = HelpContentFragment.newInstance(linkModels.get(position).link, linkModels.get(position).name);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container_view, webViewFragment).addToBackStack(null)
                                .commit();
                    } else  if (linkModels.get(position).open.equalsIgnoreCase(VIEW_IN_FILE_VIEW))  {
                        LicenseAgreementFragment licenseAgreementFragment = LicenseAgreementFragment.newInstance(linkModels.get(position).link.equalsIgnoreCase("third_party_license.txt")?R.raw.third_party_license:R.raw.end_user_license,false,linkModels.get(position).name);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, licenseAgreementFragment).addToBackStack(null).commit();
                    }
                }
            });
            if (linkModels.get(position).open.equalsIgnoreCase(VIEW_IN_BROWSER)) {
                linkViewHolder.binding.viewArrow.setVisibility(View.GONE);
            } else if(linkModels.get(position).open.equalsIgnoreCase(VIEW_IN_FILE_VIEW)) {
                linkViewHolder.binding.viewArrow.setVisibility(View.GONE);
                linkViewHolder.binding.tvTitle.setCompoundDrawables(null, null, null, null);
            } else {
                linkViewHolder.binding.viewArrow.setVisibility(View.GONE);
            }
            if (((MainActivity) activity).selectedItemPosition == holder.getAdapterPosition()) {
                linkViewHolder.binding.tvTitle.setTextColor(activity.getResources().getColor(R.color.ocean));
            } else {
                linkViewHolder.binding.tvTitle.setTextColor(activity.getResources().getColor(R.color.engineering));
            }
        }
    }

    /**
     * Returns the number of items currently available in adapter
     *
     * @return Number of items currently available in adapter
     */
    @Override
    public int getItemCount() {
        return linkModels.size();
    }

    /**
     * Link view holder class for adapter of about page links
     */
    static class LinkViewHolder extends RecyclerView.ViewHolder {
        public final ItemAboutLinkBinding binding;

        public LinkViewHolder(ItemAboutLinkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LinkModel obj) {
            binding.setVariable(BR.title, obj.name);
            binding.executePendingBindings();
        }
    }
}