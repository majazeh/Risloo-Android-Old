package com.example.moallem.Intro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.example.moallem.Knowledge.KnowledgeActivity;
import com.example.moallem.Main.LoginAndRegister.LoginActivity;
import com.example.moallem.Main.Main.MainActivity;
import com.example.moallem.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class IntroActivity extends AppCompatActivity {

    public static Activity activity;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    Handler handler;

    Bundle extras;

    Typeface typeLight, typeRegular, typeBold;

    TextView skipTextView, swipeTextView;

    TextView[] dotsTextView;

    Button introSlider3NextButton, introSlider3AccountButton;

    RtlViewPager rtlViewPager;

    LinearLayout dotsLinearLayout;

    IntroActivityAdapter introActivityAdapter;

    int[] layouts, colorsActive, colorsInactive;

    // setting OnCreate
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // defining SharedPreferences
        sharedPreferences = getSharedPreferences("user_shared_preference", Context.MODE_PRIVATE);

        // defining SharedPreference Editor
        editor = sharedPreferences.edit();
        editor.apply();

        if (!sharedPreferences.getBoolean("isFirstTimeLaunch", true)) {
            launchMainActivity();
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR );

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.AliceBlue));
        }

        setContentView(R.layout.activity_intro);

        definingIds();

        addBottomDots(0);

        settingViewPager();

        onClick();

        activity = this;
    }

    // defining Ids
    private void definingIds() {

        // defining Handler
        handler = new Handler();

        // defining Extras
        extras = getIntent().getExtras();

        // defining Font
        typeLight = Typeface.createFromAsset(this.getAssets(), "iransansmobile_light.ttf");
        typeRegular = Typeface.createFromAsset(this.getAssets(), "iransansmobile_regular.ttf");
        typeBold = Typeface.createFromAsset(this.getAssets(), "iransansmobile_bold.ttf");

        // defining TextView
        skipTextView = findViewById(R.id.activity_intro_skip_textView);
        skipTextView.setTypeface(typeBold);
        swipeTextView = findViewById(R.id.activity_intro_swipe_textView);
        swipeTextView.setTypeface(typeBold);

        // defining RtlViewPager
        rtlViewPager = findViewById(R.id.activity_intro_rtlViewPager);

        // defining LinearLayout
        dotsLinearLayout = findViewById(R.id.activity_intro_dots_linearLayout);

        // defining IntroSliders Layouts
        layouts = new int[]{R.layout.activity_intro_slider0, R.layout.activity_intro_slider1, R.layout.activity_intro_slider2, R.layout.activity_intro_slider3};

        // defining IntroActivityAdapter
        introActivityAdapter = new IntroActivityAdapter(this, layouts);
    }

    // launching MainActivity
    private void launchMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // launching KnowledgeActivity
    private void launchKnowledgeActivity() {
        Intent intent = new Intent(IntroActivity.this, KnowledgeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // launching LoginActivity
    private void launchLoginActivity() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // adding Bottom Dots
    private void addBottomDots(int currentPage) {

        // defining TextView
        dotsTextView = new TextView[layouts.length];

        // removing Views
        dotsLinearLayout.removeAllViews();

        // defining Int's
        colorsActive = getResources().getIntArray(R.array.activeDotsArray);
        colorsInactive = getResources().getIntArray(R.array.inActiveDotsArray);

        for (int i = 0; i < dotsTextView.length; i++) {
            dotsTextView[i] = new TextView(this);
            dotsTextView[i].setText(Html.fromHtml("&#8226;"));
            dotsTextView[i].setTextSize(getResources().getDimension(R.dimen._11ssp));
            dotsTextView[i].setTextColor(colorsInactive[currentPage]);
            dotsLinearLayout.addView(dotsTextView[i]);
        }

        if (dotsTextView.length > 0) {
            dotsTextView[currentPage].setTextColor(colorsActive[currentPage]);
        }
    }

    // setting ViewPager
    private void settingViewPager() {
        rtlViewPager.setAdapter(introActivityAdapter);
        rtlViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    // defining OnClick
    private void onClick() {
        skipTextView.setOnClickListener(v -> rtlViewPager.setCurrentItem(layouts.length - 1));
    }

    // setting OnViewPagerChangeListener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == 0) {
                skipTextView.setVisibility(View.VISIBLE);
                swipeTextView.setVisibility(View.VISIBLE);
            } else if (position != layouts.length - 1) {
                skipTextView.setVisibility(View.VISIBLE);
                swipeTextView.setVisibility(View.GONE);
            } else {
                skipTextView.setVisibility(View.GONE);
                swipeTextView.setVisibility(View.GONE);

                introSlider3NextButton = findViewById(R.id.activity_intro_slider3_next_button);
                introSlider3NextButton.setTransformationMethod(null);
                introSlider3NextButton.setTypeface(typeBold);
                introSlider3AccountButton = findViewById(R.id.activity_intro_slider3_account_button);
                introSlider3AccountButton.setTransformationMethod(null);
                introSlider3AccountButton.setTypeface(typeBold);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    introSlider3NextButton.setBackgroundResource(R.drawable.drawable_rectangle_button_primary_form_ripple);
                    introSlider3AccountButton.setBackgroundResource(R.drawable.drawable_rectangle_button_white_border_quartz_form_ripple);
                }

                introSlider3NextButton.setOnClickListener(v -> launchKnowledgeActivity());

                introSlider3AccountButton.setOnClickListener(v -> launchLoginActivity());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    // setting OnBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // setting Font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}