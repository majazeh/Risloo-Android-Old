package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.StringCustomizer;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.SamplesAdapter;

import org.json.JSONException;

public class SamplesActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;
    private SampleViewModel sampleViewModel;

    // Adapters
    private SamplesAdapter adapter;

    // Vars
    public boolean loading = false;

    // Objects
    private Handler handler;
    private MenuItem toolCreate;
    private ClickableSpan retrySpan;
    private LinearLayoutManager layoutManager;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView samplesRecyclerView;
    public ProgressBar pagingProgressBar;
    private TextView retryTextView;
    private ImageView retryImageView;
    private FrameLayout mainLayout;
    private LinearLayout retryLayout, emptyLayout, loadingLayout;

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
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        adapter = new SamplesAdapter(this);

        handler = new Handler();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        toolbar = findViewById(R.id.activity_samples_toolbar);
        setSupportActionBar(toolbar);

        samplesRecyclerView = findViewById(R.id.activity_samples_recyclerView);
        samplesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        samplesRecyclerView.setLayoutManager(layoutManager);
        samplesRecyclerView.setHasFixedSize(true);

        pagingProgressBar = findViewById(R.id.activity_samples_progressBar);

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
                relaunchSamples();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        samplesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        try {
                            if (!loading) {
                                pagingProgressBar.setVisibility(View.VISIBLE);
                                sampleViewModel.samples();
                                observeWork();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
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
            sampleViewModel.samples();
            SampleRepository.samplesPage = 1;
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchSamples() {
        loadingLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchSamples();
    }

    private void observeWork() {
        SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work.equals("getAll")) {
                loading = true;
                if (integer == 1) {
                    if (sampleViewModel.getAll() != null) {
                        // Show Samples

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        adapter.setSamples(sampleViewModel.getAll());
                        if (SampleRepository.samplesPage == 1) {
                            samplesRecyclerView.setAdapter(adapter);
                        }

                        if (authViewModel.hasAccess()) {
                            toolCreate.setVisible(true);
                        } else {
                            toolCreate.setVisible(false);
                        }

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else {
                        // Samples is Empty

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (authViewModel.hasAccess()) {
                            toolCreate.setVisible(true);
                        } else {
                            toolCreate.setVisible(false);
                        }

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }

                    loading = false;
                    SampleRepository.samplesPage++;

                } else if (integer != -1) {
                    if (sampleViewModel.getAll() == null) {
                        if (integer == 0) {
                            // Samples is Empty And Error

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("error");

                            if (authViewModel.hasAccess()) {
                                toolCreate.setVisible(true);
                            } else {
                                toolCreate.setVisible(false);
                            }

                            if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                pagingProgressBar.setVisibility(View.GONE);
                            }

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            // Samples is Empty And Connection

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("connection");

                            if (authViewModel.hasAccess()) {
                                toolCreate.setVisible(true);
                            } else {
                                toolCreate.setVisible(false);
                            }

                            if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                pagingProgressBar.setVisibility(View.GONE);
                            }

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        // Show Samples

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        adapter.setSamples(sampleViewModel.getAll());
                        if (SampleRepository.samplesPage == 1) {
                            samplesRecyclerView.setAdapter(adapter);
                        }

                        handler.postDelayed(() -> {
                            if (authViewModel.hasAccess()) {
                                toolCreate.setVisible(true);
                            } else {
                                toolCreate.setVisible(false);
                            }
                        }, 300);

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
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
                relaunchSamples();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_samples, menu);

        toolCreate = menu.findItem(R.id.tool_create);
        toolCreate.setOnMenuItemClickListener(menuItem -> {
            if (pagingProgressBar.isShown()) {
                loading = false;
                pagingProgressBar.setVisibility(View.GONE);
            }

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