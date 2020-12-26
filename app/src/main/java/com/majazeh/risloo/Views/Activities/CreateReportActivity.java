package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class CreateReportActivity extends AppCompatActivity {

    // ViewModels
    private SessionViewModel sessionViewModel;
    private AuthViewModel authViewModel;

    // Adapters
    private SearchAdapter encryptionTypeDialogAdapter;

    // Vars
    public String sessionId = "", report = "", encryptionTypeId = "", encryptionTypeTitle = "";
    private boolean encryptionTypeException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout encryptionTypeFrameLayout;
    public TextView encryptionTypeTextView;
    private EditText descriptionEditText;
    private Button createButton;
    private Dialog encryptionTypeDialog, progressDialog;
    private TextView encryptionTypeDialogTitleTextView;
    private RecyclerView encryptionTypeDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_report);

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
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        encryptionTypeDialogAdapter = new SearchAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CreateReportTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        encryptionTypeFrameLayout = findViewById(R.id.activity_create_report_encryption_type_frameLayout);

        encryptionTypeTextView = findViewById(R.id.activity_create_report_encryption_type_textView);

        descriptionEditText = findViewById(R.id.activity_create_report_description_editText);

        createButton = findViewById(R.id.activity_create_report_button);

        encryptionTypeDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(encryptionTypeDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        encryptionTypeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        encryptionTypeDialog.setContentView(R.layout.dialog_search);
        encryptionTypeDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsEncryptionTypeDialog = new WindowManager.LayoutParams();
        layoutParamsEncryptionTypeDialog.copyFrom(encryptionTypeDialog.getWindow().getAttributes());
        layoutParamsEncryptionTypeDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsEncryptionTypeDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        encryptionTypeDialog.getWindow().setAttributes(layoutParamsEncryptionTypeDialog);

        encryptionTypeDialogTitleTextView = encryptionTypeDialog.findViewById(R.id.dialog_search_title_textView);
        encryptionTypeDialogTitleTextView.setText(getResources().getString(R.string.CreateReportEncryptionTypeDialogTitle));

        encryptionTypeDialogRecyclerView = encryptionTypeDialog.findViewById(R.id.dialog_search_recyclerView);
        encryptionTypeDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        encryptionTypeDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        encryptionTypeDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
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

        encryptionTypeTextView.setOnClickListener(v -> {
            encryptionTypeTextView.setClickable(false);
            handler.postDelayed(() -> encryptionTypeTextView.setClickable(true), 250);

            if (encryptionTypeException) {
                clearException("encryptionType");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

//            setRecyclerView(sessionViewModel.getReportType(authViewModel.getPublicKey()), encryptionTypeDialogRecyclerView, "getEncryptionTypes");

            encryptionTypeDialog.show();
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

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (encryptionTypeId.isEmpty()) {
                errorException("encryptionType");
            }
            if (descriptionEditText.length() == 0) {
                controlEditText.error(this, descriptionEditText);
            }

            if (!encryptionTypeId.isEmpty() && descriptionEditText.length() != 0) {
                clearException("encryptionType");
                controlEditText.clear(this, descriptionEditText);

                doWork();
            }
        });

        encryptionTypeDialog.setOnCancelListener(dialog -> encryptionTypeDialog.dismiss());
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("session_id") != null)
            sessionId = extras.getString("session_id");
        if (extras.getString("report") != null)
            report = extras.getString("report");
        if (extras.getString("encryption_type_id") != null)
            encryptionTypeId = extras.getString("encryption_type_id");
        if (extras.getString("encryption_type_title") != null)
            encryptionTypeTitle = extras.getString("encryption_type_title");

        if (!report.equals("")) {
            descriptionEditText.setText(report);
            descriptionEditText.setTextColor(getResources().getColor(R.color.Grey));
        }

        if (!encryptionTypeId.equals("")) {
            encryptionTypeTextView.setText(encryptionTypeTitle);
            encryptionTypeTextView.setTextColor(getResources().getColor(R.color.Grey));
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getEncryptionTypes":
                encryptionTypeDialogAdapter.setValues(arrayList, method, "CreateReport");
                recyclerView.setAdapter(encryptionTypeDialogAdapter);
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "encryptionType":
                encryptionTypeException = true;
                encryptionTypeFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "description":
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "encryptionType":
                encryptionTypeException = false;
                encryptionTypeFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void doWork() {
        report = descriptionEditText.getText().toString().trim();

        try {
            progressDialog.show();

            sessionViewModel.Report(sessionId, report, encryptionTypeId);
            observeWork("sessionViewModel");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "sessionViewModel":
                SessionRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (SessionRepository.work.equals("createReport")) {
                        if (integer == 1) {
                            setResult(RESULT_OK, null);
                            finish();

                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            progressDialog.dismiss();
                            observeException();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            progressDialog.dismiss();
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            SessionRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;
        }
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("session_id")) {
                exceptionToast = ExceptionGenerator.getErrorBody("session_id");
            }
            if (!ExceptionGenerator.errors.isNull("report")) {
                errorException("report");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("report");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("report"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("encryption_type")) {
                errorException("encryptionType");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("encryption_type");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("encryption_type"));
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
                case "getEncryptionTypes":
                    if (!encryptionTypeId.equals(model.get("en_title").toString())) {
                        encryptionTypeId = model.get("en_title").toString();
                        encryptionTypeTitle = model.get("fa_title").toString();

                        encryptionTypeTextView.setText(encryptionTypeTitle);
                        encryptionTypeTextView.setTextColor(getResources().getColor(R.color.Grey));
                    } else if (encryptionTypeTitle.equals(model.get("en_title").toString())) {
                        encryptionTypeTitle = "";
                        encryptionTypeTitle = "";

                        encryptionTypeTextView.setText(getResources().getString(R.string.CreateReportEncryptionType));
                        encryptionTypeTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    encryptionTypeDialog.dismiss();
                    break;
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