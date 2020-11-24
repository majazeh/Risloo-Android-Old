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

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class EditCaseActivity extends AppCompatActivity {

    // ViewModels
    private RoomViewModel roomViewModel;
    private CaseViewModel caseViewModel;

    // Model
    private Model roomModel;

    // Adapters
    private SearchAdapter roomDialogAdapter, referenceDialogAdapter;
    public SpinnerAdapter referenceRecyclerViewAdapter;

    // Vars
    public String id = "", roomId = "", roomName = "", roomTitle = "", complaint = "";
    private boolean roomException = false, referenceException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager referenceLayoutManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout roomFrameLayout, referenceFrameLayout;
    private LinearLayout roomLinearLayout;
    public TextView roomNameTextView, roomTitleTextView, referenceTextView;
    public EditText complaintEditText;
    private RecyclerView referenceRecyclerView;
    private Button editButton;
    private Dialog roomDialog, referenceDialog, progressDialog;
    private TextView roomDialogTitleTextView, referenceDialogTitleTextView, referenceDialogConfirm;;
    private CoordinatorLayout roomDialogSearchLayout, referenceDialogSearchLayout;
    private EditText roomDialogEditText, referenceDialogEditText;
    private ImageView roomDialogImageView, referenceDialogImageView;
    private ProgressBar roomDialogProgressBar, referenceDialogProgressBar;
    private TextView roomDialogTextView, referenceDialogTextView;
    private RecyclerView roomDialogRecyclerView, referenceDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_case);

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
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
        caseViewModel = new ViewModelProvider(this).get(CaseViewModel.class);

        roomDialogAdapter = new SearchAdapter(this);
        referenceDialogAdapter = new SearchAdapter(this);
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
        toolbarTextView.setText(getResources().getString(R.string.EditCaseTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        roomFrameLayout = findViewById(R.id.activity_edit_case_room_frameLayout);
        referenceFrameLayout = findViewById(R.id.activity_edit_case_reference_frameLayout);

        roomLinearLayout = findViewById(R.id.activity_edit_case_room_linearLayout);

        roomNameTextView = findViewById(R.id.activity_edit_case_room_name_textView);
        roomTitleTextView = findViewById(R.id.activity_edit_case_room_title_textView);
        referenceTextView = findViewById(R.id.activity_edit_case_reference_textView);

        complaintEditText = findViewById(R.id.activity_edit_case_complaint_editText);

        referenceRecyclerView = findViewById(R.id.activity_edit_case_reference_recyclerView);
        referenceRecyclerView.setLayoutManager(referenceLayoutManager);
        referenceRecyclerView.setHasFixedSize(false);

        editButton = findViewById(R.id.activity_edit_case_button);

        roomDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(roomDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        roomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        roomDialog.setContentView(R.layout.dialog_search);
        roomDialog.setCancelable(true);
        referenceDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(referenceDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        referenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        referenceDialog.setContentView(R.layout.dialog_search);
        referenceDialog.setCancelable(true);
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
        WindowManager.LayoutParams layoutParamsReference = new WindowManager.LayoutParams();
        layoutParamsReference.copyFrom(referenceDialog.getWindow().getAttributes());
        layoutParamsReference.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsReference.height = WindowManager.LayoutParams.WRAP_CONTENT;
        referenceDialog.getWindow().setAttributes(layoutParamsReference);

        roomDialogTitleTextView = roomDialog.findViewById(R.id.dialog_search_title_textView);
        roomDialogTitleTextView.setText(getResources().getString(R.string.EditCaseRoomDialogTitle));
        referenceDialogTitleTextView = referenceDialog.findViewById(R.id.dialog_search_title_textView);
        referenceDialogTitleTextView.setText(getResources().getString(R.string.EditCaseReferenceDialogTitle));

        referenceDialogConfirm = referenceDialog.findViewById(R.id.dialog_search_confirm_textView);

        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);
        referenceDialogSearchLayout = referenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        referenceDialogSearchLayout.setVisibility(View.VISIBLE);

        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        referenceDialogEditText = referenceDialog.findViewById(R.id.dialog_search_editText);

        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        referenceDialogImageView = referenceDialog.findViewById(R.id.dialog_search_imageView);

        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);
        referenceDialogProgressBar = referenceDialog.findViewById(R.id.dialog_search_progressBar);

        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);
        referenceDialogTextView = referenceDialog.findViewById(R.id.dialog_search_textView);

        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);

        referenceDialogRecyclerView = referenceDialog.findViewById(R.id.dialog_search_recyclerView);
        referenceDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        referenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        referenceDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            referenceDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);

            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
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

            try {
                if (roomViewModel.getSuggestRoom().size() != 0) {
                    setRecyclerView(roomViewModel.getSuggestRoom(), roomDialogRecyclerView, "getRooms");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            roomDialog.show();
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

        complaintEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!complaintEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(complaintEditText);
                    controlEditText.select(complaintEditText);
                }
            }
            return false;
        });

        editButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (roomId.isEmpty()) {
                errorView("room");
            }
            if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                errorView("reference");
            }
            if (complaint.length() == 0) {
                errorView("complaint");
            }

            if (roomException) {
                clearException("room");
            }
            if (referenceException) {
                clearException("reference");
            }

            if (!roomId.isEmpty() && referenceRecyclerViewAdapter.getValues().size() != 0 && complaint.length() != 0) {
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

        referenceDialogConfirm.setOnClickListener(v -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
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

        referenceDialog.setOnCancelListener(dialog -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
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
        if (extras.getString("complaint") != null)
            complaint = extras.getString("complaint");

        if (!roomId.equals("")) {
            roomNameTextView.setText(roomName);
            roomNameTextView.setTextColor(getResources().getColor(R.color.Grey));

            roomTitleTextView.setText(roomTitle);
            roomTitleTextView.setVisibility(View.VISIBLE);
        }

        if (extras.getString("clients") != null) {
            try {
                JSONArray clients = new JSONArray(extras.getString("clients"));

                for (int j = 0; j < clients.length(); j++) {
                    Model model = new Model((JSONObject) clients.get(j));
                    observeSearchAdapter(model, "getReferences");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!complaint.equals("")) {
            complaintEditText.setText(complaint);
            complaintEditText.setTextColor(getResources().getColor(R.color.Grey));
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "references":
                referenceRecyclerViewAdapter.setValue(referenceRecyclerViewAdapter.getValues(), referenceRecyclerViewAdapter.getIds(), method, "EditCase");
                recyclerView.setAdapter(referenceRecyclerViewAdapter);
                break;
            case "getRooms":
                roomDialogAdapter.setValue(arrayList, method, "EditCase");
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
                referenceDialogAdapter.setValue(arrayList, method, "EditCase");
                recyclerView.setAdapter(referenceDialogAdapter);

                if (arrayList.size() == 0) {
                    referenceDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                        referenceDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    private void errorView(String type) {
        if (type.equals("room")) {
            roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("reference")) {
            referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("complaint")) {
            complaintEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "room":
                roomException = true;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "reference":
                referenceException = true;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "room":
                roomException = false;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "reference":
                referenceException = false;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "roomDialog":
                RoomRepository.rooms.clear();
                roomDialogRecyclerView.setAdapter(null);

                if (roomDialogTextView.getVisibility() == View.VISIBLE) {
                    roomDialogTextView.setVisibility(View.GONE);
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
                    break;
                case "getReferences":
                    referenceDialogProgressBar.setVisibility(View.VISIBLE);
                    referenceDialogImageView.setVisibility(View.GONE);

                    roomViewModel.references(roomId, q);
                    break;
            }
            observeWork("roomViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        complaint = complaintEditText.getText().toString().trim();

//        try {
//            if (roomModel != null) {
//                roomViewModel.addSuggestRoom(roomModel, 10);
//            }
//
//            progressDialog.show();
//            caseViewModel.edit(id, roomId, referenceRecyclerViewAdapter.getIds(), complaint);
//            observeWork("caseViewModel");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "caseViewModel":
                CaseRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (CaseRepository.work.equals("edit")) {
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
                            setRecyclerView(RoomRepository.references, referenceDialogRecyclerView, "getReferences");

                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
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
            if (!ExceptionGenerator.errors.isNull("client_id")) {
                errorException("reference");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("chief_complaint")) {
                complaintEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("chief_complaint");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("chief_complaint"));
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

                        roomNameTextView.setText(getResources().getString(R.string.EditCaseRoom));
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

                case "getReferences":
                    int referencePosition = referenceRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (referencePosition == -1) {
                        referenceRecyclerViewAdapter.getValues().add(model);
                        referenceRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, referenceRecyclerView, "references");
                    } else if (referencePosition != -1) {
                        referenceRecyclerViewAdapter.removeValue(referencePosition);

                        referenceDialogAdapter.notifyDataSetChanged();
                    }

                    if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                        referenceTextView.setVisibility(View.VISIBLE);

                        referenceDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (referenceTextView.getVisibility() == View.VISIBLE) {
                            referenceTextView.setVisibility(View.GONE);
                        }

                        if (referenceDialogConfirm.getVisibility() == View.GONE) {
                            referenceDialogConfirm.setVisibility(View.VISIBLE);
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