package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Fragments.MobileFragment;
import com.majazeh.risloo.Views.Fragments.PasswordFragment;
import com.majazeh.risloo.Views.Fragments.PinFragment;
import com.majazeh.risloo.Views.Fragments.RegisterFragment;
import com.majazeh.risloo.Views.Fragments.SerialFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    public AuthViewModel viewModel;

    // Vars
    public MutableLiveData<Integer> callTimer;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout navigationFooter;
    private Toolbar titleToolbar;
    private CircleImageView avatarImageView;
    public EditText inputEditText;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        detector();

        listener();

        launchAuth(0, 0);
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();

        callTimer = new MutableLiveData<>();
        callTimer.setValue(-1);

        drawerLayout = findViewById(R.id.activity_auth);

        navigationView = findViewById(R.id.activity_auth_navigationView);
        if (token()) {
            navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(true);
            navigationView.getMenu().findItem(R.id.tool_reserve_request).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_reserve_construct).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_treatment_psychologist).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_reserve_request).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_reserve_construct).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_treatment_psychologist).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
        }
        navigationFooter = navigationView.findViewById(R.id.activity_auth_footer);

        titleToolbar = findViewById(R.id.activity_auth_toolbar);

        avatarImageView = findViewById(R.id.activity_auth_avatar_circleImageView);
        if (token()) {
            avatarImageView.setVisibility(View.VISIBLE);
            if (viewModel.getAvatar().equals("")) {
                avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_light));
            } else {
                Picasso.get().load(viewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarImageView);
            }
        } else {
            avatarImageView.setVisibility(View.GONE);
        }

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            navigationFooter.setBackgroundResource(R.drawable.draw_rectangle_solid_white_ripple_quartz);
        }
    }

    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }

            drawerLayout.openDrawer(GravityCompat.START);
        });

        avatarImageView.setOnClickListener(v -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }

            startActivityForResult(new Intent(this, AccountActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        navigationFooter.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            handler.postDelayed(() -> {
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }, 300);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.tool_sample_start) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.tool_sample_list) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, SamplesActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);
            } else if (id == R.id.tool_reserve_construct) {
                handler.postDelayed(() -> {

                }, 50);
            } else if (id == R.id.tool_reserve_request) {
                handler.postDelayed(() -> {

                }, 50);
            } else if (id == R.id.tool_treatment_psychologist) {
                handler.postDelayed(() -> {

                }, 50);

            } else if (id == R.id.tool_treatment_center) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, CenterActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    public void focusInput(EditText input) {
        this.inputEditText = input;
    }

    public void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    public void errorInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);

        hideKeyboard();
    }

    public void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
    }

    public void clearException() {
        RegisterFragment registerFragment = ((RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
        if (registerFragment != null) {
            registerFragment.genderException = false;
            registerFragment.genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_auth_frameLayout, fragment);
        transaction.commit();
    }

    private void launchAuth(int enterAnim, int exitAnim) {
        AuthRepository.theory = "auth";
        if (token()) {
            titleToolbar.setTitle(getResources().getString(R.string.SerialTitleToken));
        } else {
            titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
        }
        loadFragment(new SerialFragment(this), enterAnim, exitAnim);
    }

    public void showFragment() {
        switch (AuthRepository.theory) {
            case "auth":
                if (token()) {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitleToken));
                } else {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
                }
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
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (integer == 1) {
                if (AuthRepository.preTheory.equals("mobileCode") && AuthRepository.theory.equals("mobileCode")) {
                    callTimer.postValue(1);
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else {
                    callTimer.postValue(0);
                    if (AuthRepository.key.equals("")) {
                        if (AuthRepository.callback.equals("")) {
                            AuthRepository.theory = "auth";
                        } else {
                            AuthRepository.theory = "mobile";
                        }
                        showFragment();
                        AuthRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        if (AuthRepository.theory.equals("auth")) {
                            try {
                                viewModel.authTheory("", "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (AuthRepository.theory.equals("sample")) {
                            AuthRepository.theory = "sample";

                            editor.putString("sampleId", AuthRepository.key);
                            editor.apply();

                            startActivity(new Intent(this, SampleActivity.class));
                            AuthRepository.workState.removeObservers((LifecycleOwner) this);
                        } else {
                            showFragment();
                            AuthRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                }

                if (token()) {
                    avatarImageView.setVisibility(View.VISIBLE);
                    if (viewModel.getAvatar().equals("")) {
                        avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_light));
                    } else {
                        Picasso.get().load(viewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarImageView);
                    }

                    navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(true);
                    navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(true);
                } else {
                    avatarImageView.setVisibility(View.GONE);

                    navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
                    navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
                }

                progressDialog.dismiss();
            } else if (integer == 0) {
                progressDialog.dismiss();
                observeException();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            } else if (integer == -2) {
                progressDialog.dismiss();
                Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            }
        });
    }

    private void observeException() {
        switch (ExceptionManager.current_exception) {
            case "auth":
                if (AuthRepository.theory.equals("auth")) {
                    SerialFragment serialFragment = ((SerialFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (serialFragment != null) {
                        try {
                            if (!ExceptionManager.errors.isNull("authorized_key")) {
                                serialFragment.serialEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                                Toast.makeText(this, "" + ExceptionManager.errors.getString("authorized_key"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (AuthRepository.theory.equals("mobile")) {
                    MobileFragment mobileFragment = ((MobileFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (mobileFragment != null) {
                        try {
                            if (!ExceptionManager.errors.isNull("authorized_key")) {
                                mobileFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                                Toast.makeText(this, "" + ExceptionManager.errors.getString("authorized_key"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "authTheory":
                if (AuthRepository.theory.equals("password")) {
                    PasswordFragment passwordFragment = ((PasswordFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (passwordFragment != null) {
                        try {
                            if (!ExceptionManager.errors.isNull("password")) {
                                passwordFragment.passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                                Toast.makeText(this, "" + ExceptionManager.errors.getString("password"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (AuthRepository.theory.equals("pin")) {
                    PinFragment pinFragment = ((PinFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (pinFragment != null) {
                        try {
                            if (!ExceptionManager.errors.isNull("code")) {
                                pinFragment.pinEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                                Toast.makeText(this, "" + ExceptionManager.errors.getString("code"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case "register":
                RegisterFragment registerFragment = ((RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (registerFragment != null) {
                    try {
                        String exceptionToast = "";

                        if (!ExceptionManager.errors.isNull("name")) {
                            registerFragment.nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            exceptionToast = ExceptionManager.errors.getString("name");
                        }
                        if (!ExceptionManager.errors.isNull("mobile")) {
                            registerFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            if (exceptionToast.equals("")) {
                                exceptionToast = ExceptionManager.errors.getString("mobile");
                            } else {
                                exceptionToast += (" و " + ExceptionManager.errors.getString("mobile"));
                            }
                        }
                        if (!ExceptionManager.errors.isNull("gender")) {
                            registerFragment.genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            if (exceptionToast.equals("")) {
                                exceptionToast = ExceptionManager.errors.getString("gender");
                            } else {
                                exceptionToast += (" و " + ExceptionManager.errors.getString("gender"));
                            }
                            registerFragment.genderException = true;
                        }
                        if (!ExceptionManager.errors.isNull("password")) {
                            registerFragment.passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            if (exceptionToast.equals("")) {
                                exceptionToast = ExceptionManager.errors.getString("password");
                            } else {
                                exceptionToast += (" و " + ExceptionManager.errors.getString("password"));
                            }
                        }
                        Toast.makeText(this, "" + exceptionToast, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "verification":
                PinFragment pinFragment = ((PinFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (pinFragment != null) {
                    try {
                        if (!ExceptionManager.errors.isNull("mobile")) {
                            pinFragment.pinEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "recovery":
                MobileFragment mobileFragment = ((MobileFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (mobileFragment != null) {
                    try {
                        if (!ExceptionManager.errors.isNull("username")) {
                            mobileFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("username"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public boolean token() {
        return !sharedPreferences.getString("token", "").equals("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                String resultString = Objects.requireNonNull(data).getStringExtra("RESULT_STRING");

                if (resultString.equals("logOut")) {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));

                    avatarImageView.setVisibility(View.GONE);

                    navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
                    navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);

                    SerialFragment fragment = ((SerialFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (fragment != null) {
                        fragment.setText();
                    }
                } else if (resultString.equals("edit")) {
                    if (viewModel.getAvatar().equals("")) {
                        avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_light));
                    } else {
                        Picasso.get().load(viewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarImageView);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AuthRepository.theory.equals("sample")) {
            launchAuth(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!AuthRepository.theory.equals("auth")) {
                launchAuth(R.anim.slide_in_right_with_fade, R.anim.slide_out_left_with_fade);
            } else {
                finish();
            }
        }
    }

}