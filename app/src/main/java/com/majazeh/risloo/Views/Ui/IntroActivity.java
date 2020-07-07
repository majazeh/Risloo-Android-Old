package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.Views.Adapters.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    // Vars
    private int[] layouts, activeColors, inActiveColors;
    private TextView[] dotsTextView;

    // Adapters
    private IntroAdapter adapter;

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
            launchAuth();
        } else {
            decorator();

            setContentView(R.layout.activity_intro);

            initializer();

            listener();

            addDots(0);
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        layouts = new int[]{R.layout.activity_intro_single_item_1, R.layout.activity_intro_single_item_2, R.layout.activity_intro_single_item_3, R.layout.activity_intro_single_item_4};

        activeColors = getResources().getIntArray(R.array.activeColors);
        inActiveColors = getResources().getIntArray(R.array.inActiveColors);

        dotsTextView = new TextView[layouts.length];

        adapter = new IntroAdapter(this);
        adapter.setLayout(layouts);

        handler = new Handler();

        rtlViewPager = findViewById(R.id.activity_intro_rtlViewPager);
        rtlViewPager.setAdapter(adapter);

        nextTextView = findViewById(R.id.activity_intro_next_textView);
        skipTextView = findViewById(R.id.activity_intro_skip_textView);

        dotsLinearLayout = findViewById(R.id.activity_intro_dots_linearLayout);
    }

    private void listener() {
        nextTextView.setOnClickListener(v -> {
            nextTextView.setClickable(false);
            handler.postDelayed(() -> nextTextView.setClickable(true), 500);

            int currentPage = rtlViewPager.getCurrentItem() + 1;

            if (currentPage < layouts.length) {
                rtlViewPager.setCurrentItem(currentPage);
            } else {
                launchAuth();
            }
        });

        skipTextView.setOnClickListener(v -> {
            skipTextView.setClickable(false);
            handler.postDelayed(() -> skipTextView.setClickable(true), 1000);

            launchAuth();
        });

        rtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addDots(position);

                if (position == layouts.length - 1) {
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

    private boolean firstTimeLaunch() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        return !sharedPreferences.getBoolean("firstTimeLaunch", true);
    }

    private void launchAuth() {
        editor.putBoolean("firstTimeLaunch", false);
        editor.apply();

        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

}