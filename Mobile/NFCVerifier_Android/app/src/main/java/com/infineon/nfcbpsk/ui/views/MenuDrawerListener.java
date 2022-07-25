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
package com.infineon.nfcbpsk.ui.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.ui.activities.MainActivity;
import com.infineon.nfcbpsk.ui.fragments.AuthResultFragment;
import com.infineon.nfcbpsk.ui.fragments.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Navigation drawer listener two handle the the navigation events
 */
public class MenuDrawerListener implements DrawerLayout.DrawerListener {
    /**
     * Instance of navigation icon
     */
    final ImageView navigationIcon;
    /**
     * Instance of toolbar title
     */
    final TextView title;
    /**
     * Instance of navigation drawer
     */
    final FullDrawerLayout drawerLayout;
    /**
     * Instance to hold past toolbar title
     */
    private String lastTitleName;

    final MainActivity mainActivity;

    /**
     * Constructor
     * @param mainActivity Instance of mainActivity
     * @param navigationIcon Instance of navigation icon
     * @param drawerLayout Instance of full drawer layout
     * @param title Instance of title of toolbar
     */
    public MenuDrawerListener(MainActivity mainActivity, ImageView navigationIcon, FullDrawerLayout drawerLayout, TextView title) {
        this.mainActivity = mainActivity;
        this.navigationIcon = navigationIcon;
        this.drawerLayout = drawerLayout;
        this.title = title;
    }

    /**
     * Method get called when a drawer's position changes.
     * @param drawerView The child view that was moved
     * @param slideOffset The new offset of this drawer within its range, from 0-1
     */
    @Override
    public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {
        //no implementation needed
    }

    /**
     * Called when a drawer has settled in a completely open state. The drawer is interactive at this point
     * @param drawerView Drawer view that is now open
     */
    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        lastTitleName = title.getText().toString();
        title.setText(drawerView.getResources().getString(R.string.menu));
        navigationIcon.setImageDrawable(Objects.requireNonNull(drawerView).getResources().getDrawable(R.drawable.ic_baseline_close_24));
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        Fragment currentFragment = mainActivity.getCurrentFragment(fragmentManager);
        if(currentFragment instanceof AuthResultFragment){
            mainActivity.setSelectedMenuColor(null);
        }
        if (currentFragment instanceof HomeFragment) {
            TextView textView = ((TextView) mainActivity.binding.menuList.getChildAt(0));
            if ( mainActivity.selectedMenuItem != null) {
                mainActivity.selectedMenuItem.setTextColor( mainActivity.getResources().getColor(R.color.engineering));
            }
            mainActivity.selectedMenuItem = textView;
            textView.setTextColor(mainActivity.getResources().getColor(R.color.ocean));

        }
    }

    /**
     * Called when a drawer has settled in a completely closed state.
     * @param drawerView Drawer view that is now closed
     */
    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        navigationIcon.setImageDrawable(drawerView.getResources().getDrawable(R.drawable.ic_baseline_menu_24));
    }

    /**
     * Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
     * @param newState The new drawer motion state
     */
    @Override
    public void onDrawerStateChanged(int newState) {
        //no implementation needed
    }

    /**
     * Handles the synchronization of navigation icon and drawer
     */
    public void sync() {
        if (navigationIcon != null) {
            navigationIcon.setOnClickListener(v -> toggleMenu());
        }
    }

    /**
     * Handles the toggle menu
     */
    public void toggleMenu() {

        int drawerLockMode = drawerLayout.getDrawerLockMode(GravityCompat.END);
        if (drawerLayout.isDrawerVisible(GravityCompat.END)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            drawerLayout.closeDrawer(GravityCompat.END);

        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        if (lastTitleName != null) {
            title.setText(lastTitleName);
        }
        lastTitleName = title.getText().toString();
    }
}
