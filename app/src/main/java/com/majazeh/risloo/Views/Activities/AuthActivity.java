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
import androidx.lifecycle.ViewModelProviders;

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

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();

        callTimer = new MutableLiveData<>();
        callTimer.setValue(-1);

        drawerLayout = findViewById(R.id.activity_auth);

        navigationView = findViewById(R.id.activity_auth_navigationView);
        if (token()) {
            navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(true);
            navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
        }
        navigationFooter = navigationView.findViewById(R.id.activity_auth_footer);

        titleToolbar = findViewById(R.id.activity_auth_toolbar);
        setSupportActionBar(titleToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            navigationFooter.setBackgroundResource(R.drawable.draw_rectangle_white_ripple);
        }
    }

    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationFooter.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            handler.postDelayed(() -> {
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }, 250);
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
                        navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(true);
                        navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(true);
                    } else {
                        toolUser.setVisible(false);
                        navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
                        navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);
                    }
                }

                progressDialog.dismiss();
            } else if (integer == 0) {
                progressDialog.dismiss();
                Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            } else if (integer == -2) {
                progressDialog.dismiss();
                Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            }
        });
    }

    public boolean token() {
        return !sharedPreferences.getString("token", "").equals("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auth, menu);

        toolUser = menu.findItem(R.id.tool_account);
        if (token())
            toolUser.setVisible(true);
        else
            toolUser.setVisible(false);
        toolUser.setOnMenuItemClickListener(item -> {
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
                    navigationView.getMenu().findItem(R.id.tool_treatment_center).setVisible(false);
                    navigationView.getMenu().findItem(R.id.tool_sample_list).setVisible(false);

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