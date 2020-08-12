package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Controllers.RelationshipController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.RelationshipViewModel;
import com.majazeh.risloo.Views.Adapters.RelationshipTabAdapter;

import org.json.JSONException;

public class RelationshipActivity extends AppCompatActivity {

    // ViewModels
    private RelationshipViewModel viewModel;

    // Adapters
    private RelationshipTabAdapter adapter;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ClickableSpan retrySpan;

    // Widgets
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RtlViewPager rtlViewPager;
    private TextView retryTextView;
    private ImageView retryImageView;
    private LinearLayout mainLayout, retryLayout, loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_relationship);

        initializer();

        listener();

        launchProcess("getAll");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(RelationshipViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        adapter = new RelationshipTabAdapter(getSupportFragmentManager(), 0, this, token());

        toolbar = findViewById(R.id.activity_relationship_toolbar);

        tabLayout = findViewById(R.id.activity_relationship_tabLayout);

        rtlViewPager = findViewById(R.id.activity_relationship_rtlViewPager);

        retryImageView = findViewById(R.id.activity_relationship_retry_imageView);

        retryTextView = findViewById(R.id.activity_relationship_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mainLayout = findViewById(R.id.activity_relationship_mainLayout);
        retryLayout = findViewById(R.id.activity_relationship_retryLayout);
        loadingLayout = findViewById(R.id.activity_relationship_loadingLayout);
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
                viewModel.relationships();
            } else {
                viewModel.myRelationships();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        RelationshipController.workState.observe((LifecycleOwner) this, integer -> {
            if (RelationshipController.work == "getAll") {
                if (integer == 1) {
                    if (token()) {
                        // Continue Get MyRelationship

                        launchProcess("getMy");
                    } else {
                        // Just Show AllRelationship

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        tabLayout.setVisibility(View.GONE);
                        rtlViewPager.setAdapter(adapter);

                        RelationshipController.workState.removeObservers((LifecycleOwner) this);
                    }
                } else  {
                    if (viewModel.getAll().isEmpty()) {
                        if (integer == 0) {
                            // AllRelationship is Empty And Error

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("error");

                            RelationshipController.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            // AllRelationship is Empty And Connection

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("connection");

                            RelationshipController.workState.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        if (token()) {
                            // Show Both AllRelationship And MyRelationship

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.VISIBLE);
                            rtlViewPager.setAdapter(adapter);

                            RelationshipController.workState.removeObservers((LifecycleOwner) this);
                        } else {
                            // Just Show AllRelationship

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.GONE);
                            rtlViewPager.setAdapter(adapter);

                            RelationshipController.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                }
            } else if (RelationshipController.work == "getMy") {
                // Show Both AllRelationship And MyRelationship

                loadingLayout.setVisibility(View.GONE);
                retryLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);

                tabLayout.setVisibility(View.VISIBLE);
                rtlViewPager.setAdapter(adapter);

                RelationshipController.workState.removeObservers((LifecycleOwner) this);
            }
        });
    }

    public boolean token() {
        return !sharedPreferences.getString("token", "").equals("");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}