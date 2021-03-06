package com.example.android.jitsbankingtime.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.jitsbankingtime.ui.screens.IngredientsListFragment;
import com.example.android.jitsbankingtime.ui.screens.StepsListFragment;

import timber.log.Timber;

import static com.example.android.jitsbankingtime.utils.ConstantsDefined.INGREDIENTS;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.PAGE_COUNT;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.STEPS;
import static com.example.android.jitsbankingtime.utils.ConstantsDefined.TAB_NAMES;

public class DetailPagerAdapter extends FragmentPagerAdapter {
    int numTabs;
    public DetailPagerAdapter(@NonNull FragmentManager fm, int behavior, int numOfTabs) {
        super(fm, behavior);
        numTabs = numOfTabs;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case INGREDIENTS:
                return new IngredientsListFragment();

            case STEPS:
                return new StepsListFragment();
        }
        //Error case
        Timber.e("Undefined case - getItem() - position is: %d", position);
        return null;
    }

    @Override
    public int getCount() {
        Timber.d("page count is: %d", PAGE_COUNT);
        //return PAGE_COUNT;
        return numTabs;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_NAMES[position];
    }
}
