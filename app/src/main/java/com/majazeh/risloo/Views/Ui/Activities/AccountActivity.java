package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    // Objects
    private Handler handler;
    private MenuItem toolSignOut;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AuthViewModel viewModel;

    // Adapters
    private AccountAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private CircleImageView avatarImageView;
    private TextView nameTextView, signOutDialogTitle, signOutDialogDescription, signOutDialogPositive, signOutDialogNegative;
    private RecyclerView recyclerView;
    private Dialog signOutDialog,progressDialog;

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

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        adapter = new AccountAdapter(this);

        adapter.setAccount(getAll());

        handler = new Handler();

        toolbar = findViewById(R.id.activity_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        avatarImageView = findViewById(R.id.activity_account_avatar_circleImageView);
        avatarImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_solid));

        nameTextView = findViewById(R.id.activity_account_name_textView);
        nameTextView.setText(sharedPreferences.getString("name", ""));

        recyclerView = findViewById(R.id.activity_account_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("customLayout", (int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        signOutDialog = new Dialog(this, R.style.DialogTheme);
        signOutDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        signOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signOutDialog.setContentView(R.layout.dialog_action);
        signOutDialog.setCancelable(true);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

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
            progressDialog.show();
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
        viewModel.logout();
        AuthController.workState.observe(this, integer -> {
        Log.e( "signOut: " , String.valueOf(integer));
            if (AuthController.work == "signOut"){
                if (integer == 1){
                    editor.remove("token");
                    editor.remove("name");
                    editor.remove("mobile");
                    editor.remove("gender");
                    editor.remove("email");
                    editor.apply();
                    progressDialog.dismiss();
                    finish();
                }else if (integer == 0){
                progressDialog.dismiss();
                    Toast.makeText(this, "با مشکل مواجه شد!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private JSONArray account() {
        JSONArray items = new JSONArray();
        try {
            items.put(new JSONObject().put("subTitle", sharedPreferences.getString("name", "")).put("title", "نام").put("image", getResources().getDrawable(R.drawable.ic_user_light )));
            items.put(new JSONObject().put("subTitle", sharedPreferences.getString("mobile", "")).put("title", "شماره همراه").put("image", getResources().getDrawable(R.drawable.ic_mobile )));
            items.put(new JSONObject().put("subTitle", sharedPreferences.getString("email", "")).put("title", "ایمیل").put("image", getResources().getDrawable(R.drawable.ic_envelope )));
            if (sharedPreferences.getString("gender", "").equals("male")) {
                items.put(new JSONObject().put("subTitle", "مرد").put("title", "جنسیت").put("image", getResources().getDrawable(R.drawable.ic_gender )));
            } else if (sharedPreferences.getString("gender", "").equals("female")) {
                items.put(new JSONObject().put("subTitle", "زن").put("title", "جنسیت").put("image", getResources().getDrawable(R.drawable.ic_gender )));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<Model> getAll() {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < account().length(); i++) {
            try {
                items.add(new Model(account().getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }
}