package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.InputHandler;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.WindowDecorator;
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
    private IntentCaller intentCaller;
    private InputHandler inputHandler;

    // Widgets
    private Toolbar toolbar;
    private EditText titleEditText, descriptionEditText;
    private LinearLayout attachmentLinearLayout;
    private ImageView attachmentImageView;
    private TextView attachmentTextView;
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

        intentCaller = new IntentCaller();

        inputHandler = new InputHandler();

        toolbar = findViewById(R.id.activity_send_doc_toolbar);

        titleEditText = findViewById(R.id.activity_send_doc_title_editText);
        descriptionEditText = findViewById(R.id.activity_send_doc_description_editText);

        attachmentLinearLayout = findViewById(R.id.activity_send_doc_attachment_linearLayout);

        attachmentImageView = findViewById(R.id.activity_send_doc_attachment_imageView);

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
            if(MotionEvent.ACTION_UP == event.getAction()) {
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

        attachmentLinearLayout.setOnClickListener(v -> {
            attachmentLinearLayout.setClickable(false);
            handler.postDelayed(() -> attachmentLinearLayout.setClickable(true), 300);

            if (attachmentException) {
                clearException();
            }

            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (attachmentPermissionsGranted) {
                intentCaller.file(this, getResources().getString(R.string.SendDocAttachmentChooser));
            } else {
                checkFilePermission();
            }
        });

        sendButton.setOnClickListener(v -> {
            if (inputHandler.getInput() != null && inputHandler.getInput().hasFocus()) {
                inputHandler.clear(this, inputHandler.getInput());
            }

            if (titleEditText.length() == 0) {
                inputHandler.error(this, titleEditText);
            }
            if (descriptionEditText.length() == 0) {
                inputHandler.error(this, descriptionEditText);
            }
            if (attachment.equals("")) {
                errorException();
            }

            if (attachmentException) {
                clearException();
            }

            if (titleEditText.length() != 0 && descriptionEditText.length() != 0 && !attachment.equals("")) {
                inputHandler.clear(this, titleEditText);
                inputHandler.clear(this, descriptionEditText);

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
        int slashPosition = 0;
        int dotPosition = 0;

        for(int i=0; i<fileName.length(); i++) {
            if (fileName.charAt(i) == '/') {
                slashPosition = i;
            } else if (fileName.charAt(i) == '.') {
                dotPosition = i;
            }
        }

        String title = fileName.substring(slashPosition + 1);
        String suffix = fileName.substring(dotPosition + 1);

        attachmentTextView.setText(title);
        attachmentTextView.setTextColor(getResources().getColor(R.color.Nero));

        ImageViewCompat.setImageTintList(attachmentImageView, null);
        attachmentImageView.setImageDrawable(getResources().getDrawable(R.drawable.soc_adobe));

//        switch (suffix) {
//            case "png":
//
//                break;
//            case "jpg":
//
//                break;
//            case "gif":
//
//                break;
//            case "doc":
//
//                break;
//            case "pdf":
//
//                break;
//            default:
//
//                break;
//        }
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
                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.exception.equals("sendDoc")) {
            try {
                String exceptionToast = "";

                if (!ExceptionManager.errors.isNull("title")) {
                    titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    exceptionToast = ExceptionManager.errors.getString("title");
                }
                if (!ExceptionManager.errors.isNull("description")) {
                    descriptionEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("description");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("description"));
                    }
                }
                if (!ExceptionManager.errors.isNull("attachment")) {
                    errorException();
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("attachment");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("attachment"));
                    }
                }
                Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkFilePermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                attachmentPermissionsGranted = true;
                intentCaller.file(this, getResources().getString(R.string.SendDocAttachmentChooser));
            } else {
                ActivityCompat.requestPermissions(this, permissions, 300);
            }
        } else {
            attachmentPermissionsGranted = true;
            intentCaller.file(this, getResources().getString(R.string.SendDocAttachmentChooser));
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
                intentCaller.file(this, getResources().getString(R.string.SendDocAttachmentChooser));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)     {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 300) {
                Uri uri = Objects.requireNonNull(data).getData();

                attachment = Objects.requireNonNull(uri).getPath();

                attachment = getRealPathFromURI(getApplicationContext(), uri);

                setFileWidgetsUi(attachment);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 300) {
                ExceptionManager.getException(false, 0, null, "FileException", "auth");
                Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}