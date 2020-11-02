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
    private AuthViewModel viewModel;

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
    private Dialog dateDialog, progressDialog;
    private SingleNumberPicker yearNumberPicker, monthNumberPicker, dayNumberPicker;
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
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            avatarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_quartz_ripple_quartz);
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            dateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            dateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

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
                    intent.putExtra("path", imageFilePath);
                    FileManager.writeBitmapToCache(this, selectedBitmap, "image");
                }

                startActivity(intent);
            }
        });

        avatarImageView.setOnClickListener(v -> {
            avatarImageView.setClickable(false);
            handler.postDelayed(() -> avatarImageView.setClickable(true), 300);

            if (nameEditText.hasFocus()) {
                controlEditText.clear(this, nameEditText);
            }

            imageDialog.show(this.getSupportFragmentManager(), "imageBottomSheet");
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
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

                if (nameEditText.hasFocus()) {
                    controlEditText.clear(EditAccountActivity.this, nameEditText);
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
                controlEditText.clear(this, nameEditText);
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
                controlEditText.error(this, nameEditText);
            } else {
                controlEditText.clear(this, nameEditText);

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

        dateDialogNegative.setOnClickListener(v -> {
            dateDialogNegative.setClickable(false);
            handler.postDelayed(() -> dateDialogNegative.setClickable(true), 300);
            dateDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });

        dateDialog.setOnCancelListener(dialog -> {
            dateDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
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
                    FileManager.writeBitmapToCache(this, BitmapManager.modifyOrientation(selectedBitmap, imageFilePath), "image");
                    viewModel.avatar();
                    break;
                case "edit":
                    name = nameEditText.getText().toString().trim();
                    viewModel.edit(name, gender, DateManager.jalaliToGregorian(birthday));
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
        if (ExceptionGenerator.exception.equals("edit")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("name")) {
                nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
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
                ExceptionGenerator.getException(false, 0, null, "GalleryException", "auth");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            } else if (requestCode == 400) {
                ExceptionGenerator.getException(false, 0, null, "CameraException", "auth");
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