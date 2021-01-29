package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.TabCentersAdapter;
import com.majazeh.risloo.Views.Fragments.AllCentersFragment;
import com.majazeh.risloo.Views.Fragments.MyCentersFragment;

import org.json.JSONException;

import java.util.Objects;

public class CentersActivity extends AppCompatActivity {

    // ViewModels
    public AuthViewModel authViewModel;
    public CenterViewModel centerViewModel;

    // Adapters
    public TabCentersAdapter tabCentersAdapter;

    // Vars
    public String search = "";
    public boolean finished = false, loadingAll = false, loadingMy = false;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private ClickableSpan retrySpan;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarCreateImageView, toolbarSearchImageView;
    private TextView toolbarTextView;
    private LinearLayout searchLayout;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView searchImageView, infoImageView;
    private TextView searchTextView, infoTextView;
    private TabLayout tabLayout;
    private RtlViewPager rtlViewPager;
    private Dialog searchDialog;
    private TextView searchDialogTitle, searchDialogPositive, searchDialogNegative;
    private EditText searchDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_centers);

        initializer();

        detector();

        listener();

        setData();

        getData("getAll");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);

        if (!authViewModel.getToken().equals("")) {
            tabCentersAdapter = new TabCentersAdapter(getSupportFragmentManager(), 0, this, true);
        } else {
            tabCentersAdapter = new TabCentersAdapter(getSupportFragmentManager(), 0, this, false);
        }

        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
        toolbarCreateImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarCreateImageView.setImageResource(R.drawable.ic_plus_light);
        ImageViewCompat.setImageTintList(toolbarCreateImageView, AppCompatResources.getColorStateList(this, R.color.MountainMeadow));
        toolbarSearchImageView = findViewById(R.id.layout_toolbar_thirdly_imageView);
        toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
        ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CentersTitle));

        searchLayout = findViewById(R.id.activity_centers_searchLayout);

        mainLayout = findViewById(R.id.activity_centers_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        searchImageView = findViewById(R.id.activity_centers_search_imageView);
        searchTextView = findViewById(R.id.activity_centers_search_textView);

        tabLayout = findViewById(R.id.activity_centers_tabLayout);

        rtlViewPager = findViewById(R.id.activity_centers_rtlViewPager);

        searchDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(searchDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchDialog.setContentView(R.layout.dialog_type);
        searchDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsSearch = new WindowManager.LayoutParams();
        layoutParamsSearch.copyFrom(searchDialog.getWindow().getAttributes());
        layoutParamsSearch.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsSearch.height = WindowManager.LayoutParams.WRAP_CONTENT;
        searchDialog.getWindow().setAttributes(layoutParamsSearch);

        searchDialogTitle = searchDialog.findViewById(R.id.dialog_type_title_textView);
        searchDialogTitle.setText(getResources().getString(R.string.CentersSearchDialogTitle));
        searchDialogInput = searchDialog.findViewById(R.id.dialog_type_input_editText);
        searchDialogInput.setHint(getResources().getString(R.string.CentersSearchDialogInput));
        searchDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        searchDialogPositive = searchDialog.findViewById(R.id.dialog_type_positive_textView);
        searchDialogPositive.setText(getResources().getString(R.string.CentersSearchDialogPositive));
        searchDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        searchDialogNegative = searchDialog.findViewById(R.id.dialog_type_negative_textView);
        searchDialogNegative.setText(getResources().getString(R.string.CentersSearchDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarCreateImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarSearchImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            searchImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        toolbarCreateImageView.setOnClickListener(v -> {
            toolbarCreateImageView.setClickable(false);
            handler.postDelayed(() -> toolbarCreateImageView.setClickable(true), 250);

            if (finished) {
                Fragment allFragment = tabCentersAdapter.allFragment;
                if (((AllCentersFragment) allFragment).pagingProgressBar.isShown()) {
                    loadingAll = false;
                    ((AllCentersFragment) allFragment).pagingProgressBar.setVisibility(View.GONE);
                }
                Fragment myFragment = tabCentersAdapter.myFragment;
                if (((MyCentersFragment) myFragment).pagingProgressBar.isShown()) {
                    loadingMy = false;
                    ((MyCentersFragment) myFragment).pagingProgressBar.setVisibility(View.GONE);
                }
            }

            startActivityForResult(new Intent(this, CreateCenterActivity.class).putExtra("loaded", finished), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        toolbarSearchImageView.setOnClickListener(v -> {
            toolbarSearchImageView.setClickable(false);
            handler.postDelayed(() -> toolbarSearchImageView.setClickable(true), 250);

            searchDialogInput.setText(search);

            searchDialog.show();
        });

        searchTextView.setOnClickListener(v -> {
            searchTextView.setClickable(false);
            handler.postDelayed(() -> searchTextView.setClickable(true), 250);

            searchDialogInput.setText(search);

            searchDialog.show();
        });

        searchImageView.setOnClickListener(v -> {
            searchImageView.setClickable(false);
            handler.postDelayed(() -> searchImageView.setClickable(true), 250);

            search = "";
            searchTextView.setText(search);

            relaunchData();

            searchDialog.dismiss();
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                rtlViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchData();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        searchDialogInput.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!searchDialogInput.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(searchDialogInput);
                    controlEditText.select(searchDialogInput);
                }
            }
            return false;
        });

        searchDialogPositive.setOnClickListener(v -> {
            searchDialogPositive.setClickable(false);
            handler.postDelayed(() -> searchDialogPositive.setClickable(true), 250);

            if (searchDialogInput.length() != 0) {
                search = searchDialogInput.getText().toString().trim();
                searchTextView.setText(search);

                relaunchData();

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                    controlEditText.input().getText().clear();
                }

                searchDialog.dismiss();
            } else {
                errorException("searchDialog");
            }
        });

        searchDialogNegative.setOnClickListener(v -> {
            searchDialogNegative.setClickable(false);
            handler.postDelayed(() -> searchDialogNegative.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            searchDialog.dismiss();
        });

        searchDialog.setOnCancelListener(dialog -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            searchDialog.dismiss();
        });
    }

    private void setData() {
        if (authViewModel.createCenter()) {
            toolbarCreateImageView.setVisibility(View.VISIBLE);
        } else {
            toolbarCreateImageView.setVisibility(View.GONE);
        }
    }

    private void setInfoLayout(String type) {
        switch (type) {
            case "error":
                infoImageView.setImageResource(R.drawable.illu_error);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppInfoError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppInfoConnection), 17, 26, retrySpan));
                break;
        }
    }

    private void errorException(String type) {
        if (type.equals("searchDialog")) {
            searchDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void resetData(String method) {
        if (method.equals("search")) {
            if (authViewModel.hasAccess()) {
                toolbarSearchImageView.setVisibility(View.VISIBLE);
            } else {
                toolbarSearchImageView.setVisibility(View.GONE);
            }

            if (search.equals("")) {
                searchLayout.setVisibility(View.GONE);

                toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
                ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
            } else {
                if (searchLayout.getVisibility() == View.GONE) {
                    searchLayout.setVisibility(View.VISIBLE);

                    toolbarSearchImageView.setImageResource(R.drawable.ic_search_solid);
                    ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));
                }
            }
        }
    }

    private void getData(String method) {
        try {
            switch (method) {
                case "getAll":
                    centerViewModel.centers(search);
                    CenterRepository.allPage = 1;
                    break;
                case "getMy":
                    centerViewModel.myCenters(search);
                    CenterRepository.myPage = 1;
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchData() {
        searchLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        getData("getAll");
    }

    public void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work.equals("getAll")) {
                finished = false;
                loadingAll = true;
                if (integer == 1) {
                    if (CenterRepository.allPage == 1) {
                        if (!authViewModel.getToken().equals("")) {
                            // Continue Get MyCenters

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            getData("getMy");
                        } else {
                            // Show Centers And Just AllCenters

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.GONE);
                            rtlViewPager.setAdapter(tabCentersAdapter);

                            resetData("search");

                            finished = true;

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        }

                        loadingAll = false;
                        CenterRepository.allPage++;

                    } else {
                        Fragment allFragment = tabCentersAdapter.allFragment;
                        ((AllCentersFragment) allFragment).notifyRecycler();

                        resetData("search");

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer != -1) {
                    if (centerViewModel.getAll() == null) {
                        // Centers is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (integer == 0) {
                            setInfoLayout("error"); // Show Error
                        } else if (integer == -2) {
                            setInfoLayout("connection"); // Show Connection
                        }

                        resetData("search");

                        finished = true;

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        // Show Centers

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        if (!authViewModel.getToken().equals("")) {
                            tabLayout.setVisibility(View.VISIBLE); // Both AllCenters And MyCenters
                            rtlViewPager.setAdapter(tabCentersAdapter);
                        } else {
                            tabLayout.setVisibility(View.GONE); // Just AllCenters
                            rtlViewPager.setAdapter(tabCentersAdapter);
                        }

                        resetData("search");

                        finished = true;

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                }
            } else if (CenterRepository.work.equals("getMy")) {
                loadingMy = true;
                if (integer == 1) {
                    if (CenterRepository.myPage == 1) {
                        // Show Centers And Both AllCenters And MyCenters

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        tabLayout.setVisibility(View.VISIBLE);
                        rtlViewPager.setAdapter(tabCentersAdapter);

                        loadingMy = false;
                        CenterRepository.myPage++;

                        resetData("search");

                        finished = true;

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        Fragment myFragment = tabCentersAdapter.myFragment;
                        ((MyCentersFragment) myFragment).notifyRecycler();

                        resetData("search");

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer != -1) {
                    // Show Centers And Both AllCenters And MyCenters

                    loadingLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    tabLayout.setVisibility(View.VISIBLE);
                    rtlViewPager.setAdapter(tabCentersAdapter);

                    resetData("search");

                    finished = true;

                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                relaunchData();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}