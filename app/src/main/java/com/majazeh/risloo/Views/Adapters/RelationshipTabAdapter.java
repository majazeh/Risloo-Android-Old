package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.majazeh.risloo.Views.Fragments.AllRelationshipFragment;
import com.majazeh.risloo.Views.Fragments.MyRelationshipFragment;

public class RelationshipTabAdapter extends FragmentPagerAdapter {

    // Vars
    private boolean token;

    // Object
    private Activity activity;

    public RelationshipTabAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity, boolean token) {
        super(fragmentManager, behavior);
        this.activity = activity;
        this.token = token;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new AllRelationshipFragment(activity);
        } else {
            return new MyRelationshipFragment(activity);
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