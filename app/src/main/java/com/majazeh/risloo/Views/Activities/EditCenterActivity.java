package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private SpinnerAdapter phoneAdapter;

    // Vars
    private String type = "", principal = "", title = "", description = "", address = "";
    private boolean titleError;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private Spinner principalSpinner;
    public TextView principalTextView, phoneTextView, phoneDialogPositive, phoneDialogNegative;
    private EditText titleEditText, descriptionEditText, addressEditText, phoneDialogEditText;
    private RecyclerView phoneRecyclerView;
    private ProgressBar principalProgressBar;
    private ImageView principalImageView, phoneImageView;
    private FrameLayout principalFrameLayout;
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

        setData("center");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(CenterViewModel.class);

        phoneAdapter = new SpinnerAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_edit_center_toolbar);

        principalSpinner = findViewById(R.id.activity_edit_center_principal_spinner);

        principalTextView = findViewById(R.id.activity_edit_center_principal_textView);
        phoneTextView = findViewById(R.id.activity_edit_center_phone_textView);

        titleEditText = findViewById(R.id.activity_edit_center_title_editText);
        descriptionEditText = findViewById(R.id.activity_edit_center_description_editText);
        addressEditText = findViewById(R.id.activity_edit_center_address_editText);

        phoneRecyclerView = findViewById(R.id.activity_edit_center_phone_recyclerView);
        phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        phoneRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        phoneRecyclerView.setHasFixedSize(false);

        principalProgressBar = findViewById(R.id.activity_edit_center_principal_progressBar);

        principalImageView = findViewById(R.id.activity_edit_center_principal_imageView);
        phoneImageView = findViewById(R.id.activity_edit_center_phone_imageView);

        principalFrameLayout = findViewById(R.id.activity_edit_center_principal_frameLayout);

        editButton = findViewById(R.id.activity_edit_center_button);

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
            phoneImageView.setBackgroundResource(R.drawable.draw_rectangle_quartz_ripple);

            editButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);

            phoneDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            phoneDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        principalSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (SampleRepository.rooms.size() == 0) {
                    doWork("getPrincipals");
                }

                titleEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            }
            return false;
        });

        principalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (principalSpinner.getCount() != position) {
//                    try {
//                        principal = String.valueOf(CenterRepository.principals.get(position).get("id"));
//                        principalFrameLayout.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        phoneImageView.setOnClickListener(v -> phoneDialog.show());

        titleEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                titleEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                titleEditText.setCursorVisible(true);

                titleError = false;

                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                descriptionEditText.setCursorVisible(true);

                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                if (titleError) {
                    titleEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    titleEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                addressEditText.setCursorVisible(true);

                if (titleError) {
                    titleEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    titleEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            }
            return false;
        });

        editButton.setOnClickListener(v -> {
            title = titleEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

            if (principal.isEmpty() && titleEditText.length() == 0) {
                checkInput();
            } else {
                clearData();

//                try {
//                    progressDialog.show();
//                    viewModel.edit(principal, title, description, address, phoneAdapter.getValues());
//                    observeWork();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        phoneDialogEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
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
                        setRecyclerView(phoneAdapter.getValues(), phoneRecyclerView, "phoneEdit");
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
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            } else {
                phoneDialogEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            }

        });

        phoneDialogNegative.setOnClickListener(v -> {
            phoneDialogNegative.setClickable(false);
            handler.postDelayed(() -> phoneDialogNegative.setClickable(true), 500);
            phoneDialog.dismiss();

            phoneDialogEditText.getText().clear();
            phoneDialogEditText.setCursorVisible(false);
            phoneDialogEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        });

        phoneDialog.setOnCancelListener(dialog -> {
            phoneDialog.dismiss();

            phoneDialogEditText.getText().clear();
            phoneDialogEditText.setCursorVisible(false);
            phoneDialogEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
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
                return super.getCount() - 1;
            }

        };

        adapter.add(getResources().getString(R.string.EditCenterPrincipal));
        principalTextView.setVisibility(View.GONE);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String type) {
        switch (type) {
            case "phoneEdit":
                phoneAdapter.setValue(arrayList, type);
                recyclerView.setAdapter(phoneAdapter);
                break;
        }
    }

    private void setData(String method) {
        if (method.equals("center")) {
            type = method;

            toolbar.setTitle(getResources().getString(R.string.EditCenterTitle));

//            principal = String.valueOf();
//            principalSpinner.setSelection("");
//
//            titleEditText.setText("");
//            descriptionEditText.setText("");
//            addressEditText.setText("");
//
//            setRecyclerView("", phoneRecyclerView, "phone");
        } else if (method.equals("clinic")) {
            type = method;

            toolbar.setTitle(getResources().getString(R.string.EditClinicTitle));

            principalFrameLayout.setVisibility(View.GONE);
            titleEditText.setVisibility(View.GONE);

//            descriptionEditText.setText("");
//            addressEditText.setText("");
//
//            setRecyclerView("", phoneRecyclerView, "phone");
        }
    }

    private void checkInput() {
        if (principal.isEmpty() && titleEditText.length() == 0) {
            principalFrameLayout.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            titleEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            titleError = true;
        } else if (titleEditText.length() == 0) {
            principalFrameLayout.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            titleEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            titleError = true;
        } else if (principal.isEmpty()) {
            principalFrameLayout.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            titleEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            titleError = false;
        }
    }

    private void clearData() {
        titleEditText.setCursorVisible(false);
        descriptionEditText.setCursorVisible(false);
        addressEditText.setCursorVisible(false);

        principalFrameLayout.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        titleEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        titleError = false;
    }

    private void doWork(String method) {
//        try {
//            switch (method) {
//                case "getPrincipals":
//                    principalProgressBar.setVisibility(View.VISIBLE);
//                    principalImageView.setVisibility(View.GONE);
//                    principalSpinner.setClickable(false);
//
//                    viewModel.principals();
//                    break;
//            }
//            observeWork();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork() {
//        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
//            if (CenterRepository.work == "edit") {
//                if (integer == 1) {
//                    setResult(RESULT_OK, null);
//                    finish();
//
//                    progressDialog.dismiss();
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == 0) {
//                    progressDialog.dismiss();
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == -2) {
//                    progressDialog.dismiss();
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                }
//            } else if (CenterRepository.work == "getPrincipals") {
//                if (integer == 1) {
//                    setSpinner(CenterRepository.principals, principalSpinner, "principal");
//
//                    principalProgressBar.setVisibility(View.GONE);
//                    principalImageView.setVisibility(View.VISIBLE);
//                    principalSpinner.setClickable(true);
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == 0) {
//                    principalProgressBar.setVisibility(View.GONE);
//                    principalImageView.setVisibility(View.VISIBLE);
//                    principalSpinner.setClickable(true);
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == -2) {
//                    principalProgressBar.setVisibility(View.GONE);
//                    principalImageView.setVisibility(View.VISIBLE);
//                    principalSpinner.setClickable(true);
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
//                }
//            }
//        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}