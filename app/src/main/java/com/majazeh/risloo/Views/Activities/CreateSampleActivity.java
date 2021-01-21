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
import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.CheckBoxAdapter;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SearchCaseAdapter;
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
    public SessionViewModel sessionViewModel;

    // Model
    private Model roomModel;

    // Adapters
    private SearchAdapter scaleDialogAdapter, roomDialogAdapter, referenceDialogAdapter, sessionDialogAdapter;
    private SearchCaseAdapter caseDialogAdapter;
    public SpinnerAdapter scaleRecyclerViewAdapter, referenceRecyclerViewAdapter;
    private CheckBoxAdapter caseRecyclerViewAdapter;

    // Vars
    public String roomId = "", roomName = "", roomTitle = "", caseId = "", caseName = "", count = "", sessionId = "", sessionName = "", clientId = "";
    private boolean scaleException = false, roomException = false, caseException = false, referenceException = false, sessionException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager scaleLayoutManager, referenceLayoutManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TabLayout typeTabLayout;
    private FrameLayout scaleFrameLayout, roomFrameLayout, caseFrameLayout, referenceFrameLayout, sessionFrameLayout;
    private LinearLayout roomLinearLayout, sessionLinearLayout;
    private LinearLayout referenceCaseLayout, referenceRoomLayout;
    public TextView scaleTextView, scaleCountTextView, roomNameTextView, roomTitleTextView, caseTextView, referenceTextView, referenceCountTextView, sessionNameTextView, sessionIdTextView, casesTextView;
    public EditText countEditText;
    private RecyclerView scaleRecyclerView, referenceRecyclerView, casesRecyclerView;
    private Button createButton;
    private Dialog scaleDialog, roomDialog, caseDialog, referenceDialog, sessionDialog, progressDialog;
    private TextView scaleDialogTitleTextView, roomDialogTitleTextView, caseDialogTitleTextView, referenceDialogTitleTextView, sessionDialogTitleTextView, scaleDialogConfirm, referenceDialogConfirm;;
    private CoordinatorLayout scaleDialogSearchLayout, roomDialogSearchLayout, caseDialogSearchLayout, referenceDialogSearchLayout, sessionDialogSearchLayout;
    private EditText scaleDialogEditText, roomDialogEditText, caseDialogEditText, referenceDialogEditText, sessionDialogEditText;
    private ImageView scaleDialogImageView, roomDialogImageView, caseDialogImageView, referenceDialogImageView, sessionDialogImageView;
    private ProgressBar scaleDialogProgressBar, roomDialogProgressBar, caseDialogProgressBar, referenceDialogProgressBar, sessionDialogProgressBar;
    private TextView scaleDialogTextView, roomDialogTextView, caseDialogTextView, referenceDialogTextView, sessionDialogTextView;
    private RecyclerView scaleDialogRecyclerView, roomDialogRecyclerView, caseDialogRecyclerView, referenceDialogRecyclerView, sessionDialogRecyclerView;

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
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        scaleDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);
        referenceDialogAdapter = new SearchAdapter(this);
        sessionDialogAdapter = new SearchAdapter(this);

        caseDialogAdapter = new SearchCaseAdapter(this);

        scaleRecyclerViewAdapter = new SpinnerAdapter(this);
        referenceRecyclerViewAdapter = new SpinnerAdapter(this);

        caseRecyclerViewAdapter = new CheckBoxAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        scaleLayoutManager = new FlexboxLayoutManager(this);
        scaleLayoutManager.setFlexDirection(FlexDirection.ROW);
        scaleLayoutManager.setFlexWrap(FlexWrap.WRAP);
        referenceLayoutManager = new FlexboxLayoutManager(this);
        referenceLayoutManager.setFlexDirection(FlexDirection.ROW);
        referenceLayoutManager.setFlexWrap(FlexWrap.WRAP);

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
        referenceFrameLayout = findViewById(R.id.activity_create_sample_reference_room_frameLayout);
        sessionFrameLayout = findViewById(R.id.activity_create_sample_session_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_create_sample_room_linearLayout);
        sessionLinearLayout = findViewById(R.id.activity_create_sample_session_linearLayout);

        referenceRoomLayout = findViewById(R.id.activity_create_sample_reference_roomLayout);
        referenceCaseLayout = findViewById(R.id.activity_create_sample_reference_caseLayout);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        scaleCountTextView = findViewById(R.id.activity_create_sample_scale_count_textView);
        roomNameTextView = findViewById(R.id.activity_create_sample_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_create_sample_room_title_textView);
        caseTextView = findViewById(R.id.activity_create_sample_case_textView);
        referenceTextView = findViewById(R.id.activity_create_sample_reference_room_textView);
        referenceCountTextView = findViewById(R.id.activity_create_sample_reference_count_textView);
        sessionNameTextView = findViewById(R.id.activity_create_sample_session_name_textView);
        sessionIdTextView = findViewById(R.id.activity_create_sample_session_id_textView);
        casesTextView = findViewById(R.id.activity_create_sample_reference_case_textView);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.setLayoutManager(scaleLayoutManager);
        scaleRecyclerView.setHasFixedSize(false);
        referenceRecyclerView = findViewById(R.id.activity_create_sample_reference_room_recyclerView);
        referenceRecyclerView.setLayoutManager(referenceLayoutManager);
        referenceRecyclerView.setHasFixedSize(false);
        casesRecyclerView = findViewById(R.id.activity_create_sample_reference_case_recyclerView);
        casesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._12sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        casesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        casesRecyclerView.setHasFixedSize(true);

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
        referenceDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(referenceDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        referenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        referenceDialog.setContentView(R.layout.dialog_search);
        referenceDialog.setCancelable(true);
        sessionDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(sessionDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        sessionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sessionDialog.setContentView(R.layout.dialog_search);
        sessionDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsScaleDialog = new WindowManager.LayoutParams();
        layoutParamsScaleDialog.copyFrom(scaleDialog.getWindow().getAttributes());
        layoutParamsScaleDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsScaleDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        scaleDialog.getWindow().setAttributes(layoutParamsScaleDialog);
        WindowManager.LayoutParams layoutParamsRoomDialog = new WindowManager.LayoutParams();
        layoutParamsRoomDialog.copyFrom(roomDialog.getWindow().getAttributes());
        layoutParamsRoomDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsRoomDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        roomDialog.getWindow().setAttributes(layoutParamsRoomDialog);
        WindowManager.LayoutParams layoutParamsCaseDialog = new WindowManager.LayoutParams();
        layoutParamsCaseDialog.copyFrom(caseDialog.getWindow().getAttributes());
        layoutParamsCaseDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsCaseDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        caseDialog.getWindow().setAttributes(layoutParamsCaseDialog);
        WindowManager.LayoutParams layoutParamsReferenceDialog = new WindowManager.LayoutParams();
        layoutParamsReferenceDialog.copyFrom(referenceDialog.getWindow().getAttributes());
        layoutParamsReferenceDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsReferenceDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        referenceDialog.getWindow().setAttributes(layoutParamsReferenceDialog);
        WindowManager.LayoutParams layoutParamsSessionDialog = new WindowManager.LayoutParams();
        layoutParamsSessionDialog.copyFrom(sessionDialog.getWindow().getAttributes());
        layoutParamsSessionDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsSessionDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        sessionDialog.getWindow().setAttributes(layoutParamsSessionDialog);

        scaleDialogTitleTextView = scaleDialog.findViewById(R.id.dialog_search_title_textView);
        scaleDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleScaleDialogTitle));
        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleRoomDialogTitle));
        caseDialogTitleTextView = caseDialog.findViewById(R.id.dialog_search_title_textView);
        caseDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleCaseDialogTitle));
        referenceDialogTitleTextView = referenceDialog.findViewById(R.id.dialog_search_title_textView);
        referenceDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleReferenceDialogTitle));
        sessionDialogTitleTextView = sessionDialog.findViewById(R.id.dialog_search_title_textView);
        sessionDialogTitleTextView.setText(getResources().getString(R.string.CreateSampleSessionDialogTitle));

        scaleDialogConfirm = scaleDialog.findViewById(R.id.dialog_search_confirm_textView);
        referenceDialogConfirm = referenceDialog.findViewById(R.id.dialog_search_confirm_textView);

        scaleDialogSearchLayout = scaleDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        scaleDialogSearchLayout.setVisibility(View.VISIBLE);
        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);
        caseDialogSearchLayout = caseDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        caseDialogSearchLayout.setVisibility(View.VISIBLE);
        referenceDialogSearchLayout = referenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        referenceDialogSearchLayout.setVisibility(View.VISIBLE);
        sessionDialogSearchLayout = sessionDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        sessionDialogSearchLayout.setVisibility(View.VISIBLE);

        scaleDialogEditText = scaleDialog.findViewById(R.id.dialog_search_editText);
        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        caseDialogEditText = caseDialog.findViewById(R.id.dialog_search_editText);
        referenceDialogEditText = referenceDialog.findViewById(R.id.dialog_search_editText);
        sessionDialogEditText = sessionDialog.findViewById(R.id.dialog_search_editText);

        scaleDialogImageView = scaleDialog.findViewById(R.id.dialog_search_imageView);
        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        caseDialogImageView = caseDialog.findViewById(R.id.dialog_search_imageView);
        referenceDialogImageView = referenceDialog.findViewById(R.id.dialog_search_imageView);
        sessionDialogImageView = sessionDialog.findViewById(R.id.dialog_search_imageView);

        scaleDialogProgressBar = scaleDialog.findViewById(R.id.dialog_search_progressBar);
        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);
        caseDialogProgressBar = caseDialog.findViewById(R.id.dialog_search_progressBar);
        referenceDialogProgressBar = referenceDialog.findViewById(R.id.dialog_search_progressBar);
        sessionDialogProgressBar = sessionDialog.findViewById(R.id.dialog_search_progressBar);

        scaleDialogTextView = scaleDialog.findViewById(R.id.dialog_search_textView);
        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);
        caseDialogTextView = caseDialog.findViewById(R.id.dialog_search_textView);
        referenceDialogTextView = referenceDialog.findViewById(R.id.dialog_search_textView);
        sessionDialogTextView = sessionDialog.findViewById(R.id.dialog_search_textView);

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

        referenceDialogRecyclerView = referenceDialog.findViewById(R.id.dialog_search_recyclerView);
        referenceDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        referenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        referenceDialogRecyclerView.setHasFixedSize(true);

        sessionDialogRecyclerView = sessionDialog.findViewById(R.id.dialog_search_recyclerView);
        sessionDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        sessionDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sessionDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            scaleDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            referenceDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

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
                        referenceRoomLayout.setVisibility(View.VISIBLE);
                        referenceCaseLayout.setVisibility(View.GONE);

                        resetData("case");

                        resetData("session");

                        resetData("caseReference");

                        break;
                    case 1:
                        referenceRoomLayout.setVisibility(View.GONE);
                        referenceCaseLayout.setVisibility(View.VISIBLE);

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

                setRecyclerView(sampleViewModel.getLocalScales(), scaleDialogRecyclerView, "getScales", "");

                scaleDialog.show();
            }
            return false;
        });

        roomLinearLayout.setOnClickListener(v -> {
            roomLinearLayout.setClickable(false);
            handler.postDelayed(() -> roomLinearLayout.setClickable(true), 250);

            if (roomException) {
                clearException("room");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            try {
                if (roomViewModel.getSuggestRoom().size() != 0) {
                    setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms", "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

        referenceRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (referenceException) {
                    clearException("reference");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                if (!roomId.isEmpty()) {
                    referenceDialog.show();
                } else {
                    ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException");
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        sessionLinearLayout.setOnClickListener(v -> {
            sessionLinearLayout.setClickable(false);
            handler.postDelayed(() -> sessionLinearLayout.setClickable(true), 250);

            if (sessionException) {
                clearException("session");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (!caseId.isEmpty()) {
                sessionDialog.show();
            } else {
                ExceptionGenerator.getException(false, 0, null, "SelectCaseFirstException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
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
                    referenceRecyclerView.setEnabled(false);
                    referenceRecyclerView.setClickable(false);
                    referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_border_quartz);
                } else if (countEditText.length() == 0) {
                    referenceRecyclerView.setEnabled(true);
                    referenceRecyclerView.setClickable(true);
                    referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
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
                errorException("scale");
            }
            if (roomId.isEmpty()) {
                errorException("room");
            }

            if (scaleRecyclerViewAdapter.getValues().size() != 0 && !roomId.equals("")) {
                clearException("scale");
                clearException("room");

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

        referenceDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!referenceDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(referenceDialogEditText);
                    controlEditText.select(referenceDialogEditText);
                }
            }
            return false;
        });

        sessionDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!sessionDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(sessionDialogEditText);
                    controlEditText.select(sessionDialogEditText);
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
                        setRecyclerView(sampleViewModel.getLocalScales(), scaleDialogRecyclerView, "getScales", "");

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
                        try {
                            if (roomViewModel.getSuggestRoom().size() != 0) {
                                setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms", "");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

        referenceDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (referenceDialogEditText.length() != 0) {
                        getData("getReferences", roomId, referenceDialogEditText.getText().toString().trim());
                    } else {
                        referenceDialogRecyclerView.setAdapter(null);

                        if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                            referenceDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sessionDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (sessionDialogEditText.length() != 0) {
                        getData("getSessions", caseId, sessionDialogEditText.getText().toString().trim());
                    } else {
                        sessionDialogRecyclerView.setAdapter(null);

                        if (sessionDialogTextView.getVisibility() == View.VISIBLE) {
                            sessionDialogTextView.setVisibility(View.GONE);
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

        referenceDialogConfirm.setOnClickListener(v -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
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

        referenceDialog.setOnCancelListener(dialog -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
        });

        sessionDialog.setOnCancelListener(dialog -> {
            resetData("sessionDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            sessionDialog.dismiss();
        });
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
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
        if (extras.getString("count") != null)
            count = extras.getString("count");
        if (extras.getString("session_id") != null)
            sessionId = extras.getString("session_id");
        if (extras.getString("session_name") != null)
            sessionName = sessionViewModel.getFAStatus(extras.getString("session_name"));
        if (extras.getString("client_id") != null)
            clientId = extras.getString("client_id");

        if (extras.getString("scales") != null) {
            try {
                JSONArray scales = new JSONArray(extras.getString("scales"));

                for (int j = 0; j < scales.length(); j++) {
                    JSONObject scale = (JSONObject) scales.get(j);
                    Model model = new Model(scale);

                    observeSearchAdapter(model, "getScales");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!roomId.equals("")) {
            roomNameTextView.setText(roomName);
            roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

            roomTitleTextView.setText(roomTitle);
            roomTitleTextView.setVisibility(View.VISIBLE);
        }

        if (!caseId.equals("")) {
            Objects.requireNonNull(typeTabLayout.getTabAt(1)).select();

            referenceRoomLayout.setVisibility(View.GONE);
            referenceCaseLayout.setVisibility(View.VISIBLE);

            caseTextView.setText(caseName);
            caseTextView.setTextColor(getResources().getColor(R.color.Grey));

            casesTextView.setVisibility(View.VISIBLE);
            casesRecyclerView.setVisibility(View.VISIBLE);

            if (extras.getString("clients") != null) {
                try {
                    ArrayList<Model> cases = new ArrayList<>();

                    JSONArray clients = new JSONArray(extras.getString("clients"));

                    for (int j = 0; j < clients.length(); j++) {
                        JSONObject client = (JSONObject) clients.get(j);

                        cases.add(new Model(client));
                    }

                    if (!clientId.equals("")) {
                        setRecyclerView(cases, casesRecyclerView, "cases", "single");
                    } else {
                        setRecyclerView(cases, casesRecyclerView, "cases", "all");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!sessionId.equals("")) {
            sessionNameTextView.setText(sessionName);
            sessionNameTextView.setTextColor(getResources().getColor(R.color.Grey));

            sessionIdTextView.setText(sessionId);
            sessionIdTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method, String checked) {
        switch (method) {
            case "scales":
                scaleRecyclerViewAdapter.setValues(scaleRecyclerViewAdapter.getValues(), scaleRecyclerViewAdapter.getIds(), method, "CreateSample");
                recyclerView.setAdapter(scaleRecyclerViewAdapter);
                break;
            case "references":
                referenceRecyclerViewAdapter.setValues(referenceRecyclerViewAdapter.getValues(), referenceRecyclerViewAdapter.getIds(), method, "CreateSample");
                recyclerView.setAdapter(referenceRecyclerViewAdapter);
                break;
            case "cases":
                caseRecyclerViewAdapter.setValues(arrayList, new ArrayList<>(), checked);
                recyclerView.setAdapter(caseRecyclerViewAdapter);
                break;
            case "getScales":
                scaleDialogAdapter.setValues(arrayList, method, "CreateSample");
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
                roomDialogAdapter.setValues(arrayList, method, "CreateSample");
                recyclerView.setAdapter(roomDialogAdapter);

                if (arrayList.size() == 0) {
                    roomDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                        roomDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getCases":
                caseDialogAdapter.setValues(arrayList, method, "CreateSample");
                recyclerView.setAdapter(caseDialogAdapter);

                if (arrayList.size() == 0) {
                    caseDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (caseDialogTextView.getVisibility() == View.VISIBLE) {
                        caseDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getReferences":
                referenceDialogAdapter.setValues(arrayList, method, "CreateSample");
                recyclerView.setAdapter(referenceDialogAdapter);

                if (arrayList.size() == 0) {
                    referenceDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                        referenceDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getSessions":
                sessionDialogAdapter.setValues(arrayList, method, "CreateSample");
                recyclerView.setAdapter(sessionDialogAdapter);

                if (arrayList.size() == 0) {
                    sessionDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (sessionDialogTextView.getVisibility() == View.VISIBLE) {
                        sessionDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
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
            case "reference":
                referenceException = true;
                if (typeTabLayout.getSelectedTabPosition() == 0) {
                    referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else if (typeTabLayout.getSelectedTabPosition() == 1) {
                    casesRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                }
                break;
            case "session":
                sessionException = true;
                sessionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "count":
                countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
            case "reference":
                referenceException = false;
                if (typeTabLayout.getSelectedTabPosition() == 0) {
                    referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                } else if (typeTabLayout.getSelectedTabPosition() == 1) {
                    casesRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
                break;
            case "session":
                sessionException = false;
                sessionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
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
            case "caseReference":
                if (caseRecyclerViewAdapter.getValues().size() != 0) {
                    casesTextView.setVisibility(View.GONE);
                    casesRecyclerView.setVisibility(View.GONE);

                    caseRecyclerViewAdapter.clearValues();
                    casesRecyclerView.setAdapter(null);
                }
                break;
            case "roomReference":
                if (countEditText.length() != 0) {
                    referenceRecyclerView.setEnabled(true);
                    referenceRecyclerView.setClickable(true);
                    referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                if (referenceRecyclerViewAdapter.getValues().size() != 0) {
                    referenceTextView.setVisibility(View.VISIBLE);

                    referenceRecyclerViewAdapter.clearValues();
                    referenceRecyclerView.setAdapter(null);
                }
                break;
            case "session":
                if (!sessionId.equals("")) {
                    sessionId = "";
                    sessionName = "";

                    sessionNameTextView.setText(getResources().getString(R.string.CreateSampleSession));
                    sessionNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

                    sessionIdTextView.setText(sessionId);
                    sessionIdTextView.setVisibility(View.GONE);
                }
                break;
            case "count":
                countEditText.setEnabled(!roomId.isEmpty());
                countEditText.setFocusableInTouchMode(!roomId.isEmpty());

                if (referenceRecyclerViewAdapter.getValues().size() != 0) {
                    countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                if (countEditText.length() != 0) {
                    count = "";

                    countEditText.getText().clear();
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
            case "referenceDialog":
                RoomRepository.references.clear();
                referenceDialogRecyclerView.setAdapter(null);

                if (referenceDialogConfirm.getVisibility() == View.VISIBLE) {
                    referenceDialogConfirm.setVisibility(View.GONE);
                }

                if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                    referenceDialogTextView.setVisibility(View.GONE);
                }
            case "sessionDialog":
                SessionRepository.sessions.clear();
                sessionDialogRecyclerView.setAdapter(null);

                if (sessionDialogTextView.getVisibility() == View.VISIBLE) {
                    sessionDialogTextView.setVisibility(View.GONE);
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
                    caseViewModel.cases(roomId, q, "case_dashboard");

                    observeWork("caseViewModel");
                    break;
                case "getReferences":
                    referenceDialogProgressBar.setVisibility(View.VISIBLE);
                    referenceDialogImageView.setVisibility(View.GONE);

                    roomViewModel.references(roomId, q);

                    observeWork("roomViewModel");
                    break;
                case "getSessions":
                    sessionDialogProgressBar.setVisibility(View.VISIBLE);
                    sessionDialogImageView.setVisibility(View.GONE);

                    sessionViewModel.SessionsOfCase(roomId, q);

                    observeWork("sessionViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        count = countEditText.getText().toString().trim();

        try {
            if (roomModel != null) {
                roomViewModel.addSuggestRoom(roomModel, 10);
            }

            progressDialog.show();

            sampleViewModel.create(scaleRecyclerViewAdapter.getIds(), roomId, caseId, referenceRecyclerViewAdapter.getIds(), caseRecyclerViewAdapter.getChecks(), count, sessionId);
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
                                setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales", "");

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
                            setRecyclerView(RoomRepository.rooms, roomDialogRecyclerView, "getRooms", "");

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
                            setRecyclerView(RoomRepository.references, referenceDialogRecyclerView, "getReferences", "");

                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
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
                            setRecyclerView(CaseRepository.cases, caseDialogRecyclerView, "getCases", "");

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

            case "sessionViewModel":
                SessionRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (SessionRepository.work.equals("getSessionsOfCase")) {
                        if (integer == 1) {
                            setRecyclerView(SessionRepository.sessions, sessionDialogRecyclerView, "getSessions", "");

                            sessionDialogProgressBar.setVisibility(View.GONE);
                            sessionDialogImageView.setVisibility(View.VISIBLE);
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            sessionDialogProgressBar.setVisibility(View.GONE);
                            sessionDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            sessionDialogProgressBar.setVisibility(View.GONE);
                            sessionDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
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
                errorException("reference");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("session_id")) {
                errorException("session");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("session_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("session_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("count")) {
                errorException("count");
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

                        setRecyclerView(null, scaleRecyclerView, "scales", "");
                    } else {
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
                        roomModel = model;
                        roomViewModel.addSuggestRoom(roomModel);

                        roomId = model.get("id").toString();

                        JSONObject manager = (JSONObject) model.get("manager");
                        roomName = manager.get("name").toString();

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

                    resetData("caseReference");

                    resetData("session");

                    resetData("count");

                    resetData("roomReference");

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
                        JSONArray clients = (JSONArray) model.get("clients");

                        for (int j = 0; j < clients.length(); j++) {
                            JSONObject client = (JSONObject) clients.get(j);
                            JSONObject user = (JSONObject) client.get("user");

                            cases.add(new Model(client));

                            if (j == clients.length() - 1) {
                                name.append(user.get("name").toString());
                            } else {
                                name.append(user.get("name").toString()).append(" - ");
                            }
                        }

                        caseName = name.toString();

                        caseTextView.setText(caseName);
                        caseTextView.setTextColor(getResources().getColor(R.color.Grey));

                        casesTextView.setVisibility(View.VISIBLE);
                        casesRecyclerView.setVisibility(View.VISIBLE);

                        setRecyclerView(cases, casesRecyclerView, "cases", "");
                    } else if (caseId.equals(model.get("id").toString())) {
                        caseId = "";
                        caseName = "";

                        caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        casesTextView.setVisibility(View.GONE);
                        casesRecyclerView.setVisibility(View.GONE);

                        caseRecyclerViewAdapter.clearValues();
                        casesRecyclerView.setAdapter(null);
                    }

                    resetData("session");

                    resetData("caseDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    caseDialog.dismiss();
                    break;

                case "getReferences":
                    int referencePosition = referenceRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (referencePosition == -1) {
                        referenceRecyclerViewAdapter.getValues().add(model);
                        referenceRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, referenceRecyclerView, "references", "");
                    } else {
                        referenceRecyclerViewAdapter.removeValue(referencePosition);

                        referenceDialogAdapter.notifyDataSetChanged();
                    }

                    if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                        referenceTextView.setVisibility(View.VISIBLE);

                        referenceCountTextView.setText("");
                        referenceCountTextView.setVisibility(View.GONE);

                        countEditText.setEnabled(true);
                        countEditText.setFocusableInTouchMode(true);
                        countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

                        referenceDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (referenceTextView.getVisibility() == View.VISIBLE) {
                            referenceTextView.setVisibility(View.GONE);
                        }

                        referenceCountTextView.setText(String.valueOf(referenceRecyclerViewAdapter.getValues().size()));
                        if (referenceCountTextView.getVisibility() == View.GONE) {
                            referenceCountTextView.setVisibility(View.VISIBLE);
                        }

                        countEditText.setEnabled(false);
                        countEditText.setFocusableInTouchMode(false);
                        countEditText.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_border_quartz);

                        if (referenceDialogConfirm.getVisibility() == View.GONE) {
                            referenceDialogConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                    case "getSessions":
                    if (!sessionId.equals(model.get("id").toString())) {
                        sessionId = model.get("id").toString();

                        String faStatus = sessionViewModel.getFAStatus(model.get("status").toString());

                        sessionName = faStatus;

                        sessionNameTextView.setText(sessionName);
                        sessionNameTextView.setTextColor(getResources().getColor(R.color.Grey));

                        sessionIdTextView.setText(sessionId);
                        sessionIdTextView.setVisibility(View.VISIBLE);
                    } else if (roomId.equals(model.get("id").toString())) {
                        sessionId = "";
                        sessionName = "";

                        sessionNameTextView.setText(getResources().getString(R.string.CreateSampleSession));
                        sessionNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        sessionIdTextView.setText(sessionId);
                        sessionIdTextView.setVisibility(View.GONE);
                    }

                    resetData("sessionDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    sessionDialog.dismiss();
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