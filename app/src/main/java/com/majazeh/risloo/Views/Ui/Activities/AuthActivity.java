package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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

import org.json.JSONException;

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    public MutableLiveData<Boolean> callTimer;

    // Widgets
    private Toolbar titleToolbar;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        listener();

        titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
        loadFragment(new SerialFragment(this), 0, 0);
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        callTimer = new MutableLiveData<>();

        titleToolbar = findViewById(R.id.activity_auth_toolbar);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MoreActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });
    }

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_auth_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment() {
        switch (AuthController.theory) {
            case "auth":
                titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
                loadFragment(new SerialFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "password":
                titleToolbar.setTitle(getResources().getString(R.string.PasswordTitle));
                loadFragment(new PasswordFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "mobileCode":
                titleToolbar.setTitle(getResources().getString(R.string.PinTitle));
                loadFragment(new PinFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "register":
                titleToolbar.setTitle(getResources().getString(R.string.RegisterTitle));
                loadFragment(new RegisterFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "mobile":
            case "recover":
                titleToolbar.setTitle(getResources().getString(R.string.MobileTitle));
                loadFragment(new MobileFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
        }
    }

    public void observeWork() {
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthController.workState.getValue() == 1) {
                if (AuthController.preTheory.equals("mobileCode") && AuthController.theory.equals("mobileCode")) {
                    callTimer.setValue(true);
                } else {
                    callTimer.postValue(false);
                    callTimer.removeObservers((LifecycleOwner) this);
                    if (AuthController.key.equals("")) {
                        if (AuthController.callback.equals("")) {
                            startActivity(new Intent(this, SampleActivity.class));
                        } else {
                            AuthController.theory = "mobile";
                            showFragment();
                        }
                        AuthController.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        if (AuthController.theory.equals("auth")) {
                            try {
                                viewModel.authTheory("", "");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (AuthController.theory.equals("sample")) {
                            AuthController.sampleId = AuthController.key;
                            startActivity(new Intent(this, SampleActivity.class));
                            AuthController.workState.removeObservers((LifecycleOwner) this);
                        } else {
                            Log.e("test", AuthController.theory + AuthController.key + "lol");
                            showFragment();
                            AuthController.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                }
                progressDialog.dismiss();
            } else if (AuthController.workState.getValue() == 0) {
                Log.e("token", AuthController.token);
                Log.e("theory", AuthController.theory);
                Log.e("key", AuthController.key);
                AuthController.workState.removeObservers((LifecycleOwner) this);
                progressDialog.dismiss();
                Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
            } else {
                // DO Nothing
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
        loadFragment(new SerialFragment(this), 0, 0);
    }

    @Override
    public void onBackPressed() {
        if (!AuthController.theory.equals("auth")) {
            AuthController.theory = "auth";

            titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
            loadFragment(new SerialFragment(this), R.anim.slide_in_right_with_fade, R.anim.slide_out_left_with_fade);
        } else {
            finish();
        }
    }

}