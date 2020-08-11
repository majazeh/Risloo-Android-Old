package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Controllers.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.BitmapController;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.UnitConverter;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String avatar = "", name = "", gender = "", birthday = "";
    private String imageFilePath;
    private int Year, Month, Day;
    public boolean galleryPermissionsGranted = false, cameraPermissionsGranted = false;

    // Objects
    private IntentCaller intentCaller;
    private ImageDialog imageDialog;
    private Handler handler;
    private Bitmap selectedImage;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarImageView;
    private EditText nameEditText;
    private TabLayout genderTabLayout;
    private TextView birthdayTextView;
    private Button editButton;
    private Dialog dateDialog, progressDialog;
    private DatePicker dateDialogDatePicker;
    private TextView dateDialogPositive, dateDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_account);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        intentCaller = new IntentCaller();

        imageDialog = new ImageDialog(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_edit_account_toolbar);

        avatarImageView = findViewById(R.id.activity_edit_account_avatar_circleImageView);
        if (viewModel.getAvatar().equals("")) {
            avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle));
        } else {
            avatarImageView.setImageBitmap(BitmapController.decodeToBase64(viewModel.getAvatar()));
        }

        nameEditText = findViewById(R.id.activity_edit_account_name_editText);
        try {
            nameEditText.setText(viewModel.getAll().get(0).get("subTitle").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        genderTabLayout = findViewById(R.id.activity_edit_account_gender_tabLayout);
        try {
            if (viewModel.getAll().get(4).get("subTitle").toString().equals("مرد")) {
                gender = "male";
                genderTabLayout.getTabAt(0).select();
            } else if (viewModel.getAll().get(4).get("subTitle").toString().equals("رن")) {
                gender = "female";
                genderTabLayout.getTabAt(1).select();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        birthdayTextView = findViewById(R.id.activity_edit_account_birthday_textView);
        try {
            birthday = viewModel.getAll().get(5).get("subTitle").toString();
            birthdayTextView.setText(birthday);

            Year = Integer.parseInt(UnitConverter.dateToString("yyyy", UnitConverter.stringToDate("yyyy-MM-dd", birthday)));
            Month = Integer.parseInt(UnitConverter.dateToString("MM", UnitConverter.stringToDate("yyyy-MM-dd", birthday))) - 1;
            Day = Integer.parseInt(UnitConverter.dateToString("dd", UnitConverter.stringToDate("yyyy-MM-dd", birthday)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editButton = findViewById(R.id.activity_edit_account_edit_button);

        dateDialog = new Dialog(this, R.style.DialogTheme);
        dateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.setContentView(R.layout.dialog_date);
        dateDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dateDialog.getWindow().setAttributes(layoutParams);

        dateDialogDatePicker = dateDialog.findViewById(R.id.dialog_date_datePicker);
        dateDialogDatePicker.init(Year, Month, Day, null);

        dateDialogPositive = dateDialog.findViewById(R.id.dialog_date_positive_textView);
        dateDialogNegative = dateDialog.findViewById(R.id.dialog_date_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);

            dateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            dateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        avatarImageView.setOnClickListener(v -> imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet"));

        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                nameEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                nameEditText.setCursorVisible(true);
            }
            return false;
        });

        genderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        gender = "male";
                        break;
                    case 1:
                        gender = "female";
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

        birthdayTextView.setOnTouchListener((v, event) -> {
            dateDialog.show();
            return false;
        });

        editButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();

            if (nameEditText.length() == 0) {
                checkInput();
            } else {
                clearData();

//                try {
//                    progressDialog.show();
//                    viewModel.edit(name, gender, birthday);
//                    observeWork();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

        dateDialogPositive.setOnClickListener(v -> {
            dateDialogPositive.setClickable(false);
            handler.postDelayed(() -> dateDialogPositive.setClickable(true), 1000);
            dateDialog.dismiss();

            Year = dateDialogDatePicker.getYear();
            Month = dateDialogDatePicker.getMonth() + 1;
            Day = dateDialogDatePicker.getDayOfMonth();

            dateDialogDatePicker.updateDate(Year, Month - 1, Day);

            if (Month < 10) {
                if (Day < 10)
                    birthday = Year + "-" + "0" + Month + "-" + "0" + Day;
                else
                    birthday = Year + "-" + "0" + Month + "-" + Day;
            } else {
                if (Day < 10)
                    birthday = Year + "-" + Month + "-" + "0" + Day;
                else
                    birthday = Year + "-" + Month + "-" + Day;
            }
            birthdayTextView.setText(birthday);
        });

        dateDialogNegative.setOnClickListener(v -> {
            dateDialogNegative.setClickable(false);
            handler.postDelayed(() -> dateDialogNegative.setClickable(true), 1000);
            dateDialog.dismiss();

            dateDialogDatePicker.updateDate(Year, Month, Day);
        });

        dateDialog.setOnCancelListener(dialog -> {
            dateDialog.dismiss();

            dateDialogDatePicker.updateDate(Year, Month, Day);
        });
    }

    private void checkInput() {
        nameEditText.setCursorVisible(false);

        if (nameEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
        }
    }

    private void clearData() {
        nameEditText.setCursorVisible(false);

        nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
    }

    private void observeWork() {
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthController.work == "edit") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_";
        File imageStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", imageStorageDir);
        imageFilePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void checkGalleryPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                galleryPermissionsGranted = true;
                intentCaller.gallery(this);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        } else {
            galleryPermissionsGranted = true;
            intentCaller.gallery(this);
        }
    }

    public void checkCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    cameraPermissionsGranted = true;
                    try {
                        intentCaller.camera(this, createImageFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 200);
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions, 200);
            }
        } else {
            cameraPermissionsGranted = true;
            try {
                intentCaller.camera(this, createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            galleryPermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                galleryPermissionsGranted = true;
                intentCaller.gallery(this);
            }
        } else if (requestCode ==200) {
            cameraPermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                cameraPermissionsGranted = true;
                try {
                    intentCaller.camera(this, createImageFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);

                    selectedImage = BitmapFactory.decodeStream(imageStream);

                    avatar = BitmapController.encodeToBase64(selectedImage);

                    avatarImageView.setImageBitmap(BitmapController.rotate(selectedImage, ""));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 200) {
                File imageFile = new File(imageFilePath);

                if (imageFile != null) {
                    intentCaller.mediaScan(this, imageFile);
                }

                int targetWidth = avatarImageView.getWidth();
                int targetHeight = avatarImageView.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(imageFilePath, bmOptions);

                int photoWidth = bmOptions.outWidth;
                int photoHeight = bmOptions.outHeight;

                int scaleFactor = Math.max(1, Math.min(photoWidth / targetWidth, photoHeight / targetHeight));

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                selectedImage = BitmapFactory.decodeFile(imageFilePath, bmOptions);

                avatar = BitmapController.encodeToBase64(selectedImage);

                avatarImageView.setImageBitmap(BitmapController.rotate(selectedImage, imageFilePath));
            }
        } else if (resultCode == RESULT_CANCELED){
            if (requestCode == 100) {
                Toast.makeText(this, "عکسی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            } else if (requestCode == 200) {
                Toast.makeText(this, "عکسی گرفته نشده است.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}