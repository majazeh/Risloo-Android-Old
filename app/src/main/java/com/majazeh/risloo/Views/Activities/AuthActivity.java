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
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONException;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    public AuthViewModel viewModel;

    // Vars
    public MutableLiveData<Integer> callTimer;

    // Objects
    private Handler handler;
    private MenuItem toolUser;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout navigationFooter;
    private Toolbar titleToolbar;
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
        setSupportActionBar(titleToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

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

                if (toolUser != null) {
                    if (token()) {
                        toolUser.setVisible(true);
                        navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(true);
                        navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(true);
                    } else {
                        toolUser.setVisible(false);
                        navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
                        navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
                    }
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
                                errorInput(serialFragment.serialEditText);
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
                                errorInput(mobileFragment.mobileEditText);
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
                                errorInput(passwordFragment.passwordEditText);
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
                                errorInput(pinFragment.pinEditText);
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
                        if (!ExceptionManager.errors.isNull("name") && !ExceptionManager.errors.isNull("mobile") && !ExceptionManager.errors.isNull("password")) {
                            errorInput(registerFragment.nameEditText);
                            errorInput(registerFragment.mobileEditText);
                            errorInput(registerFragment.passwordEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("name"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("password"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("mobile") && !ExceptionManager.errors.isNull("password")) {
                            errorInput(registerFragment.mobileEditText);
                            errorInput(registerFragment.passwordEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("password"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("name") && !ExceptionManager.errors.isNull("password")) {
                            errorInput(registerFragment.nameEditText);
                            errorInput(registerFragment.passwordEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("name"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("password"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("name") && !ExceptionManager.errors.isNull("mobile")) {
                            errorInput(registerFragment.nameEditText);
                            errorInput(registerFragment.mobileEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("name"), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("password")) {
                            errorInput(registerFragment.passwordEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("password"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("mobile")) {
                            errorInput(registerFragment.mobileEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                        } else if (!ExceptionManager.errors.isNull("name")) {
                            errorInput(registerFragment.nameEditText);
                            Toast.makeText(this, "" + ExceptionManager.errors.getString("name"), Toast.LENGTH_SHORT).show();
                        }
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
                            errorInput(pinFragment.pinEditText);
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
                            errorInput(mobileFragment.mobileEditText);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auth, menu);

        toolUser = menu.findItem(R.id.tool_account);
        if (token()) {
            toolUser.setVisible(true);
        } else {
            toolUser.setVisible(false);
        }
        toolUser.setOnMenuItemClickListener(item -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }

            startActivityForResult(new Intent(this, AccountActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                if (toolUser != null) {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));

                    toolUser.setVisible(false);
                    navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
                    navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);

                    SerialFragment fragment = ((SerialFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (fragment != null) {
                        fragment.setText();
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