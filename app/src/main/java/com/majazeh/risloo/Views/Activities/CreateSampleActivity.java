package com.majazeh.risloo.Views.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.CheckBoxAdapter;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CreateSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel sampleViewModel;
    private RoomViewModel roomViewModel;
    private CaseViewModel caseViewModel;

    // Adapters
    private SearchAdapter scaleDialogAdapter, roomDialogAdapter, caseDialogAdapter, roomReferenceDialogAdapter;
    public SpinnerAdapter scaleRecyclerViewAdapter, roomReferenceRecyclerViewAdapter;
    private CheckBoxAdapter caseReferenceRecyclerViewAdapter;

    // Vars
    public String roomId = "", roomName = "", roomTitle = "", caseId = "", caseName = "", count = "";
    private boolean scaleException = false, roomException = false, caseException = false, roomReferenceException = false, caseReferenceException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager scaleLayoutManager, roomReferenceLayoutManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TabLayout typeTabLayout;
    private FrameLayout scaleFrameLayout, roomFrameLayout, caseFrameLayout, roomReferenceFrameLayout;
    private LinearLayout roomLinearLayout;
    private LinearLayout caseLinearLayout, clinicLinearLayout;
    public TextView scaleTextView, scaleCountTextView, roomNameTextView, roomTitleTextView, caseTextView, roomReferenceTextView, caseReferenceTextView;
    public EditText countEditText;
    private RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView;
    private Button createButton;
    private Dialog scaleDialog, roomDialog, caseDialog, roomReferenceDialog, progressDialog;
    private TextView scaleDialogTitleTextView, roomDialogTitleTextView, caseDialogTitleTextView, roomReferenceDialogTitleTextView, scaleDialogConfirm, roomReferenceDialogConfirm;;
    private CoordinatorLayout scaleDialogSearchLayout, roomDialogSearchLayout, caseDialogSearchLayout, roomReferenceDialogSearchLayout;
    private EditText scaleDialogEditText, roomDialogEditText, caseDialogEditText, roomReferenceDialogEditText;
    private ImageView scaleDialogImageView, roomDialogImageView, caseDialogImageView, roomReferenceDialogImageView;
    private ProgressBar scaleDialogProgressBar, roomDialogProgressBar, caseDialogProgressBar, roomReferenceDialogProgressBar;
    private TextView scaleDialogTextView, roomDialogTextView, caseDialogTextView, roomReferenceDialogTextView;
    private RecyclerView scaleDialogRecyclerView, roomDialogRecyclerView, caseDialogRecyclerView, roomReferenceDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_sample);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        caseViewModel = new ViewModelProvider(this).get(CaseViewModel.class);

        scaleDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);
        caseDialogAdapter = new SearchAdapter(this);
        roomReferenceDialogAdapter = new SearchAdapter(this);
        scaleRecyclerViewAdapter = new SpinnerAdapter(this);
        roomReferenceRecyclerViewAdapter = new SpinnerAdapter(this);
        caseReferenceRecyclerViewAdapter = new CheckBoxAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        scaleLayoutManager = new FlexboxLayoutManager(this);
        scaleLayoutManager.setFlexDirection(FlexDirection.ROW);
        scaleLayoutManager.setFlexWrap(FlexWrap.WRAP);
        roomReferenceLayoutManager = new FlexboxLayoutManager(this);
        roomReferenceLayoutManager.setFlexDirection(FlexDirection.ROW);
        roomReferenceLayoutManager.setFlexWrap(FlexWrap.WRAP);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CreateSampleTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        scaleFrameLayout = findViewById(R.id.activity_create_sample_scale_frameLayout);
        roomFrameLayout = findViewById(R.id.activity_create_sample_room_frameLayout);
        caseFrameLayout = findViewById(R.id.activity_create_sample_case_frameLayout);
        roomReferenceFrameLayout = findViewById(R.id.activity_create_sample_room_reference_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_create_sample_room_linearLayout);

        clinicLinearLayout = findViewById(R.id.activity_create_sample_clinic_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_create_sample_case_linearLayout);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        scaleCountTextView = findViewById(R.id.activity_create_sample_scale_count_textView);
        roomNameTextView = findViewById(R.id.activity_create_sample_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_create_sample_room_title_textView);
        caseTextView = findViewById(R.id.activity_create_sample_case_textView);
        roomReferenceTextView = findViewById(R.id.activity_create_sample_room_reference_textView);
        caseReferenceTextView = findViewById(R.id.activity_create_sample_case_reference_textView);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.setLayoutManager(scaleLayoutManager);
        scaleRecyclerView.setHasFixedSize(false);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
        roomReferenceRecyclerView.setLayoutManager(roomReferenceLayoutManager);
        roomReferenceRecyclerView.setHasFixedSize(false);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._12sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);

        createButton = findViewById(R.id.activity_create_sample_button);

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
        caseDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(caseDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        caseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        caseDialog.setContentView(R.layout.dialog_search);
        caseDialog.setCancelable(true);
        roomReferenceDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(roomReferenceDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        roomReferenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomReferenceDialog.setContentView(R.layout.dialog_search);
        roomReferenceDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

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
        WindowManager.LayoutParams layoutParamsCase = new WindowManager.LayoutParams();
        layoutParamsCase.copyFrom(caseDialog.getWindow().getAttributes());
        layoutParamsCase.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsCase.height = WindowManager.LayoutParams.WRAP_CONTENT;
        caseDialog.getWindow().setAttributes(layoutParamsCase);
        WindowManager.LayoutParams layoutParamsRoomReference = new WindowManager.LayoutParams();
        layoutParamsRoomReference.copyFrom(roomReferenceDialog.getWindow().getAttributes());
        layoutParamsRoomReference.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsRoomReference.height = WindowManager.LayoutParams.WRAP_CONTENT;
        roomReferenceDialog.getWindow().setAttributes(layoutParamsRoomReference);

        scaleDialogTitleTextView = scaleDialog.findViewById(R.id.dialog_search_title_textView);
        scaleDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleScaleDialogTitle));
        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleRoomDialogTitle));
        caseDialogTitleTextView = caseDialog.findViewById(R.id.dialog_search_title_textView);
        caseDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleCaseDialogTitle));
        roomReferenceDialogTitleTextView = roomReferenceDialog.findViewById(R.id.dialog_search_title_textView);
        roomReferenceDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleRoomReferenceDialogTitle));

        scaleDialogConfirm = scaleDialog.findViewById(R.id.dialog_search_confirm_textView);
        roomReferenceDialogConfirm = roomReferenceDialog.findViewById(R.id.dialog_search_confirm_textView);

        scaleDialogSearchLayout = scaleDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        scaleDialogSearchLayout.setVisibility(View.VISIBLE);
        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);
        caseDialogSearchLayout = caseDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        caseDialogSearchLayout.setVisibility(View.VISIBLE);
        roomReferenceDialogSearchLayout = roomReferenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomReferenceDialogSearchLayout.setVisibility(View.VISIBLE);

        scaleDialogEditText = scaleDialog.findViewById(R.id.dialog_search_editText);
        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        caseDialogEditText = caseDialog.findViewById(R.id.dialog_search_editText);
        roomReferenceDialogEditText = roomReferenceDialog.findViewById(R.id.dialog_search_editText);

        scaleDialogImageView = scaleDialog.findViewById(R.id.dialog_search_imageView);
        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        caseDialogImageView = caseDialog.findViewById(R.id.dialog_search_imageView);
        roomReferenceDialogImageView = roomReferenceDialog.findViewById(R.id.dialog_search_imageView);

        scaleDialogProgressBar = scaleDialog.findViewById(R.id.dialog_search_progressBar);
        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);
        caseDialogProgressBar = caseDialog.findViewById(R.id.dialog_search_progressBar);
        roomReferenceDialogProgressBar = roomReferenceDialog.findViewById(R.id.dialog_search_progressBar);

        scaleDialogTextView = scaleDialog.findViewById(R.id.dialog_search_textView);
        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);
        caseDialogTextView = caseDialog.findViewById(R.id.dialog_search_textView);
        roomReferenceDialogTextView = roomReferenceDialog.findViewById(R.id.dialog_search_textView);

        scaleDialogRecyclerView = scaleDialog.findViewById(R.id.dialog_search_recyclerView);
        scaleDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        scaleDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scaleDialogRecyclerView.setHasFixedSize(true);

        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);

        caseDialogRecyclerView = caseDialog.findViewById(R.id.dialog_search_recyclerView);
        caseDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        caseDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseDialogRecyclerView.setHasFixedSize(true);

        roomReferenceDialogRecyclerView = roomReferenceDialog.findViewById(R.id.dialog_search_recyclerView);
        roomReferenceDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomReferenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomReferenceDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            scaleDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            roomReferenceDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        typeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(CreateSampleActivity.this, controlEditText.input());
                }

                switch (tab.getPosition()) {
                    case 0:
                        clinicLinearLayout.setVisibility(View.VISIBLE);
                        caseLinearLayout.setVisibility(View.GONE);

                        resetData("case");

                        resetData("caseReference");

                        break;
                    case 1:
                        clinicLinearLayout.setVisibility(View.GONE);
                        caseLinearLayout.setVisibility(View.VISIBLE);

                        resetData("count");

                        resetData("roomReference");

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        scaleRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (scaleException) {
                    clearException("scale");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                setRecyclerView(sampleViewModel.getLocalScales(), scaleDialogRecyclerView, "getScales");

                scaleDialog.show();
            }
            return false;
        });

        roomLinearLayout.setOnClickListener(v -> {
            roomLinearLayout.setClickable(false);
            handler.postDelayed(() -> roomLinearLayout.setClickable(true), 300);

            if (roomException) {
                clearException("room");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            roomDialog.show();
        });

        caseTextView.setOnClickListener(v -> {
            caseTextView.setClickable(false);
            handler.postDelayed(() -> caseTextView.setClickable(true), 300);

            if (caseException) {
                clearException("case");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (!roomId.isEmpty()) {
                caseDialog.show();
            } else {
                ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        });

        roomReferenceRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (roomReferenceException) {
                    clearException("roomReference");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                if (!roomId.isEmpty()) {
                    roomReferenceDialog.show();
                } else {
                    ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException");
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        countEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomId.isEmpty()) {
                    if (!countEditText.hasFocus()) {
                        if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                            controlEditText.clear(this, controlEditText.input());
                        }

                        controlEditText.focus(countEditText);
                        controlEditText.select(countEditText);
                    }
                } else {
                    ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException");
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (countEditText.length() == 1) {
                    roomReferenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_border_quartz);
                    roomReferenceRecyclerView.setEnabled(false);
                    roomReferenceRecyclerView.setClickable(false);
                } else if (countEditText.length() == 0) {
                    roomReferenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                    roomReferenceRecyclerView.setEnabled(true);
                    roomReferenceRecyclerView.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (scaleRecyclerViewAdapter.getValues().size() == 0) {
                errorView("scale");
            }
            if (roomId.isEmpty()) {
                errorView("room");
            }

            if (scaleException) {
                clearException("scale");
            }
            if (roomException) {
                clearException("room");
            }
            if (caseException) {
                clearException("case");
            }
            if (roomReferenceException) {
                clearException("roomReference");
            }
            if (caseReferenceException) {
                clearException("caseReference");
            }

            if (scaleRecyclerViewAdapter.getValues().size() != 0 && !roomId.isEmpty()) {
                doWork();
            }
        });

        scaleDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!scaleDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(scaleDialogEditText);
                    controlEditText.select(scaleDialogEditText);
                }
            }
            return false;
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

        caseDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!caseDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(caseDialogEditText);
                    controlEditText.select(caseDialogEditText);
                }
            }
            return false;
        });

        roomReferenceDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomReferenceDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(roomReferenceDialogEditText);
                    controlEditText.select(roomReferenceDialogEditText);
                }
            }
            return false;
        });

        scaleDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (scaleDialogEditText.length() != 0) {
                        getData("getScales", "", scaleDialogEditText.getText().toString().trim());
                    } else {
                        setRecyclerView(sampleViewModel.getLocalScales(), scaleDialogRecyclerView, "getScales");

                        if (scaleDialogTextView.getVisibility() == View.VISIBLE) {
                            scaleDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
                        getData("getRooms", "", roomDialogEditText.getText().toString().trim());
                    } else {
                        roomDialogRecyclerView.setAdapter(null);

                        if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                            roomDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        caseDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (caseDialogEditText.length() != 0) {
                        getData("getCases", roomId, caseDialogEditText.getText().toString().trim());
                    } else {
                        caseDialogRecyclerView.setAdapter(null);

                        if (caseDialogTextView.getVisibility() == View.VISIBLE) {
                            caseDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roomReferenceDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (roomReferenceDialogEditText.length() != 0) {
                        getData("getReferences", roomId, roomReferenceDialogEditText.getText().toString().trim());
                    } else {
                        roomReferenceDialogRecyclerView.setAdapter(null);

                        if (roomReferenceDialogTextView.getVisibility() == View.VISIBLE) {
                            roomReferenceDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scaleDialogConfirm.setOnClickListener(v -> {
            resetData("scaleDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            scaleDialog.dismiss();
        });

        roomReferenceDialogConfirm.setOnClickListener(v -> {
            resetData("roomReferenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            roomReferenceDialog.dismiss();
        });

        scaleDialog.setOnCancelListener(dialog -> {
            resetData("scaleDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            scaleDialog.dismiss();
        });

        roomDialog.setOnCancelListener(dialog -> {
            resetData("roomDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            roomDialog.dismiss();
        });

        caseDialog.setOnCancelListener(dialog -> {
            resetData("caseDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            caseDialog.dismiss();
        });

        roomReferenceDialog.setOnCancelListener(dialog -> {
            resetData("roomReferenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            roomReferenceDialog.dismiss();
        });
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("scale_id") != null && extras.getString("scale_title") != null) {
            try {
                Model model = new Model(new JSONObject().put("id", extras.getString("scale_id")).put("title", extras.getString("scale_title")));
                observeSearchAdapter(model, "getScales");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (extras.getString("room_id") != null)
            roomId = extras.getString("room_id");
        if (extras.getString("room_name") != null)
            roomName = extras.getString("room_name");
        if (extras.getString("room_title") != null)
            roomTitle = extras.getString("room_title");
        if (extras.getString("case_id") != null)
            caseId = extras.getString("case_id");
        if (extras.getString("case_name") != null)
            caseName = extras.getString("case_name");

        if (!roomId.equals("")) {
            roomNameTextView.setText(roomName);
            roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

            roomTitleTextView.setText(roomTitle);
            roomTitleTextView.setVisibility(View.VISIBLE);
        }

        if (!caseId.equals("")) {
            Objects.requireNonNull(typeTabLayout.getTabAt(1)).select();

            clinicLinearLayout.setVisibility(View.GONE);
            caseLinearLayout.setVisibility(View.VISIBLE);

            caseTextView.setText(caseName);
            caseTextView.setTextColor(getResources().getColor(R.color.Grey));

            caseReferenceTextView.setVisibility(View.VISIBLE);
            caseReferenceRecyclerView.setVisibility(View.VISIBLE);

            if (extras.getString("user_object") != null) {
                try {
                    ArrayList<Model> cases = new ArrayList<>();
                    JSONObject user = new JSONObject(extras.getString("user_object"));

                    cases.add(new Model(user));

                    setRecyclerView(cases, caseReferenceRecyclerView, "caseReferences");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "scales":
                scaleRecyclerViewAdapter.setValue(scaleRecyclerViewAdapter.getValues(), scaleRecyclerViewAdapter.getIds(), method, "CreateSample");
                recyclerView.setAdapter(scaleRecyclerViewAdapter);
                break;
            case "roomReferences":
                roomReferenceRecyclerViewAdapter.setValue(roomReferenceRecyclerViewAdapter.getValues(), roomReferenceRecyclerViewAdapter.getIds(), method, "CreateSample");
                recyclerView.setAdapter(roomReferenceRecyclerViewAdapter);
                break;
            case "caseReferences":
                caseReferenceRecyclerViewAdapter.setValue(arrayList, new ArrayList<>(), method, "CreateSample");
                recyclerView.setAdapter(caseReferenceRecyclerViewAdapter);
                break;
            case "getScales":
                scaleDialogAdapter.setValue(arrayList, method, "CreateSample");
                recyclerView.setAdapter(scaleDialogAdapter);

                if (arrayList.size() == 0) {
                    scaleDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (scaleDialogTextView.getVisibility() == View.VISIBLE) {
                        scaleDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getRooms":
                roomDialogAdapter.setValue(arrayList, method, "CreateSample");
                recyclerView.setAdapter(roomDialogAdapter);

                if (arrayList.size() == 0) {
                    roomDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                        roomDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getReferences":
                roomReferenceDialogAdapter.setValue(arrayList, method, "CreateSample");
                recyclerView.setAdapter(roomReferenceDialogAdapter);

                if (arrayList.size() == 0) {
                    roomReferenceDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (roomReferenceDialogTextView.getVisibility() == View.VISIBLE) {
                        roomReferenceDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getCases":
                caseDialogAdapter.setValue(arrayList, method, "CreateSample");
                recyclerView.setAdapter(caseDialogAdapter);

                if (arrayList.size() == 0) {
                    caseDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (caseDialogTextView.getVisibility() == View.VISIBLE) {
                        caseDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    private void errorView(String type) {
        if (type.equals("scale")) {
            scaleFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("room")) {
            roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "scale":
                scaleException = true;
                scaleFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "room":
                roomException = true;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "case":
                caseException = true;
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "roomReference":
                roomReferenceException = true;
                roomReferenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "caseReference":
                caseReferenceException = true;
                caseReferenceRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "scale":
                scaleException = false;
                scaleFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "room":
                roomException = false;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "case":
                caseException = false;
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "roomReference":
                roomReferenceException = false;
                roomReferenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "caseReference":
                caseReferenceException = false;
                caseReferenceRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "case":
                if (!caseId.equals("")) {
                    caseId = "";
                    caseName = "";

                    caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                    caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
                }
                break;
            case "count":
                if (roomReferenceRecyclerViewAdapter.getValues().size() != 0) {
                    countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                countEditText.setEnabled(true);
                countEditText.setFocusableInTouchMode(!roomId.isEmpty());

                if (countEditText.length() != 0) {
                    count = "";

                    countEditText.getText().clear();
                }
                break;
            case "roomReference":
                if (countEditText.length() != 0) {
                    roomReferenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                    roomReferenceRecyclerView.setEnabled(true);
                    roomReferenceRecyclerView.setClickable(true);
                }

                if (roomReferenceRecyclerViewAdapter.getValues().size() != 0) {
                    roomReferenceTextView.setVisibility(View.VISIBLE);

                    roomReferenceRecyclerView.setAdapter(null);

                    roomReferenceRecyclerViewAdapter.getValues().clear();
                    roomReferenceRecyclerViewAdapter.getIds().clear();
                    roomReferenceRecyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case "caseReference":
                if (caseReferenceRecyclerViewAdapter.getValues().size() != 0) {
                    caseReferenceTextView.setVisibility(View.GONE);
                    caseReferenceRecyclerView.setVisibility(View.GONE);

                    caseReferenceRecyclerView.setAdapter(null);

                    caseReferenceRecyclerViewAdapter.getValues().clear();
                    caseReferenceRecyclerViewAdapter.getChecks().clear();
                    caseReferenceRecyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case "scaleDialog":
                SampleRepository.scales.clear();
                scaleDialogRecyclerView.setAdapter(null);

                if (scaleDialogConfirm.getVisibility() == View.VISIBLE) {
                    scaleDialogConfirm.setVisibility(View.GONE);
                }

                if (scaleDialogTextView.getVisibility() == View.VISIBLE) {
                    scaleDialogTextView.setVisibility(View.GONE);
                }
                break;
            case "roomDialog":
                RoomRepository.rooms.clear();
                roomDialogRecyclerView.setAdapter(null);

                if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                    roomDialogTextView.setVisibility(View.GONE);
                }
                break;
            case "caseDialog":
                CaseRepository.cases.clear();
                caseDialogRecyclerView.setAdapter(null);

                if (caseDialogTextView.getVisibility() == View.VISIBLE) {
                    caseDialogTextView.setVisibility(View.GONE);
                }
                break;
            case "roomReferenceDialog":
                RoomRepository.references.clear();
                roomReferenceDialogRecyclerView.setAdapter(null);

                if (roomReferenceDialogConfirm.getVisibility() == View.VISIBLE) {
                    roomReferenceDialogConfirm.setVisibility(View.GONE);
                }

                if (roomReferenceDialogTextView.getVisibility() == View.VISIBLE) {
                    roomReferenceDialogTextView.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getData(String method, String roomId, String q) {
        try {
            switch (method) {
                case "getScales":
                    scaleDialogProgressBar.setVisibility(View.VISIBLE);
                    scaleDialogImageView.setVisibility(View.GONE);

                    SampleRepository.scalesPage = 1;
                    sampleViewModel.scales(q);

                    observeWork("sampleViewModel");
                    break;
                case "getRooms":
                    roomDialogProgressBar.setVisibility(View.VISIBLE);
                    roomDialogImageView.setVisibility(View.GONE);

                    RoomRepository.allPage = 1;
                    roomViewModel.rooms(q);

                    observeWork("roomViewModel");
                    break;
                case "getCases":
                    caseDialogProgressBar.setVisibility(View.VISIBLE);
                    caseDialogImageView.setVisibility(View.GONE);

                    CaseRepository.page = 1;
                    caseViewModel.cases(roomId, q);

                    observeWork("caseViewModel");
                    break;
                case "getReferences":
                    roomReferenceDialogProgressBar.setVisibility(View.VISIBLE);
                    roomReferenceDialogImageView.setVisibility(View.GONE);

                    roomViewModel.references(roomId, q);

                    observeWork("roomViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        count = countEditText.getText().toString().trim();

        try {
            progressDialog.show();
            sampleViewModel.create(scaleRecyclerViewAdapter.getIds(), roomId, caseId, roomReferenceRecyclerViewAdapter.getIds(), caseReferenceRecyclerViewAdapter.getChecks(), count);
            observeWork("sampleViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "sampleViewModel":
                SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
                    switch (SampleRepository.work) {
                        case "create":
                            if (integer == 1) {
                                setResult(RESULT_OK, null);
                                finish();

                                progressDialog.dismiss();
                                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            } else if (integer == 0) {
                                progressDialog.dismiss();
                                observeException();
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            } else if (integer == -2) {
                                progressDialog.dismiss();
                                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            }
                            break;
                        case "getScales":
                            if (integer == 1) {
                                setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales");

                                scaleDialogProgressBar.setVisibility(View.GONE);
                                scaleDialogImageView.setVisibility(View.VISIBLE);
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            } else if (integer == 0) {
                                scaleDialogProgressBar.setVisibility(View.GONE);
                                scaleDialogImageView.setVisibility(View.VISIBLE);
                                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            } else if (integer == -2) {
                                scaleDialogProgressBar.setVisibility(View.GONE);
                                scaleDialogImageView.setVisibility(View.VISIBLE);
                                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                                SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                            }
                            break;
                    }
                });
                break;

            case "roomViewModel":
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
                    } else if (RoomRepository.work.equals("getReferences")) {
                        if (integer == 1) {
                            setRecyclerView(RoomRepository.references, roomReferenceDialogRecyclerView, "getReferences");

                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;

            case "caseViewModel":
                CaseRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (CaseRepository.work.equals("getAll")) {
                        if (integer == 1) {
                            setRecyclerView(CaseRepository.cases, caseDialogRecyclerView, "getCases");

                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;
        }
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("scale_id")) {
                errorException("scale");
                exceptionToast = ExceptionGenerator.getErrorBody("scale_id");
            }
            if (!ExceptionGenerator.errors.isNull("room_id")) {
                errorException("room");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("room_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("room_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("case_id")) {
                errorException("case");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("case_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("case_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("client_id")) {
                if (typeTabLayout.getSelectedTabPosition() == 0) {
                    errorException("roomReference");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                    } else {
                        exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                    }
                } else if (typeTabLayout.getSelectedTabPosition() == 1) {
                    errorException("caseReference");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                    } else {
                        exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                    }
                }
            }
            if (!ExceptionGenerator.errors.isNull("count")) {
                countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("count");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("count"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getScales":
                    int scalePosition = scaleRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (scalePosition == -1) {
                        scaleRecyclerViewAdapter.getValues().add(model);
                        scaleRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, scaleRecyclerView, "scales");
                    } else if (scalePosition != -1) {
                        scaleRecyclerViewAdapter.removeValue(scalePosition);

                        scaleDialogAdapter.notifyDataSetChanged();
                    }

                    if (scaleRecyclerViewAdapter.getValues().size() == 0) {
                        scaleTextView.setVisibility(View.VISIBLE);

                        scaleCountTextView.setText("");
                        scaleCountTextView.setVisibility(View.GONE);

                        scaleDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (scaleTextView.getVisibility() == View.VISIBLE) {
                            scaleTextView.setVisibility(View.GONE);
                        }

                        scaleCountTextView.setText(String.valueOf(scaleRecyclerViewAdapter.getValues().size()));
                        if (scaleCountTextView.getVisibility() == View.GONE) {
                            scaleCountTextView.setVisibility(View.VISIBLE);
                        }

                        if (scaleDialogConfirm.getVisibility() == View.GONE) {
                            scaleDialogConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case "getRooms":
                    if (!roomId.equals(model.get("id").toString())) {
                        roomId = model.get("id").toString();

                        JSONObject manager = (JSONObject) model.get("manager");
                        roomName =manager.get("name").toString();

                        roomNameTextView.setText(roomName);
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

                        JSONObject center = (JSONObject) model.get("center");
                        JSONObject detail = (JSONObject) center.get("detail");
                        roomTitle =detail.get("title").toString();

                        roomTitleTextView.setText(roomTitle);
                        roomTitleTextView.setVisibility(View.VISIBLE);
                    } else if (roomId.equals(model.get("id").toString())) {
                        roomId = "";
                        roomName = "";
                        roomTitle = "";

                        roomNameTextView.setText(getResources().getString(R.string.CreateSampleRoom));
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        roomTitleTextView.setText(roomTitle);
                        roomTitleTextView.setVisibility(View.GONE);
                    }

                    resetData("case");

                    resetData("count");

                    resetData("roomReference");

                    resetData("caseReference");

                    resetData("roomDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    roomDialog.dismiss();
                    break;

                case "getCases":
                    if (!caseId.equals(model.get("id").toString())) {
                        caseId = model.get("id").toString();

                        ArrayList<Model> cases = new ArrayList<>();

                        StringBuilder name = new StringBuilder();
                        JSONArray client = (JSONArray) model.get("clients");

                        for (int j = 0; j < client.length(); j++) {
                            JSONObject object = client.getJSONObject(j);
                            JSONObject user = object.getJSONObject("user");

                            cases.add(new Model(user));

                            if (j == client.length() - 1)
                                name.append(user.getString("name"));
                            else
                                name.append(user.getString("name")).append(" - ");
                        }

                        if (!name.toString().equals("")) {
                            JSONObject casse = new JSONObject().put("name", name);
                            caseName = casse.get("name").toString();

                            caseTextView.setText(caseName);
                            caseTextView.setTextColor(getResources().getColor(R.color.Grey));

                            caseReferenceTextView.setVisibility(View.VISIBLE);
                            caseReferenceRecyclerView.setVisibility(View.VISIBLE);

                            setRecyclerView(cases, caseReferenceRecyclerView, "caseReferences");
                        }
                    } else if (caseId.equals(model.get("id").toString())) {
                        caseId = "";
                        caseName = "";

                        caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        caseReferenceTextView.setVisibility(View.GONE);
                        caseReferenceRecyclerView.setVisibility(View.GONE);

                        caseReferenceRecyclerView.setAdapter(null);

                        caseReferenceRecyclerViewAdapter.getValues().clear();
                        caseReferenceRecyclerViewAdapter.getChecks().clear();
                        caseReferenceRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    resetData("caseDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    caseDialog.dismiss();
                    break;

                case "getReferences":
                    int roomReferencePosition = roomReferenceRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (roomReferencePosition == -1) {
                        roomReferenceRecyclerViewAdapter.getValues().add(model);
                        roomReferenceRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, roomReferenceRecyclerView, "roomReferences");
                    } else if (roomReferencePosition != -1) {
                        roomReferenceRecyclerViewAdapter.removeValue(roomReferencePosition);

                        roomReferenceDialogAdapter.notifyDataSetChanged();
                    }

                    if (roomReferenceRecyclerViewAdapter.getValues().size() == 0) {
                        roomReferenceTextView.setVisibility(View.VISIBLE);

                        countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                        countEditText.setEnabled(true);
                        countEditText.setFocusableInTouchMode(true);

                        roomReferenceDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (roomReferenceTextView.getVisibility() == View.VISIBLE) {
                            roomReferenceTextView.setVisibility(View.GONE);
                        }

                        countEditText.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_border_quartz);
                        countEditText.setEnabled(false);
                        countEditText.setFocusableInTouchMode(false);

                        if (roomReferenceDialogConfirm.getVisibility() == View.GONE) {
                            roomReferenceDialogConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}