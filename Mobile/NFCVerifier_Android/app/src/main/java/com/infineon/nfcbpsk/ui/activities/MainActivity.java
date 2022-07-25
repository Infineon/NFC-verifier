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

package com.infineon.nfcbpsk.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.infineon.nfcbpsk.BuildConfig;
import com.infineon.nfcbpsk.R;
import com.infineon.nfcbpsk.data.storage.PreferenceHelper;
import com.infineon.nfcbpsk.databinding.ActivityMainBinding;
import com.infineon.nfcbpsk.ui.fragments.AboutFragment;
import com.infineon.nfcbpsk.ui.fragments.AuthResultFragment;
import com.infineon.nfcbpsk.ui.fragments.HelpContentFragment;
import com.infineon.nfcbpsk.ui.fragments.HelpFragment;
import com.infineon.nfcbpsk.ui.fragments.HomeFragment;
import com.infineon.nfcbpsk.ui.fragments.LicenseAgreementFragment;
import com.infineon.nfcbpsk.ui.fragments.OptionsFragment;
import com.infineon.nfcbpsk.ui.fragments.RecentTransactionFragment;
import com.infineon.nfcbpsk.ui.views.MenuDrawerListener;

import java.util.List;

/**
 * Android activity class containing the user interface fragments
 */
public class MainActivity extends AppCompatActivity {
    public int selectedItemPosition = -1;
    public ActivityMainBinding binding;

    public TextView selectedMenuItem;
    private PreferenceHelper appPreference;

    /**
     * Initializes the activity with user interface configuration
     *
     * @param savedInstanceState Previous instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        appPreference = new PreferenceHelper(this);

        assignBackStackListener();

        HomeFragment homeFragment = new HomeFragment(true);
        replaceFragment(homeFragment);

        if(!checkLicenseAcceptance()) {
            displayLicenseAgreement();
        }

        setupNavigationDrawer();
        setupMenu();
    }

    /**
     * Checks if the EULA is accepted previously by the user
     *
     * @return True if the user has accepted already, else false
     */
    private boolean checkLicenseAcceptance() {
        return (appPreference.getLicenseAcceptPref() == BuildConfig.VERSION_CODE);
    }

    /**
     * Displays the EULA user interface fragment
     */
    private void displayLicenseAgreement(){
        LicenseAgreementFragment licenseAgreementFragment = LicenseAgreementFragment.newInstance(R.raw.end_user_license, true, getString(R.string.title_end_user_license_agreement));
        replaceFragment(licenseAgreementFragment, false);
    }

    /**
     * Method to replace the fragment with existing fragment
     *
     * @param homeFragment Fragment to be replaced
     */
    private void replaceFragment(Fragment homeFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(binding.fragmentContainerView.getId(), homeFragment)
                .commit();
    }

    /**
     * Method to replace the fragment with existing fragment. It also provides option whether to
     * add this fragment to backstack or not.
     *
     * @param fragment Fragment to be replace
     * @param isRemoveBackStack Flag to indicate is fragment need to add in backstack or not
     */
    private void replaceFragment(Fragment fragment, boolean isRemoveBackStack) {
        if (isRemoveBackStack) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(binding.fragmentContainerView.getId(), fragment);
        if (!isRemoveBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * Method to get the currently loaded fragment
     *
     * @param fragmentManager Fragment manager handle
     * @return Instance of current fragment. If fragment is empty, returns null
     */
    public Fragment getCurrentFragment(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.size() > 0) {
            return fragments.get(fragments.size() - 1);
        }
        return null;
    }

    /**
     * Setup the menu list And handles its click events
     */
    private void setupMenu() {
        String[] menus = getResources().getStringArray(R.array.menus);
        binding.menuList.setAdapter(new ArrayAdapter<>(this,
                R.layout.item_menu, menus));
        binding.menuList.setOnItemClickListener((parent, view, position, id) -> {
            HomeFragment.autoStartScan = false;
            setSelectedMenuColor((TextView) view);
            switch (position) {
                case 0:
                    HomeFragment homeFragment = new HomeFragment(false);
                    replaceFragment(homeFragment, true);
                    break;
                case 1:
                    replaceFragment(RecentTransactionFragment.newInstance(), false);
                    break;
                case 2:
                    replaceFragment(OptionsFragment.newInstance(), false);
                    break;
                case 3:
                    replaceFragment(HelpFragment.newInstance(), false);
                    break;
                case 4:
                    replaceFragment(new AboutFragment(), false);
                    break;
            }
            setupToolbar(getSupportFragmentManager());
        });
    }

    /**
     * Highlights the selected menu
     *
     * @param view View of the selected menu item
     */
    public void setSelectedMenuColor(TextView view) {
        // If previously selected any menu item, reset it color as unselected
        if (selectedMenuItem != null) {
            selectedMenuItem.setTextColor(getResources().getColor(R.color.engineering));
        }
        selectedMenuItem = view;
        if (view == null) {
            return;
        }
        view.setTextColor(getResources().getColor(R.color.ocean));
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        new Handler().postDelayed(() -> selectedMenuItem.setTextColor(getResources().getColor(R.color.engineering)), 0);
    }

    /**
     * Configures the navigation drawer
     */
    private void setupNavigationDrawer() {
        setSupportActionBar(binding.toolbar);
        MenuDrawerListener listener = new MenuDrawerListener(this,
                binding.ivNavigationIcon, binding.drawerLayout, binding.title);
        binding.drawerLayout.addDrawerListener(listener);
        listener.sync();

        // Assign back button listener
        binding.appLogo.setOnClickListener(v -> {
            if (binding.ivNavigationIcon.getVisibility() == View.GONE) {
                onBackPressed();
            }
        });
        binding.ivNavigationBackIcon.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Callback method to indicate that back button is pressed
     */
    @Override
    public void onBackPressed() {
        // If EULA is displayed, close only the EULA screen
        if (appPreference.getLicenseAcceptPref() != BuildConfig.VERSION_CODE) {
            finish();
            return;
        }

        // If menu is displayed, close only the menu
        int menuDrawer = binding.drawerLayout.getDrawerLockMode(GravityCompat.END);
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.END)
                && (menuDrawer != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        // Pass the button press event to the base class
        super.onBackPressed();
    }

    /**
     * Method to assign the fragment back stack listener to this activity
     */
    private void assignBackStackListener() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(() ->
                MainActivity.this.setupToolbar(fragmentManager));
    }

    /**
     * Method to set the title of the toolbar based on the fragment loaded
     *
     * @param fragmentManager  Fragment manager handle
     */
    private void setupToolbar(FragmentManager fragmentManager) {
        Fragment currentFragment = getCurrentFragment(fragmentManager);
        if (currentFragment != null) {
            if (currentFragment instanceof RecentTransactionFragment) {
                configToolbarBackBtn(getString(R.string.recentTlog));
            }
            if (currentFragment instanceof HelpContentFragment) {
                configToolbarBackBtn(((HelpContentFragment) currentFragment).title);
            }
            if (currentFragment instanceof HomeFragment || currentFragment instanceof AuthResultFragment) {
                binding.title.setText(R.string.app_name);
                binding.appLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_app_logo_4c));
                binding.ivNavigationIcon.setVisibility(View.VISIBLE);
                binding.ivNavigationBackIcon.setVisibility(View.GONE);
            }
            if (currentFragment instanceof HelpFragment) {
                configToolbarCloseBtn(getString(R.string.help));
            }
            if (currentFragment instanceof AboutFragment) {
                configToolbarCloseBtn(getString(R.string.about));
            }
            if (currentFragment instanceof OptionsFragment) {
                configToolbarCloseBtn(getString(R.string.options));
            }
            if (currentFragment instanceof LicenseAgreementFragment) {
                LicenseAgreementFragment licenseAgreementFragment = (LicenseAgreementFragment) currentFragment;
                configToolbarCloseBtn(licenseAgreementFragment.title);
            }
        }
    }

    /**
     * Method to configure the toolbar with a close button
     *
     * @param title Title of toolbar
     */
    private void configToolbarCloseBtn(String title) {
        binding.title.setText(title);
        binding.appLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_app_logo_4c));
        binding.ivNavigationIcon.setVisibility(View.GONE);
        binding.ivNavigationBackIcon.setVisibility(View.VISIBLE);
    }

    /**
     * Method to configure the toolbar with a back button
     *
     * @param title Title of toolbar
     */
    private void configToolbarBackBtn(String title) {
        binding.title.setText(title);
        binding.appLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        binding.ivNavigationIcon.setVisibility(View.GONE);
        binding.ivNavigationBackIcon.setVisibility(View.GONE);
    }

    /**
     * Handle the received intent
     *
     * @param intent Intent received from the platform
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

}