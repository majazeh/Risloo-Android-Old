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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.BitmapManager;
import com.majazeh.risloo.Utils.Widgets.SingleNumberPicker;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;

    // Vars
    private String name = "", gender = "", birthday = "";
    public String imageFilePath = "";
    private int year, month, day;
    private boolean genderException = false, birthdayException = false;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private PathManager pathManager;
    private ImageDialog imageDialog;
    private Bitmap selectedBitmap;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private CircleImageView avatarCircleImageView;
    private ImageView avatarImageView;
    private EditText nameEditText;
    private TabLayout genderTabLayout;
    private TextView birthdayTextView;
    private Button editButton;
    private Dialog birthdayDialog, progressDialog;
    private SingleNumberPicker yearNumberPicker, monthNumberPicker, dayNumberPicker;
    private TextView birthdayDialogPositive, birthdayDialogNegative;

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

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        handler = new Handler();

        controlEditText = new ControlEditText();

        pathManager = new PathManager();

        imageDialog = new ImageDialog(this);
        imageDialog.setType("editAccount");

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.EditAccountTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        avatarCircleImageView = findViewById(R.id.activity_edit_account_avatar_circleImageView);

        avatarImageView = findViewById(R.id.activity_edit_account_avatar_imageView);

        nameEditText = findViewById(R.id.activity_edit_account_name_editText);

        genderTabLayout = findViewById(R.id.activity_edit_account_gender_tabLayout);

        birthdayTextView = findViewById(R.id.activity_edit_account_birthday_textView);

        editButton = findViewById(R.id.activity_edit_account_edit_button);

        birthdayDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(birthdayDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        birthdayDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        birthdayDialog.setContentView(R.layout.dialog_date);
        birthdayDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsBirthdayDialog = new WindowManager.LayoutParams();
        layoutParamsBirthdayDialog.copyFrom(birthdayDialog.getWindow().getAttributes());
        layoutParamsBirthdayDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsBirthdayDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        birthdayDialog.getWindow().setAttributes(layoutParamsBirthdayDialog);

        yearNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_year_NumberPicker);
        monthNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_month_NumberPicker);
        dayNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_day_NumberPicker);

        birthdayDialogPositive = birthdayDialog.findViewById(R.id.dialog_date_positive_textView);
        birthdayDialogNegative = birthdayDialog.findViewById(R.id.dialog_date_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            avatarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_quartz_ripple_quartz);
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            birthdayDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            birthdayDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

        avatarCircleImageView.setOnClickListener(v -> {
            avatarCircleImageView.setClickable(false);
            handler.postDelayed(() -> avatarCircleImageView.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (!authViewModel.getName().equals("") && !authViewModel.getAvatar().equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", authViewModel.getName());
                if (selectedBitmap == null) {
                    intent.putExtra("bitmap", false);
                    intent.putExtra("image", authViewModel.getAvatar());
                } else {
                    intent.putExtra("bitmap", true);
                    intent.putExtra("path", imageFilePath);
                    FileManager.writeBitmapToCache(this, selectedBitmap, "image");
                }

                startActivity(intent);
            }
        });

        avatarImageView.setOnClickListener(v -> {
            avatarImageView.setClickable(false);
            handler.postDelayed(() -> avatarImageView.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(nameEditText);
                    controlEditText.select(nameEditText);
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

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(EditAccountActivity.this, controlEditText.input());
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
            handler.postDelayed(() -> birthdayTextView.setClickable(true), 250);

            if (birthdayException) {
                clearException("birthday");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            birthdayDialog.show();
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
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (nameEditText.length() == 0) {
                controlEditText.error(this, nameEditText);
            }

            if (nameEditText.length() != 0) {
                controlEditText.clear(this, nameEditText);
                clearException("gender");
                clearException("birthday");

                if (selectedBitmap == null) {
                    doWork("edit");
                } else {
                    doWork("avatar");
                }
            }
        });

        birthdayDialogPositive.setOnClickListener(v -> {
            birthdayDialogPositive.setClickable(false);
            handler.postDelayed(() -> birthdayDialogPositive.setClickable(true), 250);
            birthdayDialog.dismiss();

            year = yearNumberPicker.getValue();
            month = monthNumberPicker.getValue();
            day = dayNumberPicker.getValue();

            if (month < 10) {
                if (day < 10)
                    birthday = year + "-" + "0" + month + "-" + "0" + day;
                else
                    birthday = year + "-" + "0" + month + "-" + day;
            } else {
                if (day < 10)
                    birthday = year + "-" + month + "-" + "0" + day;
                else
                    birthday = year + "-" + month + "-" + day;
            }

            birthdayTextView.setText(birthday);
        });

        birthdayDialogNegative.setOnClickListener(v -> {
            birthdayDialogNegative.setClickable(false);
            handler.postDelayed(() -> birthdayDialogNegative.setClickable(true), 250);
            birthdayDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });

        birthdayDialog.setOnCancelListener(dialog -> {
            birthdayDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });
    }

    private void setData() {
        if (authViewModel.getAvatar().equals("")) {
            avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_solid));
        } else {
            Picasso.get().load(authViewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
        }

        if (authViewModel.getName().equals("")) {
            nameEditText.setText(getResources().getString(R.string.AuthNameDefault));
        } else {
            name = authViewModel.getName();
            nameEditText.setText(name);
        }

        if (authViewModel.getGender().equals("مرد")) {
            gender = "male";
            Objects.requireNonNull(genderTabLayout.getTabAt(0)).select();
        } else if (authViewModel.getGender().equals("زن")) {
            gender = "female";
            Objects.requireNonNull(genderTabLayout.getTabAt(1)).select();
        } else {
            gender = "male";
            Objects.requireNonNull(genderTabLayout.getTabAt(0)).select();
        }

        if (authViewModel.getBirthday().equals("")) {
            birthday = getResources().getString(R.string.AuthBirthdayDefault);
            birthdayTextView.setText(birthday);
        } else {
            birthday = authViewModel.getBirthday();
            birthdayTextView.setText(birthday);
        }

        year = Integer.parseInt(DateManager.dateToString("yyyy", DateManager.stringToDate("yyyy-MM-dd", birthday)));
        month = Integer.parseInt(DateManager.dateToString("MM", DateManager.stringToDate("yyyy-MM-dd", birthday)));
        day = Integer.parseInt(DateManager.dateToString("dd", DateManager.stringToDate("yyyy-MM-dd", birthday)));
    }

    private void setCustomPicker() {
        yearNumberPicker.setMinValue(1300);
        yearNumberPicker.setMaxValue(2100);
        yearNumberPicker.setValue(year);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(month);
        monthNumberPicker.setDisplayedValues(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"});

        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        dayNumberPicker.setValue(day);
    }

    private void errorException(String type) {
        switch (type) {
            case "name":
                nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "gender":
                genderException = true;
                genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "birthday":
                birthdayException = true;
                birthdayTextView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "gender":
                genderException = false;
                genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "birthday":
                birthdayException = false;
                birthdayTextView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void doWork(String method) {
        try {
            progressDialog.show();

            switch (method) {
                case "avatar":
                    FileManager.writeBitmapToCache(this, BitmapManager.modifyOrientation(selectedBitmap, imageFilePath), "image");
                    authViewModel.avatar();
                    break;
                case "edit":
                    name = nameEditText.getText().toString().trim();
                    authViewModel.edit(name, gender, DateManager.jalaliToGregorian(birthday));
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
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            } else if (AuthRepository.work.equals("edit")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
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
        if (ExceptionGenerator.current_exception.equals("edit")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("name")) {
                errorException("name");
                exceptionToast = ExceptionGenerator.getErrorBody("name");
            }
            if (!ExceptionGenerator.errors.isNull("gender")) {
                errorException("gender");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("gender");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("gender"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("birthday")) {
                errorException("birthday");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("birthday");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("birthday"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 300) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.gallery(this);
            }
        } else if (requestCode == 400) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                imageFilePath = IntentManager.camera(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 300) {
                try {
                    Uri imageUri = Objects.requireNonNull(data).getData();
                    InputStream imageStream = getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                    imageFilePath = pathManager.getLocalPath(this, imageUri);

                    selectedBitmap = BitmapManager.scaleToCenter(imageBitmap);

                    avatarCircleImageView.setImageBitmap(BitmapManager.modifyOrientation(selectedBitmap, imageFilePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 400) {
                File imageFile = new File(imageFilePath);
                IntentManager.mediaScan(this, imageFile);

                int scaleFactor = Math.max(1, 2);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inSampleSize = scaleFactor;

                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath, options);

                selectedBitmap = BitmapManager.scaleToCenter(imageBitmap);

                avatarCircleImageView.setImageBitmap(BitmapManager.modifyOrientation(selectedBitmap, imageFilePath));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 300) {
                ExceptionGenerator.getException(false, 0, null, "GalleryException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 400) {
                ExceptionGenerator.getException(false, 0, null, "CameraException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}