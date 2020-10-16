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
import com.majazeh.risloo.Models.Managers.ExceptionManager;
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
    private SearchAdapter searchAdapter;
    private SpinnerAdapter phoneAdapter;

    // Vars
    private int managerPosition = -1;
    private String id = "", type = "", manager = "", managerId = "", title = "", description = "", address = "";
    private boolean managerException = false, phoneException = false;

    // Objects
    private Handler handler;
    private Bundle extras;
    private InputHandler inputHandler;

    // Widgets
    private Toolbar toolbar;
    public TextView managerTextView, phoneTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText titleEditText, descriptionEditText, addressEditText, searchDialogEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView, searchDialogRecyclerView;
    private ProgressBar managerProgressBar;
    private ImageView managerImageView, phoneImageView;
    private LinearLayout phoneLinearLayout;
    private FrameLayout managerFrameLayout;
    private Button editButton;
    private Dialog searchDialog, phoneDialog, progressDialog;

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

        searchAdapter = new SearchAdapter(this);
        phoneAdapter = new SpinnerAdapter(this);

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

        searchDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(searchDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchDialog.setContentView(R.layout.dialog_search);
        searchDialog.setCancelable(true);
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

        WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
        layoutParams1.copyFrom(searchDialog.getWindow().getAttributes());
        layoutParams1.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        searchDialog.getWindow().setAttributes(layoutParams1);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(phoneDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        phoneDialog.getWindow().setAttributes(layoutParams2);

        searchDialogEditText = searchDialog.findViewById(R.id.dialog_search_editText);
        phoneDialogEditText = phoneDialog.findViewById(R.id.dialog_phone_editText);

        searchDialogRecyclerView = searchDialog.findViewById(R.id.dialog_search_recyclerView);
        searchDialogRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        searchDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        searchDialogRecyclerView.setHasFixedSize(true);

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
            if (managerException) {
                clearException("manager");
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (CenterRepository.counselingCenter.size() == 0) {
                getData("getCounselingCenter");
            } else {
                searchDialog.show();
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
                if (manager.isEmpty()) {
                    errorView("manager");
                }

                if (managerException) {
                    clearException("manager");
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

                if (managerException) {
                    clearException("manager");
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

        searchDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!searchDialogEditText.hasFocus()) {
                    if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                        inputHandler.clear(this, inputHandler.getInput());
                    }

                    inputHandler.focus(searchDialogEditText);
                    inputHandler.select(searchDialogEditText);
                }
            }
            return false;
        });

        searchDialogEditText.addTextChangedListener(new TextWatcher() {
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

        searchDialog.setOnCancelListener(dialog -> {
            searchDialog.dismiss();

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }
        });

        phoneImageView.setOnClickListener(v -> phoneDialog.show());

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
                if (!phoneAdapter.getValues().contains(phoneDialogEditText.getText().toString().trim())) {
                    try {
                        phoneAdapter.getValues().add(new Model(new JSONObject().put("title", phoneDialogEditText.getText().toString().trim())));
                        setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneEdit");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (phoneAdapter.getValues().size() == 1) {
                    phoneTextView.setVisibility(View.GONE);
                }

                phoneDialog.dismiss();

                if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                    inputHandler.clear(this, inputHandler.getInput());
                    inputHandler.getInput().getText().clear();
                }
            } else {
                errorView("phone");
            }
        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 300);
            phoneDialog.dismiss();

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }
        });

        phoneDialog.setOnCancelListener(dialog -> {
            phoneDialog.dismiss();

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
                inputHandler.getInput().getText().clear();
            }
        });
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String type) {
        if ("phoneEdit".equals(type)) {
            phoneAdapter.setValue(arrayList, type);

            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                try {
                    list.add((String) arrayList.get(i).get("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            phoneAdapter.setValuesId(list);
            recyclerView.setAdapter(phoneAdapter);
        } else {
            searchAdapter.setValue(arrayList, managerPosition, type);
            recyclerView.setAdapter(searchAdapter);
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

    private void setData() {
        id = extras.getString("id");
        type = extras.getString("type");
        manager = extras.getString("manager");
        managerId = extras.getString("manager_id");

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
                        phoneAdapter.getValues().add(new Model(new JSONObject().put("title", phones.get(i))));
                    }
                    setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneEdit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (phoneAdapter.getValues().size() != 0) {
                    phoneTextView.setVisibility(View.GONE);
                }
            }
        } else {
            toolbar.setTitle(getResources().getString(R.string.EditCenterTitle));

            managerTextView.setText(manager);
            titleEditText.setText(title);

            descriptionEditText.setText(description);
            addressEditText.setText(address);

            if (extras.getString("phone_numbers") != null) {
                try {
                    JSONArray phones = new JSONArray(extras.getString("phone_numbers"));
                    for (int i = 0; i < phones.length(); i++) {
                        phoneAdapter.getValues().add(new Model(new JSONObject().put("title", phones.get(i))));
                    }
                    setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneEdit");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (phoneAdapter.getValues().size() != 0) {
                    phoneTextView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getData(String method) {
        try {
            switch (method) {
                case "getCounselingCenter":
                    managerProgressBar.setVisibility(View.VISIBLE);
                    managerImageView.setVisibility(View.GONE);
                    managerTextView.setClickable(false);

                    viewModel.counselingCenter();
                    break;
            }
            observeWork();
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
                viewModel.edit(id, managerId, title, description, address, phoneAdapter.getValuesId());
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            title = titleEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

            try {
                progressDialog.show();
                viewModel.edit(id, managerId, title, description, address, phoneAdapter.getValuesId());
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void observeWork() {
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
                    searchDialog.show();
                    setRecyclerView(CenterRepository.counselingCenter, searchDialogRecyclerView, "editCenter");

                    managerProgressBar.setVisibility(View.GONE);
                    managerImageView.setVisibility(View.VISIBLE);
                    managerTextView.setClickable(true);
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    managerProgressBar.setVisibility(View.GONE);
                    managerImageView.setVisibility(View.VISIBLE);
                    managerTextView.setClickable(true);
                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    managerProgressBar.setVisibility(View.GONE);
                    managerImageView.setVisibility(View.VISIBLE);
                    managerTextView.setClickable(true);
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

    public void observeSearchAdapter(String title, int position) {
        try {
            if (CenterRepository.counselingCenter.size() != 0) {
                managerId = String.valueOf(CenterRepository.counselingCenter.get(position).get("id"));
                managerTextView.setText(title);

                searchDialog.dismiss();

                if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                    inputHandler.clear(this, inputHandler.getInput());
                    inputHandler.getInput().getText().clear();
                }
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