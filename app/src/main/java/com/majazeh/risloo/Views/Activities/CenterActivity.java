package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.CenterTabAdapter;

import org.json.JSONException;

public class CenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private CenterTabAdapter adapter;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MenuItem toolCreate;
    private ClickableSpan retrySpan;

    // Widgets
    private Toolbar toolbar;
    private View view;
    private TabLayout tabLayout;
    private RtlViewPager rtlViewPager;
    private TextView retryTextView;
    private ImageView retryImageView;
    private LinearLayout mainLayout, retryLayout, loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_center);

        initializer();

        listener();

        launchProcess("getAll");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(CenterViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        adapter = new CenterTabAdapter(getSupportFragmentManager(), 0, this, token());

        toolbar = findViewById(R.id.activity_center_toolbar);
        setSupportActionBar(toolbar);

        view = findViewById(R.id.activity_center_view);

        tabLayout = findViewById(R.id.activity_center_tabLayout);

        rtlViewPager = findViewById(R.id.activity_center_rtlViewPager);

        retryImageView = findViewById(R.id.activity_center_retry_imageView);

        retryTextView = findViewById(R.id.activity_center_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mainLayout = findViewById(R.id.activity_center_mainLayout);
        retryLayout = findViewById(R.id.activity_center_retryLayout);
        loadingLayout = findViewById(R.id.activity_center_loadingLayout);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                loadingLayout.setVisibility(View.VISIBLE);
                retryLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.GONE);

                launchProcess("getAll");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };
    }

    private void setRetryLayout(String type) {
        if (type.equals("error")) {
            retryImageView.setImageResource(R.drawable.illu_error);
            retryTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
        } else if (type.equals("connection")) {
            retryImageView.setImageResource(R.drawable.illu_connection);
            retryTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
        }
    }

    private void launchProcess(String method) {
        try {
            if (method.equals("getAll")) {
                viewModel.centers();
            } else {
                viewModel.myCenters();
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work.equals("getAll")) {
                if (integer == 1) {
                    if (token()) {
                        // Continue Get MyCenter

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        launchProcess("getMy");
                    } else {
                        // Just Show AllCenter

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        view.setVisibility(View.GONE);

                        tabLayout.setVisibility(View.GONE);
                        rtlViewPager.setAdapter(adapter);

                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else {
                    if (viewModel.getAll() == null) {
                        if (integer == 0) {
                            // AllCenter is Empty And Error

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("error");

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            // AllCenter is Empty And Connection

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("connection");

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        if (integer != -1) {
                            if (token()) {
                                // Show Both AllCenter And MyCenter

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                view.setVisibility(View.GONE);

                                tabLayout.setVisibility(View.VISIBLE);
                                rtlViewPager.setAdapter(adapter);

                                CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            } else {
                                // Just Show AllCenter

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                view.setVisibility(View.GONE);

                                tabLayout.setVisibility(View.GONE);
                                rtlViewPager.setAdapter(adapter);

                                CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            }
                        }
                    }
                }
            } else if (CenterRepository.work.equals("getMy")) {
                // Show Both AllCenter And MyCenter

                if (integer != -1) {
                    loadingLayout.setVisibility(View.GONE);
                    retryLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    view.setVisibility(View.GONE);

                    tabLayout.setVisibility(View.VISIBLE);
                    rtlViewPager.setAdapter(adapter);

                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    public boolean token() {
        return !sharedPreferences.getString("token", "").equals("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_center, menu);

        toolCreate = menu.findItem(R.id.tool_create);
        toolCreate.setOnMenuItemClickListener(menuItem -> {
            startActivityForResult(new Intent(this, CreateCenterActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
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