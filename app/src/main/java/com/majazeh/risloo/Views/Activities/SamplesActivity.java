package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.SamplesAdapter;

import org.json.JSONException;

public class SamplesActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private SamplesAdapter adapter;

    // Objects
    private Handler handler;
    private MenuItem toolCreate;
    private ClickableSpan retrySpan;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView retryTextView;
    private ImageView retryImageView;
    private LinearLayout mainLayout, retryLayout, emptyLayout, loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_samples);

        initializer();

        listener();

        launchSamples();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        adapter = new SamplesAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_samples_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.activity_samples_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        retryImageView = findViewById(R.id.activity_samples_retry_imageView);

        retryTextView = findViewById(R.id.activity_samples_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mainLayout = findViewById(R.id.activity_samples_mainLayout);
        retryLayout = findViewById(R.id.activity_samples_retryLayout);
        emptyLayout = findViewById(R.id.activity_samples_emptyLayout);
        loadingLayout = findViewById(R.id.activity_samples_loadingLayout);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                loadingLayout.setVisibility(View.VISIBLE);
                retryLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.GONE);

                launchSamples();
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

    private void launchSamples() {
        try {
            viewModel.samples();
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work.equals("getAll")) {
                if (integer == 1) {
                    if (viewModel.getAll() != null) {
                        // Show Samples

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        adapter.setSamples(viewModel.getAll());
                        recyclerView.setAdapter(adapter);

                        toolCreate.setVisible(true);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else {
                        // Samples is Empty

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        toolCreate.setVisible(true);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                } else {
                    if (viewModel.getAll() == null) {
                        if (integer == 0) {
                            // Samples is Empty And Error

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("error");

                            toolCreate.setVisible(true);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            // Samples is Empty And Connection

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("connection");

                            toolCreate.setVisible(true);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        if (integer != -1) {
                            // Show Samples

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            adapter.setSamples(viewModel.getAll());
                            recyclerView.setAdapter(adapter);

                            handler.postDelayed(() -> toolCreate.setVisible(true), 500);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                loadingLayout.setVisibility(View.VISIBLE);
                retryLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.GONE);
            launchSamples();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_samples, menu);

        toolCreate = menu.findItem(R.id.tool_create);
        toolCreate.setVisible(false);
        toolCreate.setOnMenuItemClickListener(menuItem -> {
            startActivityForResult(new Intent(this, CreateSampleActivity.class), 100);
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