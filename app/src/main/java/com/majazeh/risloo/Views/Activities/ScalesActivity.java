package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.ScalesAdapter;

import org.json.JSONException;

import java.util.Objects;

public class ScalesActivity extends AppCompatActivity {

    // ViewModels
    public AuthViewModel authViewModel;
    private SampleViewModel sampleViewModel;

    // Adapters
    private ScalesAdapter scalesRecyclerViewAdapter;

    // Vars
    private String search = "";
    public boolean loading = false, finished = true;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private LinearLayoutManager layoutManager;
    private ClickableSpan retrySpan;

    // Widgets
    private ConstraintLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarSearchImageView;
    private TextView toolbarTextView;
    private LinearLayout searchLayout;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView searchImageView, infoImageView;
    private TextView searchTextView, infoTextView;
    private RecyclerView scalesRecyclerView;
    public ProgressBar pagingProgressBar;
    private Dialog searchDialog;
    private TextView searchDialogTitle, searchDialogPositive, searchDialogNegative;
    private EditText searchDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_scales);

        initializer();

        detector();

        listener();

        getData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        scalesRecyclerViewAdapter = new ScalesAdapter(this);

        handler = new Handler();

        controlEditText = new ControlEditText();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        toolbarLayout = findViewById(R.id.component_toolbar);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.component_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));
        toolbarSearchImageView = findViewById(R.id.component_toolbar_secondary_imageView);
        toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
        ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.component_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.ScalesTitle));

        searchLayout = findViewById(R.id.activity_scales_searchLayout);

        mainLayout = findViewById(R.id.activity_scales_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        searchImageView = findViewById(R.id.activity_scales_search_imageView);
        searchTextView = findViewById(R.id.activity_scales_search_textView);

        scalesRecyclerView = findViewById(R.id.activity_scales_recyclerView);
        scalesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        scalesRecyclerView.setLayoutManager(layoutManager);
        scalesRecyclerView.setHasFixedSize(true);

        pagingProgressBar = findViewById(R.id.activity_scales_progressBar);

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
        searchDialogTitle.setText(getResources().getString(R.string.ScalesSearchDialogTitle));
        searchDialogInput = searchDialog.findViewById(R.id.dialog_type_input_editText);
        searchDialogInput.setHint(getResources().getString(R.string.ScalesSearchDialogInput));
        searchDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        searchDialogPositive = searchDialog.findViewById(R.id.dialog_type_positive_textView);
        searchDialogPositive.setText(getResources().getString(R.string.ScalesSearchDialogPositive));
        searchDialogPositive.setTextColor(getResources().getColor(R.color.Risloo800));
        searchDialogNegative = searchDialog.findViewById(R.id.dialog_type_negative_textView);
        searchDialogNegative.setText(getResources().getString(R.string.ScalesSearchDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
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

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchData();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.Risloo800));
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
                                sampleViewModel.scales(search);
                                observeWork();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

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
            case "empty":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppInfoEmpty));
                break;
            case "search":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
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
            if (authViewModel.auth()) {
                toolbarSearchImageView.setVisibility(View.VISIBLE);
            } else {
                toolbarSearchImageView.setVisibility(View.GONE);
            }

            if (search.equals("")) {
                searchLayout.setVisibility(View.GONE);

                toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
                ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));
            } else {
                if (searchLayout.getVisibility() == View.GONE) {
                    searchLayout.setVisibility(View.VISIBLE);

                    toolbarSearchImageView.setImageResource(R.drawable.ic_search_solid);
                    ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));
                }
            }
        }
    }

    private void getData() {
        try {
            sampleViewModel.scales(search);
            SampleRepository.scalesPage = 1;
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

        getData();
    }

    private void observeWork() {
        SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work.equals("getScales")) {
                finished = false;
                loading = true;
                if (integer == 1) {
                    if (sampleViewModel.getScales() != null) {
                        // Show Scales

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        scalesRecyclerViewAdapter.setScale(sampleViewModel.getScales());
                        if (SampleRepository.scalesPage == 1) {
                            scalesRecyclerView.setAdapter(scalesRecyclerViewAdapter);
                        }
                    } else {
                        // Scales is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (search.equals("")) {
                            setInfoLayout("empty"); // Show Empty
                        } else {
                            setInfoLayout("search"); // Show Search
                        }
                    }

                    if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                        pagingProgressBar.setVisibility(View.GONE);
                    }

                    loading = false;
                    SampleRepository.scalesPage++;

                    resetData("search");

                    finished = true;

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer != -1) {
                    if (sampleViewModel.getScales() == null) {
                        // Scales is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (integer == 0) {
                            setInfoLayout("error"); // Show Error
                        } else if (integer == -2) {
                            setInfoLayout("connection"); // Show Connection
                        }

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        resetData("search");

                        finished = true;

                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else {
                        // Show Scales

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        scalesRecyclerViewAdapter.setScale(sampleViewModel.getScales());
                        if (SampleRepository.scalesPage == 1) {
                            scalesRecyclerView.setAdapter(scalesRecyclerViewAdapter);
                        }

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        resetData("search");

                        finished = true;

                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
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