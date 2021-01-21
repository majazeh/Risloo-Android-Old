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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel centerViewModel;
    private RoomViewModel roomViewModel;
    private CaseViewModel caseViewModel;

    // Model
    private Model roomModel;

    // Adapters
    private SearchAdapter referenceDialogAdapter, positionDialogAdapter, roomDialogAdapter;
    public SpinnerAdapter referenceRecyclerViewAdapter;

    // Vars
    public String type = "", caseId = "", roomId = "", roomName = "", roomTitle = "", clinicId = "", mobile = "", positionId = "", positionTitle = "";
    private boolean addCase = false;
    private boolean referenceException = false, positionException = false, roomException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager referenceLayoutManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private LinearLayout clinicLinearLayout;
    private FrameLayout referenceFrameLayout, positionFrameLayout, roomFrameLayout;
    private LinearLayout roomLinearLayout;
    public TextView referenceTextView, referenceCountTextView, positionTextView, roomNameTextView, roomTitleTextView;
    private EditText mobileEditText;
    private RecyclerView referenceRecyclerView;
    private CheckBox caseCheckbox;
    private Button createButton;
    private Dialog referenceDialog, positionDialog, roomDialog, progressDialog;
    private TextView referenceDialogTitleTextView, positionDialogTitleTextView, roomDialogTitleTextView, referenceDialogConfirm;
    private CoordinatorLayout referenceDialogSearchLayout, roomDialogSearchLayout;
    private EditText referenceDialogEditText, roomDialogEditText;
    private ImageView referenceDialogImageView, roomDialogImageView;
    private ProgressBar referenceDialogProgressBar, roomDialogProgressBar;
    private TextView referenceDialogTextView, roomDialogTextView;
    private RecyclerView referenceDialogRecyclerView, positionDialogRecyclerView, roomDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_user);

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
        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        caseViewModel = new ViewModelProvider(this).get(CaseViewModel.class);

        referenceDialogAdapter = new SearchAdapter(this);
        positionDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);

        referenceRecyclerViewAdapter = new SpinnerAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        referenceLayoutManager = new FlexboxLayoutManager(this);
        referenceLayoutManager.setFlexDirection(FlexDirection.ROW);
        referenceLayoutManager.setFlexWrap(FlexWrap.WRAP);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CreateUserTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        clinicLinearLayout = findViewById(R.id.activity_create_user_clinic_linearLayout);

        referenceFrameLayout = findViewById(R.id.activity_create_user_reference_frameLayout);
        positionFrameLayout = findViewById(R.id.activity_create_user_position_frameLayout);
        roomFrameLayout = findViewById(R.id.activity_create_user_room_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_create_user_room_linearLayout);

        referenceTextView = findViewById(R.id.activity_create_user_reference_textView);
        referenceCountTextView = findViewById(R.id.activity_create_user_reference_count_textView);
        positionTextView = findViewById(R.id.activity_create_user_position_textView);
        roomNameTextView = findViewById(R.id.activity_create_user_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_create_user_room_title_textView);

        mobileEditText = findViewById(R.id.activity_create_user_mobile_editText);

        referenceRecyclerView = findViewById(R.id.activity_create_user_reference_recyclerView);
        referenceRecyclerView.setLayoutManager(referenceLayoutManager);
        referenceRecyclerView.setHasFixedSize(false);

        caseCheckbox = findViewById(R.id.activity_create_user_case_checkBox);

        createButton = findViewById(R.id.activity_create_user_button);

        referenceDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(referenceDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        referenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        referenceDialog.setContentView(R.layout.dialog_search);
        referenceDialog.setCancelable(true);
        positionDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(positionDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        positionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        positionDialog.setContentView(R.layout.dialog_search);
        positionDialog.setCancelable(true);
        roomDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(roomDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        roomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomDialog.setContentView(R.layout.dialog_search);
        roomDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsReferenceDialog = new WindowManager.LayoutParams();
        layoutParamsReferenceDialog.copyFrom(referenceDialog.getWindow().getAttributes());
        layoutParamsReferenceDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsReferenceDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        referenceDialog.getWindow().setAttributes(layoutParamsReferenceDialog);
        WindowManager.LayoutParams layoutParamsPositionDialog = new WindowManager.LayoutParams();
        layoutParamsPositionDialog.copyFrom(positionDialog.getWindow().getAttributes());
        layoutParamsPositionDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsPositionDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        positionDialog.getWindow().setAttributes(layoutParamsPositionDialog);
        WindowManager.LayoutParams layoutParamsRoomDialog = new WindowManager.LayoutParams();
        layoutParamsRoomDialog.copyFrom(roomDialog.getWindow().getAttributes());
        layoutParamsRoomDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsRoomDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        roomDialog.getWindow().setAttributes(layoutParamsRoomDialog);

        referenceDialogTitleTextView = referenceDialog.findViewById(R.id.dialog_search_title_textView);
        referenceDialogTitleTextView.setText(getResources().getString(R.string.CreateUserReferenceDialogTitle));
        positionDialogTitleTextView = positionDialog.findViewById(R.id.dialog_search_title_textView);
        positionDialogTitleTextView.setText(getResources().getString(R.string.CreateUserPositionDialogTitle));
        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.CreateUserRoomDialogTitle));

        referenceDialogConfirm = referenceDialog.findViewById(R.id.dialog_search_confirm_textView);

        referenceDialogSearchLayout = referenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        referenceDialogSearchLayout.setVisibility(View.VISIBLE);
        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);

        referenceDialogEditText = referenceDialog.findViewById(R.id.dialog_search_editText);
        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);

        referenceDialogImageView = referenceDialog.findViewById(R.id.dialog_search_imageView);
        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);

        referenceDialogProgressBar = referenceDialog.findViewById(R.id.dialog_search_progressBar);
        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);

        referenceDialogTextView = referenceDialog.findViewById(R.id.dialog_search_textView);
        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);

        referenceDialogRecyclerView = referenceDialog.findViewById(R.id.dialog_search_recyclerView);
        referenceDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        referenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        referenceDialogRecyclerView.setHasFixedSize(true);

        positionDialogRecyclerView = positionDialog.findViewById(R.id.dialog_search_recyclerView);
        positionDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        positionDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        positionDialogRecyclerView.setHasFixedSize(true);

        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

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

        referenceRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (referenceException) {
                    clearException("reference");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                referenceDialog.show();
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(mobileEditText);
                    controlEditText.select(mobileEditText);
                }
            }
            return false;
        });

        positionTextView.setOnClickListener(v -> {
            positionTextView.setClickable(false);
            handler.postDelayed(() -> positionTextView.setClickable(true), 250);

            if (positionException) {
                clearException("position");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            setRecyclerView(centerViewModel.getLocalPosition(), positionDialogRecyclerView, "getPositions");

            positionDialog.show();
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
                    setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            roomDialog.show();
        });

        caseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                caseCheckbox.setTextColor(getResources().getColor(R.color.Nero));
                addCase = true;
            } else {
                caseCheckbox.setTextColor(getResources().getColor(R.color.Mischka));
                addCase = false;
            }
        });

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            switch (type) {
                case "case":
                case "room":
                    if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                        errorException("reference");
                    }

                    if (referenceRecyclerViewAdapter.getValues().size() != 0) {
                        clearException("reference");

                        doWork();
                    }
                    break;

                case "center":
                    if (mobileEditText.length() == 0) {
                        controlEditText.error(this, mobileEditText);
                    }
                    if (positionId.isEmpty()) {
                        errorException("position");
                    }

                    if (mobileEditText.length() != 0 && !positionId.isEmpty()) {
                        controlEditText.clear(this, mobileEditText);
                        clearException("position");

                        doWork();
                    }
                    break;
            }
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

        referenceDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (referenceDialogEditText.length() != 0) {
                        getData("getReferences", roomId, caseId, referenceDialogEditText.getText().toString().trim());
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

        roomDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (roomDialogEditText.length() != 0) {
                        getData("getRooms", roomId, caseId, roomDialogEditText.getText().toString().trim());
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

        referenceDialogConfirm.setOnClickListener(v -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
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

        positionDialog.setOnCancelListener(dialog -> positionDialog.dismiss());

        roomDialog.setOnCancelListener(dialog -> {
            resetData("roomDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            roomDialog.dismiss();
        });
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("type") != null)
            type = extras.getString("type");
        if (extras.getString("case_id") != null)
            caseId = extras.getString("case_id");
        if (extras.getString("room_id") != null)
            roomId = extras.getString("room_id");
        if (extras.getString("room_name") != null)
            roomName = extras.getString("room_name");
        if (extras.getString("room_title") != null)
            roomTitle = extras.getString("room_title");
        if (extras.getString("clinic_id") != null)
            clinicId = extras.getString("clinic_id");

        switch (type) {
            case "case":
            case "room":
                referenceFrameLayout.setVisibility(View.VISIBLE);
                clinicLinearLayout.setVisibility(View.GONE);
                break;

            case "center":
                referenceFrameLayout.setVisibility(View.GONE);
                clinicLinearLayout.setVisibility(View.VISIBLE);

                if (!roomId.equals("")) {
                    roomNameTextView.setText(roomName);
                    roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

                    roomTitleTextView.setText(roomTitle);
                    roomTitleTextView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "references":
                referenceRecyclerViewAdapter.setValues(referenceRecyclerViewAdapter.getValues(), referenceRecyclerViewAdapter.getIds(), method, "CreateUser");
                recyclerView.setAdapter(referenceRecyclerViewAdapter);
                break;
            case "getReferences":
                referenceDialogAdapter.setValues(arrayList, method, "CreateUser");
                recyclerView.setAdapter(referenceDialogAdapter);

                if (arrayList.size() == 0) {
                    referenceDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                        referenceDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
            case "getPositions":
                positionDialogAdapter.setValues(arrayList, method, "CreateUser");
                recyclerView.setAdapter(positionDialogAdapter);
                break;
            case "getRooms":
                roomDialogAdapter.setValues(arrayList, method, "CreateUser");
                recyclerView.setAdapter(roomDialogAdapter);

                if (arrayList.size() == 0) {
                    roomDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                        roomDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "reference":
                referenceException = true;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "mobile":
                mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "position":
                positionException = true;
                positionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "room":
                roomException = true;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "reference":
                referenceException = false;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "position":
                positionException = false;
                positionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "room":
                roomException = false;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "referenceDialog":

                switch (type) {
                    case "room":
                        RoomRepository.users.clear();
                        break;
                        
                    case "center":
                        CenterRepository.users.clear();
                        break;
                }

                referenceDialogRecyclerView.setAdapter(null);

                if (referenceDialogConfirm.getVisibility() == View.VISIBLE) {
                    referenceDialogConfirm.setVisibility(View.GONE);
                }

                if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                    referenceDialogTextView.setVisibility(View.GONE);
                }
                break;
            case "roomDialog":
                RoomRepository.rooms.clear();
                roomDialogRecyclerView.setAdapter(null);

                if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                    roomDialogTextView.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getData(String method, String roomId, String caseId, String q) {
        try {
            switch (method) {
                case "getReferences":
                    referenceDialogProgressBar.setVisibility(View.VISIBLE);
                    referenceDialogImageView.setVisibility(View.GONE);

                    switch (type) {
                        case "case":
                            roomViewModel.references(roomId, q, "", caseId);
                            observeWork("roomViewModel");
                            break;

                        case "room":
                            centerViewModel.references(roomId, q);
                            observeWork("centerViewModel");
                            break;
                    }

                    break;
                case "getRooms":
                    roomDialogProgressBar.setVisibility(View.VISIBLE);
                    roomDialogImageView.setVisibility(View.GONE);

                    RoomRepository.allPage = 1;
                    roomViewModel.rooms(q);

                    observeWork("roomViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        try {
            progressDialog.show();

            switch (type) {
                case "case":
                    caseViewModel.addUser(caseId, referenceRecyclerViewAdapter.getIds());
                    observeWork("caseViewModel");
                    break;

                case "room":
                    roomViewModel.addUser(roomId, referenceRecyclerViewAdapter.getIds());
                    observeWork("roomViewModel");
                    break;

                case "center":
//                    mobile = mobileEditText.getText().toString().trim();
//
//                    centerViewModel.addUser(clinicId, mobile, positionId, roomId, addCase);
//                    observeWork("centerViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "caseViewModel":
                CaseRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (CaseRepository.work.equals("addUser")) {
                        if (integer == 1) {
                            setResult(RESULT_OK, null);
                            finish();

                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            progressDialog.dismiss();
                            observeException();
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CaseRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;

            case "centerViewModel":
                CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (CenterRepository.work.equals("addUser")) {
                        if (integer == 1) {
                            setResult(RESULT_OK, null);
                            finish();

                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            progressDialog.dismiss();
                            observeException();
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    } else if (CenterRepository.work.equals("getReferences")) {
                        if (integer == 1) {
                            setRecyclerView(CenterRepository.users, referenceDialogRecyclerView, "getReferences");

                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;

            case "roomViewModel":
                RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (RoomRepository.work.equals("addUser")) {
                        if (integer == 1) {
                            setResult(RESULT_OK, null);
                            finish();

                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            progressDialog.dismiss();
                            observeException();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    } else if (RoomRepository.work.equals("getReferences")) {
                        if (integer == 1) {
                            setRecyclerView(RoomRepository.users, referenceDialogRecyclerView, "getReferences");

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
                    } else if (RoomRepository.work.equals("getAll")) {
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
        }
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("addUser")) {
            String exceptionToast = "";

            switch (type) {
                case "case":
                    if (!ExceptionGenerator.errors.isNull("case_id")) {
                        exceptionToast = ExceptionGenerator.getErrorBody("case_id");
                    }
                    if (!ExceptionGenerator.errors.isNull("client_id")) {
                        errorException("reference");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                        }
                    }
                    break;

                case "room":
                    if (!ExceptionGenerator.errors.isNull("room_id")) {
                        exceptionToast = ExceptionGenerator.getErrorBody("room_id");
                    }
                    if (!ExceptionGenerator.errors.isNull("user_id")) {
                        errorException("reference");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("user_id");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("user_id"));
                        }
                    }
                    break;

                case "center":
                    if (!ExceptionGenerator.errors.isNull("center_id")) {
                        exceptionToast = ExceptionGenerator.getErrorBody("center_id");
                    }
                    if (!ExceptionGenerator.errors.isNull("mobile")) {
                        errorException("mobile");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("mobile");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("mobile"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("position")) {
                        errorException("position");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("position");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("position"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("roomId")) {
                        errorException("room");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("roomId");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("roomId"));
                        }
                    }
                    break;
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getReferences":
                    int referencePosition = referenceRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (referencePosition == -1) {
                        referenceRecyclerViewAdapter.getValues().add(model);
                        referenceRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, referenceRecyclerView, "references");
                    } else {
                        referenceRecyclerViewAdapter.removeValue(referencePosition);

                        referenceDialogAdapter.notifyDataSetChanged();
                    }

                    if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                        referenceTextView.setVisibility(View.VISIBLE);

                        referenceCountTextView.setText("");
                        referenceCountTextView.setVisibility(View.GONE);

                        referenceDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (referenceTextView.getVisibility() == View.VISIBLE) {
                            referenceTextView.setVisibility(View.GONE);
                        }

                        referenceCountTextView.setText(String.valueOf(referenceRecyclerViewAdapter.getValues().size()));
                        if (referenceCountTextView.getVisibility() == View.GONE) {
                            referenceCountTextView.setVisibility(View.VISIBLE);
                        }

                        if (referenceDialogConfirm.getVisibility() == View.GONE) {
                            referenceDialogConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case "getPositions":
                    if (!positionId.equals(model.get("en_title").toString())) {
                        positionId = model.get("en_title").toString();
                        positionTitle = model.get("fa_title").toString();

                        positionTextView.setText(positionTitle);
                        positionTextView.setTextColor(getResources().getColor(R.color.Grey));
                    } else if (positionId.equals(model.get("en_title").toString())) {
                        positionId = "";
                        positionTitle = "";

                        positionTextView.setText(getResources().getString(R.string.CreateUserPosition));
                        positionTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    positionDialog.dismiss();
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

                        roomNameTextView.setText(getResources().getString(R.string.CreateCaseRoom));
                        roomNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        roomTitleTextView.setText(roomTitle);
                        roomTitleTextView.setVisibility(View.GONE);
                    }

                    resetData("roomDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    roomDialog.dismiss();
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