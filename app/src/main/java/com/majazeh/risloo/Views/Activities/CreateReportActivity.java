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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CreateReportActivity extends AppCompatActivity {

    // ViewModels
    private SessionViewModel sessionViewModel;
    private AuthViewModel authViewModel;

    // Adapters
    private SearchAdapter encryptionDialogAdapter;

    // Vars
    public String sessionId = "", encryptionId = "", encryptionTitle = "", description = "";
    private boolean encryptionException = false;
    private boolean encrypted = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout encryptionFrameLayout;
    public TextView encryptionTextView;
    private EditText descriptionEditText;
    private LinearLayout cryptoLinearLayout;
    private TextView cryptoTextView;
    private Button createButton;
    private Dialog encryptionDialog, progressDialog;
    private TextView encryptionDialogTitleTextView;
    private RecyclerView encryptionDialogRecyclerView;

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

        encryptionDialogAdapter = new SearchAdapter(this);

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

        encryptionFrameLayout = findViewById(R.id.activity_create_report_encryption_frameLayout);

        encryptionTextView = findViewById(R.id.activity_create_report_encryption_textView);

        descriptionEditText = findViewById(R.id.activity_create_report_description_editText);

        cryptoLinearLayout = findViewById(R.id.activity_create_report_crypto_linearLayout);

        cryptoTextView = findViewById(R.id.activity_create_report_crypto_textView);

        createButton = findViewById(R.id.activity_create_report_button);

        encryptionDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(encryptionDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        encryptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        encryptionDialog.setContentView(R.layout.dialog_search);
        encryptionDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsEncryptionDialog = new WindowManager.LayoutParams();
        layoutParamsEncryptionDialog.copyFrom(encryptionDialog.getWindow().getAttributes());
        layoutParamsEncryptionDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsEncryptionDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        encryptionDialog.getWindow().setAttributes(layoutParamsEncryptionDialog);

        encryptionDialogTitleTextView = encryptionDialog.findViewById(R.id.dialog_search_title_textView);
        encryptionDialogTitleTextView.setText(getResources().getString(R.string.CreateReportEncryptionDialogTitle));

        encryptionDialogRecyclerView = encryptionDialog.findViewById(R.id.dialog_search_recyclerView);
        encryptionDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        encryptionDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        encryptionDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            cryptoTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_islamicgreen_ripple_solitude);

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

        encryptionTextView.setOnClickListener(v -> {
            encryptionTextView.setClickable(false);
            handler.postDelayed(() -> encryptionTextView.setClickable(true), 250);

            if (encryptionException) {
                clearException("encryption");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            try {
                setRecyclerView(sessionViewModel.getReportType(authViewModel.getPublicKey()), encryptionDialogRecyclerView, "getEncryptions");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            encryptionDialog.show();
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

        cryptoTextView.setOnClickListener(v -> {
            cryptoTextView.setClickable(false);
            handler.postDelayed(() -> cryptoTextView.setClickable(true), 250);

            if (descriptionEditText.length() != 0) {
                try {
                    description = sessionViewModel.encrypt(descriptionEditText.getText().toString().trim(), authViewModel.getPublicKey());

                    descriptionEditText.setText(description);
                    descriptionEditText.setTextColor(getResources().getColor(R.color.Grey));

                    descriptionEditText.setEnabled(false);
                    descriptionEditText.setFocusableInTouchMode(false);
                    descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_border_quartz);

                    encrypted = true;
                    cryptoLinearLayout.setVisibility(View.GONE);

                } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (encryptionId.isEmpty()) {
                errorException("encryption");
            }
            if (descriptionEditText.length() == 0) {
                controlEditText.error(this, descriptionEditText);
            }

            if (!encryptionId.isEmpty() && descriptionEditText.length() != 0) {
                clearException("encryption");
                controlEditText.clear(this, descriptionEditText);

                if (encryptionId.equals("publicKey")) {
                    if (encrypted) {
                        doWork();
                    } else {
                        ExceptionGenerator.getException(false, 0, null, "EncryptFirstException");
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    doWork();
                }
            }
        });

        encryptionDialog.setOnCancelListener(dialog -> encryptionDialog.dismiss());
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("session_id") != null)
            sessionId = extras.getString("session_id");
        if (extras.getString("report") != null)
            description = extras.getString("report");
        if (extras.getString("encryption_type_id") != null)
            encryptionId = extras.getString("encryption_type_id");
        if (extras.getString("encryption_type_title") != null)
            encryptionTitle = extras.getString("encryption_type_title");

        if (!description.equals("")) {
            descriptionEditText.setText(description);
            descriptionEditText.setTextColor(getResources().getColor(R.color.Grey));
        }

        if (!encryptionId.equals("")) {
            encryptionTextView.setText(encryptionTitle);
            encryptionTextView.setTextColor(getResources().getColor(R.color.Grey));
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getEncryptions":
                encryptionDialogAdapter.setValues(arrayList, method, "CreateReport");
                recyclerView.setAdapter(encryptionDialogAdapter);
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "encryption":
                encryptionException = true;
                encryptionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "description":
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "encryption":
                encryptionException = false;
                encryptionFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void doWork() {
        description = descriptionEditText.getText().toString().trim();

        try {
            progressDialog.show();

            sessionViewModel.Report(sessionId, description, encryptionId);
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
                errorException("description");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("report");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("report"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("encryption_type")) {
                errorException("encryption");
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
                case "getEncryptions":
                    if (!encryptionId.equals(model.get("en_title").toString())) {
                        encryptionId = model.get("en_title").toString();
                        encryptionTitle = model.get("fa_title").toString();

                        encryptionTextView.setText(encryptionTitle);
                        encryptionTextView.setTextColor(getResources().getColor(R.color.Grey));
                    } else if (encryptionTitle.equals(model.get("en_title").toString())) {
                        encryptionId = "";
                        encryptionTitle = "";

                        encryptionTextView.setText(getResources().getString(R.string.CreateReportEncryption));
                        encryptionTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    if (encryptionId.equals("publicKey")) {
                        cryptoLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        cryptoLinearLayout.setVisibility(View.GONE);
                        if (encrypted) {
                            encrypted = false;

                            descriptionEditText.setEnabled(true);
                            descriptionEditText.setFocusableInTouchMode(true);
                            descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                        }
                    }

                    encryptionDialog.dismiss();
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