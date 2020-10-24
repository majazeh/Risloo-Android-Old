package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.InputHandler;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
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
    private Handler handler;
    private Bundle extras;
    private InputHandler inputHandler;

    // Widgets
    private Toolbar toolbar;
    public TextView managerTextView, phoneTextView, managerDialogTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText titleEditText, descriptionEditText, addressEditText, managerDialogEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView, managerDialogRecyclerView;
    private ProgressBar managerProgressBar, managerDialogProgressBar;
    private ImageView managerImageView, phoneImageView, managerDialogImageView;
    private LinearLayout phoneLinearLayout;
    private FrameLayout managerFrameLayout;
    private Button editButton;
    private Dialog managerDialog, phoneDialog, progressDialog;

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
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(CenterViewModel.class);

        managerDialogAdapter = new SearchAdapter(this);
        phoneRecyclerViewAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        extras = getIntent().getExtras();

        inputHandler = new InputHandler();

        toolbar = findViewById(R.id.activity_edit_center_toolbar);

        managerTextView = findViewById(R.id.activity_edit_center_manager_textView);
        phoneTextView = findViewById(R.id.activity_edit_center_phone_textView);

        titleEditText = findViewById(R.id.activity_edit_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_edit_center_description_editText);
        addressEditText = findViewById(R.id.activity_edit_center_address_editText);

        phoneRecyclerView = findViewById(R.id.activity_edit_center_phone_recyclerView);
        phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneRecyclerView.setHasFixedSize(true);

        managerProgressBar = findViewById(R.id.activity_edit_center_manager_progressBar);

        managerImageView = findViewById(R.id.activity_edit_center_manager_imageView);
        phoneImageView = findViewById(R.id.activity_edit_center_phone_imageView);

        phoneLinearLayout = findViewById(R.id.activity_edit_center_phone_linearLayout);

        managerFrameLayout = findViewById(R.id.activity_edit_center_manager_frameLayout);

        editButton = findViewById(R.id.activity_edit_center_button);

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
            phoneImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);

            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

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

        managerTextView.setOnClickListener(v -> {
            managerTextView.setClickable(false);
            handler.postDelayed(() -> managerTextView.setClickable(true), 300);

            if (managerException) {
                clearException("manager");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (CenterRepository.counselingCenter.size() == 0) {
                getData("getCounselingCenter", "");
            } else {
                setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getCounselingCenter");
                managerDialog.show();
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

        editButton.setOnClickListener(v -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
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
                    if (managerDialogEditText.length() == 0) {
                        setRecyclerView(CenterRepository.counselingCenter, managerDialogRecyclerView, "getCounselingCenter");
                    } else if (managerDialogEditText.length() == 1) {
                        ExceptionManager.getException(false, 0, null, "MustBeTwoCharException", "center");
                        Toast.makeText(EditCenterActivity.this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    } else {
                        getData("getCounselingCenter", managerDialogEditText.getText().toString().trim());
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        managerDialog.setOnCancelListener(dialog -> {
            resetSearch("counselingCenter");

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

                phoneRecyclerViewAdapter.setValue(arrayList, phones, method, "EditCenter");
                recyclerView.setAdapter(phoneRecyclerViewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (method.equals("getCounselingCenter")) {
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
            case "phone":
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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

    private void resetSearch(String method) {
        switch (method) {
            case "counselingCenter":
                if (CenterRepository.counselingCenterSearch.size() != 0) {
                    CenterRepository.counselingCenterSearch.clear();
                    managerDialogRecyclerView.setAdapter(null);
                }
                break;
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
            toolbar.setTitle(getResources().getString(R.string.EditClinicTitle));

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
            toolbar.setTitle(getResources().getString(R.string.EditCenterTitle));

            if (managerId.equals("")) {
                managerTextView.setText(getResources().getString(R.string.EditCenterManager));
                managerTextView.setTextColor(getResources().getColor(R.color.Mischka));
            } else {
                managerTextView.setText(managerName);
                managerTextView.setTextColor(getResources().getColor(R.color.Grey));
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
                viewModel.edit(id, managerId, title, description, address, phoneRecyclerViewAdapter.getIds());
                observeWork(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            title = titleEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

            try {
                progressDialog.show();
                viewModel.edit(id, managerId, title, description, address, phoneRecyclerViewAdapter.getIds());
                observeWork(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void observeWork(String q) {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work.equals("edit")) {
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
            } else if (CenterRepository.work.equals("getCounselingCenter")) {
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
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.exception.equals("edit")) {
            String exceptionToast = "";

            if (!ExceptionManager.errors.isNull("manager_id")) {
                errorException("manager");
                exceptionToast = ExceptionManager.getErrorBody("manager_id");
            }
            if (!ExceptionManager.errors.isNull("title")) {
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionManager.getErrorBody("title");
                } else {
                    exceptionToast += (" و " + ExceptionManager.getErrorBody("title"));
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
            if (!managerId.equals(model.get("id").toString())) {
                managerId = model.get("id").toString();
                managerName = model.get("name").toString();

                managerTextView.setText(managerName);
                managerTextView.setTextColor(getResources().getColor(R.color.Grey));
            } else if (managerId.equals(model.get("id").toString())) {
                managerId = "";
                managerName = "";

                managerTextView.setText(getResources().getString(R.string.EditCenterManager));
                managerTextView.setTextColor(getResources().getColor(R.color.Mischka));
            }

            resetSearch("counselingCenter");

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
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