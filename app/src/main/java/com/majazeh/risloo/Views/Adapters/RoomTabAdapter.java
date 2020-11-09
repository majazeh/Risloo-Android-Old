//package com.majazeh.risloo.Views.Adapters;
//
//import android.app.Activity;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//
//import com.majazeh.risloo.Views.Fragments.AllRoomFragment;
//import com.majazeh.risloo.Views.Fragments.MyRoomFragment;
//
//public class RoomTabAdapter extends FragmentPagerAdapter {
//
//    // Vars
//    private boolean token;
//
//    // Object
//    private Activity activity;
//    public Fragment allFragment, myFragment;
//
//    public RoomTabAdapter(@NonNull FragmentManager fragmentManager, int behavior, Activity activity, boolean token) {
//        super(fragmentManager, behavior);
//        this.activity = activity;
//        this.token = token;
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int i) {
//        if (token) {
//            if (i == 0) {
//                return myFragment = new MyRoomFragment(activity);
//            } else {
//                return allFragment = new AllRoomFragment(activity);
//            }
//        } else {
//            return allFragment = new AllRoomFragment(activity);
//        }
//    }
//
//    @Override
//    public int getCount() {
//        if (token) {
//            return 2;
//        } else {
//            return 1;
//        }
//    }
//
//}