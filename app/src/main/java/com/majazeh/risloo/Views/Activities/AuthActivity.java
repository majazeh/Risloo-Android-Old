package com.majazeh.risloo.Views.Activities;

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
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.majazeh.risloo.Models.Controller.AuthController;
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
    private Toolbar titleToolbar;
    private NavigationView navigationView;
    private FrameLayout navigationFooter;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

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

        titleToolbar = findViewById(R.id.activity_auth_toolbar);
        setSupportActionBar(titleToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView = findViewById(R.id.activity_auth_navigationView);

        navigationFooter = navigationView.findViewById(R.id.activity_auth_footer);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationFooter.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            handler.postDelayed(() -> {
                startActivity(new Intent(this, MoreActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            }, 250);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.tool_sample_start) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.tool_sample_build) {
                handler.postDelayed(() -> {

                }, 50);
            } else if (id == R.id.tool_reserve_make) {
                handler.postDelayed(() -> {

                }, 50);
            } else if (id == R.id.tool_reserve_request) {
                handler.postDelayed(() -> {

                }, 50);
            } else if (id == R.id.tool_treatment_adviser) {
                handler.postDelayed(() -> {

                }, 50);

            } else if (id == R.id.tool_treatment_station) {
                handler.postDelayed(() -> {

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
        AuthController.theory = "auth";
        if (token()) {
            titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
        } else {
            titleToolbar.setTitle(getResources().getString(R.string.SerialTitleToken));
        }
        loadFragment(new SerialFragment(this), enterAnim, exitAnim);
    }

    public void showFragment() {
        switch (AuthController.theory) {
            case "auth":
                if (token()) {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitle));
                } else {
                    titleToolbar.setTitle(getResources().getString(R.string.SerialTitleToken));
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
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (integer == 1) {
                if (AuthController.preTheory.equals("mobileCode") && AuthController.theory.equals("mobileCode")) {
                    callTimer.postValue(1);
                } else {
                    callTimer.postValue(0);
                    if (AuthController.key.equals("")) {
                        if (AuthController.callback.equals("")) {
                            AuthController.theory = "auth";
                        } else {
                            AuthController.theory = "mobile";
                        }
                        showFragment();
                    } else {
                        if (AuthController.theory.equals("auth")) {
                            try {
                                viewModel.authTheory("", "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (AuthController.theory.equals("sample")) {
                            AuthController.sampleId = AuthController.key;

                            editor.putString("sampleId", AuthController.key);
                            editor.apply();

                            AuthController.theory = "sample";
                            startActivity(new Intent(this, PrerequisiteActivity.class));
                        } else {
                            showFragment();
                        }
                    }
                }
                if (toolUser != null){
                    if (token()){
                        toolUser.setVisible(false);
                    }else{
                        toolUser.setVisible(true);
                    }
                }
                progressDialog.dismiss();
                AuthController.workState.removeObservers((LifecycleOwner) this);
            } else if (integer == 0) {
                progressDialog.dismiss();
                Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                AuthController.workState.removeObservers((LifecycleOwner) this);
            } else if (integer == -2){
                progressDialog.dismiss();
                Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                AuthController.workState.removeObservers((LifecycleOwner) this);
            }
        });
    }

    public boolean token() {
        return sharedPreferences.getString("token", "").equals("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auth, menu);

        toolUser = menu.findItem(R.id.tool_user);

        if (token()){
            toolUser.setVisible(false);
        } else {
            toolUser.setVisible(true);
        }

        toolUser.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(this, AccountActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (toolUser != null){
            if (token()){
                toolUser.setVisible(false);
            }else{
                toolUser.setVisible(true);
            }
        }

        if (AuthController.theory == "sample") {
            launchAuth(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!AuthController.theory.equals("auth")) {
                launchAuth(R.anim.slide_in_right_with_fade, R.anim.slide_out_left_with_fade);
            } else {
                finish();
            }
        }
    }

}