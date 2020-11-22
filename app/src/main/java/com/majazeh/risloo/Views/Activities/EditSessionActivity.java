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

    // Adapters
    private SearchAdapter roomDialogAdapter, caseDialogAdapter, statusDialogAdapter;

    // Vars
    public String id = "", roomId = "", roomName = "", roomTitle = "", casseId = "", casseName = "", timestamp = "", time = "", date = "", period = "", statusId = "", statusTitle = "";
    private boolean roomException = false, caseException = false, timeException = false, dateException = false, statusException = false;
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
    public TextView roomNameTextView, roomTitleTextView, caseTextView, timeTextView, dateTextView, statusTextView;
    public EditText periodEditText;
    private Button editButton;
    private Dialog roomDialog, caseDialog, timeDialog, dateDialog, statusDialog, progressDialog;
    private TextView roomDialogTitleTextView, caseDialogTitleTextView, dateDialogTitleTextView, statusDialogTitleTextView;
    private CoordinatorLayout roomDialogSearchLayout, caseDialogSearchLayout;
    private EditText roomDialogEditText, caseDialogEditText;
    private ImageView roomDialogImageView, caseDialogImageView;
    private ProgressBar roomDialogProgressBar, caseDialogProgressBar;
    private TextView roomDialogTextView, caseDialogTextView, timeDialogPositive, timeDialogNegative, dateDialogPositive, dateDialogNegative;
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
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.EditSessionTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        roomFrameLayout = findViewById(R.id.activity_edit_session_room_frameLayout);
        caseFrameLayout = findViewById(R.id.activity_edit_session_case_frameLayout);
        statusFrameLayout = findViewById(R.id.activity_edit_session_status_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_edit_session_room_linearLayout);

        roomNameTextView = findViewById(R.id.activity_edit_session_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_edit_session_room_title_textView);
        caseTextView = findViewById(R.id.activity_edit_session_case_textView);
        timeTextView = findViewById(R.id.activity_edit_session_start_time_textView);
        dateTextView = findViewById(R.id.activity_edit_session_start_date_textView);
        statusTextView = findViewById(R.id.activity_edit_session_status_textView);

        periodEditText = findViewById(R.id.activity_edit_session_period_editText);

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
        timeDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(timeDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.setContentView(R.layout.dialog_time);
        timeDialog.setCancelable(true);
        dateDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(dateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.setContentView(R.layout.dialog_date);
        dateDialog.setCancelable(true);
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
        WindowManager.LayoutParams layoutParamsTime = new WindowManager.LayoutParams();
        layoutParamsTime.copyFrom(timeDialog.getWindow().getAttributes());
        layoutParamsTime.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsTime.height = WindowManager.LayoutParams.WRAP_CONTENT;
        timeDialog.getWindow().setAttributes(layoutParamsTime);
        WindowManager.LayoutParams layoutParamsDate = new WindowManager.LayoutParams();
        layoutParamsDate.copyFrom(dateDialog.getWindow().getAttributes());
        layoutParamsDate.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsDate.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dateDialog.getWindow().setAttributes(layoutParamsDate);
        WindowManager.LayoutParams layoutParamsStatus = new WindowManager.LayoutParams();
        layoutParamsStatus.copyFrom(statusDialog.getWindow().getAttributes());
        layoutParamsStatus.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsStatus.height = WindowManager.LayoutParams.WRAP_CONTENT;
        statusDialog.getWindow().setAttributes(layoutParamsStatus);

        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.EditSessionRoomDialogTitle));
        caseDialogTitleTextView = caseDialog.findViewById(R.id.dialog_search_title_textView);
        caseDialogTitleTextView.setText(getResources().getString(R.string.EditSessionCaseDialogTitle));
        dateDialogTitleTextView = dateDialog.findViewById(R.id.dialog_date_title_textView);
        dateDialogTitleTextView.setText(getResources().getString(R.string.EditSessionDateDialogTitle));
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

        hourNumberPicker = timeDialog.findViewById(R.id.dialog_time_hour_NumberPicker);
        minuteNumberPicker = timeDialog.findViewById(R.id.dialog_time_minute_NumberPicker);
        yearNumberPicker = dateDialog.findViewById(R.id.dialog_date_year_NumberPicker);
        monthNumberPicker = dateDialog.findViewById(R.id.dialog_date_month_NumberPicker);
        dayNumberPicker = dateDialog.findViewById(R.id.dialog_date_day_NumberPicker);

        timeDialogPositive = timeDialog.findViewById(R.id.dialog_time_positive_textView);
        timeDialogNegative = timeDialog.findViewById(R.id.dialog_time_negative_textView);
        dateDialogPositive = dateDialog.findViewById(R.id.dialog_date_positive_textView);
        dateDialogNegative = dateDialog.findViewById(R.id.dialog_date_negative_textView);

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

            timeDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            timeDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            dateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            dateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

        timeTextView.setOnTouchListener((v, event) -> {
            timeTextView.setClickable(false);
            handler.postDelayed(() -> timeTextView.setClickable(true), 300);

            if (timeException) {
                clearException("time");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            timeDialog.show();
            return false;
        });

        dateTextView.setOnTouchListener((v, event) -> {
            dateTextView.setClickable(false);
            handler.postDelayed(() -> dateTextView.setClickable(true), 300);

            if (dateException) {
                clearException("date");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            dateDialog.show();
            return false;
        });

        periodEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!periodEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(periodEditText);
                    controlEditText.select(periodEditText);
                }
            }
            return false;
        });

        statusTextView.setOnClickListener(v -> {
            statusTextView.setClickable(false);
            handler.postDelayed(() -> statusTextView.setClickable(true), 300);

            if (caseException) {
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
                errorView("room");
            }
            if (casseId.isEmpty()) {
                errorView("case");
            }
            if (time.isEmpty()) {
                errorView("time");
            }
            if (date.isEmpty()) {
                errorView("date");
            }
            if (periodEditText.length() == 0) {
                errorView("period");
            }
            if (statusId.isEmpty()) {
                errorView("status");
            }

            if (roomException) {
                clearException("room");
            }
            if (caseException) {
                clearException("case");
            }
            if (timeException) {
                clearException("time");
            }
            if (dateException) {
                clearException("date");
            }
            if (statusException) {
                clearException("status");
            }

            if (!roomId.isEmpty() && !casseId.isEmpty() && !time.isEmpty() && !date.isEmpty() && periodEditText.length() != 0 && !statusId.isEmpty()) {
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

        timeDialogPositive.setOnClickListener(v -> {
            timeDialogPositive.setClickable(false);
            handler.postDelayed(() -> timeDialogPositive.setClickable(true), 300);
            timeDialog.dismiss();

            hour = hourNumberPicker.getValue();
            minute = minuteNumberPicker.getValue();

            if (hour < 10) {
                if (minute < 10)
                    time = "0" + hour + ":" + "0" + minute;
                else
                    time = "0" + hour + ":" + minute;
            } else {
                if (minute < 10)
                    time = hour + ":" + "0" + minute;
                else
                    time = hour + ":" + minute;
            }

            timeTextView.setText(time);
        });

        timeDialogNegative.setOnClickListener(v -> {
            timeDialogNegative.setClickable(false);
            handler.postDelayed(() -> timeDialogNegative.setClickable(true), 300);
            timeDialog.dismiss();

            hourNumberPicker.setValue(hour);
            minuteNumberPicker.setValue(minute);
        });

        dateDialogPositive.setOnClickListener(v -> {
            dateDialogPositive.setClickable(false);
            handler.postDelayed(() -> dateDialogPositive.setClickable(true), 300);
            dateDialog.dismiss();

            year = yearNumberPicker.getValue();
            month = monthNumberPicker.getValue();
            day = dayNumberPicker.getValue();

            if (month < 10) {
                if (day < 10)
                    date = year + "-" + "0" + month + "-" + "0" + day;
                else
                    date = year + "-" + "0" + month + "-" + day;
            } else {
                if (day < 10)
                    date = year + "-" + month + "-" + "0" + day;
                else
                    date = year + "-" + month + "-" + day;
            }

            dateTextView.setText(date);
        });

        dateDialogNegative.setOnClickListener(v -> {
            dateDialogNegative.setClickable(false);
            handler.postDelayed(() -> dateDialogNegative.setClickable(true), 300);
            dateDialog.dismiss();

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

        timeDialog.setOnCancelListener(dialog -> {
            timeDialog.dismiss();

            hourNumberPicker.setValue(hour);
            minuteNumberPicker.setValue(minute);
        });

        dateDialog.setOnCancelListener(dialog -> {
            dateDialog.dismiss();

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
            casseId = extras.getString("case_id");
        if (extras.getString("case_name") != null)
            casseName = extras.getString("case_name");
        if (extras.getString("time") != null)
            time = extras.getString("time");
        else
            time = DateManager.currentTime();
        if (extras.getString("date") != null)
            date = extras.getString("date");
        else
            date = DateManager.currentJalaliDate();
        if (extras.getString("period") != null)
            period = extras.getString("period");
        if (extras.getString("en_status") != null)
            statusId = extras.getString("en_status");
        if (extras.getString("fa_status") != null)
            statusTitle = extras.getString("fa_status");

        if (!roomId.equals("")) {
            roomNameTextView.setText(roomName);
            roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

            roomTitleTextView.setText(roomTitle);
            roomTitleTextView.setVisibility(View.VISIBLE);
        }

        if (!casseId.equals("")) {
            caseTextView.setText(casseName);
            caseTextView.setTextColor(getResources().getColor(R.color.Grey));
        }

        timeTextView.setText(time);

        hour = Integer.parseInt(DateManager.dateToString("HH", DateManager.stringToDate("HH:mm", time)));
        minute = Integer.parseInt(DateManager.dateToString("mm", DateManager.stringToDate("HH:mm", time)));

        dateTextView.setText(date);

        year = Integer.parseInt(DateManager.dateToString("yyyy", DateManager.stringToDate("yyyy-MM-dd", date)));
        month = Integer.parseInt(DateManager.dateToString("MM", DateManager.stringToDate("yyyy-MM-dd", date)));
        day = Integer.parseInt(DateManager.dateToString("dd", DateManager.stringToDate("yyyy-MM-dd", date)));

        if (!period.equals("")) {
            periodEditText.setText(period);
            periodEditText.setTextColor(getResources().getColor(R.color.Grey));
        }

        if (!statusId.equals("")) {
            statusTextView.setText(statusTitle);
            statusTextView.setTextColor(getResources().getColor(R.color.Grey));
        }
    }

    private void setCustomPicker() {
        hourNumberPicker.setMinValue(0);
        hourNumberPicker.setMaxValue(100);
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
                roomDialogAdapter.setValue(arrayList, method, "EditSession");
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
                caseDialogAdapter.setValue(arrayList, method, "EditSession");
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
                statusDialogAdapter.setValue(arrayList, method, "EditSession");
                recyclerView.setAdapter(statusDialogAdapter);
                break;
        }
    }

    private void errorView(String type) {
        switch (type) {
            case "room":
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "case":
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "time":
                timeTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "date":
                dateTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "period":
                periodEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "status":
                statusFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
            case "time":
                timeException = true;
                timeTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "date":
                dateException = true;
                dateTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
            case "time":
                timeException = false;
                timeTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "date":
                dateException = false;
                dateTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
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
                if (!casseId.equals("")) {
                    casseId = "";
                    casseName = "";

                    caseTextView.setText(getResources().getString(R.string.EditSessionCase));
                    caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
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
                    caseViewModel.cases(roomId, q);

                    observeWork("caseViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        timestamp = String.valueOf(DateManager.dateToTimestamp(DateManager.stringToDate("yyyy-MM-dd HH:mm", DateManager.jalaliToGregorian(date) + " " + time)));
        period = periodEditText.getText().toString().trim();

        try {
            progressDialog.show();
            sessionViewModel.update(id, casseId, timestamp, period, statusId);
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
        if (ExceptionGenerator.current_exception.equals("create")) {
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
                errorException("time");
                errorException("date");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("started_at");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("started_at"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("duration")) {
                periodEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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

                        roomNameTextView.setText(getResources().getString(R.string.EditSessionRoom));
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

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
                    if (!casseId.equals(model.get("id").toString())) {
                        casseId = model.get("id").toString();

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
                            casseName =casse.get("name").toString();

                            caseTextView.setText(casseName);
                            caseTextView.setTextColor(getResources().getColor(R.color.Grey));
                        }
                    } else if (casseId.equals(model.get("id").toString())) {
                        casseId = "";
                        casseName = "";

                        caseTextView.setText(getResources().getString(R.string.EditSessionCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
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
                        statusTextView.setTextColor(getResources().getColor(R.color.Grey));
                    } else if (statusId.equals(model.get("en_title").toString())) {
                        statusId = "";
                        statusTitle = "";

                        statusTextView.setText(getResources().getString(R.string.EditSessionStatus));
                        statusTextView.setTextColor(getResources().getColor(R.color.Mischka));
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