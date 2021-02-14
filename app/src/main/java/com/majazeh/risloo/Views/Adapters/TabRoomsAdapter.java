package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.majazeh.risloo.Views.Fragments.AllRoomsFragment;
import com.majazeh.risloo.Views.Fragments.MyRoomsFragment;

public class TabRoomsAdapter extends FragmentPagerAdapter {

    // Fragments
    public Fragment allFragment, myFragment;

    // Vars
    private boolean token;

    // Object
    private Activity activity;

    public TabRoomsAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity, boolean token) {
        super(fragmentManager, behavior);
        this.activity = activity;
        this.token = token;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        if (token) {
            if (i == 0) {
                return myFragment = new MyRoomsFragment(activity);
            } else {
                return allFragment = new AllRoomsFragment(activity);
            }
        } else {
            return allFragment = new AllRoomsFragment(activity);
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