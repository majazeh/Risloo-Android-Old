package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.BitmapController;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Adapters
    private AccountAdapter adapter;

    // Objects
    private Handler handler;
    private MenuItem toolLogOut;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarImageView;
    private TextView nameTextView, editTextView, sendTextView;
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
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        adapter = new AccountAdapter(this);
        try {
            adapter.setAccount(viewModel.getAll());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler = new Handler();

        toolbar = findViewById(R.id.activity_account_toolbar);
        setSupportActionBar(toolbar);

        avatarImageView = findViewById(R.id.activity_account_avatar_circleImageView);
        if (viewModel.getAvatar().equals("")) {
            avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle));
        } else {
            Picasso.get().load(viewModel.getAvatar()).into(avatarImageView);
        }

        nameTextView = findViewById(R.id.activity_account_name_textView);
        nameTextView.setText(viewModel.getName());
        editTextView = findViewById(R.id.activity_account_edit_textView);
        sendTextView = findViewById(R.id.activity_account_send_textView);

        accountRecyclerView = findViewById(R.id.activity_account_recyclerView);
        accountRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._8sdp), (int) getResources().getDimension(R.dimen._32sdp)));
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountRecyclerView.setHasFixedSize(true);
        accountRecyclerView.setAdapter(adapter);

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

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(logOutDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        logOutDialog.getWindow().setAttributes(layoutParams);

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
            editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            sendTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);

            logOutDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            logOutDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        editTextView.setOnClickListener(v -> {
            editTextView.setClickable(false);
            handler.postDelayed(() -> editTextView.setClickable(true), 300);

            startActivityForResult(new Intent(this, EditAccountActivity.class), 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        sendTextView.setOnClickListener(v -> {
            sendTextView.setClickable(false);
            handler.postDelayed(() -> sendTextView.setClickable(true), 300);

            startActivity(new Intent(this, SendDocumentsActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        logOutDialogPositive.setOnClickListener(v -> {
            logOutDialogPositive.setClickable(false);
            handler.postDelayed(() -> logOutDialogPositive.setClickable(true), 300);
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

    private void doWork() {
        try {
            progressDialog.show();
            viewModel.logOut();
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("logOut")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);

        toolLogOut = menu.findItem(R.id.tool_log_out);
        toolLogOut.setOnMenuItemClickListener(item -> {
            logOutDialog.show();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                try {
                    adapter.setAccount(viewModel.getAll());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (viewModel.getAvatar().equals("")) {
                    avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_circle));
                } else {
                            Log.e("aaa", viewModel.getAvatar()+"AAA");
                    Picasso.get().load(viewModel.getAvatar()).into(avatarImageView);
                }

                nameTextView.setText(viewModel.getName());
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}