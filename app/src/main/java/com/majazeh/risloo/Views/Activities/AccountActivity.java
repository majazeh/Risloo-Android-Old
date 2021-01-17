package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;

    // Adapters
    private AccountAdapter accountAdapter;

    // Objects
    private Handler handler;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarLogOutImageView;
    private TextView toolbarTextView;
    private CircleImageView avatarCircleImageView;
    private TextView nameTextView, editTextView, sendTextView, cryptographyTextView;
    private RecyclerView accountRecyclerView;
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
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        accountAdapter = new AccountAdapter(this);

        handler = new Handler();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
        toolbarLogOutImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarLogOutImageView.setVisibility(View.VISIBLE);
        toolbarLogOutImageView.setImageResource(R.drawable.ic_power_light);
        ImageViewCompat.setImageTintList(toolbarLogOutImageView, AppCompatResources.getColorStateList(this, R.color.VioletRed));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.AccountTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        avatarCircleImageView = findViewById(R.id.activity_account_avatar_circleImageView);

        nameTextView = findViewById(R.id.activity_account_name_textView);
        editTextView = findViewById(R.id.activity_account_edit_textView);
        sendTextView = findViewById(R.id.activity_account_send_textView);
        cryptographyTextView = findViewById(R.id.activity_account_cryptography_textView);

        accountRecyclerView = findViewById(R.id.activity_account_recyclerView);
        accountRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._8sdp), (int) getResources().getDimension(R.dimen._32sdp)));
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountRecyclerView.setHasFixedSize(true);

        logOutDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(logOutDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        logOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logOutDialog.setContentView(R.layout.dialog_action);
        logOutDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsLogOutDialog = new WindowManager.LayoutParams();
        layoutParamsLogOutDialog.copyFrom(logOutDialog.getWindow().getAttributes());
        layoutParamsLogOutDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsLogOutDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        logOutDialog.getWindow().setAttributes(layoutParamsLogOutDialog);

        logOutDialogTitle = logOutDialog.findViewById(R.id.dialog_action_title_textView);
        logOutDialogTitle.setText(getResources().getString(R.string.AccountLogOutDialogTitle));
        logOutDialogDescription = logOutDialog.findViewById(R.id.dialog_action_description_textView);
        logOutDialogDescription.setText(getResources().getString(R.string.AccountLogOutDialogDescription));
        logOutDialogPositive = logOutDialog.findViewById(R.id.dialog_action_positive_textView);
        logOutDialogPositive.setText(getResources().getString(R.string.AccountLogOutDialogPositive));
        logOutDialogPositive.setTextColor(getResources().getColor(R.color.VioletRed));
        logOutDialogNegative = logOutDialog.findViewById(R.id.dialog_action_negative_textView);
        logOutDialogNegative.setText(getResources().getString(R.string.AccountLogOutDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarLogOutImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            sendTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            cryptographyTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);

            logOutDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            logOutDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

            if (!authViewModel.getName().equals("") && !authViewModel.getAvatar().equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", authViewModel.getName());
                intent.putExtra("bitmap", false);
                intent.putExtra("image", authViewModel.getAvatar());

                startActivity(intent);
            }
        });

        editTextView.setOnClickListener(v -> {
            editTextView.setClickable(false);
            handler.postDelayed(() -> editTextView.setClickable(true), 250);

            startActivityForResult(new Intent(this, EditAccountActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        sendTextView.setOnClickListener(v -> {
            sendTextView.setClickable(false);
            handler.postDelayed(() -> sendTextView.setClickable(true), 250);

            startActivity(new Intent(this, AttachmentActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        cryptographyTextView.setOnClickListener(view -> {
            cryptographyTextView.setClickable(false);
            handler.postDelayed(() -> cryptographyTextView.setClickable(true), 250);

            startActivity(new Intent(this, CryptographyActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);

        });

        logOutDialogPositive.setOnClickListener(v -> {
            logOutDialogPositive.setClickable(false);
            handler.postDelayed(() -> logOutDialogPositive.setClickable(true), 250);
            logOutDialog.dismiss();

            doWork();
        });

        logOutDialogNegative.setOnClickListener(v -> {
            logOutDialogNegative.setClickable(false);
            handler.postDelayed(() -> logOutDialogNegative.setClickable(true), 300);
            logOutDialog.dismiss();
        });

        logOutDialog.setOnCancelListener(dialog -> logOutDialog.dismiss());
    }

    private void setData() {
        try {
            accountAdapter.setAccount(authViewModel.getAll());
            accountRecyclerView.setAdapter(accountAdapter);

            if (authViewModel.getAvatar().equals("")) {
                avatarCircleImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle_solid));
            } else {
                Picasso.get().load(authViewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
            }

            if (authViewModel.getName().equals("")) {
                nameTextView.setText(getResources().getString(R.string.AuthNameDefault));
            } else {
                nameTextView.setText(authViewModel.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                    setResult(RESULT_OK, new Intent().putExtra("RESULT_STRING", AuthRepository.work));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                setResult(RESULT_OK, new Intent().putExtra("RESULT_STRING", AuthRepository.work));
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