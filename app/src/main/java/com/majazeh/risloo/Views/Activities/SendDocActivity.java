package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.InputEditText;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathProvider;
import com.majazeh.risloo.Utils.Managers.StringCustomizer;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;

import org.json.JSONException;

import java.util.Objects;

public class SendDocActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String title = "", description = "", attachment = "";
    private boolean attachmentException = false;
    private boolean attachmentPermissionsGranted = false;

    // Objects
    private Handler handler;
    private IntentManager intentManager;
    private InputEditText inputEditText;
    private PathProvider pathProvider;

    // Widgets
    private Toolbar toolbar;
    private EditText titleEditText, descriptionEditText;
    private LinearLayout attachmentLinearLayout;
    private ImageView attachmentImageView;
    private TextView suffixTextView, attachmentTextView;
    private Button sendButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_send_doc);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        handler = new Handler();

        intentManager = new IntentManager();

        inputEditText = new InputEditText();

        pathProvider = new PathProvider();

        toolbar = findViewById(R.id.activity_send_doc_toolbar);

        titleEditText = findViewById(R.id.activity_send_doc_title_editText);
        descriptionEditText = findViewById(R.id.activity_send_doc_description_editText);

        attachmentLinearLayout = findViewById(R.id.activity_send_doc_attachment_linearLayout);

        attachmentImageView = findViewById(R.id.activity_send_doc_attachment_imageView);

        suffixTextView = findViewById(R.id.activity_send_doc_suffix_textView);
        attachmentTextView = findViewById(R.id.activity_send_doc_attachment_textView);

        sendButton = findViewById(R.id.activity_send_doc_send_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(titleEditText);
                    inputEditText.select(titleEditText);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!descriptionEditText.hasFocus()) {
                    if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                        inputEditText.clear(this, inputEditText.getInput());
                    }

                    inputEditText.focus(descriptionEditText);
                    inputEditText.select(descriptionEditText);
                }
            }
            return false;
        });

        attachmentLinearLayout.setOnClickListener(v -> {
            attachmentLinearLayout.setClickable(false);
            handler.postDelayed(() -> attachmentLinearLayout.setClickable(true), 300);

            if (attachmentException) {
                clearException();
            }

            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
            }

            if (attachmentPermissionsGranted) {
                intentManager.file(this);
            } else {
                checkFilePermission();
            }
        });

        sendButton.setOnClickListener(v -> {
            if (inputEditText.getInput() != null && inputEditText.getInput().hasFocus()) {
                inputEditText.clear(this, inputEditText.getInput());
            }

            if (titleEditText.length() == 0) {
                inputEditText.error(this, titleEditText);
            }
            if (descriptionEditText.length() == 0) {
                inputEditText.error(this, descriptionEditText);
            }
            if (attachment.equals("")) {
                errorException();
            }

            if (attachmentException) {
                clearException();
            }

            if (titleEditText.length() != 0 && descriptionEditText.length() != 0 && !attachment.equals("")) {
                inputEditText.clear(this, titleEditText);
                inputEditText.clear(this, descriptionEditText);

                doWork();
            }
        });
    }

    private void errorException() {
        attachmentException = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p_ripple_violetred);
        } else {
            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p);
        }
    }

    private void clearException() {
        attachmentException = false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);
        } else {
            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude);
        }
    }

    private void setFileWidgetsUi(String fileName) {
        attachmentTextView.setText(StringCustomizer.substring(fileName, '/'));
        attachmentTextView.setTextColor(getResources().getColor(R.color.Nero));

        attachmentImageView.setVisibility(View.GONE);
        suffixTextView.setVisibility(View.VISIBLE);

        switch (StringCustomizer.substring(fileName, '.')) {
            case "png":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixPNG));
                break;
            case "jpg":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixJPG));
                break;
            case "jpeg":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixJPEG));
                break;
            case "gif":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixGIF));
                break;
            case "doc":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixDOC));
                break;
            case "pdf":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixPDF));
                break;
            case "mp4":
                suffixTextView.setText(getResources().getString(R.string.SendDocSuffixMP4));
                break;
            default:
                suffixTextView.setText(StringCustomizer.substring(fileName, '.'));
                break;
        }
    }

    private void doWork() {
        title = titleEditText.getText().toString().trim();
        description = descriptionEditText.getText().toString().trim();

        try {
            progressDialog.show();
            viewModel.sendDoc(title, description, attachment);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("sendDoc")) {
                if (integer == 1) {
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.exception.equals("sendDoc")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("title")) {
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                exceptionToast = ExceptionGenerator.getErrorBody("title");
            }
            if (!ExceptionGenerator.errors.isNull("description")) {
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("description");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("description"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("attachment")) {
                errorException();
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("attachment");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("attachment"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkFilePermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                attachmentPermissionsGranted = true;
                intentManager.file(this);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 300);
            }
        } else {
            attachmentPermissionsGranted = true;
            intentManager.file(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 300) {
            attachmentPermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                attachmentPermissionsGranted = true;
                intentManager.file(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 300) {
                Uri uri = Objects.requireNonNull(data).getData();

                attachment = pathProvider.getLocalPath(this, uri);

                setFileWidgetsUi(attachment);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 300) {
                ExceptionGenerator.getException(false, 0, null, "FileException", "auth");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);

        FileManager.deleteFolderFromCache(this, "documents");
    }

}