package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.DocumentRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.DocumentViewModel;

import org.json.JSONException;

import java.util.Objects;

public class SendDocumentActivity extends AppCompatActivity {

    // ViewModels
    private DocumentViewModel documentViewModel;

    // Vars
    private String title = "", description = "", attachment = "";
    private boolean attachmentException = false;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private PathManager pathManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
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

        setContentView(R.layout.activity_send_document);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        documentViewModel = new ViewModelProvider(this).get(DocumentViewModel.class);

        handler = new Handler();

        controlEditText = new ControlEditText();

        pathManager = new PathManager();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.SendDocumentTitle));

        titleEditText = findViewById(R.id.activity_send_document_name_editText);
        descriptionEditText = findViewById(R.id.activity_send_document_description_editText);

        attachmentLinearLayout = findViewById(R.id.activity_send_document_file_linearLayout);

        attachmentImageView = findViewById(R.id.activity_send_document_file_imageView);

        suffixTextView = findViewById(R.id.activity_send_document_suffix_textView);
        attachmentTextView = findViewById(R.id.activity_send_document_file_textView);

        sendButton = findViewById(R.id.activity_send_document_send_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
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

        titleEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(titleEditText);
                    controlEditText.select(titleEditText);
                }
            }
            return false;
        });

        descriptionEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
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

        attachmentLinearLayout.setOnClickListener(v -> {
            attachmentLinearLayout.setClickable(false);
            handler.postDelayed(() -> attachmentLinearLayout.setClickable(true), 250);

            if (attachmentException) {
                clearException("attachment");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (PermissionManager.filePermission(this)) {
                IntentManager.file(this);
            }
        });

        sendButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (titleEditText.length() == 0) {
                controlEditText.error(this, titleEditText);
            }
            if (descriptionEditText.length() == 0) {
                controlEditText.error(this, descriptionEditText);
            }
            if (attachment.equals("")) {
                errorException("attachment");
            }

            if (titleEditText.length() != 0 && descriptionEditText.length() != 0 && !attachment.equals("")) {
                controlEditText.clear(this, titleEditText);
                controlEditText.clear(this, descriptionEditText);
                clearException("attachment");

                doWork();
            }
        });
    }

    private void errorException(String type) {
        switch (type) {
            case "title":
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "description":
                descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "attachment":
                attachmentException = true;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p_ripple_violetred);
                } else {
                    attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p);
                }
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "attachment":
                attachmentException = false;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);
                } else {
                    attachmentLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude);
                }
            break;
        }
    }

    private void doWork() {
        title = titleEditText.getText().toString().trim();
        description = descriptionEditText.getText().toString().trim();

        try {
            progressDialog.show();

            documentViewModel.send(title, description, attachment);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        DocumentRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (DocumentRepository.work.equals("send")) {
                if (integer == 1) {
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    DocumentRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    DocumentRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    DocumentRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("send")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("title")) {
                errorException("title");
                exceptionToast = ExceptionGenerator.getErrorBody("title");
            }
            if (!ExceptionGenerator.errors.isNull("description")) {
                errorException("description");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("description");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("description"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("attachment")) {
                errorException("attachment");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("attachment");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("attachment"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.file(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = Objects.requireNonNull(data).getData();

                attachment = pathManager.getLocalPath(this, uri);

                attachmentTextView.setText(StringManager.substring(attachment, '/'));
                attachmentTextView.setTextColor(getResources().getColor(R.color.Nero));

                attachmentImageView.setVisibility(View.GONE);
                suffixTextView.setVisibility(View.VISIBLE);

                switch (StringManager.substring(attachment, '.')) {
                    case "png":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixPNG));
                        break;
                    case "jpg":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixJPG));
                        break;
                    case "jpeg":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixJPEG));
                        break;
                    case "gif":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixGIF));
                        break;
                    case "doc":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixDOC));
                        break;
                    case "pdf":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixPDF));
                        break;
                    case "mp4":
                        suffixTextView.setText(getResources().getString(R.string.SendDocumentSuffixMP4));
                        break;
                    default:
                        suffixTextView.setText(StringManager.substring(attachment, '.'));
                        break;
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100) {
                ExceptionGenerator.getException(false, 0, null, "FileException");
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