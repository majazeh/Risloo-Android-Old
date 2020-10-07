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
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Models.Managers.BitmapManager;
import com.majazeh.risloo.Utils.CustomNumberPicker;
import com.majazeh.risloo.Utils.InputHandler;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String name = "", gender = "", birthday = "";
    private String imageFilePath = "";
    private int Year, Month, Day;
    private boolean genderException = false, birthdayException = false;
    public boolean galleryPermissionsGranted = false, cameraPermissionsGranted = false;

    // Objects
    private Handler handler;
    private IntentCaller intentCaller;
    private InputHandler inputHandler;
    private ImageDialog imageDialog;
    private Bitmap selectedBitmap;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarCircleImageView;
    private ImageView avatarImageView;
    private EditText nameEditText;
    private TabLayout genderTabLayout;
    private TextView birthdayTextView;
    private Button editButton;
    private Dialog dateDialog, progressDialog;
    private CustomNumberPicker yearNumberPicker, monthNumberPicker, dayNumberPicker;
    private TextView dateDialogPositive, dateDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_account);

        initializer();

        detector();

        listener();

        setData();

        setCustomPicker();
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

        imageDialog = new ImageDialog(this);
        imageDialog.setType("editAccount");

        toolbar = findViewById(R.id.activity_edit_account_toolbar);

        avatarCircleImageView = findViewById(R.id.activity_edit_account_avatar_circleImageView);

        avatarImageView = findViewById(R.id.activity_edit_account_avatar_imageView);

        nameEditText = findViewById(R.id.activity_edit_account_name_editText);

        genderTabLayout = findViewById(R.id.activity_edit_account_gender_tabLayout);

        birthdayTextView = findViewById(R.id.activity_edit_account_birthday_textView);

        editButton = findViewById(R.id.activity_edit_account_edit_button);

        dateDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(dateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.setContentView(R.layout.dialog_date);
        dateDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dateDialog.getWindow().setAttributes(layoutParams);

        yearNumberPicker = dateDialog.findViewById(R.id.dialog_date_year_NumberPicker);
        monthNumberPicker = dateDialog.findViewById(R.id.dialog_date_month_NumberPicker);
        dayNumberPicker = dateDialog.findViewById(R.id.dialog_date_day_NumberPicker);

        dateDialogPositive = dateDialog.findViewById(R.id.dialog_date_positive_textView);
        dateDialogNegative = dateDialog.findViewById(R.id.dialog_date_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            avatarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_quartz_ripple_quartz);
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            dateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            dateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        avatarCircleImageView.setOnClickListener(v -> {
            avatarCircleImageView.setClickable(false);
            handler.postDelayed(() -> avatarCircleImageView.setClickable(true), 300);

            if (!viewModel.getName().equals("") && !viewModel.getAvatar().equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", viewModel.getName());
                if (selectedBitmap == null) {
                    intent.putExtra("bitmap", false);
                    intent.putExtra("image", viewModel.getAvatar());
                } else {
                    intent.putExtra("bitmap", true);
                    intent.putExtra("image", BitmapManager.bitmapToByte(selectedBitmap));
                }

                startActivity(intent);
            }
        });

        avatarImageView.setOnClickListener(v -> {
            avatarImageView.setClickable(false);
            handler.postDelayed(() -> avatarImageView.setClickable(true), 300);

            if (nameEditText.hasFocus()) {
                inputHandler.clear(this, nameEditText);
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    inputHandler.select(nameEditText);
                }
            }
            return false;
        });

        genderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (genderException) {
                    clearException("gender");
                }

                if (nameEditText.hasFocus()) {
                    inputHandler.clear((Activity) getApplicationContext(), nameEditText);
                }

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
            birthdayTextView.setClickable(false);
            handler.postDelayed(() -> birthdayTextView.setClickable(true), 300);

            if (birthdayException) {
                clearException("birthday");
            }

            if (nameEditText.hasFocus()) {
                inputHandler.clear(this, nameEditText);
            }

            dateDialog.show();
            return false;
        });

        monthNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (picker == monthNumberPicker) {
                if (newVal <= 6) {
                    dayNumberPicker.setMaxValue(31);
                } else {
                    dayNumberPicker.setMaxValue(30);
                }
            }
        });

        editButton.setOnClickListener(v -> {
            if (nameEditText.length() == 0) {
                inputHandler.error(this, nameEditText);
            } else {
                inputHandler.clear(this, nameEditText);

                if (genderException) {
                    clearException("gender");
                }
                if (birthdayException) {
                    clearException("birthday");
                }

                if (selectedBitmap == null) {
                    doWork("edit");
                } else {
                    doWork("avatar");
                }
            }
        });

        dateDialogPositive.setOnClickListener(v -> {
            dateDialogPositive.setClickable(false);
            handler.postDelayed(() -> dateDialogPositive.setClickable(true), 300);
            dateDialog.dismiss();

            Year = yearNumberPicker.getValue();
            Month = monthNumberPicker.getValue();
            Day = dayNumberPicker.getValue();

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
            handler.postDelayed(() -> dateDialogNegative.setClickable(true), 300);
            dateDialog.dismiss();

            yearNumberPicker.setValue(Year);
            monthNumberPicker.setValue(Month);
            dayNumberPicker.setValue(Day);
        });

        dateDialog.setOnCancelListener(dialog -> {
            dateDialog.dismiss();

            yearNumberPicker.setValue(Year);
            monthNumberPicker.setValue(Month);
            dayNumberPicker.setValue(Day);
        });
    }

    private void setData() {
        if (viewModel.getAvatar().equals("")) {
            avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_solid));
        } else {
            Picasso.get().load(viewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
        }

        if (viewModel.getName().equals("")) {
            nameEditText.setText(getResources().getString(R.string.AuthNameDefault));
        } else {
            name = viewModel.getName();
            nameEditText.setText(name);
        }

        if (viewModel.getGender().equals("مرد")) {
            gender = "male";
            Objects.requireNonNull(genderTabLayout.getTabAt(0)).select();
        } else if (viewModel.getGender().equals("زن")) {
            gender = "female";
            Objects.requireNonNull(genderTabLayout.getTabAt(1)).select();
        } else {
            gender = "male";
            Objects.requireNonNull(genderTabLayout.getTabAt(0)).select();
        }

        if (viewModel.getBirthday().equals("")) {
            birthday = getResources().getString(R.string.AuthBirthdayDefault);
            birthdayTextView.setText(birthday);
        } else {
            birthday = viewModel.getBirthday();
            birthdayTextView.setText(birthday);
        }

        Year = Integer.parseInt(StringCustomizer.dateToString("yyyy", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));
        Month = Integer.parseInt(StringCustomizer.dateToString("MM", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));
        Day = Integer.parseInt(StringCustomizer.dateToString("dd", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));
    }

    private void setCustomPicker() {
        yearNumberPicker.setMinValue(1300);
        yearNumberPicker.setMaxValue(2100);
        yearNumberPicker.setValue(Year);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(Month);
        monthNumberPicker.setDisplayedValues(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"});

        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        dayNumberPicker.setValue(Day);
    }

    private void errorException(String type) {
        if (type.equals("gender")) {
            genderException = true;
            genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("birthday")) {
            birthdayException = true;
            birthdayTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void clearException(String type) {
        if (type.equals("gender")) {
            genderException = false;
            genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        } else if (type.equals("birthday")) {
            birthdayException = false;
            birthdayTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        }
    }

    private void doWork(String method) {
        try {
            progressDialog.show();
            switch (method) {
                case "avatar":
                    FileManager.writeBitmapToCache(this, BitmapManager.bitmapToByte(selectedBitmap), "avatar");
                    viewModel.avatar();
                    break;
                case "edit":
                    name = nameEditText.getText().toString().trim();
                    viewModel.edit(name, gender, birthday);
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("avatar")) {
                if (integer == 1) {
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                    doWork("edit");
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            } else if (AuthRepository.work.equals("edit")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("edit")) {
            try {
                String exceptionToast = "";

                if (!ExceptionManager.errors.isNull("name")) {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    exceptionToast = ExceptionManager.errors.getString("name");
                }
                if (!ExceptionManager.errors.isNull("gender")) {
                    errorException("gender");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("gender");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("gender"));
                    }
                }
                if (!ExceptionManager.errors.isNull("birthday")) {
                    errorException("birthday");
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("birthday");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("birthday"));
                    }
                }
                Toast.makeText(this, "" + exceptionToast, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        } else if (requestCode == 200) {
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
                    Uri imageUri = Objects.requireNonNull(data).getData();
                    InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    selectedBitmap = Bitmap.createScaledBitmap(bitmap,1024,1024,true);

                    avatarCircleImageView.setImageBitmap(BitmapManager.rotate(selectedBitmap, ""));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 200) {
                File imageFile = new File(imageFilePath);
                intentCaller.mediaScan(this, imageFile);

                int targetWidth = avatarCircleImageView.getWidth();
                int targetHeight = avatarCircleImageView.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(imageFilePath, bmOptions);

                int photoWidth = bmOptions.outWidth;
                int photoHeight = bmOptions.outHeight;

                int scaleFactor = Math.max(1, Math.min(photoWidth / targetWidth, photoHeight / targetHeight));

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
                selectedBitmap = Bitmap.createScaledBitmap(bitmap,720,720,true);

                avatarCircleImageView.setImageBitmap(BitmapManager.rotate(selectedBitmap, imageFilePath));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100)
                Toast.makeText(this, "عکسی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            else if (requestCode == 200)
                Toast.makeText(this, "عکسی گرفته نشده است.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}