package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Adapters
    private AccountAdapter adapter;

    // Objects
    private Handler handler;
    private MenuItem toolSignOut;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarImageView;
    private TextView nameTextView, logOutDialogTitle, logOutDialogDescription, logOutDialogPositive, logOutDialogNegative;
    private RecyclerView recyclerView;
    private Dialog logOutDialog, progressDialog;

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
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        adapter = new AccountAdapter(this);
        adapter.setAccount(viewModel.getAll());

        handler = new Handler();

        toolbar = findViewById(R.id.activity_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        avatarImageView = findViewById(R.id.activity_account_avatar_circleImageView);
        avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_solid));

        nameTextView = findViewById(R.id.activity_account_name_textView);
        nameTextView.setText(viewModel.getName());

        recyclerView = findViewById(R.id.activity_account_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout2", (int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        logOutDialog = new Dialog(this, R.style.DialogTheme);
        logOutDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        logOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logOutDialog.setContentView(R.layout.dialog_action);
        logOutDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
            logOutDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            logOutDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        logOutDialogPositive.setOnClickListener(v -> {
            logOutDialogPositive.setClickable(false);
            handler.postDelayed(() -> logOutDialogPositive.setClickable(true), 1000);
            logOutDialog.dismiss();

            try {
                progressDialog.show();
                viewModel.logOut();
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        logOutDialogNegative.setOnClickListener(v -> {
            logOutDialogNegative.setClickable(false);
            handler.postDelayed(() -> logOutDialogNegative.setClickable(true), 1000);
            logOutDialog.dismiss();
        });

        logOutDialog.setOnCancelListener(dialog -> logOutDialog.dismiss());
    }

    private void observeWork() {
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthController.work == "logOut") {
                if (integer == 1) {
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + AuthController.exception, Toast.LENGTH_SHORT).show();
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);

        toolSignOut = menu.findItem(R.id.tool_sign_out);
        toolSignOut.setOnMenuItemClickListener(menuItem -> {
            logOutDialog.show();
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}