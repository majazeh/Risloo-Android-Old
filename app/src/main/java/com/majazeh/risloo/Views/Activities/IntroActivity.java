package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Views.Adapters.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    // Adapters
    private IntroAdapter introAdapter;

    // Vars
    private int[] introLayouts, activeColors, inActiveColors;
    private TextView[] dotsTextView;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private RtlViewPager rtlViewPager;
    private TextView nextTextView, skipTextView;
    private LinearLayout dotsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (firstTimeLaunch()) {
            decorator();

            setContentView(R.layout.activity_intro);

            initializer();

            listener();

            setViewPager();
        } else {
            launchAuth();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightNavShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, Color.TRANSPARENT, getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        introLayouts = new int[]{R.layout.single_item_intro_1, R.layout.single_item_intro_2, R.layout.single_item_intro_3, R.layout.single_item_intro_4};

        activeColors = getResources().getIntArray(R.array.activeColors);
        inActiveColors = getResources().getIntArray(R.array.inActiveColors);

        dotsTextView = new TextView[introLayouts.length];

        introAdapter = new IntroAdapter(this);

        handler = new Handler();

        rtlViewPager = findViewById(R.id.activity_intro_rtlViewPager);

        nextTextView = findViewById(R.id.activity_intro_next_textView);
        skipTextView = findViewById(R.id.activity_intro_skip_textView);

        dotsLinearLayout = findViewById(R.id.activity_intro_dots_linearLayout);
    }

    private void listener() {
        nextTextView.setOnClickListener(v -> {
            nextTextView.setClickable(false);
            handler.postDelayed(() -> nextTextView.setClickable(true), 100);

            nextPage();
        });

        skipTextView.setOnClickListener(v -> {
            skipTextView.setClickable(false);
            handler.postDelayed(() -> skipTextView.setClickable(true), 250);

            launchAuth();
        });

        rtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addDots(position);

                if (position == introLayouts.length - 1) {
                    nextTextView.setText(getString(R.string.IntroStart));
                    skipTextView.setVisibility(View.GONE);
                } else {
                    nextTextView.setText(getString(R.string.IntroNext));
                    skipTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void setViewPager() {
        introAdapter.setLayouts(introLayouts);
        rtlViewPager.setAdapter(introAdapter);

        addDots(0);
    }

    private void addDots(int currentPage) {
        dotsLinearLayout.removeAllViews();

        for (int i = 0; i < dotsTextView.length; i++) {
            dotsTextView[i] = new TextView(this);
            dotsTextView[i].setText(Html.fromHtml("&#8226;"));
            dotsTextView[i].setTextSize(getResources().getDimension(R.dimen._13ssp));
            dotsTextView[i].setTextColor(inActiveColors[currentPage]);
            dotsLinearLayout.addView(dotsTextView[i]);
        }

        if (dotsTextView.length > 0) {
            dotsTextView[currentPage].setTextColor(activeColors[currentPage]);
        }
    }

    private void nextPage() {
        int currentPage = rtlViewPager.getCurrentItem() + 1;

        if (currentPage < introLayouts.length) {
            rtlViewPager.setCurrentItem(currentPage);
        } else {
            launchAuth();
        }
    }

    private void launchAuth() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();

        editor.putBoolean("firstLaunch", false);
        editor.apply();
    }

    private boolean firstTimeLaunch() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        return sharedPreferences.getBoolean("firstLaunch", true);
    }

}