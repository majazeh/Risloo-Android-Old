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
    public SpinnerAdapter scaleRecyclerViewAdapter, roomReferenceRecyclerViewAdapter;
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
    public TextView scaleTextView, roomTextView, caseTextView, roomReferenceTextView, caseReferenceTextView, scaleDialogTextView, roomDialogTextView, caseDialogTextView, roomReferenceDialogTextView, scaleDialogConfirm, roomReferenceDialogConfirm;
    public EditText countEditText, scaleDialogEditText, roomDialogEditText, caseDialogEditText, roomReferenceDialogEditText;
    public RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView, scaleDialogRecyclerView, roomDialogRecyclerView, caseDialogRecyclerView, roomReferenceDialogRecyclerView;
    private ProgressBar scaleProgressBar, roomProgressBar, caseProgressBar, roomReferenceProgressBar, scaleDialogProgressBar, roomDialogProgressBar, caseDialogProgressBar, roomReferenceDialogProgressBar;
    private ImageView scaleImageView, roomImageView, caseImageView, roomReferenceImageView, scaleDialogImageView, roomDialogImageView, caseDialogImageView, roomReferenceDialogImageView;
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

        scaleDialogConfirm = scaleDialog.findViewById(R.id.dialog_search_confirm_textView);
        scaleDialogConfirm.setVisibility(View.VISIBLE);
        roomReferenceDialogConfirm = roomReferenceDialog.findViewById(R.id.dialog_search_confirm_textView);
        roomReferenceDialogConfirm.setVisibility(View.VISIBLE);

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

            scaleDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            roomReferenceDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);

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
                            SampleRepository.casesSearch.clear();
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
                            SampleRepository.referencesSearch.clear();
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
                getData("getScales", "", "");
            } else {
                setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales");
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
                getData("getRooms", "", "");
            } else {
                setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");
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
                    getData("getCases", room, "");
                } else {
                    setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
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
                    getData("getReferences", room, "");
                } else {
                    setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReferences");
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
                    if (scaleDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales");
                    } else if (scaleDialogEditText.length() == 1) {
                        ExceptionManager.getException(false, 0, null, "MustBeTwoCharException", "sample");
                        Toast.makeText(CreateSampleActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    } else {
                        getData("getScales", "", scaleDialogEditText.getText().toString().trim());
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scaleDialogConfirm.setOnClickListener(v -> {
            scaleDialogConfirm.setClickable(false);
            handler.postDelayed(() -> scaleDialogConfirm.setClickable(true), 300);

            // Reset Scale Search
            if (SampleRepository.scalesSearch.size() != 0) {
                SampleRepository.scalesSearch.clear();
                scaleDialogRecyclerView.setAdapter(null);
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            scaleDialog.dismiss();
        });

        scaleDialog.setOnCancelListener(dialog -> {
            // Reset Scale Search
            if (SampleRepository.scalesSearch.size() != 0) {
                SampleRepository.scalesSearch.clear();
                scaleDialogRecyclerView.setAdapter(null);
            }

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
                    if (roomDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");
                    } else if (roomDialogEditText.length() == 1) {
                        ExceptionManager.getException(false, 0, null, "MustBeTwoCharException", "sample");
                        Toast.makeText(CreateSampleActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    } else {
                        getData("getRooms", "", roomDialogEditText.getText().toString().trim());
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roomDialog.setOnCancelListener(dialog -> {
            // Reset Room Search
            if (SampleRepository.roomsSearch.size() != 0) {
                SampleRepository.roomsSearch.clear();
                roomDialogRecyclerView.setAdapter(null);
            }

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
                    if (caseDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
                    } else if (caseDialogEditText.length() == 1) {
                        ExceptionManager.getException(false, 0, null, "MustBeTwoCharException", "sample");
                        Toast.makeText(CreateSampleActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    } else {
                        getData("getCases", room, caseDialogEditText.getText().toString().trim());
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        caseDialog.setOnCancelListener(dialog -> {
            // Reset Case Search
            if (SampleRepository.casesSearch.size() != 0) {
                SampleRepository.casesSearch.clear();
                caseDialogRecyclerView.setAdapter(null);
            }

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
                    if (roomReferenceDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReference");
                    } else if (roomReferenceDialogEditText.length() == 1) {
                        ExceptionManager.getException(false, 0, null, "MustBeTwoCharException", "sample");
                        Toast.makeText(CreateSampleActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    } else {
                        getData("getReference", room, roomReferenceDialogEditText.getText().toString().trim());
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roomReferenceDialogConfirm.setOnClickListener(v -> {
            roomReferenceDialogConfirm.setClickable(false);
            handler.postDelayed(() -> roomReferenceDialogConfirm.setClickable(true), 300);

            // Reset Reference Search
            if (SampleRepository.referencesSearch.size() != 0) {
                SampleRepository.referencesSearch.clear();
                roomReferenceDialogRecyclerView.setAdapter(null);
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            roomReferenceDialog.dismiss();
        });

        roomReferenceDialog.setOnCancelListener(dialog -> {
            // Reset Reference Search
            if (SampleRepository.referencesSearch.size() != 0) {
                SampleRepository.referencesSearch.clear();
                roomReferenceDialogRecyclerView.setAdapter(null);
            }

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

    private void getData(String method, String roomId, String q) {
        try {
            switch (method) {
                case "getScales":
                    if (q.equals("")) {
                        scaleProgressBar.setVisibility(View.VISIBLE);
                        scaleImageView.setClickable(false);
                    } else {
                        scaleDialogProgressBar.setVisibility(View.VISIBLE);
                        scaleDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.scales(q);
                    break;
                case "getRooms":
                    if (q.equals("")) {
                        roomProgressBar.setVisibility(View.VISIBLE);
                        roomImageView.setVisibility(View.GONE);
                        roomTextView.setClickable(false);
                    } else {
                        roomDialogProgressBar.setVisibility(View.VISIBLE);
                        roomDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.rooms(q);
                    break;
                case "getCases":
                    if (q.equals("")) {
                        caseProgressBar.setVisibility(View.VISIBLE);
                        caseImageView.setVisibility(View.GONE);
                        caseTextView.setClickable(false);
                    } else {
                        caseDialogProgressBar.setVisibility(View.VISIBLE);
                        caseDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.cases(roomId, q);
                    break;
                case "getReferences":
                    if (q.equals("")) {
                        roomReferenceProgressBar.setVisibility(View.VISIBLE);
                        roomReferenceImageView.setClickable(false);
                    } else {
                        roomReferenceDialogProgressBar.setVisibility(View.VISIBLE);
                        roomReferenceDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.references(roomId, q);
                    break;
            }
            observeWork(q);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        count = countEditText.getText().toString().trim();

        try {
            progressDialog.show();
            viewModel.create(scaleRecyclerViewAdapter.getIds(), room, casse, roomReferenceRecyclerViewAdapter.getIds(), caseReferenceRecyclerViewAdapter.getChecks(), count);
            observeWork(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String q) {
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
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales");
                            scaleDialog.show();

                            scaleProgressBar.setVisibility(View.GONE);
                            scaleImageView.setClickable(true);
                        } else {
                            setRecyclerView(SampleRepository.scalesSearch, scaleDialogRecyclerView, "getScales");

                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            scaleProgressBar.setVisibility(View.GONE);
                            scaleImageView.setClickable(true);
                        } else {
                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            scaleProgressBar.setVisibility(View.GONE);
                            scaleImageView.setClickable(true);
                        } else {
                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getRooms":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");
                            roomDialog.show();

                            roomProgressBar.setVisibility(View.GONE);
                            roomImageView.setVisibility(View.VISIBLE);
                            roomTextView.setClickable(true);
                        } else {
                            setRecyclerView(SampleRepository.roomsSearch, roomDialogRecyclerView, "getRooms");

                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            roomProgressBar.setVisibility(View.GONE);
                            roomImageView.setVisibility(View.VISIBLE);
                            roomTextView.setClickable(true);
                        } else {
                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            roomProgressBar.setVisibility(View.GONE);
                            roomImageView.setVisibility(View.VISIBLE);
                            roomTextView.setClickable(true);
                        } else {
                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCases":
                    if (integer == 1) {
                        if (q.equals("")) {
                            if (SampleRepository.cases.size() != 0) {
                                setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
                                caseDialog.show();
                            } else {
                                ExceptionManager.getException(false, 0, null, "EmptyCaseForRoomException", "sample");
                                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                            }

                            caseProgressBar.setVisibility(View.GONE);
                            caseImageView.setVisibility(View.VISIBLE);
                            caseTextView.setClickable(true);
                        } else {
                            setRecyclerView(SampleRepository.casesSearch, caseDialogRecyclerView, "getCases");

                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            caseProgressBar.setVisibility(View.GONE);
                            caseImageView.setVisibility(View.VISIBLE);
                            caseTextView.setClickable(true);
                        } else {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            caseProgressBar.setVisibility(View.GONE);
                            caseImageView.setVisibility(View.VISIBLE);
                            caseTextView.setClickable(true);
                        } else {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getReferences":
                    if (integer == 1) {
                        if (q.equals("")) {
                            if (SampleRepository.references.size() != 0) {
                                setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReferences");
                                roomReferenceDialog.show();
                            } else {
                                ExceptionManager.getException(false, 0, null, "EmptyReferenceForRoomException", "sample");
                                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                            }

                            roomReferenceProgressBar.setVisibility(View.GONE);
                            roomReferenceImageView.setClickable(true);
                        } else {
                            setRecyclerView(SampleRepository.referencesSearch, roomReferenceDialogRecyclerView, "getReferences");

                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            roomReferenceProgressBar.setVisibility(View.GONE);
                            roomReferenceImageView.setClickable(true);
                        } else {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            roomReferenceProgressBar.setVisibility(View.GONE);
                            roomReferenceImageView.setClickable(true);
                        } else {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
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

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getScales":
                    if (!scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        JSONObject scale = new JSONObject().put("name", model.get("title"));

                        scaleRecyclerViewAdapter.getValues().add(new Model(scale));
                        scaleRecyclerViewAdapter.getIds().add(model.get("id").toString());
                        setRecyclerView(null, scaleRecyclerView, "scales");
                    }

                    if (scaleRecyclerViewAdapter.getValues().size() == 1) {
                        scaleTextView.setVisibility(View.GONE);
                    }
                    break;

                case "getRooms":
                    room = model.get("id").toString();

                    JSONObject manager = (JSONObject) model.get("manager");

                    roomTextView.setText(manager.get("name").toString());
                    roomTextView.setTextColor(getResources().getColor(R.color.Grey));

                    countEditText.setFocusableInTouchMode(true);

                    // Reset Case
                    if (SampleRepository.cases.size() != 0) {
                        casse = "";
                        SampleRepository.cases.clear();
                        SampleRepository.casesSearch.clear();
                        caseDialogRecyclerView.setAdapter(null);
                        caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    // Reset RoomReferences
                    if (SampleRepository.references.size() != 0) {
                        SampleRepository.references.clear();
                        SampleRepository.referencesSearch.clear();
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

                    // Reset Room Search
                    if (SampleRepository.roomsSearch.size() != 0) {
                        SampleRepository.roomsSearch.clear();
                        roomDialogRecyclerView.setAdapter(null);
                    }

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    roomDialog.dismiss();
                    break;

                case "getCases":
                    casse = model.get("id").toString();

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

                        caseTextView.setText(casse.get("name").toString());
                        caseTextView.setTextColor(getResources().getColor(R.color.Grey));
                    }

                    setRecyclerView(cases, caseReferenceRecyclerView, "caseReferences");

                    caseReferenceTextView.setVisibility(View.VISIBLE);
                    caseReferenceRecyclerView.setVisibility(View.VISIBLE);

                    // Reset Case Search
                    if (SampleRepository.casesSearch.size() != 0) {
                        SampleRepository.casesSearch.clear();
                        caseDialogRecyclerView.setAdapter(null);
                    }

                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                        inputHandler.getInput().getText().clear();
                    }

                    caseDialog.dismiss();
                    break;

                case "getReferences":
                    if (!roomReferenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        JSONObject user = (JSONObject) model.get("user");

                        roomReferenceRecyclerViewAdapter.getValues().add(new Model(user));
                        roomReferenceRecyclerViewAdapter.getIds().add(model.get("id").toString());
                        setRecyclerView(null, roomReferenceRecyclerView, "roomReferences");
                    }

                    if (roomReferenceRecyclerViewAdapter.getValues().size() == 1) {
                        roomReferenceTextView.setVisibility(View.GONE);
                        countEditText.setVisibility(View.GONE);
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