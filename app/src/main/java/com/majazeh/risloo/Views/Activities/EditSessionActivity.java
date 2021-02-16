package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Widgets.SingleNumberPicker;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class EditSessionActivity extends AppCompatActivity {

    // ViewModels
    private SessionViewModel sessionViewModel;
    private RoomViewModel roomViewModel;
    private CaseViewModel caseViewModel;

    // Model
    private Model roomModel;

    // Adapters
    private SearchAdapter roomDialogAdapter, caseDialogAdapter, statusDialogAdapter;

    // Vars
    public String id = "", roomId = "", roomName = "", roomTitle = "", caseId = "", caseName = "", timestamp = "", startedAtTime = "", startedAtDate = "", duration = "", statusId = "", statusTitle = "";
    private boolean roomException = false, caseException = false, startedAtException = false, statusException = false;
    private int hour, minute, year, month, day;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout roomFrameLayout, caseFrameLayout, statusFrameLayout;
    private LinearLayout roomLinearLayout;
    public TextView roomNameTextView, roomTitleTextView, caseTextView, startedAtTimeTextView, startedAtDateTextView, statusTextView;
    private EditText durationEditText;
    private Button editButton;
    private Dialog roomDialog, caseDialog, startedAtTimeDialog, startedAtDateDialog, statusDialog, progressDialog;
    private TextView roomDialogTitleTextView, caseDialogTitleTextView, startedAtDateDialogTitleTextView, statusDialogTitleTextView;
    private CoordinatorLayout roomDialogSearchLayout, caseDialogSearchLayout;
    private EditText roomDialogEditText, caseDialogEditText;
    private ImageView roomDialogImageView, caseDialogImageView;
    private ProgressBar roomDialogProgressBar, caseDialogProgressBar;
    private TextView roomDialogTextView, caseDialogTextView, startedAtTimeDialogPositive, startedAtTimeDialogNegative, startedAtDateDialogPositive, startedAtDateDialogNegative;
    private RecyclerView roomDialogRecyclerView, caseDialogRecyclerView, statusDialogRecyclerView;
    private SingleNumberPicker hourNumberPicker, minuteNumberPicker, yearNumberPicker, monthNumberPicker, dayNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_session);

        initializer();

        detector();

        listener();

        setData();

        setCustomPicker();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        caseViewModel = new ViewModelProvider(this).get(CaseViewModel.class);

        roomDialogAdapter = new SearchAdapter(this);
        caseDialogAdapter = new SearchAdapter(this);
        statusDialogAdapter = new SearchAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.EditSessionTitle));

        roomFrameLayout = findViewById(R.id.activity_edit_session_room_frameLayout);
        caseFrameLayout = findViewById(R.id.activity_edit_session_case_frameLayout);
        statusFrameLayout = findViewById(R.id.activity_edit_session_status_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_edit_session_room_linearLayout);

        roomNameTextView = findViewById(R.id.activity_edit_session_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_edit_session_room_title_textView);
        caseTextView = findViewById(R.id.activity_edit_session_case_textView);
        startedAtTimeTextView = findViewById(R.id.activity_edit_session_started_at_time_textView);
        startedAtDateTextView = findViewById(R.id.activity_edit_session_started_at_date_textView);
        statusTextView = findViewById(R.id.activity_edit_session_status_textView);

        durationEditText = findViewById(R.id.activity_edit_session_duration_editText);

        editButton = findViewById(R.id.activity_edit_session_button);

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
        startedAtTimeDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(startedAtTimeDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        startedAtTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startedAtTimeDialog.setContentView(R.layout.dialog_time);
        startedAtTimeDialog.setCancelable(true);
        startedAtDateDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(startedAtDateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        startedAtDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startedAtDateDialog.setContentView(R.layout.dialog_date);
        startedAtDateDialog.setCancelable(true);
        statusDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(statusDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        statusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        statusDialog.setContentView(R.layout.dialog_search);
        statusDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

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
        WindowManager.LayoutParams layoutParamsTimeDialog = new WindowManager.LayoutParams();
        layoutParamsTimeDialog.copyFrom(startedAtTimeDialog.getWindow().getAttributes());
        layoutParamsTimeDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsTimeDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        startedAtTimeDialog.getWindow().setAttributes(layoutParamsTimeDialog);
        WindowManager.LayoutParams layoutParamsDateDialog = new WindowManager.LayoutParams();
        layoutParamsDateDialog.copyFrom(startedAtDateDialog.getWindow().getAttributes());
        layoutParamsDateDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsDateDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        startedAtDateDialog.getWindow().setAttributes(layoutParamsDateDialog);
        WindowManager.LayoutParams layoutParamsStatusDialog = new WindowManager.LayoutParams();
        layoutParamsStatusDialog.copyFrom(statusDialog.getWindow().getAttributes());
        layoutParamsStatusDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsStatusDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        statusDialog.getWindow().setAttributes(layoutParamsStatusDialog);

        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.EditSessionRoomDialogTitle));
        caseDialogTitleTextView = caseDialog.findViewById(R.id.dialog_search_title_textView);
        caseDialogTitleTextView.setText(getResources().getString(R.string.EditSessionCaseDialogTitle));
        startedAtDateDialogTitleTextView = startedAtDateDialog.findViewById(R.id.dialog_date_title_textView);
        startedAtDateDialogTitleTextView.setText(getResources().getString(R.string.EditSessionDateDialogTitle));
        statusDialogTitleTextView = statusDialog.findViewById(R.id.dialog_search_title_textView);
        statusDialogTitleTextView.setText(getResources().getString(R.string.EditSessionStatusDialogTitle));

        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);
        caseDialogSearchLayout = caseDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        caseDialogSearchLayout.setVisibility(View.VISIBLE);

        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        caseDialogEditText = caseDialog.findViewById(R.id.dialog_search_editText);

        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        caseDialogImageView = caseDialog.findViewById(R.id.dialog_search_imageView);

        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);
        caseDialogProgressBar = caseDialog.findViewById(R.id.dialog_search_progressBar);

        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);
        caseDialogTextView = caseDialog.findViewById(R.id.dialog_search_textView);

        hourNumberPicker = startedAtTimeDialog.findViewById(R.id.dialog_time_hour_NumberPicker);
        minuteNumberPicker = startedAtTimeDialog.findViewById(R.id.dialog_time_minute_NumberPicker);
        yearNumberPicker = startedAtDateDialog.findViewById(R.id.dialog_date_year_NumberPicker);
        monthNumberPicker = startedAtDateDialog.findViewById(R.id.dialog_date_month_NumberPicker);
        dayNumberPicker = startedAtDateDialog.findViewById(R.id.dialog_date_day_NumberPicker);

        startedAtTimeDialogPositive = startedAtTimeDialog.findViewById(R.id.dialog_time_positive_textView);
        startedAtTimeDialogNegative = startedAtTimeDialog.findViewById(R.id.dialog_time_negative_textView);
        startedAtDateDialogPositive = startedAtDateDialog.findViewById(R.id.dialog_date_positive_textView);
        startedAtDateDialogNegative = startedAtDateDialog.findViewById(R.id.dialog_date_negative_textView);

        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);

        caseDialogRecyclerView = caseDialog.findViewById(R.id.dialog_search_recyclerView);
        caseDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        caseDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseDialogRecyclerView.setHasFixedSize(true);

        statusDialogRecyclerView = statusDialog.findViewById(R.id.dialog_search_recyclerView);
        statusDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        statusDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        statusDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            startedAtTimeDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            startedAtTimeDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            startedAtDateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            startedAtDateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

//        roomLinearLayout.setOnClickListener(v -> {
//            roomLinearLayout.setClickable(false);
//            handler.postDelayed(() -> roomLinearLayout.setClickable(true), 250);
//
//            if (roomException) {
//                clearException("room");
//            }
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//            }
//
//            try {
//                if (roomViewModel.getSuggestRoom().size() != 0) {
//                    setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            roomDialog.show();
//        });
//
//        caseTextView.setOnClickListener(v -> {
//            caseTextView.setClickable(false);
//            handler.postDelayed(() -> caseTextView.setClickable(true), 250);
//
//            if (caseException) {
//                clearException("case");
//            }
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//            }
//
//            if (!roomId.isEmpty()) {
//                caseDialog.show();
//            } else {
//                ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException");
//                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//            }
//        });

        startedAtTimeTextView.setOnTouchListener((v, event) -> {
            startedAtTimeTextView.setClickable(false);
            handler.postDelayed(() -> startedAtTimeTextView.setClickable(true), 250);

            if (startedAtException) {
                clearException("startedAt");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            startedAtTimeDialog.show();
            return false;
        });

        startedAtDateTextView.setOnTouchListener((v, event) -> {
            startedAtDateTextView.setClickable(false);
            handler.postDelayed(() -> startedAtDateTextView.setClickable(true), 250);

            if (startedAtException) {
                clearException("startedAt");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            startedAtDateDialog.show();
            return false;
        });

        durationEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!durationEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(durationEditText);
                    controlEditText.select(durationEditText);
                }
            }
            return false;
        });

        statusTextView.setOnClickListener(v -> {
            statusTextView.setClickable(false);
            handler.postDelayed(() -> statusTextView.setClickable(true), 250);

            if (statusException) {
                clearException("status");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            setRecyclerView(sessionViewModel.getLocalSessionStatus(), statusDialogRecyclerView, "getStatus");

            statusDialog.show();
        });

        editButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (roomId.isEmpty()) {
                errorException("room");
            }
            if (caseId.isEmpty()) {
                errorException("case");
            }
            if (durationEditText.length() == 0) {
                controlEditText.error(this, durationEditText);
            }
            if (statusId.isEmpty()) {
                errorException("status");
            }

            if (!roomId.isEmpty() && !caseId.isEmpty() && durationEditText.length() != 0 && !statusId.isEmpty()) {
                clearException("room");
                clearException("case");
                controlEditText.clear(this, durationEditText);
                clearException("status");

                doWork();
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
                                setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms");
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

        startedAtTimeDialogPositive.setOnClickListener(v -> {
            startedAtTimeDialogPositive.setClickable(false);
            handler.postDelayed(() -> startedAtTimeDialogPositive.setClickable(true), 250);
            startedAtTimeDialog.dismiss();

            hour = hourNumberPicker.getValue();
            minute = minuteNumberPicker.getValue();

            if (hour < 10) {
                if (minute < 10)
                    startedAtTime = "0" + hour + ":" + "0" + minute;
                else
                    startedAtTime = "0" + hour + ":" + minute;
            } else {
                if (minute < 10)
                    startedAtTime = hour + ":" + "0" + minute;
                else
                    startedAtTime = hour + ":" + minute;
            }

            startedAtTimeTextView.setText(startedAtTime);
        });

        startedAtTimeDialogNegative.setOnClickListener(v -> {
            startedAtTimeDialogNegative.setClickable(false);
            handler.postDelayed(() -> startedAtTimeDialogNegative.setClickable(true), 250);
            startedAtTimeDialog.dismiss();

            hourNumberPicker.setValue(hour);
            minuteNumberPicker.setValue(minute);
        });

        startedAtDateDialogPositive.setOnClickListener(v -> {
            startedAtDateDialogPositive.setClickable(false);
            handler.postDelayed(() -> startedAtDateDialogPositive.setClickable(true), 250);
            startedAtDateDialog.dismiss();

            year = yearNumberPicker.getValue();
            month = monthNumberPicker.getValue();
            day = dayNumberPicker.getValue();

            if (month < 10) {
                if (day < 10)
                    startedAtDate = year + "-" + "0" + month + "-" + "0" + day;
                else
                    startedAtDate = year + "-" + "0" + month + "-" + day;
            } else {
                if (day < 10)
                    startedAtDate = year + "-" + month + "-" + "0" + day;
                else
                    startedAtDate = year + "-" + month + "-" + day;
            }

            startedAtDateTextView.setText(startedAtDate);
        });

        startedAtDateDialogNegative.setOnClickListener(v -> {
            startedAtDateDialogNegative.setClickable(false);
            handler.postDelayed(() -> startedAtDateDialogNegative.setClickable(true), 250);
            startedAtDateDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
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

        statusDialog.setOnCancelListener(dialog -> statusDialog.dismiss());

        startedAtTimeDialog.setOnCancelListener(dialog -> {
            startedAtTimeDialog.dismiss();

            hourNumberPicker.setValue(hour);
            minuteNumberPicker.setValue(minute);
        });

        startedAtDateDialog.setOnCancelListener(dialog -> {
            startedAtDateDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });
    }

    private void setData() {
        if (extras.getString("id") != null)
            id = extras.getString("id");
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
        if (extras.getString("started_at_time") != null)
            startedAtTime = extras.getString("started_at_time");
        else
            startedAtTime = DateManager.currentTime();
        if (extras.getString("started_at_date") != null)
            startedAtDate = extras.getString("started_at_date");
        else
            startedAtDate = DateManager.currentJalaliDate();
        if (extras.getString("duration") != null)
            duration = extras.getString("duration");
        if (extras.getString("en_status") != null)
            statusId = extras.getString("en_status");
        if (extras.getString("fa_status") != null)
            statusTitle = extras.getString("fa_status");

        if (!roomId.equals("")) {
            roomNameTextView.setText(roomName);
            roomNameTextView.setTextColor(getResources().getColor(R.color.Gray700));

            roomTitleTextView.setText(roomTitle);
            roomTitleTextView.setVisibility(View.VISIBLE);
        }

        if (!caseId.equals("")) {
            caseTextView.setText(caseName);
            caseTextView.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (!duration.equals("")) {
            durationEditText.setText(duration);
            durationEditText.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (!statusId.equals("")) {
            statusTextView.setText(statusTitle);
            statusTextView.setTextColor(getResources().getColor(R.color.Gray700));
        }

        startedAtTimeTextView.setText(startedAtTime);

        hour = Integer.parseInt(DateManager.dateToString("HH", DateManager.stringToDate("HH:mm", startedAtTime)));
        minute = Integer.parseInt(DateManager.dateToString("mm", DateManager.stringToDate("HH:mm", startedAtTime)));

        startedAtDateTextView.setText(startedAtDate);

        year = Integer.parseInt(DateManager.dateToString("yyyy", DateManager.stringToDate("yyyy-MM-dd", startedAtDate)));
        month = Integer.parseInt(DateManager.dateToString("MM", DateManager.stringToDate("yyyy-MM-dd", startedAtDate)));
        day = Integer.parseInt(DateManager.dateToString("dd", DateManager.stringToDate("yyyy-MM-dd", startedAtDate)));
    }

    private void setCustomPicker() {
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(23);
        hourNumberPicker.setValue(hour);

        minuteNumberPicker.setMinValue(0);
        minuteNumberPicker.setMaxValue(59);
        minuteNumberPicker.setValue(minute);

        yearNumberPicker.setMinValue(1300);
        yearNumberPicker.setMaxValue(2100);
        yearNumberPicker.setValue(year);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(month);
        monthNumberPicker.setDisplayedValues(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"});

        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        dayNumberPicker.setValue(day);
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getRooms":
                roomDialogAdapter.setValues(arrayList, method, "EditSession");
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
                caseDialogAdapter.setValues(arrayList, method, "EditSession");
                recyclerView.setAdapter(caseDialogAdapter);

                if (arrayList.size() == 0) {
                    caseDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (caseDialogTextView.getVisibility() == View.VISIBLE) {
                        caseDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getStatus":
                statusDialogAdapter.setValues(arrayList, method, "EditSession");
                recyclerView.setAdapter(statusDialogAdapter);
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "room":
                roomException = true;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "case":
                caseException = true;
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "startedAt":
                startedAtException = true;
                startedAtTimeTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                startedAtDateTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "duration":
                durationEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "status":
                statusException = true;
                statusFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "room":
                roomException = false;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "case":
                caseException = false;
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "startedAt":
                startedAtException = false;
                startedAtTimeTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                startedAtDateTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "status":
                statusException = false;
                statusFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "case":
                if (!caseId.equals("")) {
                    caseId = "";
                    caseName = "";

                    caseTextView.setText(getResources().getString(R.string.EditSessionCase));
                    caseTextView.setTextColor(getResources().getColor(R.color.Gray300));
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
        }
    }

    private void getData(String method, String roomId, String q) {
        try {
            switch (method) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        timestamp = String.valueOf(DateManager.dateToTimestamp(DateManager.stringToDate("yyyy-MM-dd HH:mm", DateManager.jalaliToGregorian(startedAtDate) + " " + startedAtTime)));
        duration = durationEditText.getText().toString().trim();

        try {
            if (roomModel != null) {
                roomViewModel.addSuggestRoom(roomModel, 10);
            }

            progressDialog.show();

            sessionViewModel.update(id, caseId, timestamp, duration, statusId);
            observeWork("sessionViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "sessionViewModel":
                SessionRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (SessionRepository.work.equals("update")) {
                        if (integer == 1) {
                            setResult(RESULT_OK, null);
                            finish();

                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            progressDialog.dismiss();
                            observeException();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        }
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
        if (ExceptionGenerator.current_exception.equals("update")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("roomId")) {
                errorException("room");
                exceptionToast = ExceptionGenerator.getErrorBody("roomId");
            }
            if (!ExceptionGenerator.errors.isNull("case_id")) {
                errorException("case");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("case_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("case_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("started_at")) {
                errorException("startedAt");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("started_at");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("started_at"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("duration")) {
                errorException("duration");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("duration");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("duration"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("status")) {
                errorException("status");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("status");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("status"));
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
                case "getRooms":
                    if (!roomId.equals(model.get("id").toString())) {
                        roomModel = model;
                        roomViewModel.addSuggestRoom(roomModel);

                        roomId = model.get("id").toString();

                        JSONObject manager = (JSONObject) model.get("manager");
                        roomName =manager.get("name").toString();

                        roomNameTextView.setText(roomName);
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Gray700));

                        JSONObject center = (JSONObject) model.get("center");
                        JSONObject detail = (JSONObject) center.get("detail");
                        roomTitle =detail.get("title").toString();

                        roomTitleTextView.setText(roomTitle);
                        roomTitleTextView.setVisibility(View.VISIBLE);
                    } else if (roomId.equals(model.get("id").toString())) {
                        roomId = "";
                        roomName = "";
                        roomTitle = "";

                        roomNameTextView.setText(getResources().getString(R.string.EditSessionRoom));
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Gray300));

                        roomTitleTextView.setText(roomTitle);
                        roomTitleTextView.setVisibility(View.GONE);
                    }

                    resetData("case");

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

                        StringBuilder name = new StringBuilder();
                        JSONArray clients = (JSONArray) model.get("clients");

                        for (int j = 0; j < clients.length(); j++) {
                            JSONObject client = (JSONObject) clients.get(j);
                            JSONObject user = (JSONObject) client.get("user");

                            if (j == clients.length() - 1) {
                                name.append(user.get("name").toString());
                            } else {
                                name.append(user.get("name").toString()).append(" - ");
                            }
                        }

                        caseName = name.toString();

                        caseTextView.setText(caseName);
                        caseTextView.setTextColor(getResources().getColor(R.color.Gray700));
                    } else if (caseId.equals(model.get("id").toString())) {
                        caseId = "";
                        caseName = "";

                        caseTextView.setText(getResources().getString(R.string.EditSessionCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Gray300));
                    }

                    resetData("caseDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    caseDialog.dismiss();
                    break;

                case "getStatus":
                    if (!statusId.equals(model.get("en_title").toString())) {
                        statusId = model.get("en_title").toString();
                        statusTitle = model.get("fa_title").toString();

                        statusTextView.setText(statusTitle);
                        statusTextView.setTextColor(getResources().getColor(R.color.Gray700));
                    } else if (statusId.equals(model.get("en_title").toString())) {
                        statusId = "";
                        statusTitle = "";

                        statusTextView.setText(getResources().getString(R.string.EditSessionStatus));
                        statusTextView.setTextColor(getResources().getColor(R.color.Gray300));
                    }

                    statusDialog.dismiss();
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