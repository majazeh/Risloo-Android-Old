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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
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
    private SpinnerAdapter phoneAdapter;

    // Vars
    private String id = "", type = "", manager = "", title = "", description = "", address = "";
    private boolean managerException = false, phoneException =false;

    // Objects
    private Handler handler;
    private Bundle extras;

    // Widgets
    private Toolbar toolbar;
    private Spinner managerSpinner;
    public TextView managerTextView, phoneTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText inputEditText, titleEditText, descriptionEditText, addressEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView;
    private ProgressBar managerProgressBar;
    private ImageView managerImageView, phoneImageView;
    private LinearLayout phoneLinearLayout;
    private FrameLayout managerFrameLayout;
    private Button editButton;
    private Dialog phoneDialog, progressDialog;

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

        phoneAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        extras = getIntent().getExtras();

        toolbar = findViewById(R.id.activity_edit_center_toolbar);

        managerSpinner = findViewById(R.id.activity_edit_center_manager_spinner);

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

        managerSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (managerException) {
                    clearException("manager");
                }

                if (inputEditText != null && inputEditText.hasFocus()) {
                    clearInput(inputEditText);
                }

                if (CenterRepository.counselingCenter.size() == 0)
                    getData("getCounselingCenter");
            }
            return false;
        });

        managerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (managerSpinner.getCount() != position) {
                    try {
                        manager = String.valueOf(CenterRepository.counselingCenter.get(position).get("id"));
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
            if(MotionEvent.ACTION_UP == event.getAction()) {
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
            if(MotionEvent.ACTION_UP == event.getAction()) {
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
            if(MotionEvent.ACTION_UP == event.getAction()) {
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

        editButton.setOnClickListener(v -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
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
                    clearInput(titleEditText);

                    doWork();
                }
            }

        });

        phoneImageView.setOnClickListener(v -> phoneDialog.show());

        phoneDialogEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
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
                        setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneEdit");
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
        if ("phoneEdit".equals(type)) {
            phoneAdapter.setValue(arrayList, type);

            ArrayList list = new ArrayList<String>();
            for (int i = 0; i < arrayList.size(); i++) {
                try {
                    list.add(arrayList.get(i).get("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            phoneAdapter.setValuesId(list);
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

    private void setData() {
        id = extras.getString("id");
        type = extras.getString("type");
        manager = extras.getString("manager");

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
        } else  {
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
                viewModel.edit(id, manager, title, description, address, phoneAdapter.getValuesId());
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
                viewModel.edit(id, manager, title, description, address, phoneAdapter.getValuesId());
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
            } else if (CenterRepository.work.equals("getCounselingCenter")) {
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
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("edit")) {
            try {
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