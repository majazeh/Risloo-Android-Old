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
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.BitmapController;
import com.majazeh.risloo.Utils.CustomNumberPicker;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private String avatar = "", name = "", gender = "", birthday = "";
    private String imageFilePath;
    private int Year, Month, Day;
    private boolean genderException = false, birthdayException = false;
    public boolean galleryPermissionsGranted = false, cameraPermissionsGranted = false;

    // Objects
    private IntentCaller intentCaller;
    private ImageDialog imageDialog;
    private Handler handler;
    private Bitmap selectedImage;

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

        setCustomPicker();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        intentCaller = new IntentCaller();

        imageDialog = new ImageDialog(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_edit_account_toolbar);

        avatarCircleImageView = findViewById(R.id.activity_edit_account_avatar_circleImageView);
        if (viewModel.getAvatar().equals("")) {
            avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_solid));
        } else {
            avatar = viewModel.getAvatar();
            Picasso.get().load(avatar).placeholder(R.color.Solitude).into(avatarCircleImageView);
        }
        avatarImageView = findViewById(R.id.activity_edit_account_avatar_imageView);

        nameEditText = findViewById(R.id.activity_edit_account_name_editText);
        name = viewModel.getName();
        nameEditText.setText(name);

        genderTabLayout = findViewById(R.id.activity_edit_account_gender_tabLayout);
        if (viewModel.getGender().equals("مرد")) {
            gender = "male";
            Objects.requireNonNull(genderTabLayout.getTabAt(0)).select();
        } else if (viewModel.getGender().equals("زن")) {
            gender = "female";
            Objects.requireNonNull(genderTabLayout.getTabAt(1)).select();
        }

        birthdayTextView = findViewById(R.id.activity_edit_account_birthday_textView);
        birthday = viewModel.getBirthday();
        birthdayTextView.setText(birthday);

        Year = Integer.parseInt(StringCustomizer.dateToString("yyyy", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));
        Month = Integer.parseInt(StringCustomizer.dateToString("MM", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));
        Day = Integer.parseInt(StringCustomizer.dateToString("dd", StringCustomizer.stringToDate("yyyy-MM-dd", birthday)));

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

            if (!name.equals("") && !avatar.equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", viewModel.getName());
                intent.putExtra("image", viewModel.getAvatar());

                startActivity(intent);
            }
        });

        avatarImageView.setOnClickListener(v -> {
            avatarImageView.setClickable(false);
            handler.postDelayed(() -> avatarImageView.setClickable(true), 300);

            if (nameEditText.hasFocus()) {
                clearInput(nameEditText);
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    selectInput(nameEditText);
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
                    clearInput(nameEditText);
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
                clearInput(nameEditText);
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
                errorInput(nameEditText);
            } else {
                clearInput(nameEditText);

                if (genderException) {
                    clearException("gender");
                }
                if (birthdayException) {
                    clearException("birthday");
                }

                doWork();
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

    private void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    private void errorInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);

        hideKeyboard();
    }

    private void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
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

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void doWork() {
        name = nameEditText.getText().toString().trim();

        try {
            progressDialog.show();
            viewModel.edit(name, gender, birthday);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("edit")) {
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
                    genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("gender");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("gender"));
                    }
                    genderException = true;
                }
                if (!ExceptionManager.errors.isNull("birthday")) {
                    birthdayTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    if (exceptionToast.equals("")) {
                        exceptionToast = ExceptionManager.errors.getString("birthday");
                    } else {
                        exceptionToast += (" و " + ExceptionManager.errors.getString("birthday"));
                    }
                    birthdayException = true;
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

                    selectedImage = BitmapFactory.decodeStream(imageStream);

                    avatar = BitmapController.encodeToBase64(selectedImage);
                    bitmapToFileConverter();
                    viewModel.avatar();
                    avatarCircleImageView.setImageBitmap(BitmapController.rotate(selectedImage, ""));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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

                selectedImage = BitmapFactory.decodeFile(imageFilePath, bmOptions);

                avatar = BitmapController.encodeToBase64(selectedImage);
                bitmapToFileConverter();
                try {
                    viewModel.avatar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                avatarCircleImageView.setImageBitmap(BitmapController.rotate(selectedImage, imageFilePath));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100)
                Toast.makeText(this, "عکسی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            else if (requestCode == 200)
                Toast.makeText(this, "عکسی گرفته نشده است.", Toast.LENGTH_SHORT).show();
        }
    }

    public void bitmapToFileConverter() {
        File file = new File(getApplicationContext().getCacheDir(), "avatar.png");
        try {
            file.createNewFile();

            //Convert bitmap to byte array
            Bitmap bitmap = selectedImage;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}