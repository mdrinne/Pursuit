package com.example.pursuit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class RegistrationPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> registrationFragmentList = new ArrayList<>();
    private final List<String> registrationFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title) {
        registrationFragmentList.add(fragment);
        registrationFragmentTitleList.add(title);
    }

    public RegistrationPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return registrationFragmentTitleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return registrationFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return registrationFragmentList.size();
    }
}
