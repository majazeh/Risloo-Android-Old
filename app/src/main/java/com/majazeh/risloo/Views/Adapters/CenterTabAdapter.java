package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.majazeh.risloo.Views.Fragments.AllCenterFragment;
import com.majazeh.risloo.Views.Fragments.MyCenterFragment;

public class CenterTabAdapter extends FragmentPagerAdapter {

    // Vars
    private boolean token;

    // Object
    private Activity activity;
    public Fragment allFragment, myFragment;

    public CenterTabAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity, boolean token) {
        super(fragmentManager, behavior);
        this.activity = activity;
        this.token = token;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        if (token) {
            if (i == 0) {
                myFragment = new MyCenterFragment(activity);
                return myFragment;
            } else {
                allFragment = new AllCenterFragment(activity);
                return allFragment;
            }
        } else {
            allFragment = new AllCenterFragment(activity);
            return allFragment;
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