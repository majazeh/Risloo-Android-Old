package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Ui.Fragments.MobileFragment;
import com.majazeh.risloo.Views.Ui.Fragments.PasswordFragment;
import com.majazeh.risloo.Views.Ui.Fragments.PinFragment;
import com.majazeh.risloo.Views.Ui.Fragments.RegisterFragment;
import com.majazeh.risloo.Views.Ui.Fragments.SerialFragment;

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar titleToolbar;
    private TextView infoDialogTitle, infoDialogDescription, infoDialogConfirm;
    public Dialog infoDialog, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        detector();

        listener();

        showFragment();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        handler = new Handler();

        titleToolbar = findViewById(R.id.activity_auth_toolbar);

        infoDialog = new Dialog(this, R.style.DialogTheme);
        infoDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.setContentView(R.layout.dialog_note);
        infoDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParams);

        infoDialogTitle = infoDialog.findViewById(R.id.dialog_note_title_textView);
        infoDialogTitle.setText(getResources().getString(R.string.AuthInfoDialogTitle));
        infoDialogDescription = infoDialog.findViewById(R.id.dialog_note_description_textView);
        infoDialogDescription.setText(getResources().getString(R.string.AuthInfoDialogDescription));
        infoDialogConfirm = infoDialog.findViewById(R.id.dialog_note_confirm_textView);
        infoDialogConfirm.setText(getResources().getString(R.string.AuthInfoDialogConfirm));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            infoDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_quartz_ripple);
        }
    }

    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MoreActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        infoDialogConfirm.setOnClickListener(v -> {
            infoDialogConfirm.setClickable(false);
            handler.postDelayed(() -> infoDialogConfirm.setClickable(true), 1000);
            infoDialog.dismiss();
        });

        infoDialog.setOnCancelListener(dialog -> infoDialog.dismiss());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_auth_frameLayout, fragment);
        transaction.commit();
    }

    private Fragment findFragment(int resID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(resID);
    }

    public void showFragment() {
        switch (AuthController.theory) {
            case "auth":
                titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
                loadFragment(new SerialFragment(this));
                break;
            case "password":
                titleToolbar.setTitle(getResources().getString(R.string.PasswordTitle));
                loadFragment(new PasswordFragment(this));
                break;
            case "mobileCode":
                titleToolbar.setTitle(getResources().getString(R.string.PinTitle));
                loadFragment(new PinFragment(this));
                break;
            case "register":
                titleToolbar.setTitle(getResources().getString(R.string.RegisterTitle));
                loadFragment(new RegisterFragment(this));
                break;
            case "mobile":
            case "recover":
                titleToolbar.setTitle(getResources().getString(R.string.MobileTitle));
                loadFragment(new MobileFragment(this));
                break;
        }
    }

    public void observeWork() {
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthController.workState.getValue() == 1) {
                if (AuthController.key.equals("")) {
                    if (AuthController.callback.equals("")) {
                        startActivity(new Intent(this, SampleActivity.class));
                    } else {
                        AuthController.theory = "mobile";
                        showFragment();
                    }
                } else {
//                    if (AuthController.theory.equals("auth")) {
//                        try {
//                            viewModel.authTheory("", "");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else
                        showFragment();
                }
                AuthController.workState.removeObservers((LifecycleOwner) this);
                progressDialog.dismiss();
            } else if (AuthController.workState.getValue() == 0) {
                AuthController.workState.removeObservers((LifecycleOwner) this);
                progressDialog.dismiss();
                Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
            } else {
                // DO Nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!AuthController.theory.equals("auth")) {
            AuthController.theory = "auth";
            showFragment();
        } else {
            finish();
        }
    }

}