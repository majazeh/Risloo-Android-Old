package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
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
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.BitmapManager;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class CreateCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SearchAdapter managerDialogAdapter;
    private SpinnerAdapter phoneRecyclerViewAdapter;

    // Vars
    public String type = "", managerId = "", managerName = "", title = "", description = "", address = "";
    public String imageFilePath = "";
    private boolean typeException = false, managerException = false, avatarException = false, phoneException =false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager phoneLayoutManager;
    private PathManager pathManager;
    private ImageDialog imageDialog;
    private Bitmap selectedBitmap;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TabLayout typeTabLayout;
    private FrameLayout managerFrameLayout, phoneFrameLayout;
    private LinearLayout managerLinearLayout, avatarLinearLayout;
    public TextView managerNameTextView, managerIdTextView, selectTextView, avatarTextView, phoneTextView;
    private EditText titleEditText, descriptionEditText, addressEditText;
    private RecyclerView phoneRecyclerView;
    private Button createButton;
    private Dialog managerDialog, phoneDialog, progressDialog;
    private TextView managerDialogTitleTextView;
    private CoordinatorLayout managerDialogSearchLayout;
    private EditText managerDialogEditText;
    private ImageView managerDialogImageView;
    private ProgressBar managerDialogProgressBar;
    private TextView managerDialogTextView;
    private RecyclerView managerDialogRecyclerView;
    private TextView phoneDialogTitle, phoneDialogPositive, phoneDialogNegative;
    private EditText phoneDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_center);

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
        viewModel = new ViewModelProvider(this).get(CenterViewModel.class);

        managerDialogAdapter = new SearchAdapter(this);

        phoneRecyclerViewAdapter = new SpinnerAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        phoneLayoutManager = new FlexboxLayoutManager(this);
        phoneLayoutManager.setFlexDirection(FlexDirection.ROW);
        phoneLayoutManager.setFlexWrap(FlexWrap.WRAP);

        pathManager = new PathManager();

        imageDialog = new ImageDialog(this);
        imageDialog.setType("CreateCenter");

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CreateCenterTitle));

        typeTabLayout = findViewById(R.id.activity_create_center_type_tabLayout);

        titleEditText = findViewById(R.id.activity_create_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_create_center_description_editText);
        addressEditText = findViewById(R.id.activity_create_center_address_editText);

        managerFrameLayout = findViewById(R.id.activity_create_center_manager_frameLayout);
        phoneFrameLayout = findViewById(R.id.activity_create_center_phone_frameLayout);

        managerLinearLayout = findViewById(R.id.activity_create_center_manager_linearLayout);
        avatarLinearLayout = findViewById(R.id.activity_create_center_avatar_linearLayout);

        managerNameTextView = findViewById(R.id.activity_create_center_manager_name_textView);
        managerIdTextView = findViewById(R.id.activity_create_center_manager_id_textView);
        selectTextView = findViewById(R.id.activity_create_center_select_textView);
        avatarTextView = findViewById(R.id.activity_create_center_avatar_textView);
        phoneTextView = findViewById(R.id.activity_create_center_phone_textView);

        phoneRecyclerView = findViewById(R.id.activity_create_center_phone_recyclerView);
        phoneRecyclerView.setLayoutManager(phoneLayoutManager);
        phoneRecyclerView.setHasFixedSize(false);

        createButton = findViewById(R.id.activity_create_center_button);

        managerDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(managerDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        managerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        managerDialog.setContentView(R.layout.dialog_search);
        managerDialog.setCancelable(true);
        phoneDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(phoneDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        phoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phoneDialog.setContentView(R.layout.dialog_type);
        phoneDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsManagerDialog = new WindowManager.LayoutParams();
        layoutParamsManagerDialog.copyFrom(managerDialog.getWindow().getAttributes());
        layoutParamsManagerDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsManagerDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        managerDialog.getWindow().setAttributes(layoutParamsManagerDialog);
        WindowManager.LayoutParams layoutParamsPhoneDialog = new WindowManager.LayoutParams();
        layoutParamsPhoneDialog.copyFrom(phoneDialog.getWindow().getAttributes());
        layoutParamsPhoneDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsPhoneDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        phoneDialog.getWindow().setAttributes(layoutParamsPhoneDialog);

        managerDialogTitleTextView = managerDialog.findViewById(R.id.dialog_search_title_textView);
        managerDialogTitleTextView.setText(getResources().getString(R.string.CreateCenterManagerDialogTitle));

        managerDialogSearchLayout = managerDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        managerDialogSearchLayout.setVisibility(View.VISIBLE);

        managerDialogEditText = managerDialog.findViewById(R.id.dialog_search_editText);
        managerDialogImageView = managerDialog.findViewById(R.id.dialog_search_imageView);
        managerDialogProgressBar = managerDialog.findViewById(R.id.dialog_search_progressBar);

        managerDialogTextView = managerDialog.findViewById(R.id.dialog_search_textView);

        managerDialogRecyclerView = managerDialog.findViewById(R.id.dialog_search_recyclerView);
        managerDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        managerDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        managerDialogRecyclerView.setHasFixedSize(true);

        phoneDialogTitle = phoneDialog.findViewById(R.id.dialog_type_title_textView);
        phoneDialogTitle.setText(getResources().getString(R.string.CreateCenterPhoneDialogTitle));
        phoneDialogInput = phoneDialog.findViewById(R.id.dialog_type_input_editText);
        phoneDialogInput.setHint(getResources().getString(R.string.CreateCenterPhoneDialogInput));
        phoneDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        phoneDialogPositive = phoneDialog.findViewById(R.id.dialog_type_positive_textView);
        phoneDialogPositive.setText(getResources().getString(R.string.CreateCenterPhoneDialogPositive));
        phoneDialogPositive.setTextColor(getResources().getColor(R.color.Risloo800));
        phoneDialogNegative = phoneDialog.findViewById(R.id.dialog_type_negative_textView);
        phoneDialogNegative.setText(getResources().getString(R.string.CreateCenterPhoneDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            selectTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            phoneDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            phoneDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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
                if (typeException) {
                    clearException("type");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(CreateCenterActivity.this, controlEditText.input());
                }

                switch (tab.getPosition()) {
                    case 0:
                        type = "personal_clinic";

                        titleEditText.setVisibility(View.GONE);
                        avatarLinearLayout.setVisibility(View.GONE);

                        resetData("manager");

                        resetData("title");

                        resetData("avatar");

                        break;
                    case 1:
                        type = "counseling_center";

                        titleEditText.setVisibility(View.VISIBLE);
                        avatarLinearLayout.setVisibility(View.VISIBLE);

                        resetData("manager");

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

        managerLinearLayout.setOnClickListener(v -> {
            managerLinearLayout.setClickable(false);
            handler.postDelayed(() -> managerLinearLayout.setClickable(true), 250);

            if (managerException) {
                clearException("manager");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            managerDialog.show();
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(titleEditText);
                    controlEditText.select(titleEditText);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!descriptionEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(descriptionEditText);
                    controlEditText.select(descriptionEditText);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!addressEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(addressEditText);
                    controlEditText.select(addressEditText);
                }
            }
            return false;
        });

        selectTextView.setOnClickListener(v -> {
            selectTextView.setClickable(false);
            handler.postDelayed(() -> selectTextView.setClickable(true), 250);

            if (avatarException) {
                clearException("avatar");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        avatarTextView.setOnClickListener(v -> {
            avatarTextView.setClickable(false);
            handler.postDelayed(() -> avatarTextView.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (selectedBitmap != null) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", "");
                intent.putExtra("bitmap", true);
                intent.putExtra("path", imageFilePath);
                FileManager.writeBitmapToCache(this, selectedBitmap, "image");

                startActivity(intent);
            }
        });

        phoneRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (phoneException) {
                    clearException("phone");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                phoneDialog.show();
            }
            return false;
        });

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            switch (type) {
                case "personal_clinic":
                    if (managerId.equals("")) {
                        errorException("manager");
                    }

                    if (!managerId.equals("")) {
                        clearException("manager");

                        doWork();
                    }
                    break;

                case "counseling_center":
                    if (managerId.equals("")) {
                        errorException("manager");
                    }
                    if (titleEditText.length() == 0) {
                        controlEditText.error(this, titleEditText);
                    }

                    if (!managerId.equals("") && titleEditText.length() != 0) {
                        clearException("manager");
                        controlEditText.clear(this, titleEditText);

                        doWork();
                    }
                    break;
            }
        });

        managerDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!managerDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(managerDialogEditText);
                    controlEditText.select(managerDialogEditText);
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
                    if (managerDialogEditText.length() != 0) {
                        if (type.equals("personal_clinic")) {
                            getData("getPersonalClinic", managerDialogEditText.getText().toString().trim());
                        } else {
                            getData("getCounselingCenter", managerDialogEditText.getText().toString().trim());
                        }
                    } else {
                        managerDialogRecyclerView.setAdapter(null);

                        if (managerDialogTextView.getVisibility() == View.VISIBLE) {
                            managerDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        managerDialog.setOnCancelListener(dialog -> {
            resetData("managerDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            managerDialog.dismiss();
        });

        phoneDialogInput.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!phoneDialogInput.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(phoneDialogInput);
                    controlEditText.select(phoneDialogInput);
                }
            }
            return false;
        });

        phoneDialogPositive.setOnClickListener(v -> {
            phoneDialogPositive.setClickable(false);
            handler.postDelayed(() -> phoneDialogPositive.setClickable(true), 250);

            if (phoneDialogInput.length() != 0) {
                if (!phoneRecyclerViewAdapter.getIds().contains(phoneDialogInput.getText().toString().trim())) {
                    try {
                        JSONObject phone = new JSONObject().put("name", phoneDialogInput.getText().toString().trim());
                        Model model = new Model(phone);

                        phoneRecyclerViewAdapter.getValues().add(model);
                        setRecyclerView(phoneRecyclerViewAdapter.getValues(), phoneRecyclerView, "phones");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (phoneRecyclerViewAdapter.getValues().size() == 1) {
                    phoneTextView.setVisibility(View.GONE);
                }

                resetData("phoneDialog");

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                    controlEditText.input().getText().clear();
                }

                phoneDialog.dismiss();
            } else {
                errorException("phoneDialog");
            }
        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 250);

            resetData("phoneDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            phoneDialog.dismiss();
        });

        phoneDialog.setOnCancelListener(dialog -> {
            resetData("phoneDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            phoneDialog.dismiss();
        });
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("type") != null)
            type = extras.getString("type");
        else
            type = "personal_clinic";
        if (extras.getString("manager_id") != null)
            managerId = extras.getString("manager_id");
        if (extras.getString("manager_name") != null)
            managerName = extras.getString("manager_name");
        if (extras.getString("title") != null)
            title = extras.getString("title");
        if (extras.getString("avatar") != null)
            imageFilePath = extras.getString("avatar");
        if (extras.getString("description") != null)
            description = extras.getString("description");
        if (extras.getString("address") != null)
            address = extras.getString("address");

        if (type.equals("personal_clinic")) {
            Objects.requireNonNull(typeTabLayout.getTabAt(0)).select();

            titleEditText.setVisibility(View.GONE);
            avatarLinearLayout.setVisibility(View.GONE);
        } else if (type.equals("counseling_center")) {
            Objects.requireNonNull(typeTabLayout.getTabAt(1)).select();

            titleEditText.setVisibility(View.VISIBLE);
            avatarLinearLayout.setVisibility(View.VISIBLE);
        }

        if (!managerId.equals("")) {
            managerNameTextView.setText(managerName);
            managerNameTextView.setTextColor(getResources().getColor(R.color.Gray700));

            managerIdTextView.setText(managerId);
            managerIdTextView.setVisibility(View.VISIBLE);
        }

        if (!title.equals("")) {
            titleEditText.setText(title);
            titleEditText.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (!imageFilePath.equals("")) {
            avatarTextView.setText(imageFilePath);
            avatarTextView.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (!description.equals("")) {
            descriptionEditText.setText(description);
            descriptionEditText.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (!address.equals("")) {
            addressEditText.setText(address);
            addressEditText.setTextColor(getResources().getColor(R.color.Gray700));
        }

        if (extras.getString("phone_numbers") != null) {
            try {
                JSONArray phones = new JSONArray(extras.getString("phone_numbers"));

                for (int i = 0; i < phones.length(); i++) {
                    JSONObject phone = new JSONObject().put("name", phones.get(i));
                    Model model = new Model(phone);

                    phoneRecyclerViewAdapter.getValues().add(model);
                }
                setRecyclerView(phoneRecyclerViewAdapter.getValues(), phoneRecyclerView, "phones");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (phoneRecyclerViewAdapter.getValues().size() != 0) {
                phoneTextView.setVisibility(View.GONE);
            }
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "phones":
                try {
                    ArrayList<String> phones = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        phones.add(arrayList.get(i).get("name").toString());
                    }

                    phoneRecyclerViewAdapter.setValues(arrayList, phones, method, "CreateCenter");
                    recyclerView.setAdapter(phoneRecyclerViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "getManagers":
                managerDialogAdapter.setValues(arrayList, method, "CreateCenter");
                recyclerView.setAdapter(managerDialogAdapter);

                if (arrayList.size() == 0) {
                    managerDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (managerDialogTextView.getVisibility() == View.VISIBLE) {
                        managerDialogTextView.setVisibility(View.GONE);
                    }
                }
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
            case "title":
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "avatar":
                avatarException = true;
                avatarLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "description":
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "address":
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "phone":
                phoneException = true;
                phoneFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "phoneDialog":
                phoneDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
                phoneFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "manager":
                if (!managerId.equals("")) {
                    managerId = "";
                    managerName = "";

                    managerNameTextView.setText(getResources().getString(R.string.CreateCenterManager));
                    managerNameTextView.setTextColor(getResources().getColor(R.color.Gray300));

                    managerIdTextView.setText(managerId);
                    managerIdTextView.setVisibility(View.GONE);
                }
                break;
            case "title":
                if (titleEditText.length() != 0) {
                    title = "";

                    titleEditText.getText().clear();
                }
                break;
            case "avatar":
                if (selectedBitmap != null) {
                    selectedBitmap = null;

                    avatarTextView.setText(getResources().getString(R.string.CreateCenterAvatar));
                    avatarTextView.setTextColor(getResources().getColor(R.color.Gray300));
                }
                break;
            case "managerDialog":
                if (type.equals("personal_clinic")) {
                    CenterRepository.personalClinic.clear();
                } else {
                    CenterRepository.counselingCenter.clear();
                }
                managerDialogRecyclerView.setAdapter(null);

                if (managerDialogTextView.getVisibility() == View.VISIBLE) {
                    managerDialogTextView.setVisibility(View.GONE);
                }
                break;
            case "phoneDialog":
                if (phoneDialogInput.length() != 0) {
                    phoneDialogInput.getText().clear();
                }
                break;
        }
    }

    private void getData(String method, String q) {
        try {
            switch (method) {
                case "getPersonalClinic":
                    managerDialogProgressBar.setVisibility(View.VISIBLE);
                    managerDialogImageView.setVisibility(View.GONE);

                    viewModel.personalClinic(q);

                    observeWork();
                    break;
                case "getCounselingCenter":
                    managerDialogProgressBar.setVisibility(View.VISIBLE);
                    managerDialogImageView.setVisibility(View.GONE);

                    viewModel.counselingCenter(q);

                    observeWork();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        switch (type) {
            case "personal_clinic":
                description = descriptionEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();

                try {
                    progressDialog.show();

                    viewModel.create(type, managerId, "", "", address, description, phoneRecyclerViewAdapter.getIds());
                    observeWork();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "counseling_center":
                title = titleEditText.getText().toString().trim();
                description = descriptionEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();

                //            FileManager.writeBitmapToCache(this, selectedBitmap, "image");

                try {
                    progressDialog.show();

                    viewModel.create(type, managerId, title, "", address, description, phoneRecyclerViewAdapter.getIds());
                    observeWork();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            switch (CenterRepository.work) {
                case "create":
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
                    break;
                case "getPersonalClinic":
                    if (integer == 1) {
                        setRecyclerView(CenterRepository.personalClinic, managerDialogRecyclerView, "getManagers");

                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCounselingCenter":
                    if (integer == 1) {
                        setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getManagers");

                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        managerDialogProgressBar.setVisibility(View.GONE);
                        managerDialogImageView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("type")) {
                errorException("type");
                exceptionToast = ExceptionGenerator.getErrorBody("type");
            }
            if (!ExceptionGenerator.errors.isNull("manager_id")) {
                errorException("manager");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("manager_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("manager_id"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("title")) {
                errorException("title");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("title");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("title"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("avatar")) {
                errorException("avatar");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("avatar");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("avatar"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("description")) {
                errorException("description");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("description");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("description"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("address")) {
                errorException("address");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("address");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("address"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("phone_numbers")) {
                errorException("phone");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("phone_numbers");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("phone_numbers"));
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
                case "getManagers":
                    if (!managerId.equals(model.get("id").toString())) {
                        managerId = model.get("id").toString();

                        managerName = model.get("name").toString();

                        managerNameTextView.setText(managerName);
                        managerNameTextView.setTextColor(getResources().getColor(R.color.Gray700));

                        managerIdTextView.setText(managerId);
                        managerIdTextView.setVisibility(View.VISIBLE);
                    } else if (managerId.equals(model.get("id").toString())) {
                        managerId = "";
                        managerName = "";

                        managerNameTextView.setText(getResources().getString(R.string.CreateCenterManager));
                        managerNameTextView.setTextColor(getResources().getColor(R.color.Gray300));

                        managerIdTextView.setText(managerId);
                        managerIdTextView.setVisibility(View.GONE);
                    }

                    resetData("managerDialog");

                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                        controlEditText.input().getText().clear();

                        handler.removeCallbacksAndMessages(null);
                    }

                    managerDialog.dismiss();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 300) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.gallery(this);
            }
        } else if (requestCode == 400) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                imageFilePath = IntentManager.camera(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 300) {
                try {
                    Uri imageUri = Objects.requireNonNull(data).getData();
                    InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                    imageFilePath = pathManager.getLocalPath(this, imageUri);

                    selectedBitmap = BitmapManager.scaleToCenter(imageBitmap);

                    avatarTextView.setText(imageFilePath);
                    avatarTextView.setTextColor(getResources().getColor(R.color.Gray700));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 400) {
                File imageFile = new File(imageFilePath);
                IntentManager.mediaScan(this, imageFile);

                int scaleFactor = Math.max(1, 2);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;

                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath, options);

                selectedBitmap = BitmapManager.scaleToCenter(imageBitmap);

                avatarTextView.setText(imageFilePath);
                avatarTextView.setTextColor(getResources().getColor(R.color.Gray700));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 300) {
                ExceptionGenerator.getException(false, 0, null, "GalleryException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 400) {
                ExceptionGenerator.getException(false, 0, null, "CameraException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}