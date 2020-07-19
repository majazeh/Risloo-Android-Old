package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // Objects
    private Handler handler;
    private MenuItem toolSignOut;

    // Adapters
    private AccountAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarImageView;
    private TextView nameTextView,  signOutDialogTitle,  signOutDialogDescription,  signOutDialogPositive,  signOutDialogNegative;
    private RecyclerView recyclerView;
    private Dialog signOutDialog;

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
        adapter = new AccountAdapter(this);
//        adapter.setAccount(viewModel.getAll());

        handler = new Handler();

        toolbar = findViewById(R.id.activity_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        avatarImageView = findViewById(R.id.activity_account_avatar_circleImageView);

        nameTextView = findViewById(R.id.activity_account_name_textView);

        recyclerView = findViewById(R.id.activity_account_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("customLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);

        signOutDialog = new Dialog(this, R.style.DialogTheme);
        signOutDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        signOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signOutDialog.setContentView(R.layout.dialog_action);
        signOutDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(signOutDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        signOutDialog.getWindow().setAttributes(layoutParams);

        signOutDialogTitle = signOutDialog.findViewById(R.id.dialog_action_title_textView);
        signOutDialogTitle.setText(getResources().getString(R.string.AccountSignOutDialogTitle));
        signOutDialogDescription = signOutDialog.findViewById(R.id.dialog_action_description_textView);
        signOutDialogDescription.setText(getResources().getString(R.string.AccountSignOutDialogDescription));
        signOutDialogPositive = signOutDialog.findViewById(R.id.dialog_action_positive_textView);
        signOutDialogPositive.setText(getResources().getString(R.string.AccountSignOutDialogPositive));
        signOutDialogPositive.setTextColor(getResources().getColor(R.color.VioletRed));
        signOutDialogNegative = signOutDialog.findViewById(R.id.dialog_action_negative_textView);
        signOutDialogNegative.setText(getResources().getString(R.string.AccountSignOutDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            signOutDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            signOutDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        signOutDialogPositive.setOnClickListener(v -> {
            signOutDialogPositive.setClickable(false);
            handler.postDelayed(() -> signOutDialogPositive.setClickable(true), 1000);
            signOutDialog.dismiss();

            signOut();
        });

        signOutDialogNegative.setOnClickListener(v -> {
            signOutDialogNegative.setClickable(false);
            handler.postDelayed(() -> signOutDialogNegative.setClickable(true), 1000);
            signOutDialog.dismiss();
        });

        signOutDialog.setOnCancelListener(dialog -> signOutDialog.dismiss());
    }

    private void signOut() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);

        toolSignOut = menu.findItem(R.id.tool_sign_out);
        toolSignOut.setOnMenuItemClickListener(menuItem -> {
            signOutDialog.show();
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