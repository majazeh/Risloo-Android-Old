package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroAdapter extends PagerAdapter {

    // Vars
    private int[] layouts;

    // Object
    private Activity activity;

    public IntroAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(layouts[i], viewGroup, false);

        viewGroup.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object object) {
        viewGroup.removeView((View) object);
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    public void setLayouts(int[] layouts) {
        this.layouts = layouts;
        notifyDataSetChanged();
    }

}