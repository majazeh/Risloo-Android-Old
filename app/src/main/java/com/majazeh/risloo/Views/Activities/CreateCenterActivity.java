package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Models.Managers.BitmapManager;
import com.majazeh.risloo.Utils.InputHandler;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CreateCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SearchAdapter managerDialogAdapter;
    private SpinnerAdapter phoneRecyclerViewAdapter;

    // Vars
    public String type = "personal_clinic", manager = "", title = "", description = "", address = "";
    private String imageFilePath = "";
    private boolean typeException = false, managerException = false, avatarException = false, phoneException =false;
    public boolean galleryPermissionsGranted = false, cameraPermissionsGranted = false;

    // Objects
    private Handler handler;
    private IntentCaller intentCaller;
    private InputHandler inputHandler;
    private ImageDialog imageDialog;
    private Bitmap selectedBitmap;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    public TextView managerTextView, selectTextView, avatarTextView, phoneTextView, managerDialogTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText titleEditText, descriptionEditText, addressEditText, managerDialogEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView, managerDialogRecyclerView;
    private ProgressBar managerProgressBar, managerDialogProgressBar;
    private ImageView managerImageView, phoneImageView, managerDialogImageView;
    private LinearLayout avatarLinearLayout, phoneLinearLayout;
    private FrameLayout managerFrameLayout;
    private Button createButton;
    private Dialog managerDialog, phoneDialog, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_center);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(CenterViewModel.class);

        managerDialogAdapter = new SearchAdapter(this);
        phoneRecyclerViewAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        intentCaller = new IntentCaller();

        inputHandler = new InputHandler();

        imageDialog = new ImageDialog(this);
        imageDialog.setType("createCenter");

        toolbar = findViewById(R.id.activity_create_center_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_center_type_tabLayout);

        managerTextView = findViewById(R.id.activity_create_center_manager_textView);
        selectTextView = findViewById(R.id.activity_create_center_select_textView);
        avatarTextView = findViewById(R.id.activity_create_center_avatar_textView);
        phoneTextView = findViewById(R.id.activity_create_center_phone_textView);

        titleEditText = findViewById(R.id.activity_create_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_create_center_description_editText);
        addressEditText = findViewById(R.id.activity_create_center_address_editText);

        phoneRecyclerView = findViewById(R.id.activity_create_center_phone_recyclerView);
        phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneRecyclerView.setHasFixedSize(true);

        managerProgressBar = findViewById(R.id.activity_create_center_manager_progressBar);

        managerImageView = findViewById(R.id.activity_create_center_manager_imageView);
        phoneImageView = findViewById(R.id.activity_create_center_phone_imageView);

        avatarLinearLayout = findViewById(R.id.activity_create_center_avatar_linearLayout);
        phoneLinearLayout = findViewById(R.id.activity_create_center_phone_linearLayout);

        managerFrameLayout = findViewById(R.id.activity_create_center_manager_frameLayout);

        createButton = findViewById(R.id.activity_create_center_button);

        managerDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(managerDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        managerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        managerDialog.setContentView(R.layout.dialog_search);
        managerDialog.setCancelable(true);
        phoneDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(phoneDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        phoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phoneDialog.setContentView(R.layout.dialog_phone);
        phoneDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsManager = new WindowManager.LayoutParams();
        layoutParamsManager.copyFrom(managerDialog.getWindow().getAttributes());
        layoutParamsManager.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsManager.height = WindowManager.LayoutParams.WRAP_CONTENT;
        managerDialog.getWindow().setAttributes(layoutParamsManager);
        WindowManager.LayoutParams layoutParamsPhone = new WindowManager.LayoutParams();
        layoutParamsPhone.copyFrom(phoneDialog.getWindow().getAttributes());
        layoutParamsPhone.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsPhone.height = WindowManager.LayoutParams.WRAP_CONTENT;
        phoneDialog.getWindow().setAttributes(layoutParamsPhone);

        managerDialogEditText = managerDialog.findViewById(R.id.dialog_search_editText);
        phoneDialogEditText = phoneDialog.findViewById(R.id.dialog_phone_editText);

        managerDialogImageView = managerDialog.findViewById(R.id.dialog_search_imageView);

        managerDialogProgressBar = managerDialog.findViewById(R.id.dialog_search_progressBar);

        managerDialogTextView = managerDialog.findViewById(R.id.dialog_search_textView);

        managerDialogRecyclerView = managerDialog.findViewById(R.id.dialog_search_recyclerView);
        managerDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        managerDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        managerDialogRecyclerView.setHasFixedSize(true);

        phoneDialogPositive = phoneDialog.findViewById(R.id.dialog_phone_positive_textView);
        phoneDialogNegative = phoneDialog.findViewById(R.id.dialog_phone_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            selectTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
            phoneImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            phoneDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            phoneDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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
                if (typeException) {
                    clearException("type");
                }

                if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                    inputHandler.clear(CreateCenterActivity.this, inputHandler.getInput());
                }

                switch (tab.getPosition()) {
                    case 0:
                        type = "personal_clinic";
                        titleEditText.setVisibility(View.GONE);
                        avatarLinearLayout.setVisibility(View.GONE);

                        // Reset Title
                        if (titleEditText.length() != 0) {
                            title = "";
                            titleEditText.getText().clear();
                            titleEditText.setFocusableInTouchMode(true);
                        }

                        // Reset Avatar
                        if (selectedBitmap != null) {
                            selectedBitmap = null;
                            avatarTextView.setText(getResources().getString(R.string.CreateCenterAvatar));
                            avatarTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        }

                        // Reset Counseling Center
                        if (CenterRepository.counselingCenter.size() != 0) {
                            manager = "";
                            CenterRepository.counselingCenter.clear();
                            CenterRepository.counselingCenterSearch.clear();
                            managerDialogRecyclerView.setAdapter(null);
                            managerTextView.setText(getResources().getString(R.string.CreateCenterManager));
                            managerTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        }

                        break;
                    case 1:
                        type = "counseling_center";
                        titleEditText.setVisibility(View.VISIBLE);
                        avatarLinearLayout.setVisibility(View.VISIBLE);

                        // Reset Title
                        if (titleEditText.length() != 0) {
                            title = "";
                            titleEditText.getText().clear();
                            titleEditText.setFocusableInTouchMode(true);
                        }

                        // Reset Avatar
                        if (selectedBitmap != null) {
                            selectedBitmap = null;
                            avatarTextView.setText(getResources().getString(R.string.CreateCenterAvatar));
                            avatarTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        }

                        // Reset Personal Clinic
                        if (CenterRepository.personalClinic.size() != 0) {
                            manager = "";
                            CenterRepository.personalClinic.clear();
                            CenterRepository.personalClinicSearch.clear();
                            managerDialogRecyclerView.setAdapter(null);
                            managerTextView.setText(getResources().getString(R.string.CreateCenterManager));
                            managerTextView.setTextColor(getResources().getColor(R.color.Mischka));
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

        managerTextView.setOnClickListener(v -> {
            managerTextView.setClickable(false);
            handler.postDelayed(() -> managerTextView.setClickable(true), 300);

            if (managerException) {
                clearException("manager");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (type.equals("personal_clinic")) {
                if (CenterRepository.personalClinic.size() == 0) {
                    getData("getPersonalClinic", "");
                } else {
                    setRecyclerView(CenterRepository.personalClinic, managerDialogRecyclerView, "getPersonalClinic");
                    managerDialog.show();
                }
            } else {
                if (CenterRepository.counselingCenter.size() == 0) {
                    getData("getCounselingCenter", "");
                } else {
                    setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getCounselingCenter");
                    managerDialog.show();
                }
            }
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(titleEditText);
                    inputHandler.select(titleEditText);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!descriptionEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(descriptionEditText);
                    inputHandler.select(descriptionEditText);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!addressEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(addressEditText);
                    inputHandler.select(addressEditText);
                }
            }
            return false;
        });

        selectTextView.setOnClickListener(v -> {
            selectTextView.setClickable(false);
            handler.postDelayed(() -> selectTextView.setClickable(true), 300);

            if (avatarException) {
                clearException("avatar");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        avatarTextView.setOnClickListener(v -> {
            avatarTextView.setClickable(false);
            handler.postDelayed(() -> avatarTextView.setClickable(true), 300);

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (selectedBitmap != null) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", "");
                intent.putExtra("bitmap", true);
                if (imageFilePath.equals("")) {
                    intent.putExtra("path", "");
                } else {
                    intent.putExtra("path", imageFilePath);
                }
                FileManager.writeBitmapToCache(this, selectedBitmap, "image");

                startActivity(intent);
            }
        });

        createButton.setOnClickListener(v -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (type.equals("personal_clinic")) {
                if (manager.isEmpty()) {
                    errorView("manager");
                }

                if (typeException) {
                    clearException("type");
                }
                if (managerException) {
                    clearException("manager");
                }
                if (avatarException) {
                    clearException("avatar");
                }
                if (phoneException) {
                    clearException("phone");
                }

                if (!manager.isEmpty()) {
                    doWork();
                }
            } else {
                if (manager.isEmpty()) {
                    errorView("manager");
                }
                if (titleEditText.length() == 0) {
                    errorView("title");
                }

                if (typeException) {
                    clearException("type");
                }
                if (managerException) {
                    clearException("manager");
                }
                if (avatarException) {
                    clearException("avatar");
                }
                if (phoneException) {
                    clearException("phone");
                }

                if (!manager.isEmpty() && titleEditText.length() != 0) {
                    inputHandler.clear(this, titleEditText);

                    doWork();
                }
            }

        });

        managerDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!managerDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(managerDialogEditText);
                    inputHandler.select(managerDialogEditText);
                }
            }
            return false;
        });

        managerDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (type.equals("personal_clinic")) {
                        if (managerDialogEditText.length() == 0) {
                            setRecyclerView(CenterRepository.personalClinic, managerDialogRecyclerView, "getPersonalClinic");
                        } else if (managerDialogEditText.length() == 1 || managerDialogEditText.length() == 2 ) {
                            ExceptionManager.getException(false, 0, null, "MustBeThreeCharException", "center");
                            Toast.makeText(CreateCenterActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        } else {
                            getData("getPersonalClinic", managerDialogEditText.getText().toString().trim());
                        }
                    } else {
                        if (managerDialogEditText.length() == 0) {
                            setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getCounselingCenter");
                        } else if (managerDialogEditText.length() == 1 || managerDialogEditText.length() == 2 ) {
                            ExceptionManager.getException(false, 0, null, "MustBeThreeCharException", "center");
                            Toast.makeText(CreateCenterActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        } else {
                            getData("getCounselingCenter", managerDialogEditText.getText().toString().trim());
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        managerDialog.setOnCancelListener(dialog -> {
            // Reset Manager Search
            if (type.equals("personal_clinic")) {
                if (CenterRepository.personalClinicSearch.size() != 0) {
                    CenterRepository.personalClinicSearch.clear();
                    managerDialogRecyclerView.setAdapter(null);
                }
            } else {
                if (CenterRepository.counselingCenterSearch.size() != 0) {
                    CenterRepository.counselingCenterSearch.clear();
                    managerDialogRecyclerView.setAdapter(null);
                }
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            managerDialog.dismiss();
        });

        phoneImageView.setOnClickListener(v -> {
            phoneImageView.setClickable(false);
            handler.postDelayed(() -> phoneImageView.setClickable(true), 300);

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            phoneDialog.show();
        });

        phoneDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!phoneDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(phoneDialogEditText);
                    inputHandler.select(phoneDialogEditText);
                }
            }
            return false;
        });

        phoneDialogPositive.setOnClickListener(v -> {
            phoneDialogPositive.setClickable(false);
            handler.postDelayed(() -> phoneDialogPositive.setClickable(true), 300);

            if (phoneDialogEditText.length() != 0) {
                if (!phoneRecyclerViewAdapter.getIds().contains(phoneDialogEditText.getText().toString().trim())) {
                    try {
                        JSONObject phone = new JSONObject().put("name", phoneDialogEditText.getText().toString().trim());

                        phoneRecyclerViewAdapter.getValues().add(new Model(phone));
                        setRecyclerView(phoneRecyclerViewAdapter.getValues(), phoneRecyclerView, "phones");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (phoneRecyclerViewAdapter.getValues().size() == 1) {
                    phoneTextView.setVisibility(View.GONE);
                }

                if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                    inputHandler.clear(this, inputHandler.getInput());
                    inputHandler.getInput().getText().clear();
                }

                phoneDialog.dismiss();
            } else {
                errorView("phone");
            }
        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 300);

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            phoneDialog.dismiss();
        });

        phoneDialog.setOnCancelListener(dialog -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            phoneDialog.dismiss();
        });
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        if (method.equals("phones")) {
            try {
                ArrayList<String> phones = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    phones.add(arrayList.get(i).get("name").toString());
                }

                phoneRecyclerViewAdapter.setValue(arrayList, phones, method, "CreateCenter");
                recyclerView.setAdapter(phoneRecyclerViewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (method.equals("getPersonalClinic") || method.equals("getCounselingCenter")) {
            managerDialogAdapter.setValue(arrayList, method, "CreateCenter");
            recyclerView.setAdapter(managerDialogAdapter);
            
            if (arrayList.size() == 0) {
                managerDialogTextView.setVisibility(View.VISIBLE);
            } else {
                if (managerDialogTextView.getVisibility() == View.VISIBLE) {
                    managerDialogTextView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void errorView(String type) {
        switch (type) {
            case "manager":
                managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "title":
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "phone":
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "type":
                typeException = true;
                typeTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "manager":
                managerException = true;
                managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "avatar":
                avatarException = true;
                avatarLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "phone":
                phoneException = true;
                phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "type":
                typeException = false;
                typeTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "manager":
                managerException = false;
                managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "avatar":
                avatarException = false;
                avatarLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "phone":
                phoneException = false;
                phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void getData(String method, String q) {
        try {
            switch (method) {
                case "getPersonalClinic":
                    if (q.equals("")) {
                        managerProgressBar.setVisibility(View.VISIBLE);
                        managerImageView.setVisibility(View.GONE);
                        managerTextView.setClickable(false);
                    } else {
                        managerDialogProgressBar.setVisibility(View.VISIBLE);
                        managerDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.personalClinic(q);
                    break;
                case "getCounselingCenter":
                    if (q.equals("")) {
                        managerProgressBar.setVisibility(View.VISIBLE);
                        managerImageView.setVisibility(View.GONE);
                        managerTextView.setClickable(false);
                    } else {
                        managerDialogProgressBar.setVisibility(View.VISIBLE);
                        managerDialogImageView.setVisibility(View.GONE);
                    }

                    viewModel.counselingCenter(q);
                    break;
            }
            observeWork(q);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        if (type.equals("personal_clinic")) {
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

            try {
                progressDialog.show();
                viewModel.create(type, manager, "", "", address, description, phoneRecyclerViewAdapter.getIds());
                observeWork(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            title = titleEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

//            FileManager.writeBitmapToCache(this, selectedBitmap, "image");

            try {
                progressDialog.show();
                viewModel.create(type, manager, title, "", address, description, phoneRecyclerViewAdapter.getIds());
                observeWork(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void observeWork(String q) {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            switch (CenterRepository.work) {
                case "create":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);
                        finish();

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        observeException();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getPersonalClinic":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(CenterRepository.personalClinic, managerDialogRecyclerView, "getPersonalClinic");
                            managerDialog.show();

                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            setRecyclerView(CenterRepository.personalClinicSearch, managerDialogRecyclerView, "getPersonalClinic");

                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCounselingCenter":
                    if (integer == 1) {
                        if (q.equals("")) {
                            setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getCounselingCenter");
                            managerDialog.show();

                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            setRecyclerView(CenterRepository.counselingCenterSearch, managerDialogRecyclerView, "getCounselingCenter");

                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        if (q.equals("")) {
                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        if (q.equals("")) {
                            managerProgressBar.setVisibility(View.GONE);
                            managerImageView.setVisibility(View.VISIBLE);
                            managerTextView.setClickable(true);
                        } else {
                            managerDialogProgressBar.setVisibility(View.GONE);
                            managerDialogImageView.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionManager.errors.isNull("type")) {
                errorException("type");
                exceptionToast = ExceptionManager.getErrorBody("type");
            }
            if (!ExceptionManager.errors.isNull("manager_id")) {
                errorException("manager");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("manager_id");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("manager_id"));
                }
            }
            if (!ExceptionManager.errors.isNull("title")) {
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("title");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("title"));
                }
            }
            if (!ExceptionManager.errors.isNull("avatar")) {
                errorException("avatar");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("avatar");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("avatar"));
                }
            }
            if (!ExceptionManager.errors.isNull("description")) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("description");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("description"));
                }
            }
            if (!ExceptionManager.errors.isNull("address")) {
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("address");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("address"));
                }
            }
            if (!ExceptionManager.errors.isNull("phone_numbers")) {
                errorException("phone");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("phone_numbers");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("phone_numbers"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            manager = model.get("id").toString();

            managerTextView.setText(model.get("name").toString());
            managerTextView.setTextColor(getResources().getColor(R.color.Grey));

            // Reset Manager Search
            if (type.equals("personal_clinic")) {
                if (CenterRepository.personalClinicSearch.size() != 0) {
                    CenterRepository.personalClinicSearch.clear();
                    managerDialogRecyclerView.setAdapter(null);
                }
            } else {
                if (CenterRepository.counselingCenterSearch.size() != 0) {
                    CenterRepository.counselingCenterSearch.clear();
                    managerDialogRecyclerView.setAdapter(null);
                }
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }

            managerDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() throws IOException {
        String imageFileName = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date()) + "_";
        String imageFileSuffix = ".jpg";
        File imageStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, imageFileSuffix, imageStorageDir);
        imageFilePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void checkGalleryPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                galleryPermissionsGranted = true;
                intentCaller.gallery(this);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        } else {
            galleryPermissionsGranted = true;
            intentCaller.gallery(this);
        }
    }

    public void checkCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraPermissionsGranted = true;
                    try {
                        intentCaller.camera(this, createImageFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 200);
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, 200);
            }
        } else {
            cameraPermissionsGranted = true;
            try {
                intentCaller.camera(this, createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            galleryPermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                galleryPermissionsGranted = true;
                intentCaller.gallery(this);
            }
        } else if (requestCode == 200) {
            cameraPermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                cameraPermissionsGranted = true;
                try {
                    intentCaller.camera(this, createImageFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                try {
                    Uri imageUri = Objects.requireNonNull(data).getData();
                    InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                    selectedBitmap = BitmapManager.scaleToCenter(imageBitmap);

                    imageFilePath = "";

                    avatarTextView.setText(imageUri.getPath());
                    avatarTextView.setTextColor(getResources().getColor(R.color.Grey));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 200) {
                File imageFile = new File(imageFilePath);
                intentCaller.mediaScan(this, imageFile);

                int scaleFactor = Math.max(1, 2);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;

                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath, options);

                selectedBitmap = BitmapManager.modifyOrientation(BitmapManager.scaleToCenter(imageBitmap), imageFilePath);

                avatarTextView.setText(imageFilePath);
                avatarTextView.setTextColor(getResources().getColor(R.color.Grey));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100) {
                ExceptionManager.getException(false, 0, null, "GalleryException", "center");
                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 200) {
                ExceptionManager.getException(false, 0, null, "CameraException", "center");
                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}