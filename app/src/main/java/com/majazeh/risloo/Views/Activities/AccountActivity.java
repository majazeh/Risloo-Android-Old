package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.ParamsManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;

    // Objects
    private Handler handler;

    // Widgets
    private ConstraintLayout toolbarConstraintLayout;
    private ImageView toolbarImageView, toolbarLogOutImageView;
    private TextView toolbarTextView;
    private CircleImageView avatarCircleImageView;
    private TextView avatarTextView;
    private TextView nameTextView, usernameTextView, mobileTextView, emailTextView, birthdayTextView;
    private ImageView enterImageView;
    private TextView editTextView;
    private Dialog logOutDialog, progressDialog;
    private TextView logOutDialogTitle, logOutDialogDescription, logOutDialogPositive, logOutDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_account);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Gray50), getResources().getColor(R.color.Gray50));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        handler = new Handler();

        toolbarConstraintLayout = findViewById(R.id.activity_account_toolbar);
        toolbarConstraintLayout.setBackgroundColor(getResources().getColor(R.color.Gray50));

        toolbarImageView = findViewById(R.id.component_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.icon_angle_right_light);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray500));
        toolbarLogOutImageView = findViewById(R.id.component_toolbar_secondary_imageView);
        toolbarLogOutImageView.setImageResource(R.drawable.icon_logout_alt_light);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray500));
        toolbarLogOutImageView.setVisibility(View.VISIBLE);
        toolbarLogOutImageView.setRotation(toolbarLogOutImageView.getRotation() + 180);

        toolbarTextView = findViewById(R.id.component_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.AccountTitle));

        avatarCircleImageView = findViewById(R.id.component_avatar_big_circleImageView);

        avatarTextView = findViewById(R.id.component_avatar_big_textView);

        nameTextView = findViewById(R.id.activity_account_name_textView);
        usernameTextView = findViewById(R.id.activity_account_username_textView);
        mobileTextView = findViewById(R.id.activity_account_mobile_textView);
        emailTextView = findViewById(R.id.activity_account_email_textView);
        birthdayTextView = findViewById(R.id.activity_account_birthday_textView);

        enterImageView = findViewById(R.id.activity_account_enter_imageView);
        enterImageView.setImageResource(R.drawable.icon_user_cog_light);
        ImageViewCompat.setImageTintList(enterImageView, AppCompatResources.getColorStateList(this, R.color.Gray500));

        editTextView = findViewById(R.id.activity_account_edit_textView);
        editTextView.setText(getResources().getString(R.string.AccountEdit));
        editTextView.setTextColor(getResources().getColor(R.color.Gray500));

        logOutDialog = new Dialog(this, R.style.DialogTheme);
        logOutDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        logOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logOutDialog.setContentView(R.layout.dialog_action);
        logOutDialog.setCancelable(true);
        logOutDialog.getWindow().setAttributes(ParamsManager.set(logOutDialog));

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        logOutDialogTitle = logOutDialog.findViewById(R.id.dialog_action_title_textView);
        logOutDialogTitle.setText(getResources().getString(R.string.AccountLogOutDialogTitle));
        logOutDialogDescription = logOutDialog.findViewById(R.id.dialog_action_description_textView);
        logOutDialogDescription.setText(getResources().getString(R.string.AccountLogOutDialogDescription));
        logOutDialogPositive = logOutDialog.findViewById(R.id.dialog_action_positive_textView);
        logOutDialogPositive.setText(getResources().getString(R.string.AccountLogOutDialogPositive));
        logOutDialogPositive.setTextColor(getResources().getColor(R.color.Red500));
        logOutDialogNegative = logOutDialog.findViewById(R.id.dialog_action_negative_textView);
        logOutDialogNegative.setText(getResources().getString(R.string.AccountLogOutDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_gray50_ripple_gray200);
            toolbarLogOutImageView.setBackgroundResource(R.drawable.draw_oval_solid_gray50_ripple_red200);

            editTextView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_gray500_ripple_gray500);
            enterImageView.setBackgroundResource(R.drawable.draw_oval_solid_white_border_gray500_ripple_gray500);

            logOutDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            logOutDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        } else {
            editTextView.setBackgroundResource(R.drawable.draw_16sdp_border_gray500);
            enterImageView.setBackgroundResource(R.drawable.draw_oval_border_gray500);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        toolbarLogOutImageView.setOnClickListener(v -> {
            toolbarLogOutImageView.setClickable(false);
            handler.postDelayed(() -> toolbarLogOutImageView.setClickable(true), 250);

            logOutDialog.show();
        });

        avatarCircleImageView.setOnClickListener(v -> {
            avatarCircleImageView.setClickable(false);
            handler.postDelayed(() -> avatarCircleImageView.setClickable(true), 250);

            navigator("Image");
        });

        editTextView.setOnClickListener(v -> {
            editTextView.setClickable(false);
            handler.postDelayed(() -> editTextView.setClickable(true), 250);

            navigator("EditAccount");
        });

        enterImageView.setOnClickListener(v -> {
            enterImageView.setClickable(false);
            handler.postDelayed(() -> enterImageView.setClickable(true), 250);

          // TODO : Enter User From Another Account
        });

        logOutDialogPositive.setOnClickListener(v -> {
            logOutDialogPositive.setClickable(false);
            handler.postDelayed(() -> logOutDialogPositive.setClickable(true), 250);
            logOutDialog.dismiss();

            doWork();
        });

        logOutDialogNegative.setOnClickListener(v -> {
            logOutDialogNegative.setClickable(false);
            handler.postDelayed(() -> logOutDialogNegative.setClickable(true), 250);
            logOutDialog.dismiss();
        });

        logOutDialog.setOnCancelListener(dialog -> logOutDialog.dismiss());
    }

    private void setData() {
        if (authViewModel.getAvatar().equals("")) {
            Picasso.get().load(R.color.Gray100).placeholder(R.color.Gray100).into(avatarCircleImageView);

            avatarTextView.setVisibility(View.VISIBLE);
            avatarTextView.setText(StringManager.firstChars(authViewModel.getName()));
        } else {
            Picasso.get().load(authViewModel.getAvatar()).placeholder(R.color.Gray100).into(avatarCircleImageView);

            avatarTextView.setVisibility(View.GONE);
        }

        if (authViewModel.getName().equals("")) {
            nameTextView.setText(getResources().getString(R.string.AuthNameDefault));
        } else {
            nameTextView.setText(authViewModel.getName());
        }

        if (authViewModel.getUsername().equals("")) {
            usernameTextView.setText(getResources().getString(R.string.AuthUsernameDefault));
        } else {
            usernameTextView.setText(authViewModel.getUsername());
        }

        if (authViewModel.getMobile().equals("")) {
            mobileTextView.setText(getResources().getString(R.string.AuthMobileDefault));
        } else {
            mobileTextView.setText(authViewModel.getMobile());
        }

        if (authViewModel.getEmail().equals("")) {
            emailTextView.setText(getResources().getString(R.string.AuthEmailDefault));
        } else {
            emailTextView.setText(authViewModel.getEmail());
        }

        if (authViewModel.getBirthday().equals("")) {
            birthdayTextView.setText(getResources().getString(R.string.AuthBirthdayDefault));
        } else {
            birthdayTextView.setText(authViewModel.getBirthday());
        }
    }

    private void doWork() {
        try {
            progressDialog.show();

            authViewModel.logOut();
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("logOut")) {
                if (integer == 1) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("RESULT_STRING", AuthRepository.work);

                    setResult(RESULT_OK, resultIntent);
                    finish();

                    progressDialog.dismiss();

                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();

                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();

                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void navigator(String activity) {
        if (activity.equals("EditAccount")) {
            Intent editAccountIntent = new Intent(this, EditAccountActivity.class);
            startActivityForResult(editAccountIntent, 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        } else {
            if (!nameTextView.getText().toString().equals("") && !authViewModel.getAvatar().equals("")) {
                Intent imageIntent = new Intent(this, ImageActivity.class);

                imageIntent.putExtra("title", nameTextView.getText().toString().equals(""));
                imageIntent.putExtra("bitmap", false);
                imageIntent.putExtra("image", authViewModel.getAvatar());

                startActivity(imageIntent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("RESULT_STRING", AuthRepository.work);

                setResult(RESULT_OK, resultIntent);
                setData();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}