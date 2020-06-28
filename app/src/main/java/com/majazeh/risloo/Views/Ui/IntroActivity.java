package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Adapters.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    // Vars
    private int[] layouts, activeColors, inActiveColors;

    // Adapters
    private IntroAdapter introAdapter;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private RtlViewPager rtlViewPager;
    private TextView nextTextView, skipTextView;
    private TextView[] dotsTextView;
    private LinearLayout dotsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (firstTimeLaunch()) {
            launchActivity();
        } else {
            decorator();

            setContentView(R.layout.activity_intro);

            initializer();

            listener();

            addIntroDots(0);
        }
    }

    @SuppressLint("InlinedApi")
    private void decorator() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.Snow));
        }
    }

    private void initializer() {
        layouts = new int[]{R.layout.activity_intro_1, R.layout.activity_intro_2, R.layout.activity_intro_3, R.layout.activity_intro_4};

        activeColors = getResources().getIntArray(R.array.activeColors);
        inActiveColors = getResources().getIntArray(R.array.inActiveColors);

        introAdapter = new IntroAdapter(this, layouts);

        rtlViewPager = findViewById(R.id.activity_intro_rtlViewPager);
        rtlViewPager.setAdapter(introAdapter);

        nextTextView = findViewById(R.id.activity_intro_next_textView);
        skipTextView = findViewById(R.id.activity_intro_skip_textView);

        dotsTextView = new TextView[layouts.length];

        dotsLinearLayout = findViewById(R.id.activity_intro_dots_linearLayout);
    }

    private void listener() {
        nextTextView.setOnClickListener(v -> {
            int currentPage = rtlViewPager.getCurrentItem() + 1;

            if (currentPage < layouts.length) {
                rtlViewPager.setCurrentItem(currentPage);
            } else {
                launchActivity();
            }
        });

        skipTextView.setOnClickListener(v -> {
            launchActivity();
        });

        rtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addIntroDots(position);

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

    private boolean firstTimeLaunch() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        return !sharedPreferences.getBoolean("firstTimeLaunch", true);
    }

    private void launchActivity() {
        editor.putBoolean("firstTimeLaunch", false);
        editor.apply();

        Intent intent = new Intent(IntroActivity.this, SampleActivity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void addIntroDots(int currentPage) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}