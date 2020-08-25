package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;

import org.json.JSONException;

public class EditCenterActivity extends AppCompatActivity {

    // ViewModels
    private CenterViewModel viewModel;

    // Vars
    private int principal = -1;
    private String name = "", description = "", address = "";
    private boolean principalError, nameError;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private FrameLayout principalFrameLayout;
    private Spinner principalSpinner;
    private EditText nameEditText, descriptionEditText, addressEditText;
    private Button editButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_center);

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

        handler = new Handler();

        toolbar = findViewById(R.id.activity_edit_center_toolbar);

        principalFrameLayout = findViewById(R.id.activity_edit_center_principal_frameLayout);

        principalSpinner = findViewById(R.id.activity_edit_center_principal_spinner);

        nameEditText = findViewById(R.id.activity_edit_center_name_editText);
        descriptionEditText = findViewById(R.id.activity_edit_center_description_editText);
        addressEditText = findViewById(R.id.activity_edit_center_address_editText);

        editButton = findViewById(R.id.activity_edit_center_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        principalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                nameEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                nameEditText.setCursorVisible(true);

                nameError = false;

                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

                if (principalError) {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                descriptionEditText.setCursorVisible(true);

                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

                if (principalError) {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
            }
            return false;
        });

        addressEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                addressEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                addressEditText.setCursorVisible(true);

                if (principalError) {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }

                descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            }
            return false;
        });

        editButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();
            description = descriptionEditText.getText().toString().trim();
            address = addressEditText.getText().toString().trim();

            if (principal == -1 || nameEditText.length() == 0) {
                checkInput();
            } else {
                clearData();

//                try {
//                    progressDialog.show();
//                    viewModel.edit(principal, name, description, address, phones);
//                    observeWork();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void checkInput() {
        nameEditText.setCursorVisible(false);
        descriptionEditText.setCursorVisible(false);
        addressEditText.setCursorVisible(false);

        if (principal == -1 && nameEditText.length() == 0) {
            principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            principalError = true;
            nameError = true;
        } else if (nameEditText.length() == 0) {
            principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            principalError = false;
            nameError = true;
        } else if (principal == -1) {
            principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            principalError = true;
            nameError = false;
        } else {
            principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            principalError = false;
            nameError = false;
        }
    }

    private void clearData() {
        nameEditText.setCursorVisible(false);
        descriptionEditText.setCursorVisible(false);
        addressEditText.setCursorVisible(false);

        principalSpinner.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        descriptionEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        addressEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
    }

    private void observeWork() {
        CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CenterRepository.work == "edit") {
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
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}