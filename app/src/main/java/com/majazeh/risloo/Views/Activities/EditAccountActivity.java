package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

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
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.BitmapManager;
import com.majazeh.risloo.Utils.Managers.ExceptionManager;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.TabAccountAdapter;
import com.majazeh.risloo.Views.Fragments.EditAvatarFragment;
import com.majazeh.risloo.Views.Fragments.EditCryptoFragment;
import com.majazeh.risloo.Views.Fragments.EditPasswordFragment;
import com.majazeh.risloo.Views.Fragments.EditPersonalFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditAccountActivity extends AppCompatActivity {

    // Fragments
    public EditPersonalFragment editPersonalFragment;
    public EditPasswordFragment editPasswordFragment;
    public EditAvatarFragment editAvatarFragment;
    public EditCryptoFragment editCryptoFragment;

    // ViewModels
    public AuthViewModel authViewModel;

    // Adapters
    public TabAccountAdapter tabAccountAdapter;

    // Objects
    public Handler handler;
    public ControlEditText controlEditText;
    public ExceptionManager exceptionManager;
    public PathManager pathManager;

    // Widgets
    private ConstraintLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TabLayout editAccountTabLayout;
    private RtlViewPager editAccountRtlViewPager;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_account);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        editPersonalFragment = ((EditPersonalFragment) getSupportFragmentManager().findFragmentById(R.id.edit_account_rtlViewPager));
        editPasswordFragment = ((EditPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.edit_account_rtlViewPager));
        editAvatarFragment = ((EditAvatarFragment) getSupportFragmentManager().findFragmentById(R.id.edit_account_rtlViewPager));
        editCryptoFragment = ((EditCryptoFragment) getSupportFragmentManager().findFragmentById(R.id.edit_account_rtlViewPager));

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        tabAccountAdapter = new TabAccountAdapter(getSupportFragmentManager(), 0, this);

        handler = new Handler();

        controlEditText = new ControlEditText();

        exceptionManager = new ExceptionManager();

        pathManager = new PathManager();

        toolbarLayout = findViewById(R.id.edit_account_toolbar);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.component_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.component_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.EditAccountTitle));

        editAccountTabLayout = findViewById(R.id.edit_account_tabLayout);

        editAccountRtlViewPager = findViewById(R.id.edit_account_rtlViewPager);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
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

        editAccountTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(EditAccountActivity.this, controlEditText.input());
                }

                editAccountRtlViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        editAccountRtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(EditAccountActivity.this, controlEditText.input());
                }

                editAccountTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setData() {
        editAccountRtlViewPager.setAdapter(tabAccountAdapter);
    }

    public void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("personal") || AuthRepository.work.equals("password") || AuthRepository.work.equals("avatar")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);

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
        switch (ExceptionGenerator.current_exception) {
            case "personal":
                if (editPersonalFragment != null) {
                    String personalExceptionToast = "";

                    if (!ExceptionGenerator.errors.isNull("name")) {
                        exceptionManager.error(editPersonalFragment.nameEditText);
                        personalExceptionToast = ExceptionGenerator.getErrorBody("name");
                    }
                    if (!ExceptionGenerator.errors.isNull("username")) {
                        exceptionManager.error(editPersonalFragment.usernameEditText);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("username");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("username"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("mobile")) {
                        exceptionManager.error(editPersonalFragment.mobileEditText);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("mobile");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("mobile"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("email")) {
                        exceptionManager.error(editPersonalFragment.emailEditText);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("email");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("email"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("birthday")) {
                        editPersonalFragment.birthdayException = exceptionManager.error(editPersonalFragment.birthdayTextView);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("birthday");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("birthday"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("gender")) {
                        editPersonalFragment.genderException = exceptionManager.error(editPersonalFragment.genderTabLayout);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("gender");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("gender"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("status")) {
                        editPersonalFragment.statusException = exceptionManager.error(editPersonalFragment.statusTabLayout);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("status");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("status"));
                        }
                    }
                    if (!ExceptionGenerator.errors.isNull("type")) {
                        editPersonalFragment.typeException = exceptionManager.error(editPersonalFragment.typeTabLayout);
                        if (personalExceptionToast.equals("")) {
                            personalExceptionToast = ExceptionGenerator.getErrorBody("type");
                        } else {
                            personalExceptionToast += (" و " + ExceptionGenerator.getErrorBody("type"));
                        }
                    }

                    Toast.makeText(this, personalExceptionToast, Toast.LENGTH_SHORT).show();
                }
                break;

            case "password":
                if (editPasswordFragment != null) {
                    String passwordExceptionToast = "";

                    if (!ExceptionGenerator.errors.isNull("password")) {
                        exceptionManager.error(editPasswordFragment.passwordEditText);
                        passwordExceptionToast = ExceptionGenerator.getErrorBody("password");
                    }
                    Toast.makeText(this, passwordExceptionToast, Toast.LENGTH_SHORT).show();
                }
                break;

            case "avatar":
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                break;
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
                if (editAvatarFragment != null) {
                    editAvatarFragment.avatarFilePath = IntentManager.camera(this);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (editAvatarFragment != null) {
                if (requestCode == 300) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);

                        editAvatarFragment.avatarFilePath = pathManager.getLocalPath(this, imageUri);

                        editAvatarFragment.avatarBitmap = BitmapManager.scaleToCenter(imageBitmap);

                        editAvatarFragment.avatarCircleImageView.setImageBitmap(BitmapManager.modifyOrientation(editAvatarFragment.avatarBitmap, editAvatarFragment.avatarFilePath));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 400) {
                    File imageFile = new File(editAvatarFragment.avatarFilePath);
                    IntentManager.mediaScan(this, imageFile);

                    int scaleFactor = Math.max(1, 2);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scaleFactor;

                    Bitmap imageBitmap = BitmapFactory.decodeFile(editAvatarFragment.avatarFilePath, options);

                    editAvatarFragment.avatarBitmap = BitmapManager.scaleToCenter(imageBitmap);

                    editAvatarFragment.avatarCircleImageView.setImageBitmap(BitmapManager.modifyOrientation(editAvatarFragment.avatarBitmap, editAvatarFragment.avatarFilePath));
                }
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