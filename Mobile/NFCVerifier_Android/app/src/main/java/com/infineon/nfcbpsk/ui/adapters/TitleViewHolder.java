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

import androidx.recyclerview.widget.RecyclerView;

import com.infineon.nfcbpsk.BR;
import com.infineon.nfcbpsk.databinding.ItemInfoTitleBinding;

/**
 * Title view holder class which is used in product, service and about pages
 */
public class TitleViewHolder extends RecyclerView.ViewHolder {

    final public ItemInfoTitleBinding binding;

    /**
     * Constructor
     *
     * @param binding Title binding instance
     */
    public TitleViewHolder(ItemInfoTitleBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /**
     * Method that bind the title with user interface
     *
     * @param obj Object to be bound
     */
    public void bind(Object obj) {
        binding.setVariable(BR.title, obj);
        binding.executePendingBindings();
    }
}
