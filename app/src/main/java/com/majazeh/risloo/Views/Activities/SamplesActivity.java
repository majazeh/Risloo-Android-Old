package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.SamplesAdapter;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;
import com.majazeh.risloo.Views.Dialogs.FilterDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SamplesActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;
    private SampleViewModel sampleViewModel;
    private RoomViewModel roomViewModel;

    // Adapters
    private SpinnerAdapter filterRecyclerViewAdapter;
    private SamplesAdapter samplesRecyclerViewAdapter;
    private SearchAdapter scaleDialogAdapter, roomDialogAdapter, statusDialogAdapter;

    // Vars
    public String scale = "", room = "", status = "";
    public boolean loading = false, finished = false;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private FilterDialog filterDialog;
    private LinearLayoutManager layoutManager;
    private ClickableSpan retrySpan;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarCreateImageView, toolbarFilterImageView;
    private TextView toolbarTextView;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout filterLayout;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView infoImageView;
    private TextView infoTextView;
    private RecyclerView filterRecyclerView, samplesRecyclerView;
    public ProgressBar pagingProgressBar;
    private Dialog scaleDialog, roomDialog, statusDialog;
    private TextView scaleDialogTitleTextView, roomDialogTitleTextView, statusDialogTitleTextView;
    private CoordinatorLayout roomDialogSearchLayout;
    private EditText roomDialogEditText;
    private ImageView roomDialogImageView;
    private ProgressBar roomDialogProgressBar;
    private TextView roomDialogTextView;
    private RecyclerView scaleDialogRecyclerView, roomDialogRecyclerView, statusDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_samples);

        initializer();

        detector();

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
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        filterRecyclerViewAdapter = new SpinnerAdapter(this);
        samplesRecyclerViewAdapter = new SamplesAdapter(this);
        scaleDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);
        statusDialogAdapter = new SearchAdapter(this);

        handler = new Handler();

        controlEditText = new ControlEditText();

        filterDialog = new FilterDialog(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
        toolbarCreateImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarCreateImageView.setVisibility(View.VISIBLE);
        toolbarCreateImageView.setImageResource(R.drawable.ic_plus_light);
        ImageViewCompat.setImageTintList(toolbarCreateImageView, AppCompatResources.getColorStateList(this, R.color.IslamicGreen));
        toolbarFilterImageView = findViewById(R.id.layout_toolbar_thirdly_imageView);
        toolbarFilterImageView.setImageResource(R.drawable.ic_filter_light);
        ImageViewCompat.setImageTintList(toolbarFilterImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.SamplesTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        swipeLayout = findViewById(R.id.activity_samples_swipeLayout);
        swipeLayout.setColorSchemeResources(R.color.Primary);
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.White));

        filterLayout = findViewById(R.id.activity_samples_filterLayout);

        mainLayout = findViewById(R.id.activity_samples_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        filterRecyclerView = findViewById(R.id.activity_samples_filter_recyclerView);
        filterRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filterRecyclerView.setHasFixedSize(true);
        samplesRecyclerView = findViewById(R.id.activity_samples_recyclerView);
        samplesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        samplesRecyclerView.setLayoutManager(layoutManager);
        samplesRecyclerView.setHasFixedSize(true);

        pagingProgressBar = findViewById(R.id.activity_samples_progressBar);

        scaleDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(scaleDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        scaleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scaleDialog.setContentView(R.layout.dialog_search);
        scaleDialog.setCancelable(true);
        roomDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(roomDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        roomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomDialog.setContentView(R.layout.dialog_search);
        roomDialog.setCancelable(true);
        statusDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(statusDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        statusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        statusDialog.setContentView(R.layout.dialog_search);
        statusDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsScale = new WindowManager.LayoutParams();
        layoutParamsScale.copyFrom(scaleDialog.getWindow().getAttributes());
        layoutParamsScale.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsScale.height = WindowManager.LayoutParams.WRAP_CONTENT;
        scaleDialog.getWindow().setAttributes(layoutParamsScale);
        WindowManager.LayoutParams layoutParamsRoom = new WindowManager.LayoutParams();
        layoutParamsRoom.copyFrom(roomDialog.getWindow().getAttributes());
        layoutParamsRoom.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsRoom.height = WindowManager.LayoutParams.WRAP_CONTENT;
        roomDialog.getWindow().setAttributes(layoutParamsRoom);
        WindowManager.LayoutParams layoutParamsStatus = new WindowManager.LayoutParams();
        layoutParamsStatus.copyFrom(statusDialog.getWindow().getAttributes());
        layoutParamsStatus.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsStatus.height = WindowManager.LayoutParams.WRAP_CONTENT;
        statusDialog.getWindow().setAttributes(layoutParamsStatus);

        scaleDialogTitleTextView = scaleDialog.findViewById(R.id.dialog_search_title_textView);
        scaleDialogTitleTextView.setText(getResources().getString(R.string.SamplesScaleDialogTitle));
        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.SamplesRoomDialogTitle));
        statusDialogTitleTextView = statusDialog.findViewById(R.id.dialog_search_title_textView);
        statusDialogTitleTextView.setText(getResources().getString(R.string.SamplesStatusDialogTitle));

        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);

        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);

        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);

        scaleDialogRecyclerView = scaleDialog.findViewById(R.id.dialog_search_recyclerView);
        scaleDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        scaleDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scaleDialogRecyclerView.setHasFixedSize(true);
        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);
        statusDialogRecyclerView = statusDialog.findViewById(R.id.dialog_search_recyclerView);
        statusDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        statusDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        statusDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarCreateImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarFilterImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        toolbarCreateImageView.setOnClickListener(v -> {
            toolbarCreateImageView.setClickable(false);
            handler.postDelayed(() -> toolbarCreateImageView.setClickable(true), 300);

            if (finished) {
                if (pagingProgressBar.isShown()) {
                    loading = false;
                    pagingProgressBar.setVisibility(View.GONE);
                }
            }

            startActivityForResult(new Intent(this, CreateSampleActivity.class).putExtra("loaded", finished), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        toolbarFilterImageView.setOnClickListener(v -> {
            toolbarFilterImageView.setClickable(false);
            handler.postDelayed(() -> toolbarFilterImageView.setClickable(true), 300);

            filterDialog.show(this.getSupportFragmentManager(), "filterBottomSheet");
        });

        swipeLayout.setOnRefreshListener(() -> {
            swipeLayout.setRefreshing(false);
            relaunchSamples();
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
                                sampleViewModel.samples(scale, status, room);
                                observeWork("sampleViewModel");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        roomDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(roomDialogEditText);
                    controlEditText.select(roomDialogEditText);
                }
            }
            return false;
        });

        roomDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (roomDialogEditText.length() != 0) {
                        getData("getRooms", roomDialogEditText.getText().toString().trim());
                    } else {
                        ExceptionGenerator.getException(false, 0, null, "EmptyInputException");
                        Toast.makeText(SamplesActivity.this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scaleDialog.setOnCancelListener(dialog -> scaleDialog.dismiss());

        roomDialog.setOnCancelListener(dialog -> {
            resetData("roomDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            roomDialog.dismiss();
        });

        statusDialog.setOnCancelListener(dialog -> statusDialog.dismiss());
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
            case "search":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
                break;
        }
    }

    public void setFilter(String method) {
        if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
            controlEditText.clear(this, controlEditText.input());
        }

        switch (method) {
            case "scale":
                if (SampleRepository.scaleFilter.size() == 0) {
                    ExceptionGenerator.getException(false, 0, null, "EmptyScalesForFilterException");
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                } else {
                    setRecyclerView(SampleRepository.scaleFilter, scaleDialogRecyclerView, "getScalesFilter");
                    scaleDialog.show();
                }
                break;
            case "room":
                roomDialog.show();
                break;
            case "status":
                if (SampleRepository.statusFilter.size() == 0) {
                    ExceptionGenerator.getException(false, 0, null, "EmptyStatusForFilterException");
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        ArrayList<Model> translatedStatusFilter = new ArrayList<>();
                        Model statusModel;

                        for (int i = 0; i < SampleRepository.statusFilter.size(); i++) {
                            switch (SampleRepository.statusFilter.get(i).get("title").toString()) {
                                case "seald":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusSeald)));
                                    break;
                                case "open":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusOpen)));
                                    break;
                                case "closed":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusClosed)));
                                    break;
                                case "scoring":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusScoring)));
                                    break;
                                case "creating_files":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusCreatingFiles)));
                                    break;
                                case "done":
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", getResources().getString(R.string.SamplesStatusDone)));
                                    break;
                                default:
                                    statusModel = new Model(new JSONObject().put("id", SampleRepository.statusFilter.get(i).get("id").toString()).put("title", SampleRepository.statusFilter.get(i).get("title").toString()));
                                    break;
                            }

                            translatedStatusFilter.add(statusModel);
                        }

                        setRecyclerView(translatedStatusFilter, statusDialogRecyclerView, "getStatusFilter");
                        statusDialog.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "scalesFilter":
            case "roomsFilter":
            case "statusFilter":
                filterRecyclerViewAdapter.setValue(filterRecyclerViewAdapter.getValues(), filterRecyclerViewAdapter.getIds(), method, "Samples");
                recyclerView.setAdapter(filterRecyclerViewAdapter);
                break;
            case "getScalesFilter":
                scaleDialogAdapter.setValue(arrayList, method, "Samples");
                recyclerView.setAdapter(scaleDialogAdapter);
                break;
            case "getRooms":
                roomDialogAdapter.setValue(arrayList, method, "Samples");
                recyclerView.setAdapter(roomDialogAdapter);

                if (arrayList.size() == 0) {
                    roomDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                        roomDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getStatusFilter":
                statusDialogAdapter.setValue(arrayList, method, "Samples");
                recyclerView.setAdapter(statusDialogAdapter);
                break;
        }
    }

    private void resetData(String method) {
        if (method.equals("filter")) {
            if (authViewModel.hasAccess()) {
                toolbarFilterImageView.setVisibility(View.VISIBLE);
            } else {
                toolbarFilterImageView.setVisibility(View.GONE);
            }

            if (filterRecyclerViewAdapter.getValues().size() == 0) {
                filterLayout.setVisibility(View.GONE);

                toolbarFilterImageView.setImageResource(R.drawable.ic_filter_light);
                ImageViewCompat.setImageTintList(toolbarFilterImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
            } else {
                if (filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);

                    toolbarFilterImageView.setImageResource(R.drawable.ic_filter_solid);
                    ImageViewCompat.setImageTintList(toolbarFilterImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));
                }
            }
        } else if (method.equals("roomDialog")) {
            RoomRepository.rooms.clear();
            roomDialogRecyclerView.setAdapter(null);

            if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                roomDialogTextView.setVisibility(View.GONE);
            }
        }
    }

    private void getData(String method, String q) {
        try {
            if (method.equals("getRooms")) {
                roomDialogProgressBar.setVisibility(View.VISIBLE);
                roomDialogImageView.setVisibility(View.GONE);

                RoomRepository.allPage = 1;
                roomViewModel.rooms(q);
            }
            observeWork("roomViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchSamples() {
        try {
            sampleViewModel.samples(scale, status, room);
            SampleRepository.samplesPage = 1;
            observeWork("sampleViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void relaunchSamples() {
        filterLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchSamples();
    }

    private void observeWork(String method) {
        if (method.equals("sampleViewModel")) {
            SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
                if (SampleRepository.work.equals("getAll")) {
                    finished = false;
                    loading = true;
                    if (integer == 1) {
                        if (sampleViewModel.getAll() != null) {
                            // Show Samples

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            samplesRecyclerViewAdapter.setSamples(sampleViewModel.getAll());
                            if (SampleRepository.samplesPage == 1) {
                                samplesRecyclerView.setAdapter(samplesRecyclerViewAdapter);
                            }
                        } else {
                            // Samples is Empty

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            if (scale.equals("") && room.equals("") && status.equals("")) {
                                setInfoLayout("empty"); // Show Empty
                            } else {
                                setInfoLayout("search"); // Show Search
                            }
                        }

                        if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                            pagingProgressBar.setVisibility(View.GONE);
                        }

                        loading = false;
                        SampleRepository.samplesPage++;

                        resetData("filter");

                        finished = true;

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer != -1) {
                        if (sampleViewModel.getAll() == null) {
                            // Samples is Empty

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

                            resetData("filter");

                            finished = true;

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            // Show Samples

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            samplesRecyclerViewAdapter.setSamples(sampleViewModel.getAll());
                            if (SampleRepository.samplesPage == 1) {
                                samplesRecyclerView.setAdapter(samplesRecyclerViewAdapter);
                            }

                            if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                pagingProgressBar.setVisibility(View.GONE);
                            }

                            resetData("filter");

                            finished = true;

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    }
                }
            });
        } else if (method.equals("roomViewModel")) {
            RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
                if (RoomRepository.work.equals("getAll")) {
                    if (integer == 1) {
                        setRecyclerView(RoomRepository.rooms, roomDialogRecyclerView, "getRooms");

                        roomDialogProgressBar.setVisibility(View.GONE);
                        roomDialogImageView.setVisibility(View.VISIBLE);
                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        roomDialogProgressBar.setVisibility(View.GONE);
                        roomDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        roomDialogProgressBar.setVisibility(View.GONE);
                        roomDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                }
            });
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getScalesFilter":
                    if (!scale.equals(model.get("id").toString())) {
                        if (scale.equals("")) {
                            scale = model.get("id").toString();

                            filterRecyclerViewAdapter.getValues().add(model);
                            filterRecyclerViewAdapter.getIds().add(model.get("id").toString());

                            setRecyclerView(null, filterRecyclerView, "scalesFilter");
                        } else {
                            scale = model.get("id").toString();

                            for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                                if (filterRecyclerViewAdapter.getIds().get(i).charAt(0) == model.get("id").toString().charAt(0)) {
                                    filterRecyclerViewAdapter.replaceValue(i, model);
                                    break;
                                }
                            }
                        }
                    } else if (scale.equals(model.get("id").toString())) {
                        scale = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                filterRecyclerViewAdapter.removeValue(i);
                                break;
                            }
                        }
                    }

                    relaunchSamples();

                    scaleDialog.dismiss();
                    break;

                case "getRooms":
                    JSONObject manager = (JSONObject) model.get("manager");

                    Model roomModel = new Model(new JSONObject().put("id", model.get("id").toString()).put("title", manager.get("name").toString()));

                    if (!room.equals(roomModel.get("id").toString())) {
                        if (room.equals("")) {
                            room = roomModel.get("id").toString();

                            filterRecyclerViewAdapter.getValues().add(roomModel);
                            filterRecyclerViewAdapter.getIds().add(roomModel.get("id").toString());

                            setRecyclerView(null, filterRecyclerView, "roomsFilter");
                        } else {
                            room = roomModel.get("id").toString();

                            for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                                if (filterRecyclerViewAdapter.getIds().get(i).charAt(0) == model.get("id").toString().charAt(0)) {
                                    filterRecyclerViewAdapter.replaceValue(i, roomModel);
                                    break;
                                }
                            }
                        }
                    } else if (room.equals(roomModel.get("id").toString())) {
                        room = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(roomModel.get("id").toString())) {
                                filterRecyclerViewAdapter.removeValue(i);
                                break;
                            }
                        }
                    }

                    relaunchSamples();

                    resetData("roomDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    roomDialog.dismiss();
                    break;

                case "getStatusFilter":
                    if (!status.equals(model.get("id").toString())) {
                        if (status.equals("")) {
                            status = model.get("id").toString();

                            filterRecyclerViewAdapter.getValues().add(model);
                            filterRecyclerViewAdapter.getIds().add(model.get("id").toString());

                            setRecyclerView(null, filterRecyclerView, "statusFilter");
                        } else {
                            status = model.get("id").toString();

                            for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                                if (filterRecyclerViewAdapter.getIds().get(i).charAt(0) != 'R' && filterRecyclerViewAdapter.getIds().get(i).charAt(0) != '$') {
                                    filterRecyclerViewAdapter.replaceValue(i, model);
                                    break;
                                }
                            }
                        }
                    } else if (status.equals(model.get("id").toString())) {
                        status = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                filterRecyclerViewAdapter.removeValue(i);
                                break;
                            }
                        }
                    }

                    relaunchSamples();

                    statusDialog.dismiss();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}