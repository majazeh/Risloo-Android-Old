package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SpinnerAdapter phoneAdapter;

    // Vars
    private String type = "personal_clinic", principal = "", title = "", description = "", address = "";
    private boolean titleError;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    private Spinner principalSpinner;
    public TextView principalTextView, phoneTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText titleEditText, descriptionEditText, addressEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView;
    private ProgressBar principalProgressBar;
    private ImageView principalImageView, phoneImageView;
    private FrameLayout principalFrameLayout;
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
        viewModel = ViewModelProviders.of(this).get(CenterViewModel.class);

        phoneAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_create_center_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_center_type_tabLayout);

        principalSpinner = findViewById(R.id.activity_create_center_principal_spinner);

        principalTextView = findViewById(R.id.activity_create_center_principal_textView);
        phoneTextView = findViewById(R.id.activity_create_center_phone_textView);

        titleEditText = findViewById(R.id.activity_create_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_create_center_description_editText);
        addressEditText = findViewById(R.id.activity_create_center_address_editText);

        phoneRecyclerView = findViewById(R.id.activity_create_center_phone_recyclerView);
        phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneRecyclerView.setHasFixedSize(false);

        principalProgressBar = findViewById(R.id.activity_create_center_principal_progressBar);

        principalImageView = findViewById(R.id.activity_create_center_principal_imageView);
        phoneImageView = findViewById(R.id.activity_create_center_phone_imageView);

        principalFrameLayout = findViewById(R.id.activity_create_center_principal_frameLayout);

        createButton = findViewById(R.id.activity_create_center_button);

        phoneDialog = new Dialog(this, R.style.DialogTheme);
        phoneDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        phoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phoneDialog.setContentView(R.layout.dialog_phone);
        phoneDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
                switch (tab.getPosition()) {
                    case 0:
                        setData("clinic");
                        principal = "";
                        setSpinner(new ArrayList<>(), principalSpinner, "personal_clinic");

                        break;
                    case 1:
                        setData("center");
                        principal = "";
                        setSpinner(new ArrayList<>(), principalSpinner, "counseling_center");
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

        principalSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (SampleRepository.rooms.size() == 0) {
                    doWork("getPrincipals");
                }

                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            }
            return false;
        });

        principalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (principalSpinner.getCount() != position) {
                    try {
                        if (type.equals("personal_clinic")) {
                            principal = String.valueOf(CenterRepository.personal_clinic.get(position).get("id"));
                        } else {
                            principal = String.valueOf(CenterRepository.counseling_center.get(position).get("id"));
                        }
                        principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phoneImageView.setOnClickListener(v -> phoneDialog.show());

        titleEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                titleEditText.setCursorVisible(true);

                titleError = false;

                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                descriptionEditText.setCursorVisible(true);

                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                if (titleError) {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                addressEditText.setCursorVisible(true);

                if (titleError) {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            }
            return false;
        });

        createButton.setOnClickListener(v -> {
            if (type.equals("counseling_center")) {
                title = titleEditText.getText().toString().trim();
                description = descriptionEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();

                if (principal.isEmpty() && titleEditText.length() == 0) {
                    checkInput(type);
                } else {
                    clearData();

                    try {
                        progressDialog.show();
                        viewModel.create(type, principal, title, "", description, address, phoneAdapter.getValues());
                        observeWork();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else if (type.equals("personal_clinic")) {
                description = descriptionEditText.getText().toString().trim();
                address = addressEditText.getText().toString().trim();

                if (principal.isEmpty()) {
                    checkInput(type);
                } else {
                    clearData();

                    try {
                        progressDialog.show();
                        viewModel.create(type, principal, title, "", address, description, phoneAdapter.getValues());

                        observeWork();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        phoneDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                phoneDialogEditText.setCursorVisible(true);
            }
            return false;
        });

        phoneDialogPositive.setOnClickListener(v -> {
            phoneDialogPositive.setClickable(false);
            handler.postDelayed(() -> phoneDialogPositive.setClickable(true), 500);

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

                phoneDialogEditText.getText().clear();
                phoneDialogEditText.setCursorVisible(false);
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            } else {
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            }

        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 500);
            phoneDialog.dismiss();

            phoneDialogEditText.getText().clear();
            phoneDialogEditText.setCursorVisible(false);
            phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        });

        phoneDialog.setOnCancelListener(dialog -> {
            phoneDialog.dismiss();

            phoneDialogEditText.getText().clear();
            phoneDialogEditText.setCursorVisible(false);
            phoneDialogEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        });
    }

    private void setSpinner(ArrayList<Model> arrayList, Spinner spinner, String type) {
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

        //adapter.add(getResources().getString(R.string.EditCenterPrincipal));
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                adapter.add((String) arrayList.get(i).get("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        principalTextView.setVisibility(View.GONE);
        principalProgressBar.setVisibility(View.GONE);
        principalImageView.setVisibility(View.VISIBLE);
        principalSpinner.setClickable(true);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount()-1);
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String type) {
        switch (type) {
            case "phoneCreate":
                phoneAdapter.setValue(arrayList, type);
                recyclerView.setAdapter(phoneAdapter);
                break;
        }
    }

    private void setData(String method) {
        if (method.equals("center")) {
            type = "counseling_center";
            titleEditText.setVisibility(View.VISIBLE);
        } else if (method.equals("clinic")) {
            type = "personal_clinic";
            titleEditText.setVisibility(View.GONE);
            titleEditText.getText().clear();
            titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        }
    }

    private void checkInput(String type) {
        if (type.equals("center")) {
            if (principal.isEmpty() && titleEditText.length() == 0) {
                principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                titleError = true;
            } else if (titleEditText.length() == 0) {
                principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                titleError = true;
            } else if (principal.isEmpty()) {
                principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                titleError = false;
            }
        } else if (type.equals("clinic")) {
            principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void clearData() {
        titleEditText.setCursorVisible(false);
        descriptionEditText.setCursorVisible(false);
        addressEditText.setCursorVisible(false);

        principalFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        addressEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        titleError = false;
    }

    private void doWork(String method) {
        try {
            switch (method) {
                case "getPrincipals":
                    principalProgressBar.setVisibility(View.VISIBLE);
                    principalImageView.setVisibility(View.GONE);
                    principalSpinner.setClickable(false);
                    if (type.equals("personal_clinic")) {
                        if (CenterRepository.personal_clinic.size() == 0) {
                            viewModel.personal_clinic();
                            observeWork();
                        } else {
                            setSpinner(CenterRepository.personal_clinic, principalSpinner, "personal_clinic");
                        }
                    } else {
                        if (CenterRepository.counseling_center.size() == 0) {
                            viewModel.counseling_center();
                            observeWork();
                        } else {
                            setSpinner(CenterRepository.counseling_center, principalSpinner, "counseling_center");

                        }
                    }
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work == "create") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            } else if (CenterRepository.work == "personal_clinic") {
                if (integer == 1) {
                    setSpinner(CenterRepository.personal_clinic, principalSpinner, "personal_clinic");

                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            } else if (CenterRepository.work == "counseling_center") {
                if (integer == 1) {
                    setSpinner(CenterRepository.counseling_center, principalSpinner, "counseling_center");

                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    principalProgressBar.setVisibility(View.GONE);
                    principalImageView.setVisibility(View.VISIBLE);
                    principalSpinner.setClickable(true);
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}