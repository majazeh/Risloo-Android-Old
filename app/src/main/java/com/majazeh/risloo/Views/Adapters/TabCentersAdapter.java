package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.majazeh.risloo.Views.Fragments.AllCentersFragment;
import com.majazeh.risloo.Views.Fragments.MyCentersFragment;

public class TabCentersAdapter extends FragmentPagerAdapter {

    // Fragments
    public Fragment allFragment, myFragment;

    // Vars
    private boolean token;

    // Object
    private Activity activity;

    public TabCentersAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity, boolean token) {
        super(fragmentManager, behavior);
        this.activity = activity;
        this.token = token;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        if (token) {
            if (i == 0) {
                return myFragment = new MyCentersFragment(activity);
            } else {
                return allFragment = new AllCentersFragment(activity);
            }
        } else {
            return allFragment = new AllCentersFragment(activity);
        }
    }

    @Override
    public int getCount() {
        if (token) {
            return 2;
        } else {
            return 1;
        }
    }

}