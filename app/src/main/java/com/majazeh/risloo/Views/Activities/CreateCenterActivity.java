package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CreateCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SpinnerAdapter phoneAdapter;

    // Vars
    private String type = "personal_clinic", manager = "", title = "", description = "", address = "";
    private boolean typeException = false, managerException = false, phoneException =false;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    private Spinner managerSpinner;
    public TextView managerTextView, phoneTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText inputEditText, titleEditText, descriptionEditText, addressEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView;
    private ProgressBar managerProgressBar;
    private ImageView managerImageView, phoneImageView;
    private LinearLayout phoneLinearLayout;
    private FrameLayout managerFrameLayout;
    private Button createButton;
    private Dialog phoneDialog, progressDialog;

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

        phoneAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_create_center_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_center_type_tabLayout);

        managerSpinner = findViewById(R.id.activity_create_center_manager_spinner);

        managerTextView = findViewById(R.id.activity_create_center_manager_textView);
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

        phoneLinearLayout = findViewById(R.id.activity_create_center_phone_linearLayout);

        managerFrameLayout = findViewById(R.id.activity_create_center_manager_frameLayout);

        createButton = findViewById(R.id.activity_create_center_button);

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

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(phoneDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        phoneDialog.getWindow().setAttributes(layoutParams);

        phoneDialogEditText = phoneDialog.findViewById(R.id.dialog_phone_editText);

        phoneDialogPositive = phoneDialog.findViewById(R.id.dialog_phone_positive_textView);
        phoneDialogNegative = phoneDialog.findViewById(R.id.dialog_phone_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
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
                if (inputEditText != null && inputEditText.hasFocus()) {
                    clearInput(inputEditText);
                }

                switch (tab.getPosition()) {
                    case 0:
                        type = "personal_clinic";
                        titleEditText.setVisibility(View.GONE);

                        // Reset Title
                        if (titleEditText.length() != 0) {
                            title = "";
                            titleEditText.getText().clear();
                            titleEditText.setFocusableInTouchMode(true);
                        }

                        // Reset Personal Clinic
                        if (CenterRepository.personalClinic.size() != 0) {
                            manager = "";
                            CenterRepository.personalClinic.clear();
                            managerSpinner.setAdapter(null);
                            managerTextView.setVisibility(View.VISIBLE);
                        }

                        break;
                    case 1:
                        type = "counseling_center";
                        titleEditText.setVisibility(View.VISIBLE);

                        // Reset Title
                        if (titleEditText.length() != 0) {
                            title = "";
                            titleEditText.getText().clear();
                            titleEditText.setFocusableInTouchMode(true);
                        }

                        // Reset Counseling Center
                        if (CenterRepository.counselingCenter.size() != 0) {
                            manager = "";
                            CenterRepository.counselingCenter.clear();
                            managerSpinner.setAdapter(null);
                            managerTextView.setVisibility(View.VISIBLE);
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

        managerSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (managerException) {
                    clearException("manager");
                }

                if (inputEditText != null && inputEditText.hasFocus()) {
                    clearInput(inputEditText);
                }

                if (type.equals("personal_clinic")) {
                    if (CenterRepository.personalClinic.size() == 0)
                        getData("getPersonalClinic");
                } else {
                    if (CenterRepository.counselingCenter.size() == 0)
                        getData("getCounselingCenter");
                }
            }
            return false;
        });

        managerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (managerSpinner.getCount() != position) {
                    try {
                        if (type.equals("personal_clinic")) {
                            manager = String.valueOf(CenterRepository.personalClinic.get(position).get("id"));
                        } else {
                            manager = String.valueOf(CenterRepository.counselingCenter.get(position).get("id"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(titleEditText);
                    focusInput(titleEditText);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!descriptionEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(descriptionEditText);
                    focusInput(descriptionEditText);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!addressEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(addressEditText);
                    focusInput(addressEditText);
                }
            }
            return false;
        });

        createButton.setOnClickListener(v -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
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
                if (phoneException) {
                    clearException("phone");
                }

                if (!manager.isEmpty() && titleEditText.length() != 0) {
                    clearInput(titleEditText);

                    doWork();
                }
            }

        });

        phoneImageView.setOnClickListener(v -> phoneDialog.show());

        phoneDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!phoneDialogEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(phoneDialogEditText);
                    focusInput(phoneDialogEditText);
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
                        setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneCreate");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (phoneAdapter.getValues().size() == 1) {
                    phoneTextView.setVisibility(View.GONE);
                }

                phoneDialog.dismiss();

                if (inputEditText != null && inputEditText.hasFocus()) {
                    clearInput(inputEditText);
                }
            } else {
                errorView("phone");
            }
        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 300);
            phoneDialog.dismiss();

            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }
        });

        phoneDialog.setOnCancelListener(dialog -> {
            phoneDialog.dismiss();

            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }
        });
    }

    private void setSpinner(ArrayList<Model> arrayList, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_background) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (position == getCount()) {
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setText("");
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint(getItem(getCount()));
                } else {
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setText(getItem(position));
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint("");
                }

                return view;
            }

            @Override
            public int getCount() {
                return arrayList.size();
            }

        };
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                adapter.add((String) arrayList.get(i).get("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        managerTextView.setVisibility(View.GONE);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount() - 1);
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String type) {
        if ("phoneCreate".equals(type)) {
            phoneAdapter.setValue(arrayList, type);
            recyclerView.setAdapter(phoneAdapter);
        }
    }

    private void focusInput(EditText input) {
        this.inputEditText = input;
    }

    private void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
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

    private void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
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
            case "phone":
                phoneException = false;
                phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void getData(String method) {
        try {
            switch (method) {
                case "getPersonalClinic":
                    managerProgressBar.setVisibility(View.VISIBLE);
                    managerImageView.setVisibility(View.GONE);
                    managerSpinner.setClickable(false);

                    viewModel.personalClinic();
                    break;
                case "getCounselingCenter":
                    managerProgressBar.setVisibility(View.VISIBLE);
                    managerImageView.setVisibility(View.GONE);
                    managerSpinner.setClickable(false);

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
                viewModel.create(type, manager, "", "", address, description, phoneAdapter.getValues());
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
                viewModel.create(type, manager, "", title, address, description, phoneAdapter.getValues());
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        observeException();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getPersonalClinic":
                    if (integer == 1) {
                        setSpinner(CenterRepository.personalClinic, managerSpinner);

                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCounselingCenter":
                    if (integer == 1) {
                        setSpinner(CenterRepository.counselingCenter, managerSpinner);

                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        managerProgressBar.setVisibility(View.GONE);
                        managerImageView.setVisibility(View.VISIBLE);
                        managerSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("create")) {
            try {
                if (!ExceptionManager.errors.isNull("type")) {
                    typeTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("type"), Toast.LENGTH_SHORT).show();
                    typeException = true;
                }
                if (!ExceptionManager.errors.isNull("manager_id")) {
                    managerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("manager_id"), Toast.LENGTH_SHORT).show();
                    managerException = true;
                }
                if (!ExceptionManager.errors.isNull("title")) {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("title"), Toast.LENGTH_SHORT).show();
                }
                if (!ExceptionManager.errors.isNull("description")) {
                    descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("description"), Toast.LENGTH_SHORT).show();
                }
                if (!ExceptionManager.errors.isNull("address")) {
                    addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("address"), Toast.LENGTH_SHORT).show();
                }
                if (!ExceptionManager.errors.isNull("phone_numbers")) {
                    phoneLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("phone_numbers"), Toast.LENGTH_SHORT).show();
                    phoneException = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}