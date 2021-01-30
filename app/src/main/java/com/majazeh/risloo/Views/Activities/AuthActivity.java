package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ImageViewCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
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
    public AuthViewModel authViewModel;

    // Vars
    public MutableLiveData<Integer> callTimer;

    // Objects
    private Handler handler;
    public ControlEditText controlEditText;

    // Widgets
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout navigationFooter;
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private CircleImageView avatarCircleImageView;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        detector();

        listener();

        setData();

        launchAuth(0, 0);
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightNavShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, Color.TRANSPARENT, getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        handler = new Handler();

        controlEditText = new ControlEditText();

        callTimer = new MutableLiveData<>();
        callTimer.setValue(-1);

        drawerLayout = findViewById(R.id.activity_auth);

        navigationView = findViewById(R.id.activity_auth_navigationView);

        navigationFooter = navigationView.findViewById(R.id.activity_auth_footer);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_bars_light);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);

        avatarCircleImageView = findViewById(R.id.activity_auth_avatar_circleImageView);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_white_ripple_quartz);

            navigationFooter.setBackgroundResource(R.drawable.draw_rectangle_solid_white_ripple_quartz);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            drawerLayout.openDrawer(GravityCompat.START);
        });

        avatarCircleImageView.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            startActivityForResult(new Intent(this, AccountActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        navigationFooter.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);

            handler.postDelayed(() -> {
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }, 250);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.tool_main) {
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (id == R.id.tool_samples) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, SamplesActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_scales) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, ScalesActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_documents) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, DocumentsActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_centers) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, CentersActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_rooms) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, RoomsActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_cases) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, CasesActivity.class).putExtra("", ""));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);

            } else if (id == R.id.tool_sessions) {
                handler.postDelayed(() -> {
                    startActivity(new Intent(this, SessionsActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }, 50);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    private void setData() {
        if (!authViewModel.getToken().equals("")) {
            if (authViewModel.hasAccess()) {
                navigationView.getMenu().findItem(R.id.tool_samples).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_scales).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_documents).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_centers).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_sessions).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_rooms).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_cases).setVisible(true);
            } else {
                navigationView.getMenu().findItem(R.id.tool_samples).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_scales).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_documents).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_centers).setVisible(true);
                navigationView.getMenu().findItem(R.id.tool_sessions).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_rooms).setVisible(false);
                navigationView.getMenu().findItem(R.id.tool_cases).setVisible(false);
            }
            avatarCircleImageView.setVisibility(View.VISIBLE);
            if (authViewModel.getAvatar().equals("")) {
                avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_light));
            } else {
                Picasso.get().load(authViewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
            }
        } else {
            navigationView.getMenu().findItem(R.id.tool_samples).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_scales).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_documents).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_centers).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_sessions).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_rooms).setVisible(false);
            navigationView.getMenu().findItem(R.id.tool_cases).setVisible(false);

            avatarCircleImageView.setVisibility(View.GONE);
        }
    }

    private void launchAuth(int enterAnim, int exitAnim) {
        AuthRepository.theory = "auth";
        if (!authViewModel.getToken().equals("")) {
            toolbarTextView.setText(getResources().getString(R.string.SerialTitleToken));
        } else {
            toolbarTextView.setText(getResources().getString(R.string.SerialTitle));
        }
        loadFragment(new SerialFragment(this), enterAnim, exitAnim);
    }

    private void errorException(String exception, String type) {
        switch (exception) {
            case "auth":
                if (type.equals("auth")) {
                    SerialFragment serialFragment = ((SerialFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (serialFragment != null) {
                        serialFragment.serialEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    }
                } else if (type.equals("mobile")) {
                    MobileFragment mobileFragment = ((MobileFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (mobileFragment != null) {
                        mobileFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    }
                }
                break;

            case "authTheory":
                if (type.equals("password")) {
                    PasswordFragment passwordFragment = ((PasswordFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (passwordFragment != null) {
                        passwordFragment.passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    }
                } else if (type.equals("pin")) {
                    PinFragment pinFragment = ((PinFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (pinFragment != null) {
                        pinFragment.pinEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    }
                }
                break;

            case "register":
                RegisterFragment registerFragment = ((RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (registerFragment != null) {
                    switch (type) {
                        case "name":
                            registerFragment.nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            break;
                        case "mobile":
                            registerFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            break;
                        case "gender":
                            registerFragment.genderException = true;
                            registerFragment.genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            break;
                        case "password":
                            registerFragment.passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                            break;
                    }
                }
                break;

            case "verification":
                PinFragment pinFragment = ((PinFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (pinFragment != null) {
                    pinFragment.pinEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                }
                break;

            case "recovery":
                MobileFragment mobileFragment = ((MobileFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (mobileFragment != null) {
                    mobileFragment.mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                }
                break;
        }
    }

    public void clearException(String exception, String type) {
        switch (exception) {
            case "register":
                RegisterFragment registerFragment = ((RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (registerFragment != null) {
                    if (type.equals("gender")) {
                        registerFragment.genderException = false;
                        registerFragment.genderTabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                    }
                }
                break;
        }
    }

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_auth_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment() {
        switch (AuthRepository.theory) {
            case "auth":
                if (!authViewModel.getToken().equals("")) {
                    toolbarTextView.setText(getResources().getString(R.string.SerialTitleToken));
                } else {
                    toolbarTextView.setText(getResources().getString(R.string.SerialTitle));
                }
                loadFragment(new SerialFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "password":
                toolbarTextView.setText(getResources().getString(R.string.PasswordTitle));
                loadFragment(new PasswordFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "mobileCode":
                toolbarTextView.setText(getResources().getString(R.string.PinTitle));
                loadFragment(new PinFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "register":
                toolbarTextView.setText(getResources().getString(R.string.RegisterTitle));
                loadFragment(new RegisterFragment(this), R.anim.slide_in_left_with_fade, R.anim.slide_out_right_with_fade);
                break;
            case "mobile":
            case "recovery":
                toolbarTextView.setText(getResources().getString(R.string.MobileTitle));
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
                                authViewModel.authTheory("", "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (AuthRepository.theory.equals("sample")) {
                            AuthRepository.theory = "sample";

                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.apply();

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

                setData();
                progressDialog.dismiss();
            } else if (integer == 0) {
                progressDialog.dismiss();
                observeException();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            } else if (integer == -2) {
                progressDialog.dismiss();
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                AuthRepository.workState.removeObservers((LifecycleOwner) this);
            }
        });
    }

    private void observeException() {
        switch (ExceptionGenerator.current_exception) {
            case "auth":
                if (AuthRepository.theory.equals("auth")) {
                    if (!ExceptionGenerator.errors.isNull("authorized_key")) {
                        errorException(ExceptionGenerator.current_exception, AuthRepository.theory);
                        Toast.makeText(this, ExceptionGenerator.getErrorBody("authorized_key"), Toast.LENGTH_SHORT).show();
                    }
                } else if (AuthRepository.theory.equals("mobile")) {
                    if (!ExceptionGenerator.errors.isNull("authorized_key")) {
                        errorException(ExceptionGenerator.current_exception, AuthRepository.theory);
                        Toast.makeText(this, ExceptionGenerator.getErrorBody("authorized_key"), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case "authTheory":
                if (AuthRepository.theory.equals("password")) {
                    if (!ExceptionGenerator.errors.isNull("password")) {
                        errorException(ExceptionGenerator.current_exception, AuthRepository.theory);
                        Toast.makeText(this, ExceptionGenerator.getErrorBody("password"), Toast.LENGTH_SHORT).show();
                    }
                } else if (AuthRepository.theory.equals("pin")) {
                    if (!ExceptionGenerator.errors.isNull("code")) {
                        errorException(ExceptionGenerator.current_exception, AuthRepository.theory);
                        Toast.makeText(this, ExceptionGenerator.getErrorBody("code"), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case "register":
                RegisterFragment registerFragment = ((RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                if (registerFragment != null) {
                    String exceptionToast = "";

                    if (!ExceptionGenerator.errors.isNull("name")) {
                        errorException(ExceptionGenerator.current_exception, "name");
                        exceptionToast = ExceptionGenerator.getErrorBody("name");
                    }
                    if (!ExceptionGenerator.errors.isNull("mobile")) {
                        errorException(ExceptionGenerator.current_exception, "mobile");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("mobile");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("mobile"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("gender")) {
                        errorException(ExceptionGenerator.current_exception, "gender");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("gender");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("gender"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("password")) {
                        errorException(ExceptionGenerator.current_exception, "password");
                        if (exceptionToast.equals("")) {
                            exceptionToast = ExceptionGenerator.getErrorBody("password");
                        } else {
                            exceptionToast += (" و " + ExceptionGenerator.getErrorBody("password"));
                        }
                    }
                    Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
                }
                break;

            case "verification":
                if (!ExceptionGenerator.errors.isNull("mobile")) {
                    errorException(ExceptionGenerator.current_exception, "");
                    Toast.makeText(this, ExceptionGenerator.getErrorBody("mobile"), Toast.LENGTH_SHORT).show();
                }
                break;

            case "recovery":
                if (!ExceptionGenerator.errors.isNull("username")) {
                    errorException(ExceptionGenerator.current_exception, "");
                    Toast.makeText(this, ExceptionGenerator.getErrorBody("username"), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                String resultString = Objects.requireNonNull(data).getStringExtra("RESULT_STRING");

                if (resultString.equals("logOut")) {
                    toolbarTextView.setText(getResources().getString(R.string.SerialTitle));

                    setData();

                    SerialFragment fragment = ((SerialFragment) getSupportFragmentManager().findFragmentById(R.id.activity_auth_frameLayout));
                    if (fragment != null) {
                        fragment.setText();
                    }
                } else if (resultString.equals("edit")) {
                    if (authViewModel.getAvatar().equals("")) {
                        avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_light));
                    } else {
                        Picasso.get().load(authViewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
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