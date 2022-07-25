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

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.infineon.nfcbpsk.BR;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.services.appfiledecoder.InformationDataModel;
import com.infineon.nfcbpsk.databinding.ItemInfoContentBinding;
import com.infineon.nfcbpsk.databinding.ItemInfoTitleBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Provides the adapter for product and service information recycler view data adapter
 */
public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Constant for title view
     */
    public final static int VIEW_TYPE_TITLE = 1;
    /**
     * Constant for data view
     */
    public static final int VIEW_TYPE_DATA = 2;

    final ArrayList<InformationDataModel> informationDataModels;

    /**
     * Constructor
     *
     * @param informationDataModels Array list of the information madel
     */
    public InformationAdapter(ArrayList<InformationDataModel> informationDataModels) {
        this.informationDataModels = informationDataModels;
    }

    /**
     * Return the type of the view to be loaded
     *
     * @param position Index of data
     * @return Returns the view type
     */
    @Override
    public int getItemViewType(int position) {
        return informationDataModels.get(position).type;
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

            ItemInfoContentBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.item_info_content, parent, false);
            return new PropertyViewHolder(binding);
        }
    }

    /**
     * Method to bind the data view holder of current position
     * @param holder  RecyclerView.ViewHolder
     * @param position Index for which view is bound
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.bind(informationDataModels.get(position).title);
        } else {
            PropertyViewHolder propertyViewHolder = (PropertyViewHolder) holder;
            propertyViewHolder.bindKey(informationDataModels.get(position).title);
            propertyViewHolder.bindValue(informationDataModels.get(position).value);
            propertyViewHolder.binding.getRoot().setOnClickListener(view -> {
                if (informationDataModels.get(position).value.length() > 0) {
                    showDetailDialog(view.getContext(), informationDataModels.get(position).title, informationDataModels.get(position).value);
                }
            });
        }
    }

    /**
     * Returns the number of items currently available in adapter
     * @return Number of items currently available in adapter
     */
    @Override
    public int getItemCount() {
        return informationDataModels.size();
    }

    /**
     * Method to show the details dialog of the product and service information value
     * @param c Context to view
     * @param key Title of the product or service information
     * @param value Value of the product or service information
     */
    public void showDetailDialog(Context c, String key, String value) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
        LayoutInflater inflater = LayoutInflater.from(c);
        final View dialogView = inflater.inflate(R.layout.dialog_details, null);
        dialogBuilder.setView(dialogView);

        final ImageButton btnClose = dialogView.findViewById(R.id.btn_close);
        final TextView tvKey = dialogView.findViewById(R.id.tv_key);
        final TextView tvValue = dialogView.findViewById(R.id.value);
        tvKey.setText(key);
        tvValue.setText(value);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        btnClose.setOnClickListener(view -> alertDialog.dismiss());
    }

    /**
     * Property view holder class for adapter of product and service information page
     */
    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        final ItemInfoContentBinding binding;

        public PropertyViewHolder(ItemInfoContentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindKey(Object obj) {
            binding.setVariable(BR.key, obj);
            binding.executePendingBindings();
        }

        public void bindValue(Object obj) {
            binding.setVariable(BR.value, obj);
            binding.executePendingBindings();
        }
    }
}
