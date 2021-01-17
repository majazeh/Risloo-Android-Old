package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.Objects;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;

public class CryptographyActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;

    // Objects
    private Handler handler;

    // Widgets
    private ImageView toolbarImageView;
    private RelativeLayout toolbarLayout;
    private TextView toolbarTextView;
    private EditText privateKeyEditText, publicKeyEditText;
    private ControlEditText controlEditText;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_cryptography);

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
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        publicKeyEditText = findViewById(R.id.activity_cryptography_publicKey_editText);
        privateKeyEditText = findViewById(R.id.activity_cryptography_privateKey_editText);

        controlEditText = new ControlEditText();

        handler = new Handler();

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));


        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CryptographyTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));


    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);
            editor.putString("public_key", publicKeyEditText.getText().toString());
            editor.putString("private_key", privateKeyEditText.getText().toString());
            editor.apply();
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        publicKeyEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!publicKeyEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(publicKeyEditText);
                    controlEditText.select(publicKeyEditText);
                }
            }
            return false;
        });

        privateKeyEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!privateKeyEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(privateKeyEditText);
                    controlEditText.select(privateKeyEditText);
                }
            }
            return false;
        });

    }

    private void setData() {
        if (!sharedPreferences.getString("public_key", "").equals("")) {
            publicKeyEditText.setText(sharedPreferences.getString("public_key", ""));
        }
        if (!sharedPreferences.getString("public_key", "").equals("")) {
            privateKeyEditText.setText(sharedPreferences.getString("private_key", ""));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editor.putString("public_key", publicKeyEditText.getText().toString());
        editor.putString("private_key", privateKeyEditText.getText().toString());
        editor.apply();
    }
}