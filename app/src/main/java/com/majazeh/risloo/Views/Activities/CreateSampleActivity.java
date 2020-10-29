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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.InputEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
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
    private InputEditText inputEditText;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    public TextView scaleTextView, roomTextView, caseTextView, roomReferenceTextView, caseReferenceTextView, scaleDialogTextView, roomDialogTextView, caseDialogTextView, roomReferenceDialogTextView, scaleDialogConfirm, roomReferenceDialogConfirm;
    public EditText countEditText, scaleDialogEditText, roomDialogEditText, caseDialogEditText, roomReferenceDialogEditText;
    public RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView, scaleDialogRecyclerView, roomDialogRecyclerView, caseDialogRecyclerView, roomReferenceDialogRecyclerView;
    private ProgressBar scaleDialogProgressBar, roomDialogProgressBar, caseDialogProgressBar, roomReferenceDialogProgressBar;
    private ImageView scaleImageView, roomReferenceImageView, scaleDialogImageView, roomDialogImageView, caseDialogImageView, roomReferenceDialogImageView;
    private CardView roomReferenceCardView;
    private LinearLayout scaleLinearLayout, roomLinearLayout, caseLinearLayout, roomReferenceLinearLayout;
    private FrameLayout roomFrameLayout, caseFrameLayout;
    private Button createButton;
    private CoordinatorLayout scaleDialogSearchLayout, roomDialogSearchLayout, caseDialogSearchLayout, roomReferenceDialogSearchLayout;
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

        inputEditText = new InputEditText();

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        roomTextView = findViewById(R.id.activity_create_sample_room_textView);
        caseTextView = findViewById(R.id.activity_create_sample_case_textView);
        roomReferenceTextView = findViewById(R.id.activity_create_sample_room_reference_textView);
        caseReferenceTextView = findViewById(R.id.activity_create_sample_case_reference_textView);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scaleRecyclerView.setHasFixedSize(true);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
        roomReferenceRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomReferenceRecyclerView.setHasFixedSize(true);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._12sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);

        scaleImageView = findViewById(R.id.activity_create_sample_scale_imageView);
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

        scaleDialogSearchLayout = scaleDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        scaleDialogSearchLayout.setVisibility(View.VISIBLE);
        scaleDialogEditText = scaleDialog.findViewById(R.id.dialog_search_editText);
        scaleDialogTextView = scaleDialog.findViewById(R.id.dialog_search_textView);
        scaleDialogImageView = scaleDialog.findViewById(R.id.dialog_search_imageView);
        scaleDialogProgressBar = scaleDialog.findViewById(R.id.dialog_search_progressBar);
        scaleDialogConfirm = scaleDialog.findViewById(R.id.dialog_search_confirm_textView);
        scaleDialogConfirm.setVisibility(View.VISIBLE);

        roomDialogSearchLayout = roomDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomDialogSearchLayout.setVisibility(View.VISIBLE);
        roomDialogEditText = roomDialog.findViewById(R.id.dialog_search_editText);
        roomDialogTextView = roomDialog.findViewById(R.id.dialog_search_textView);
        roomDialogImageView = roomDialog.findViewById(R.id.dialog_search_imageView);
        roomDialogProgressBar = roomDialog.findViewById(R.id.dialog_search_progressBar);

        caseDialogSearchLayout = caseDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        caseDialogSearchLayout.setVisibility(View.VISIBLE);
        caseDialogEditText = caseDialog.findViewById(R.id.dialog_search_editText);
        caseDialogTextView = caseDialog.findViewById(R.id.dialog_search_textView);
        caseDialogImageView = caseDialog.findViewById(R.id.dialog_search_imageView);
        caseDialogProgressBar = caseDialog.findViewById(R.id.dialog_search_progressBar);

        roomReferenceDialogSearchLayout = roomReferenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        roomReferenceDialogSearchLayout.setVisibility(View.VISIBLE);
        roomReferenceDialogEditText = roomReferenceDialog.findViewById(R.id.dialog_search_editText);
        roomReferenceDialogTextView = roomReferenceDialog.findViewById(R.id.dialog_search_textView);
        roomReferenceDialogImageView = roomReferenceDialog.findViewById(R.id.dialog_search_imageView);
        roomReferenceDialogProgressBar = roomReferenceDialog.findViewById(R.id.dialog_search_progressBar);
        roomReferenceDialogConfirm = roomReferenceDialog.findViewById(R.id.dialog_search_confirm_textView);
        roomReferenceDialogConfirm.setVisibility(View.VISIBLE);

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
                if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                    inputEditText.clear(CreateSampleActivity.this, inputEditText.getInput());
                }

                switch (tab.getPosition()) {
                    case 0:
                        roomLinearLayout.setVisibility(View.VISIBLE);
                        caseLinearLayout.setVisibility(View.GONE);

                        resetData("case");

                        resetData("caseReference");

                        break;
                    case 1:
                        roomLinearLayout.setVisibility(View.GONE);
                        caseLinearLayout.setVisibility(View.VISIBLE);

                        resetData("roomReference");

                        resetData("count");

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

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
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

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
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

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
            }

            if (!room.isEmpty()) {
                if (SampleRepository.cases.size() == 0) {
                    getData("getCases", room, "");
                } else {
                    setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
                    caseDialog.show();
                }
            } else {
                ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException", "sample");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        });

        roomReferenceImageView.setOnClickListener(v -> {
            roomReferenceImageView.setClickable(false);
            handler.postDelayed(() -> roomReferenceImageView.setClickable(true), 300);

            if (roomReferenceException) {
                clearException("roomReference");
            }

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
            }

            if (!room.isEmpty()) {
                if (SampleRepository.references.size() == 0) {
                    getData("getReferences", room, "");
                } else {
                    setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReferences");
                    roomReferenceDialog.show();
                }
            } else {
                ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException", "sample");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        });

        countEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!room.isEmpty()) {
                    if (!countEditText.hasFocus()) {
                        if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                            inputEditText.clear(this, inputEditText.getInput());
                        }

                        inputEditText.focus(countEditText);
                        inputEditText.select(countEditText);
                    }
                } else {
                    ExceptionGenerator.getException(false, 0, null, "SelectRoomFirstException", "sample");
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
            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
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
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(scaleDialogEditText);
                    inputEditText.select(scaleDialogEditText);
                }
            }
            return false;
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

        caseDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!caseDialogEditText.hasFocus()) {
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(caseDialogEditText);
                    inputEditText.select(caseDialogEditText);
                }
            }
            return false;
        });

        roomReferenceDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!roomReferenceDialogEditText.hasFocus()) {
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(roomReferenceDialogEditText);
                    inputEditText.select(roomReferenceDialogEditText);
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
                    } else {
                        getData("getScales", "", scaleDialogEditText.getText().toString().trim());
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
                    if (roomDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");
                    } else {
                        getData("getRooms", "", roomDialogEditText.getText().toString().trim());
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
                    if (caseDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
                    } else {
                        getData("getCases", room, caseDialogEditText.getText().toString().trim());
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
                    if (roomReferenceDialogEditText.length() == 0) {
                        setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReference");
                    } else {
                        getData("getReference", room, roomReferenceDialogEditText.getText().toString().trim());
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

            resetSearch("scales");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            scaleDialog.dismiss();
        });

        roomReferenceDialogConfirm.setOnClickListener(v -> {
            roomReferenceDialogConfirm.setClickable(false);
            handler.postDelayed(() -> roomReferenceDialogConfirm.setClickable(true), 300);

            resetSearch("references");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            roomReferenceDialog.dismiss();
        });

        scaleDialog.setOnCancelListener(dialog -> {
            resetSearch("scales");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            scaleDialog.dismiss();
        });

        roomDialog.setOnCancelListener(dialog -> {
            resetSearch("rooms");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            roomDialog.dismiss();
        });

        caseDialog.setOnCancelListener(dialog -> {
            resetSearch("cases");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            caseDialog.dismiss();
        });

        roomReferenceDialog.setOnCancelListener(dialog -> {
            resetSearch("references");

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
                inputEditText.getInput().getText().clear();
            }

            roomReferenceDialog.dismiss();
        });
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

    private void resetData(String method) {
        switch (method) {
            case "case":
                if (SampleRepository.cases.size() != 0) {
                    casse = "";
                    SampleRepository.cases.clear();
                    SampleRepository.casesSearch.clear();
                    caseDialogRecyclerView.setAdapter(null);
                    caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                    caseTextView.setTextColor(getResources().getColor(R.color.Mischka));
                }
                break;
            case "roomReference":
                if (SampleRepository.references.size() != 0) {
                    SampleRepository.references.clear();
                    SampleRepository.referencesSearch.clear();
                    roomReferenceDialogRecyclerView.setAdapter(null);
                    roomReferenceTextView.setVisibility(View.VISIBLE);

                    roomReferenceRecyclerViewAdapter.getValues().clear();
                    roomReferenceRecyclerViewAdapter.getIds().clear();
                    roomReferenceRecyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case "caseReference":
                if (caseReferenceRecyclerViewAdapter.getValues() != null) {
                    caseReferenceTextView.setVisibility(View.GONE);

                    caseReferenceRecyclerViewAdapter.getValues().clear();
                    caseReferenceRecyclerViewAdapter.getChecks().clear();
                    caseReferenceRecyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case "count":
                if (countEditText.length() != 0) {
                    count = "";
                    countEditText.getText().clear();
                    countEditText.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void resetSearch(String method) {
        switch (method) {
            case "scales":
                if (SampleRepository.scalesSearch.size() != 0) {
                    SampleRepository.scalesSearch.clear();
                    scaleDialogRecyclerView.setAdapter(null);
                }
                break;
            case "rooms":
                if (SampleRepository.roomsSearch.size() != 0) {
                    SampleRepository.roomsSearch.clear();
                    roomDialogRecyclerView.setAdapter(null);
                }
                break;
            case "cases":
                if (SampleRepository.casesSearch.size() != 0) {
                    SampleRepository.casesSearch.clear();
                    caseDialogRecyclerView.setAdapter(null);
                }
                break;
            case "references":
                if (SampleRepository.referencesSearch.size() != 0) {
                    SampleRepository.referencesSearch.clear();
                    roomReferenceDialogRecyclerView.setAdapter(null);
                }
                break;
        }
    }

    private void getData(String method, String roomId, String q) {
        try {
            switch (method) {
                case "getScales":
                    if (q.equals("")) {
                        progressDialog.show();
                    } else {
                        scaleDialogProgressBar.setVisibility(View.VISIBLE);
                        scaleDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.scales(q);
                    break;
                case "getRooms":
                    if (q.equals("")) {
                        progressDialog.show();
                    } else {
                        roomDialogProgressBar.setVisibility(View.VISIBLE);
                        roomDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.rooms(q);
                    break;
                case "getCases":
                    if (q.equals("")) {
                        progressDialog.show();
                    } else {
                        caseDialogProgressBar.setVisibility(View.VISIBLE);
                        caseDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.cases(roomId, q);
                    break;
                case "getReferences":
                    if (q.equals("")) {
                        progressDialog.show();
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
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.scales, scaleDialogRecyclerView, "getScales");
                            scaleDialog.show();

                            progressDialog.dismiss();
                        } else {
                            setRecyclerView(SampleRepository.scalesSearch, scaleDialogRecyclerView, "getScales");

                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            scaleDialogProgressBar.setVisibility(View.GONE);
                            scaleDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getRooms":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.rooms, roomDialogRecyclerView, "getRooms");
                            roomDialog.show();

                            progressDialog.dismiss();
                        } else {
                            setRecyclerView(SampleRepository.roomsSearch, roomDialogRecyclerView, "getRooms");

                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            roomDialogProgressBar.setVisibility(View.GONE);
                            roomDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCases":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.cases, caseDialogRecyclerView, "getCases");
                            caseDialog.show();

                            progressDialog.dismiss();
                        } else {
                            setRecyclerView(SampleRepository.casesSearch, caseDialogRecyclerView, "getCases");

                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            caseDialogProgressBar.setVisibility(View.GONE);
                            caseDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getReferences":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(SampleRepository.references, roomReferenceDialogRecyclerView, "getReferences");
                            roomReferenceDialog.show();

                            progressDialog.dismiss();
                        } else {
                            setRecyclerView(SampleRepository.referencesSearch, roomReferenceDialogRecyclerView, "getReferences");

                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            progressDialog.dismiss();
                        } else {
                            roomReferenceDialogProgressBar.setVisibility(View.GONE);
                            roomReferenceDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.exception.equals("create")) {
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
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getScales":
                    if (!scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        scaleRecyclerViewAdapter.getValues().add(model);
                        scaleRecyclerViewAdapter.getIds().add(model.get("id").toString());
                        setRecyclerView(null, scaleRecyclerView, "scales");
                    } else if (scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        for (int i=0; i<scaleRecyclerViewAdapter.getValues().size(); i++) {
                            if (scaleRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                scaleRecyclerViewAdapter.getValues().remove(model);
                                scaleRecyclerViewAdapter.getIds().remove(model.get("id").toString());
                                scaleRecyclerViewAdapter.notifyItemRemoved(i);
                                scaleRecyclerViewAdapter.notifyItemChanged(i);

                                scaleDialogAdapter.notifyDataSetChanged();

                                break;
                            }
                        }
                    }

                    if (scaleRecyclerViewAdapter.getValues().size() == 0) {
                        scaleTextView.setVisibility(View.VISIBLE);
                    } else {
                        if (scaleTextView.getVisibility() == View.VISIBLE) {
                            scaleTextView.setVisibility(View.GONE);
                        }
                    }
                    break;

                case "getRooms":
                    if (!room.equals(model.get("id").toString())) {
                        room = model.get("id").toString();

                        JSONObject manager = (JSONObject) model.get("manager");

                        roomTextView.setText(manager.get("name").toString());
                        roomTextView.setTextColor(getResources().getColor(R.color.Grey));

                        countEditText.setFocusableInTouchMode(true);
                    } else if (room.equals(model.get("id").toString())) {
                        room = "";

                        roomTextView.setText(getResources().getString(R.string.CreateSampleRoom));
                        roomTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        countEditText.setFocusableInTouchMode(false);
                    }

                    resetData("case");

                    resetData("caseReference");

                    resetData("roomReference");

                    resetData("count");

                    resetSearch("rooms");

                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                        inputEditText.getInput().getText().clear();
                    }

                    roomDialog.dismiss();
                    break;

                case "getCases":
                    if (!casse.equals(model.get("id").toString())) {
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

                            setRecyclerView(cases, caseReferenceRecyclerView, "caseReferences");

                            caseReferenceTextView.setVisibility(View.VISIBLE);
                            caseReferenceRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else if (casse.equals(model.get("id").toString())) {
                        casse = "";

                        caseTextView.setText(getResources().getString(R.string.CreateSampleCase));
                        caseTextView.setTextColor(getResources().getColor(R.color.Mischka));

                        caseReferenceRecyclerView.setAdapter(null);

                        caseReferenceTextView.setVisibility(View.GONE);
                        caseReferenceRecyclerView.setVisibility(View.GONE);
                    }

                    resetSearch("cases");

                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                        inputEditText.getInput().getText().clear();
                    }

                    caseDialog.dismiss();
                    break;

                case "getReferences":
                    if (!roomReferenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        roomReferenceRecyclerViewAdapter.getValues().add(model);
                        roomReferenceRecyclerViewAdapter.getIds().add(model.get("id").toString());
                        setRecyclerView(null, roomReferenceRecyclerView, "roomReferences");
                    } else if (roomReferenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        for (int i=0; i<roomReferenceRecyclerViewAdapter.getValues().size(); i++) {
                            if (roomReferenceRecyclerViewAdapter.getIds().get(i).equals(model.get("id").toString())) {
                                roomReferenceRecyclerViewAdapter.getValues().remove(model);
                                roomReferenceRecyclerViewAdapter.getIds().remove(model.get("id").toString());
                                roomReferenceRecyclerViewAdapter.notifyItemRemoved(i);
                                roomReferenceRecyclerViewAdapter.notifyItemChanged(i);

                                roomReferenceDialogAdapter.notifyDataSetChanged();

                                break;
                            }
                        }
                    }

                    if (roomReferenceRecyclerViewAdapter.getValues().size() == 0) {
                        roomReferenceTextView.setVisibility(View.VISIBLE);

                        countEditText.setVisibility(View.VISIBLE);
                    } else {
                        if (roomReferenceTextView.getVisibility() == View.VISIBLE) {
                            roomReferenceTextView.setVisibility(View.GONE);

                            count = "";
                            countEditText.getText().clear();
                            countEditText.setVisibility(View.GONE);
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