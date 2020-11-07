package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONException;

public class ScalesActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel sampleViewModel;

    // Adapters
//    private ScalesAdapter scalesRecyclerViewAdapter;

    // Vars
    public boolean loading = false;

    // Objects
    private Handler handler;
    private LinearLayoutManager layoutManager;
    private ClickableSpan retrySpan;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView infoImageView;
    private TextView infoTextView;
    private RecyclerView scalesRecyclerView;
    public ProgressBar pagingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_scales);

        initializer();

        listener();

        launchScales();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

//        scalesRecyclerViewAdapter = new ScalesAdapter(this);

        handler = new Handler();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.ScalesTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        mainLayout = findViewById(R.id.activity_scales_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        scalesRecyclerView = findViewById(R.id.activity_scales_recyclerView);
        scalesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        scalesRecyclerView.setLayoutManager(layoutManager);
        scalesRecyclerView.setHasFixedSize(true);

        pagingProgressBar = findViewById(R.id.activity_scales_progressBar);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchScales();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        scalesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                sampleViewModel.scales("", 1);
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

    private void setInfoLayout(String type) {
        switch (type) {
            case "error":
                infoImageView.setImageResource(R.drawable.illu_error);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
                break;
            case "empty":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppEmpty));
                break;
        }
    }

    private void launchScales() {
        try {
            sampleViewModel.scales("", 1);
            SampleRepository.scalesPage = 1;
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void relaunchScales() {
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchScales();
    }

    private void observeWork() {
//        SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
//            if (SampleRepository.work.equals("getScales")) {
//                loading = true;
//                if (integer == 1) {
//                    if (sampleViewModel.getScales() != null) {
//                        // Show Scales

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

//                        scalesRecyclerViewAdapter.setScales(sampleViewModel.getScales());
//                        if (SampleRepository.scalesPage == 1) {
//                            scalesRecyclerView.setAdapter(scalesRecyclerViewAdapter);
//                        }
//                    } else {
//                        // Samples is Empty
//
//                        loadingLayout.setVisibility(View.GONE);
//                        infoLayout.setVisibility(View.VISIBLE);
//                        mainLayout.setVisibility(View.GONE);
//
//                        setInfoLayout("empty"); // Show Empty
//                    }
//
//                    if (pagingProgressBar.getVisibility() == View.VISIBLE) {
//                        pagingProgressBar.setVisibility(View.GONE);
//                    }
//
//                    loading = false;
//                    SampleRepository.scalesPage++;
//
//                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
//                } else if (integer != -1) {
//                    if (sampleViewModel.getScales() == null) {
//                        // Samples is Empty
//
//                        loadingLayout.setVisibility(View.GONE);
//                        infoLayout.setVisibility(View.VISIBLE);
//                        mainLayout.setVisibility(View.GONE);
//
//                        if (integer == 0) {
//                            setInfoLayout("error"); // Show Error
//                        } else if (integer == -2) {
//                            setInfoLayout("connection"); // Show Connection
//                        }
//
//                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
//                            pagingProgressBar.setVisibility(View.GONE);
//                        }
//
//                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
//                    } else {
//                        // Show Samples
//
//                        loadingLayout.setVisibility(View.GONE);
//                        infoLayout.setVisibility(View.GONE);
//                        mainLayout.setVisibility(View.VISIBLE);
//
//                        scalesRecyclerViewAdapter.setSamples(sampleViewModel.getScales());
//                        if (SampleRepository.scalesPage == 1) {
//                            scalesRecyclerView.setAdapter(scalesRecyclerViewAdapter);
//                        }
//
//                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
//                            pagingProgressBar.setVisibility(View.GONE);
//                        }
//
//                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                relaunchScales();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}