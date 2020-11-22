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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class EditCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SearchAdapter managerDialogAdapter;
    private SpinnerAdapter phoneRecyclerViewAdapter;

    // Vars
    public String id = "", type = "", managerName = "", managerId = "", title = "", description = "", address = "";
    private boolean managerException = false, phoneException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private EditText titleEditText, descriptionEditText, addressEditText;
    private FrameLayout managerFrameLayout;
    private LinearLayout managerLinearLayout, phoneLinearLayout;
    public TextView managerNameTextView, managerIdTextView, phoneTextView;
    private RecyclerView phoneRecyclerView;
    private ImageView phoneImageView;
    private Button editButton;
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

        setContentView(R.layout.activity_edit_center);

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

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        titleEditText = findViewById(R.id.activity_edit_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_edit_center_description_editText);
        addressEditText = findViewById(R.id.activity_edit_center_address_editText);

        managerFrameLayout = findViewById(R.id.activity_edit_center_manager_frameLayout);

        managerLinearLayout = findViewById(R.id.activity_edit_center_manager_linearLayout);
        phoneLinearLayout = findViewById(R.id.activity_edit_center_phone_linearLayout);

        managerNameTextView = findViewById(R.id.activity_edit_center_manager_name_textView);
        managerIdTextView = findViewById(R.id.activity_edit_center_manager_id_textView);
        phoneTextView = findViewById(R.id.activity_edit_center_phone_textView);

        phoneRecyclerView = findViewById(R.id.activity_edit_center_phone_recyclerView);
        phoneRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneRecyclerView.setHasFixedSize(true);

        phoneImageView = findViewById(R.id.activity_edit_center_phone_imageView);

        editButton = findViewById(R.id.activity_edit_center_button);

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

        managerDialogTitleTextView = managerDialog.findViewById(R.id.dialog_search_title_textView);
        managerDialogTitleTextView.setText(getResources().getString(R.string.EditCenterManagerDialogTitle));

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
        phoneDialogTitle.setText(getResources().getString(R.string.EditCenterPhoneDialogTitle));
        phoneDialogInput = phoneDialog.findViewById(R.id.dialog_type_input_editText);
        phoneDialogInput.setHint(getResources().getString(R.string.EditCenterPhoneDialogInput));
        phoneDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        phoneDialogPositive = phoneDialog.findViewById(R.id.dialog_type_positive_textView);
        phoneDialogPositive.setText(getResources().getString(R.string.EditCenterPhoneDialogPositive));
        phoneDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        phoneDialogNegative = phoneDialog.findViewById(R.id.dialog_type_negative_textView);
        phoneDialogNegative.setText(getResources().getString(R.string.EditCenterPhoneDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            phoneImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);

            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            phoneDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            phoneDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

        managerLinearLayout.setOnClickListener(v -> {
            managerLinearLayout.setClickable(false);
            handler.postDelayed(() -> managerLinearLayout.setClickable(true), 300);

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

        editButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (type.equals("personal_clinic")) {
                if (managerId.equals("")) {
                    errorView("manager");
                }

                if (managerException) {
                    clearException("manager");
                }
                if (phoneException) {
                    clearException("phone");
                }

                if (!managerId.equals("")) {
                    doWork();
                }
            } else {
                if (managerId.equals("")) {
                    errorView("manager");
                }
                if (titleEditText.length() == 0) {
                    errorView("title");
                }

                if (managerException) {
                    clearException("manager");
                }
                if (phoneException) {
                    clearException("phone");
                }

                if (!managerId.equals("") && titleEditText.length() != 0) {
                    controlEditText.clear(this, titleEditText);

                    doWork();
                }
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
                        getData("getCounselingCenter", managerDialogEditText.getText().toString().trim());
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

        phoneImageView.setOnClickListener(v -> {
            phoneImageView.setClickable(false);
            handler.postDelayed(() -> phoneImageView.setClickable(true), 300);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            phoneDialog.show();
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
            handler.postDelayed(() -> phoneDialogPositive.setClickable(true), 300);

            if (phoneDialogInput.length() != 0) {
                if (!phoneRecyclerViewAdapter.getIds().contains(phoneDialogInput.getText().toString().trim())) {
                    try {
                        JSONObject phone = new JSONObject().put("name", phoneDialogInput.getText().toString().trim());

                        phoneRecyclerViewAdapter.getValues().add(new Model(phone));
                        setRecyclerView(phoneRecyclerViewAdapter.getValues(), phoneRecyclerView, "phones");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (phoneRecyclerViewAdapter.getValues().size() == 1) {
                    phoneTextView.setVisibility(View.GONE);
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                    controlEditText.input().getText().clear();
                }

                phoneDialog.dismiss();
            } else {
                errorView("phoneDialog");
            }
        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 300);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            phoneDialog.dismiss();
        });

        phoneDialog.setOnCancelListener(dialog -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
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

                phoneRecyclerViewAdapter.setValue(arrayList, phones, method, "EditCenter");
                recyclerView.setAdapter(phoneRecyclerViewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (method.equals("getManagers")) {
            managerDialogAdapter.setValue(arrayList, method, "EditCenter");
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
            case "phoneDialog":
                phoneDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "manager":
                managerException = true;
                managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "phone":
                phoneException = true;
                phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "manager":
                managerException = false;
                managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "phone":
                phoneException = false;
                phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        if (method.equals("managerDialog")) {
            CenterRepository.counselingCenter.clear();
            managerDialogRecyclerView.setAdapter(null);

            if (managerDialogTextView.getVisibility() == View.VISIBLE) {
                managerDialogTextView.setVisibility(View.GONE);
            }
        }
    }

    private void setData() {
        if (extras.getString("id") != null)
            id = extras.getString("id");
        if (extras.getString("type") != null)
            type = extras.getString("type");
        if (extras.getString("manager_id") != null)
            managerId = extras.getString("manager_id");
        if (extras.getString("manager") != null)
            managerName = extras.getString("manager");
        if (extras.getString("title") != null)
            title = extras.getString("title");
        if (extras.getString("description") != null)
            description = extras.getString("description");
        if (extras.getString("address") != null)
            address = extras.getString("address");

        if (type.equals("personal_clinic")) {
            toolbarTextView.setText(getResources().getString(R.string.EditClinicTitle));

            managerFrameLayout.setVisibility(View.GONE);
            titleEditText.setVisibility(View.GONE);

            descriptionEditText.setText(description);
            addressEditText.setText(address);

            if (extras.getString("phone_numbers") != null) {
                try {
                    JSONArray phones = new JSONArray(extras.getString("phone_numbers"));

                    for (int i = 0; i < phones.length(); i++) {
                        JSONObject phone = new JSONObject().put("name", phones.get(i));
                        phoneRecyclerViewAdapter.getValues().add(new Model(phone));
                    }
                    setRecyclerView(phoneRecyclerViewAdapter.getValues(), phoneRecyclerView, "phones");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (phoneRecyclerViewAdapter.getValues().size() != 0) {
                    phoneTextView.setVisibility(View.GONE);
                }
            }
        } else {
            toolbarTextView.setText(getResources().getString(R.string.EditCenterTitle));

            if (!managerId.equals("")) {
                managerNameTextView.setText(managerName);
                managerNameTextView.setTextColor(getResources().getColor(R.color.Grey));

                managerIdTextView.setText(managerId);
                managerIdTextView.setVisibility(View.VISIBLE);
            }
            titleEditText.setText(title);

            descriptionEditText.setText(description);
            addressEditText.setText(address);

            if (extras.getString("phone_numbers") != null) {
                try {
                    JSONArray phones = new JSONArray(extras.getString("phone_numbers"));

                    for (int i = 0; i < phones.length(); i++) {
                        JSONObject phone = new JSONObject().put("name", phones.get(i));
                        phoneRecyclerViewAdapter.getValues().add(new Model(phone));
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
    }

    private void getData(String method, String q) {
        try {
            switch (method) {
                case "getCounselingCenter":
                    managerDialogProgressBar.setVisibility(View.VISIBLE);
                    managerDialogImageView.setVisibility(View.GONE);

                    viewModel.counselingCenter(q);
                    break;
            }
            observeWork();
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
                    viewModel.edit(id, managerId, title, description, address, phoneRecyclerViewAdapter.getIds());
                    observeWork();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "counseling_center":
                title = titleEditText.getText().toString().trim();
                description = descriptionEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();

                try {
                    progressDialog.show();
                    viewModel.edit(id, managerId, title, description, address, phoneRecyclerViewAdapter.getIds());
                    observeWork();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work.equals("edit")) {
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
            } else if (CenterRepository.work.equals("getCounselingCenter")) {
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
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("edit")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("manager_id")) {
                errorException("manager");
                exceptionToast = ExceptionGenerator.getErrorBody("manager_id");
            }
            if (!ExceptionGenerator.errors.isNull("title")) {
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("title");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("title"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("description")) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("description");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("description"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("address")) {
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
            if (!managerId.equals(model.get("id").toString())) {
                managerId = model.get("id").toString();
                managerName = model.get("name").toString();

                managerNameTextView.setText(managerName);
                managerNameTextView.setTextColor(getResources().getColor(R.color.Grey));

                managerIdTextView.setText(managerId);
                managerIdTextView.setVisibility(View.VISIBLE);
            } else if (managerId.equals(model.get("id").toString())) {
                managerId = "";
                managerName = "";

                managerNameTextView.setText(getResources().getString(R.string.EditCenterManager));
                managerNameTextView.setTextColor(getResources().getColor(R.color.Mischka));

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