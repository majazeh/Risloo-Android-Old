package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Widgets.InputEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.StringCustomizer;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
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

    // Adapters
    private SpinnerAdapter filterRecyclerViewAdapter;
    private SamplesAdapter samplesRecyclerViewAdapter;
    private SearchAdapter scaleDialogAdapter, roomDialogAdapter, statusDialogAdapter;

    // Vars
    public String scale = "", room = "", status = "";
    public boolean loading = false, finished = false;

    // Objects
    private Handler handler;
    private InputEditText inputEditText;
    private MenuItem toolCreate, toolFilter;
    private LinearLayoutManager layoutManager;
    private ClickableSpan retrySpan;
    private FilterDialog filterDialog;

    // Widgets
    private Toolbar toolbar;
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

        filterRecyclerViewAdapter = new SpinnerAdapter(this);
        samplesRecyclerViewAdapter = new SamplesAdapter(this);
        scaleDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);
        statusDialogAdapter = new SearchAdapter(this);

        handler = new Handler();

        inputEditText = new InputEditText();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        filterDialog = new FilterDialog(this);

        toolbar = findViewById(R.id.activity_samples_toolbar);
        setSupportActionBar(toolbar);

        filterLayout = findViewById(R.id.activity_samples_filterLayout);

        mainLayout = findViewById(R.id.activity_samples_mainLayout);
        infoLayout = findViewById(R.id.layout_info);
        loadingLayout = findViewById(R.id.layout_loading);

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
        scaleDialogTitleTextView.setText(getResources().getString(R.string.AppSearchTitle1));
        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.AppSearchTitle2));
        statusDialogTitleTextView = statusDialog.findViewById(R.id.dialog_search_title_textView);
        statusDialogTitleTextView.setText(getResources().getString(R.string.AppSearchTitle1));

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

    @SuppressLint("ClickableViewAccessibility")
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
                                sampleViewModel.samples(scale, status, room);
                                observeWork(null);
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
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(roomDialogEditText);
                    inputEditText.select(roomDialogEditText);
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
                handler.postDelayed(() -> getData("getRooms", roomDialogEditText.getText().toString().trim()), 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scaleDialog.setOnCancelListener(dialog -> scaleDialog.dismiss());

        roomDialog.setOnCancelListener(dialog -> {
            resetData("room");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();

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
                infoTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
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
        if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
            inputEditText.clear(this, inputEditText.getInput());
        }

        switch (method) {
            case "scale":
                if (SampleRepository.scaleFilter.size() == 0) {
                    ExceptionGenerator.getException(false, 0, null, "EmptyScalesForFilterException", "sample");
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
                    ExceptionGenerator.getException(false, 0, null, "EmptyStatusForFilterException", "sample");
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
            toolFilter.setVisible(authViewModel.hasAccess());

            if (filterRecyclerViewAdapter.getValues().size() == 0) {
                filterLayout.setVisibility(View.GONE);
                toolFilter.setIcon(getResources().getDrawable(R.drawable.tool_filter_default));
            } else {
                if (filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    toolFilter.setIcon(getResources().getDrawable(R.drawable.tool_filter_active));
                }
            }
        } else if (method.equals("room")) {
            SampleRepository.rooms.clear();
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

                sampleViewModel.rooms(q);
            }
            observeWork(q);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchSamples() {
        try {
            sampleViewModel.samples(scale, status, room);
            SampleRepository.samplesPage = 1;
            observeWork(null);
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

    private void observeWork(String q) {
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
            } else if (SampleRepository.work.equals("getRooms")) {
                if (integer == 1) {
                    setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");

                    roomDialogProgressBar.setVisibility(View.GONE);
                    roomDialogImageView.setVisibility(View.VISIBLE);
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    roomDialogProgressBar.setVisibility(View.GONE);
                    roomDialogImageView.setVisibility(View.VISIBLE);
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    roomDialogProgressBar.setVisibility(View.GONE);
                    roomDialogImageView.setVisibility(View.VISIBLE);
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            }
        });
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
                                    filterRecyclerViewAdapter.getValues().set(i, model);
                                    filterRecyclerViewAdapter.getIds().set(i, model.get("id").toString());
                                    filterRecyclerViewAdapter.notifyItemChanged(i);

                                    filterRecyclerViewAdapter.notifyDataSetChanged();

                                    break;
                                }
                            }
                        }
                    } else if (scale.equals(model.get("id").toString())) {
                        scale = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                filterRecyclerViewAdapter.getValues().remove(model);
                                filterRecyclerViewAdapter.getIds().remove(model.get("id").toString());
                                filterRecyclerViewAdapter.notifyItemRemoved(i);
                                filterRecyclerViewAdapter.notifyItemChanged(i);

                                filterRecyclerViewAdapter.notifyDataSetChanged();

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
                                    filterRecyclerViewAdapter.getValues().set(i, roomModel);
                                    filterRecyclerViewAdapter.getIds().set(i, roomModel.get("id").toString());
                                    filterRecyclerViewAdapter.notifyItemChanged(i);

                                    filterRecyclerViewAdapter.notifyDataSetChanged();

                                    break;
                                }
                            }
                        }
                    } else if (room.equals(roomModel.get("id").toString())) {
                        room = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(roomModel.get("id").toString())) {
                                filterRecyclerViewAdapter.getValues().remove(roomModel);
                                filterRecyclerViewAdapter.getIds().remove(roomModel.get("id").toString());
                                filterRecyclerViewAdapter.notifyItemRemoved(i);
                                filterRecyclerViewAdapter.notifyItemChanged(i);

                                filterRecyclerViewAdapter.notifyDataSetChanged();

                                break;
                            }
                        }
                    }

                    relaunchSamples();

                    resetData("room");

                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                        inputEditText.getInput().getText().clear();

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
                                    filterRecyclerViewAdapter.getValues().set(i, model);
                                    filterRecyclerViewAdapter.getIds().set(i, model.get("id").toString());
                                    filterRecyclerViewAdapter.notifyItemChanged(i);

                                    filterRecyclerViewAdapter.notifyDataSetChanged();

                                    break;
                                }
                            }
                        }
                    } else if (status.equals(model.get("id").toString())) {
                        status = "";

                        for (int i = 0; i< filterRecyclerViewAdapter.getValues().size(); i++) {
                            if (filterRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                filterRecyclerViewAdapter.getValues().remove(model);
                                filterRecyclerViewAdapter.getIds().remove(model.get("id").toString());
                                filterRecyclerViewAdapter.notifyItemRemoved(i);
                                filterRecyclerViewAdapter.notifyItemChanged(i);

                                filterRecyclerViewAdapter.notifyDataSetChanged();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_samples, menu);

        toolCreate = menu.findItem(R.id.tool_create);
        toolCreate.setOnMenuItemClickListener(menuItem -> {
            if (finished) {
                if (pagingProgressBar.isShown()) {
                    loading = false;
                    pagingProgressBar.setVisibility(View.GONE);
                }
            }

            startActivityForResult(new Intent(this, CreateSampleActivity.class).putExtra("loaded", finished), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            return false;
        });

        toolFilter = menu.findItem(R.id.tool_filter);
        toolFilter.setOnMenuItemClickListener(item -> {
            filterDialog.show(this.getSupportFragmentManager(), "filterBottomSheet");
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