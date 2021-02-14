package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.ParamsManager;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

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
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private CircleImageView avatarCircleImageView;
    private TextView nameTextView, editTextView, logOutTextView;
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

        toolbarLayout = findViewById(R.id.account_toolbar);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.AccountTitle));

        avatarCircleImageView = findViewById(R.id.account_avatar_circleImageView);

        nameTextView = findViewById(R.id.account_name_textView);
        editTextView = findViewById(R.id.account_edit_textView);
        logOutTextView = findViewById(R.id.account_logout_textView);

        accountRecyclerView = findViewById(R.id.account_recyclerView);
        accountRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._8sdp), (int) getResources().getDimension(R.dimen._32sdp)));
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountRecyclerView.setHasFixedSize(true);

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
        logOutDialogPositive.setTextColor(getResources().getColor(R.color.CoralRed));
        logOutDialogNegative = logOutDialog.findViewById(R.id.dialog_action_negative_textView);
        logOutDialogNegative.setText(getResources().getString(R.string.AccountLogOutDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            logOutTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);

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

        logOutTextView.setOnClickListener(v -> {
            logOutTextView.setClickable(false);
            handler.postDelayed(() -> logOutTextView.setClickable(true), 250);

            logOutDialog.show();
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
        try {
            accountAdapter.setAccount(authViewModel.getAll());
            accountRecyclerView.setAdapter(accountAdapter);

            if (authViewModel.getAvatar().equals("")) {
                avatarCircleImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user_circle_solid, null));
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
                    Intent result = new Intent();
                    result.putExtra("RESULT_STRING", AuthRepository.work);

                    setResult(RESULT_OK, result);
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
            if (!authViewModel.getName().equals("") && !authViewModel.getAvatar().equals("")) {
                Intent imageIntent = new Intent(this, ImageActivity.class);

                imageIntent.putExtra("title", authViewModel.getName());
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
                Intent result = new Intent();
                result.putExtra("RESULT_STRING", AuthRepository.work);

                setResult(RESULT_OK, result);
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