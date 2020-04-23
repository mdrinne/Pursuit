package com.example.pursuit.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.pursuit.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.discover_users_tab, R.string.discover_companies_tab, R.string.discover_opportunities_tab};
    private final Context mContext;
    private int numTabs;
    private String currentUserId;
    private String currentUserRole;

    public SectionsPagerAdapter(Context context, FragmentManager fm, int numTabs, String currentUserId, String currentUserRole) {
        super(fm);
        this.mContext = context;
        this.numTabs = numTabs;
        this.currentUserId = currentUserId;
        this.currentUserRole = currentUserRole;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch(position) {
            case 0:
                return DiscoverUsersFragment.newInstance(currentUserId, currentUserRole);
            case 1:
                return DiscoverCompaniesFragment.newInstance(currentUserId, currentUserRole);
            case 2:
                return DiscoverOpportunitiesFragment.newInstance(currentUserId);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}