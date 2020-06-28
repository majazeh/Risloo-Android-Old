package com.example.moallem.Intro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroActivityAdapter extends PagerAdapter {
    private Activity activity;
    private View view;
    private int[] layouts;

    IntroActivityAdapter(Activity activity, int[] layouts) {
        this.activity = activity;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {

        // inflate Layout
        view = LayoutInflater.from(activity).inflate(layouts[i], viewGroup, false);

        viewGroup.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, @NonNull Object object) {
        viewGroup.removeView((View) object);
    }

}