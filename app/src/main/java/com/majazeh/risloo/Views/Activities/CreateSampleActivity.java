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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.InputHandler;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
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
    private SampleViewModel viewModel;

    // Adapters
    private SearchAdapter scaleDialogAdapter, roomDialogAdapter, caseDialogAdapter, roomReferenceDialogAdapter;
    private SpinnerAdapter scaleRecyclerViewAdapter, roomReferenceRecyclerViewAdapter;
    private CheckBoxAdapter caseReferenceRecyclerViewAdapter;

    // Vars
    public String room = "", casse = "", count = "";
    private boolean scaleException = false, roomException = false, caseException = false, roomReferenceException = false, caseReferenceException = false;

    // Objects
    private Handler handler;
    private InputHandler inputHandler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    public TextView scaleTextView, roomTextView, caseTextView, roomReferenceTextView, caseReferenceTextView;
    public EditText countEditText, scaleDialogEditText, roomDialogEditText, caseDialogEditText, roomReferenceDialogEditText;
    public RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView, scaleDialogRecyclerView, roomDialogRecyclerView, caseDialogRecyclerView, roomReferenceDialogRecyclerView;
    private ProgressBar scaleProgressBar, roomProgressBar, caseProgressBar, roomReferenceProgressBar;
    private ImageView scaleImageView, roomImageView, caseImageView, roomReferenceImageView;
    private CardView roomReferenceCardView;
    private LinearLayout scaleLinearLayout, roomLinearLayout, caseLinearLayout, roomReferenceLinearLayout;
    private FrameLayout roomFrameLayout, caseFrameLayout;
    private Button createButton;
    private Dialog scaleDialog, roomDialog, caseDialog, roomReferenceDialog, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_sample);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        scaleDialogAdapter = new SearchAdapter(this);
        roomDialogAdapter = new SearchAdapter(this);
        caseDialogAdapter = new SearchAdapter(this);
        roomReferenceDialogAdapter = new SearchAdapter(this);
        scaleRecyclerViewAdapter = new SpinnerAdapter(this);
        roomReferenceRecyclerViewAdapter = new SpinnerAdapter(this);
        caseReferenceRecyclerViewAdapter = new CheckBoxAdapter(this);

        handler = new Handler();

        inputHandler = new InputHandler();

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        roomTextView = findViewById(R.id.activity_create_sample_room_textView);
        caseTextView = findViewById(R.id.activity_create_sample_case_textView);
        roomReferenceTextView = findViewById(R.id.activity_create_sample_room_reference_textView);
        caseReferenceTextView = findViewById(R.id.activity_create_sample_case_reference_textView);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scaleRecyclerView.setHasFixedSize(true);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
        roomReferenceRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomReferenceRecyclerView.setHasFixedSize(true);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._12sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);

        scaleProgressBar = findViewById(R.id.activity_create_sample_scale_progressBar);
        roomProgressBar = findViewById(R.id.activity_create_sample_room_progressBar);
        caseProgressBar = findViewById(R.id.activity_create_sample_case_progressBar);
        roomReferenceProgressBar = findViewById(R.id.activity_create_sample_room_reference_progressBar);

        scaleImageView = findViewById(R.id.activity_create_sample_scale_imageView);
        roomImageView = findViewById(R.id.activity_create_sample_room_imageView);
        caseImageView = findViewById(R.id.activity_create_sample_case_imageView);
        roomReferenceImageView = findViewById(R.id.activity_create_sample_room_reference_imageView);

        roomReferenceCardView = findViewById(R.id.activity_create_sample_room_reference_cardView);

        scaleLinearLayout = findViewById(R.id.activity_create_sample_scale_linearLayout);
        roomLinearLayout = findViewById(R.id.activity_create_sample_clinic_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_create_sample_case_linearLayout);
        roomReferenceLinearLayout = findViewById(R.id.activity_create_sample_room_reference_linearLayout);

        roomFrameLayout = findViewById(R.id.activity_create_sample_room_frameLayout);
        caseFrameLayout = findViewById(R.id.activity_create_sample_case_frameLayout);

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

        scaleDialogEditText = scaleDialog.findViewById(R.id.dialog_search_editText);
        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        caseDialogEditText = caseDialog.findViewById(R.id.dialog_search_editText);
        roomReferenceDialogEditText = roomReferenceDialog.findViewById(R.id.dialog_search_editText);

        scaleDialogRecyclerView = scaleDialog.findViewById(R.id.dialog_search_recyclerView);
        scaleDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        scaleDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scaleDialogRecyclerView.setHasFixedSize(true);
        roomDialogRecyclerView = roomDialog.findViewById(R.id.dialog_search_recyclerView);
        roomDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomDialogRecyclerView.setHasFixedSize(true);
        caseDialogRecyclerView = caseDialog.findViewById(R.id.dialog_search_recyclerView);
        caseDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        caseDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseDialogRecyclerView.setHasFixedSize(true);
        roomReferenceDialogRecyclerView = roomReferenceDialog.findViewById(R.id.dialog_search_recyclerView);
        roomReferenceDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        roomReferenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        roomReferenceDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            scaleImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
            roomReferenceImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        typeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                    inputHandler.clear(CreateSampleActivity.this, inputHandler.getInput());
                }

                switch (tab.getPosition()) {
                    case 0:
                        roomLinearLayout.setVisibility(View.VISIBLE);
                        caseLinearLayout.setVisibility(View.GONE);

                        // Reset Case
                        if (SampleRepository.cases.size() != 0) {
                            casse = "";
                            SampleRepository.cases.clear();
                            caseDialogRecyclerView.setAdapter(null);
                            caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                            caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        }

                        // Reset CaseReferences
                        if (caseReferenceRecyclerViewAdapter.getValues() != null) {
                            caseReferenceTextView.setVisibility(View.GONE);

                            caseReferenceRecyclerViewAdapter.getValues().clear();
                            caseReferenceRecyclerViewAdapter.getChecks().clear();
                            caseReferenceRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        break;
                    case 1:
                        roomLinearLayout.setVisibility(View.GONE);
                        caseLinearLayout.setVisibility(View.VISIBLE);

                        // Reset RoomReferences
                        if (SampleRepository.references.size() != 0) {
                            SampleRepository.references.clear();
                            roomReferenceDialogRecyclerView.setAdapter(null);
                            roomReferenceTextView.setVisibility(View.VISIBLE);

                            roomReferenceRecyclerViewAdapter.getValues().clear();
                            roomReferenceRecyclerViewAdapter.getIds().clear();
                            roomReferenceRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        // Reset Count
                        if (countEditText.length() != 0) {
                            count = "";
                            countEditText.getText().clear();
                            countEditText.setVisibility(View.VISIBLE);
                        }

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

        scaleImageView.setOnClickListener(v -> {
            scaleImageView.setClickable(false);
            handler.postDelayed(() -> scaleImageView.setClickable(true), 300);

            if (scaleException) {
                clearException("scale");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (SampleRepository.scales.size() == 0) {
                getData("getScales", "");
            } else {
                scaleDialog.show();
            }
        });

        roomTextView.setOnClickListener(v -> {
            roomTextView.setClickable(false);
            handler.postDelayed(() -> roomTextView.setClickable(true), 300);

            if (roomException) {
                clearException("room");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (SampleRepository.rooms.size() == 0) {
                getData("getRooms", "");
            } else {
                roomDialog.show();
            }
        });

        caseTextView.setOnClickListener(v -> {
            caseTextView.setClickable(false);
            handler.postDelayed(() -> caseTextView.setClickable(true), 300);

            if (caseException) {
                clearException("case");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (!room.isEmpty()) {
                if (SampleRepository.cases.size() == 0) {
                    getData("getCases", room);
                } else {
                    caseDialog.show();
                }
            } else {
                ExceptionManager.getException(false, 0, null, "SelectRoomFirstException", "sample");
                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        });

        roomReferenceImageView.setOnClickListener(v -> {
            roomReferenceImageView.setClickable(false);
            handler.postDelayed(() -> roomReferenceImageView.setClickable(true), 300);

            if (roomReferenceException) {
                clearException("roomReference");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (!room.isEmpty()) {
                if (SampleRepository.references.size() == 0) {
                    getData("getReferences", room);
                } else {
                    roomReferenceDialog.show();
                }
            } else {
                ExceptionManager.getException(false, 0, null, "SelectRoomFirstException", "sample");
                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        });

        countEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!room.isEmpty()) {
                    if (!countEditText.hasFocus()) {
                        if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                            inputHandler.clear(this, inputHandler.getInput());
                        }

                        inputHandler.focus(countEditText);
                        inputHandler.select(countEditText);
                    }
                } else {
                    ExceptionManager.getException(false, 0, null, "SelectRoomFirstException", "sample");
                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
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
                    roomReferenceCardView.setVisibility(View.GONE);
                } else if (countEditText.length() == 0) {
                    roomReferenceCardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createButton.setOnClickListener(v -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (scaleRecyclerViewAdapter.getValues().size() == 0) {
                errorView("scale");
            }
            if (room.isEmpty()) {
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

            if (scaleRecyclerViewAdapter.getValues().size() != 0 && !room.isEmpty()) {
                doWork();
            }
        });

        scaleDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!scaleDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(scaleDialogEditText);
                    inputHandler.select(scaleDialogEditText);
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
                    // TODO : Reset SearchDialog
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scaleDialog.setOnCancelListener(dialog -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            scaleDialog.dismiss();
        });

        roomDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(roomDialogEditText);
                    inputHandler.select(roomDialogEditText);
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
                    // TODO : Reset SearchDialog
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roomDialog.setOnCancelListener(dialog -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            roomDialog.dismiss();
        });

        caseDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!caseDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(caseDialogEditText);
                    inputHandler.select(caseDialogEditText);
                }
            }
            return false;
        });

        caseDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    // TODO : Reset SearchDialog
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        caseDialog.setOnCancelListener(dialog -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            caseDialog.dismiss();
        });

        roomReferenceDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomReferenceDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(roomReferenceDialogEditText);
                    inputHandler.select(roomReferenceDialogEditText);
                }
            }
            return false;
        });

        roomReferenceDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    // TODO : Reset SearchDialog
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roomReferenceDialog.setOnCancelListener(dialog -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            roomReferenceDialog.dismiss();
        });
    }

    public void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
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
                try {
                    ArrayList<Model> getScales = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        JSONObject scale = new JSONObject().put("name", arrayList.get(i).get("title").toString());
                        getScales.add(new Model(scale));
                    }

                    scaleDialogAdapter.setValue(getScales, method, "CreateSample");
                    recyclerView.setAdapter(scaleDialogAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getRooms":
                try {
                    ArrayList<Model> getManagers = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        JSONObject manager = (JSONObject) arrayList.get(i).get("manager");
                        getManagers.add(new Model(manager));
                    }

                    roomDialogAdapter.setValue(getManagers, method, "CreateSample");
                    recyclerView.setAdapter(roomDialogAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getReferences":
                try {
                    ArrayList<Model> getUsers = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        JSONObject user = (JSONObject) arrayList.get(i).get("user");
                        getUsers.add(new Model(user));
                    }

                    roomReferenceDialogAdapter.setValue(getUsers, method, "CreateSample");
                    recyclerView.setAdapter(roomReferenceDialogAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getCases":
                try {
                    ArrayList<Model> cases = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        StringBuilder name = new StringBuilder();
                        JSONArray client = (JSONArray) arrayList.get(i).get("clients");

                        for (int j = 0; j < client.length(); j++) {
                            JSONObject object = client.getJSONObject(j);
                            JSONObject user = object.getJSONObject("user");

                            if (j == client.length() - 1) {
                                name.append(user.getString("name"));
                            } else {
                                name.append(user.getString("name")).append(" - ");
                            }
                        }

                        if (!name.toString().equals("")) {
                            JSONObject casse = new JSONObject().put("name", name);
                            cases.add(new Model(casse));
                        }
                    }

                    caseDialogAdapter.setValue(cases, method, "CreateSample");
                    recyclerView.setAdapter(caseDialogAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void errorView(String type) {
        if (type.equals("scale")) {
            scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("room")) {
            roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "scale":
                scaleException = true;
                scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
                roomReferenceLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
                scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
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
                roomReferenceLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "caseReference":
                caseReferenceException = false;
                caseReferenceRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void getData(String method, String roomId) {
        try {
            switch (method) {
                case "getScales":
                    scaleProgressBar.setVisibility(View.VISIBLE);
                    scaleImageView.setClickable(false);

                    viewModel.scales("");
                    break;
                case "getRooms":
                    roomProgressBar.setVisibility(View.VISIBLE);
                    roomImageView.setVisibility(View.GONE);
                    roomTextView.setClickable(false);

                    viewModel.rooms("");
                    break;
                case "getCases":
                    caseProgressBar.setVisibility(View.VISIBLE);
                    caseImageView.setVisibility(View.GONE);
                    caseTextView.setClickable(false);

                    viewModel.cases(roomId, "");
                    break;
                case "getReferences":
                    roomReferenceProgressBar.setVisibility(View.VISIBLE);
                    roomReferenceImageView.setClickable(false);

                    viewModel.references(roomId, "");
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        count = countEditText.getText().toString().trim();

        try {
            progressDialog.show();
            viewModel.create(scaleRecyclerViewAdapter.getIds(), room, casse, roomReferenceRecyclerViewAdapter.getIds(), caseReferenceRecyclerViewAdapter.getChecks(), count);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
            switch (SampleRepository.work) {
                case "create":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);
                        finish();

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        observeException();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getScales":
                    if (integer == 1) {
                        scaleDialog.show();
                        setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, SampleRepository.work);

                        scaleProgressBar.setVisibility(View.GONE);
                        scaleImageView.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        scaleProgressBar.setVisibility(View.GONE);
                        scaleImageView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        scaleProgressBar.setVisibility(View.GONE);
                        scaleImageView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getRooms":
                    if (integer == 1) {
                        roomDialog.show();
                        setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, SampleRepository.work);

                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomTextView.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomTextView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomTextView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCases":
                    if (integer == 1) {
                        if (SampleRepository.cases.size() != 0) {
                            caseDialog.show();
                            setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, SampleRepository.work);
                        } else {
                            ExceptionManager.getException(false, 0, null, "EmptyCaseRoomFirstException", "sample");
                            Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        }

                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseTextView.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseTextView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseTextView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getReferences":
                    if (integer == 1) {
                        if (SampleRepository.references.size() != 0) {
                            roomReferenceDialog.show();
                            setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, SampleRepository.work);
                        } else {
                            ExceptionManager.getException(false, 0, null, "EmptyReferenceRoomFirstException", "sample");
                            Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        }

                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceImageView.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceImageView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceImageView.setClickable(true);
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionManager.errors.isNull("scale_id")) {
                errorException("scale");
                exceptionToast = ExceptionManager.getErrorBody("scale_id");
            }
            if (!ExceptionManager.errors.isNull("room_id")) {
                errorException("room");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("room_id");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("room_id"));
                }
            }
            if (!ExceptionManager.errors.isNull("case_id")) {
                errorException("case");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("case_id");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("case_id"));
                }
            }
            if (!ExceptionManager.errors.isNull("client_id")) {
                if (typeTabLayout.getSelectedTabPosition() == 0) {
                    errorException("roomReference");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.getErrorBody("client_id");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.getErrorBody("client_id"));
                    }
                } else if (typeTabLayout.getSelectedTabPosition() == 1) {
                    errorException("caseReference");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.getErrorBody("client_id");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.getErrorBody("client_id"));
                    }
                }
            }
            if (!ExceptionManager.errors.isNull("count")) {
                countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("count");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("count"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        }
    }

    public void observeSearchAdapter(String value, int position, String method) {
        try {
            switch (method) {
                case "getScales":
                    Model scaleModel = SampleRepository.scales.get(position);

                    if (!scaleRecyclerViewAdapter.getIds().contains((String) scaleModel.get("id"))) {
                        JSONObject scale = new JSONObject().put("name", scaleModel.get("title"));

                        scaleRecyclerViewAdapter.getValues().add(new Model(scale));
                        scaleRecyclerViewAdapter.getIds().add((String) scaleModel.get("id"));
                        setRecyclerView(null, scaleRecyclerView, "scales");
                    }

                    if (scaleRecyclerViewAdapter.getValues().size() == 1) {
                        scaleTextView.setVisibility(View.GONE);
                    }

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    scaleDialog.dismiss();
                    break;

                case "getRooms":
                    room = String.valueOf(SampleRepository.rooms.get(position).get("id"));

                    roomTextView.setText(value);
                    roomTextView.setTextColor(getResources().getColor(R.color.Grey));

                    countEditText.setFocusableInTouchMode(true);

                    // Reset Case
                    if (SampleRepository.cases.size() != 0) {
                        casse = "";
                        SampleRepository.cases.clear();
                        caseDialogRecyclerView.setAdapter(null);
                        caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    // Reset RoomReferences
                    if (SampleRepository.references.size() != 0) {
                        SampleRepository.references.clear();
                        roomReferenceDialogRecyclerView.setAdapter(null);
                        roomReferenceTextView.setVisibility(View.VISIBLE);

                        roomReferenceRecyclerViewAdapter.getValues().clear();
                        roomReferenceRecyclerViewAdapter.getIds().clear();
                        roomReferenceRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    // Reset CaseReferences
                    if (caseReferenceRecyclerViewAdapter.getValues() != null) {
                        caseReferenceTextView.setVisibility(View.GONE);

                        caseReferenceRecyclerViewAdapter.getValues().clear();
                        caseReferenceRecyclerViewAdapter.getChecks().clear();
                        caseReferenceRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    // Reset Count
                    if (countEditText.length() != 0) {
                        count = "";
                        countEditText.getText().clear();
                        countEditText.setVisibility(View.VISIBLE);
                    }

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    roomDialog.dismiss();
                    break;

                case "getCases":
                    casse = String.valueOf(SampleRepository.cases.get(position).get("id"));

                    caseTextView.setText(value);
                    caseTextView.setTextColor(getResources().getColor(R.color.Grey));

                    ArrayList<Model> cases = new ArrayList<>();
                    JSONArray clients = (JSONArray) SampleRepository.cases.get(position).get("clients");
                    for (int i = 0; i < clients.length(); i++) {
                        JSONObject object = (JSONObject) clients.get(i);
                        JSONObject user = object.getJSONObject("user");
                        cases.add(new Model(user));
                    }

                    setRecyclerView(cases, caseReferenceRecyclerView, "caseReferences");

                    caseReferenceTextView.setVisibility(View.VISIBLE);
                    caseReferenceRecyclerView.setVisibility(View.VISIBLE);

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    caseDialog.dismiss();
                    break;

                case "getReferences":
                    Model referenceModel = SampleRepository.references.get(position);

                    if (!roomReferenceRecyclerViewAdapter.getIds().contains((String) referenceModel.get("id"))) {
                        JSONObject user = (JSONObject) referenceModel.get("user");

                        roomReferenceRecyclerViewAdapter.getValues().add(new Model(user));
                        roomReferenceRecyclerViewAdapter.getIds().add((String) referenceModel.get("id"));
                        setRecyclerView(null, roomReferenceRecyclerView, "roomReferences");
                    }

                    if (roomReferenceRecyclerViewAdapter.getValues().size() == 1) {
                        roomReferenceTextView.setVisibility(View.GONE);
                        countEditText.setVisibility(View.GONE);
                    }

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    roomReferenceDialog.dismiss();
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