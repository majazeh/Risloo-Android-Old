package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.Menu;
import android.view.MenuItem;
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
import com.majazeh.risloo.Utils.Managers.StringCustomizer;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.InputEditText;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.CenterTabAdapter;
import com.majazeh.risloo.Views.Fragments.AllCenterFragment;
import com.majazeh.risloo.Views.Fragments.MyCenterFragment;

import org.json.JSONException;

import java.util.Objects;

public class CenterActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;
    public CenterViewModel centerViewModel;

    // Adapters
    private CenterTabAdapter adapter;

    // Vars
    public String search = "";
    public boolean loadingAll = false, loadingMy = false;

    // Objects
    private Handler handler;
    private InputEditText inputEditText;
    private MenuItem toolCreate, toolSearch;
    private ClickableSpan retrySpan;

    // Widgets
    private Toolbar toolbar;
    private LinearLayout searchLayout;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private TextView infoTextView;
    private ImageView infoImageView;
    private ImageView searchImageView;
    private TextView searchTextView;
    private TabLayout tabLayout;
    private RtlViewPager rtlViewPager;
    private Dialog searchDialog;
    private TextView searchDialogTitle, searchDialogPositive, searchDialogNegative;
    private EditText searchDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_center);

        initializer();

        detector();

        listener();

        launchProcess("getAll");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);

        if (!authViewModel.getToken().equals("")) {
            adapter = new CenterTabAdapter(getSupportFragmentManager(), 0, this, true);
        } else {
            adapter = new CenterTabAdapter(getSupportFragmentManager(), 0, this, false);
        }

        handler = new Handler();

        inputEditText = new InputEditText();

        toolbar = findViewById(R.id.activity_center_toolbar);
        setSupportActionBar(toolbar);

        searchLayout = findViewById(R.id.activity_center_searchLayout);

        mainLayout = findViewById(R.id.activity_center_mainLayout);
        infoLayout = findViewById(R.id.layout_info);
        loadingLayout = findViewById(R.id.layout_loading);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        searchImageView = findViewById(R.id.activity_center_search_imageView);
        searchTextView = findViewById(R.id.activity_center_search_textView);

        tabLayout = findViewById(R.id.activity_center_tabLayout);

        rtlViewPager = findViewById(R.id.activity_center_rtlViewPager);

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
        searchDialogTitle.setText(getResources().getString(R.string.CenterSearchDialogTitle));
        searchDialogInput = searchDialog.findViewById(R.id.dialog_type_input_editText);
        searchDialogInput.setHint(getResources().getString(R.string.CenterSearchDialogInput));
        searchDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        searchDialogPositive = searchDialog.findViewById(R.id.dialog_type_positive_textView);
        searchDialogPositive.setText(getResources().getString(R.string.CenterSearchDialogPositive));
        searchDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        searchDialogNegative = searchDialog.findViewById(R.id.dialog_type_negative_textView);
        searchDialogNegative.setText(getResources().getString(R.string.CenterSearchDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            infoLayout.setBackgroundResource(R.drawable.draw_4sdp_solid_snow_ripple_quartz);
            infoImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        searchLayout.setOnClickListener(v -> {
            searchLayout.setClickable(false);
            handler.postDelayed(() -> searchLayout.setClickable(true), 300);

            searchDialog.show();
        });

        searchImageView.setOnClickListener(v -> {
            searchImageView.setClickable(false);
            handler.postDelayed(() -> searchImageView.setClickable(true), 300);

            search = "";

            searchTextView.setText(search);

            relaunchCenters();

            if (search.equals("")) inputEditText.getInput().getText().clear(); else inputEditText.getInput().setText(search);

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
                relaunchCenters();
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
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(searchDialogInput);
                    inputEditText.select(searchDialogInput);
                }
            }
            return false;
        });

        searchDialogPositive.setOnClickListener(v -> {
            searchDialogPositive.setClickable(false);
            handler.postDelayed(() -> searchDialogPositive.setClickable(true), 300);

            if (searchDialogInput.length() != 0) {
                search = searchDialogInput.getText().toString().trim();

                searchTextView.setText(search);

                relaunchCenters();

                if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                    inputEditText.clear(this, inputEditText.getInput());
                    if (search.equals("")) inputEditText.getInput().getText().clear(); else inputEditText.getInput().setText(search);
                }

                searchDialog.dismiss();
            } else {
                errorView("search");
            }
        });

        searchDialogNegative.setOnClickListener(v -> {
            searchDialogNegative.setClickable(false);
            handler.postDelayed(() -> searchDialogNegative.setClickable(true), 300);

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                if (search.equals("")) inputEditText.getInput().getText().clear(); else inputEditText.getInput().setText(search);
            }

            searchDialog.dismiss();
        });

        searchDialog.setOnCancelListener(dialog -> {
            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                if (search.equals("")) inputEditText.getInput().getText().clear(); else inputEditText.getInput().setText(search);
            }

            searchDialog.dismiss();
        });
    }

    private void setInfoLayout(String type) {
        switch (type) {
            case "error":
                infoImageView.setImageResource(R.drawable.illu_error);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
                break;
        }
    }

    private void resetData(String method) {
        if ("search".equals(method)) {
            if (search.equals("")) {
                searchLayout.setVisibility(View.GONE);
                toolSearch.setIcon(getResources().getDrawable(R.drawable.tool_search_default));
            } else {
                if (searchLayout.getVisibility() == View.GONE) {
                    searchLayout.setVisibility(View.VISIBLE);
                    toolSearch.setIcon(getResources().getDrawable(R.drawable.tool_search_active));
                }
            }
            relaunchCenters();
        }
    }

    private void errorView(String type) {
        if ("search".equals(type)) {
            searchDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void launchProcess(String method) {
        try {
            if (method.equals("getAll")) {
                centerViewModel.centers();
                CenterRepository.allPage = 1;
            } else {
                centerViewModel.myCenters();
                CenterRepository.myPage = 1;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchCenters() {
        searchLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchProcess("getAll");
    }

    public void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work.equals("getAll")) {
                loadingAll = true;
                if (integer == 1) {
                    if (CenterRepository.allPage == 1) {
                        if (!authViewModel.getToken().equals("")) {
                            // Continue Get MyCenter

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            launchProcess("getMy");
                        } else {
                            // Show Centers And Just AllCenters

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.GONE);
                            rtlViewPager.setAdapter(adapter);

                            if (authViewModel.hasAccess()) {
                                toolCreate.setVisible(true);
                                toolSearch.setVisible(true);
                            } else {
                                toolCreate.setVisible(false);
                                toolSearch.setVisible(false);
                            }

                            resetData("search");

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        }

                        loadingAll = false;
                        CenterRepository.allPage++;

                    } else {
                        Fragment allFragment = adapter.allFragment;
                        ((AllCenterFragment) allFragment).notifyRecycler();

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

                        if (authViewModel.hasAccess()) {
                            toolCreate.setVisible(true);
                            toolSearch.setVisible(true);
                        } else {
                            toolCreate.setVisible(false);
                            toolSearch.setVisible(false);
                        }

                        resetData("search");

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        // Show Centers

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        if (!authViewModel.getToken().equals("")) {
                            tabLayout.setVisibility(View.VISIBLE); // Both AllCenters And MyCenters
                            rtlViewPager.setAdapter(adapter);
                        } else {
                            tabLayout.setVisibility(View.GONE); // Just AllCenters
                            rtlViewPager.setAdapter(adapter);
                        }

                        if (authViewModel.hasAccess()) {
                            toolCreate.setVisible(true);
                            toolSearch.setVisible(true);
                        } else {
                            toolCreate.setVisible(false);
                            toolSearch.setVisible(false);
                        }

                        resetData("search");

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
                        rtlViewPager.setAdapter(adapter);

                        if (authViewModel.hasAccess()) {
                            toolCreate.setVisible(true);
                            toolSearch.setVisible(true);
                        } else {
                            toolCreate.setVisible(false);
                            toolSearch.setVisible(false);
                        }

                        loadingMy = false;
                        CenterRepository.myPage++;

                        resetData("search");

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        Fragment myFragment = adapter.myFragment;
                        ((MyCenterFragment) myFragment).notifyRecycler();

                        resetData("search");

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer != -1) {
                    // Show Centers And Both AllCenters And MyCenters

                    loadingLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    tabLayout.setVisibility(View.VISIBLE);
                    rtlViewPager.setAdapter(adapter);

                    if (authViewModel.hasAccess()) {
                        toolCreate.setVisible(true);
                        toolSearch.setVisible(true);
                    } else {
                        toolCreate.setVisible(false);
                        toolSearch.setVisible(false);
                    }

                    resetData("search");

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
                relaunchCenters();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_center, menu);

        toolCreate = menu.findItem(R.id.tool_create);
        toolCreate.setOnMenuItemClickListener(menuItem -> {
            Fragment allFragment = adapter.allFragment;
            if (((AllCenterFragment) allFragment).pagingProgressBar.isShown()) {
                loadingAll = false;
                ((AllCenterFragment) allFragment).pagingProgressBar.setVisibility(View.GONE);
            }
            Fragment myFragment = adapter.myFragment;
            if (((MyCenterFragment) myFragment).pagingProgressBar.isShown()) {
                loadingMy = false;
                ((MyCenterFragment) myFragment).pagingProgressBar.setVisibility(View.GONE);
            }

            startActivityForResult(new Intent(this, CreateCenterActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            return false;
        });

        toolSearch = menu.findItem(R.id.tool_search);
        toolSearch.setOnMenuItemClickListener(menuItem -> {
            searchDialog.show();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}