package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.majazeh.risloo.Views.Fragments.EditAvatarFragment;
import com.majazeh.risloo.Views.Fragments.EditCryptoFragment;
import com.majazeh.risloo.Views.Fragments.EditPasswordFragment;
import com.majazeh.risloo.Views.Fragments.EditPersonalFragment;

public class TabAccountAdapter extends FragmentPagerAdapter {

    // Object
    private Activity activity;
    public Fragment editPersonalFragment, editPasswordFragment, editAvatarFragment, editCryptoFragment;

    public TabAccountAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity) {
        super(fragmentManager, behavior);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return editPersonalFragment = new EditPersonalFragment(activity);
        } else if (i == 1){
            return editPasswordFragment = new EditPasswordFragment(activity);
        } else if (i == 2){
            return editAvatarFragment = new EditAvatarFragment(activity);
        } else {
            return editCryptoFragment = new EditCryptoFragment(activity);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}