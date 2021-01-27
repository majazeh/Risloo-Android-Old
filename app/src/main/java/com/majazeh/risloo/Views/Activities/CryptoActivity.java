package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.CryptoViewModel;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

import org.json.JSONException;

public class CryptoActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;
    public CryptoViewModel cryptoViewModel;

    // Adapters
    private ListAdapter publicListAdapter, privateListAdapter;

    // Vars
    private String publicKey = "", privateKey = "";

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private RecyclerView publicKeyRecyclerview, privateKeyRecyclerview;
    private EditText publicKeyEditText, privateKeyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_crypto);

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
        cryptoViewModel = new ViewModelProvider(this).get(CryptoViewModel.class);

        publicListAdapter = new ListAdapter(this);
        privateListAdapter = new ListAdapter(this);

        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CryptoTitle));

        publicKeyRecyclerview = findViewById(R.id.activity_crypto_publicKey_recyclerView);
        publicKeyRecyclerview.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._24sdp), (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        publicKeyRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        publicKeyRecyclerview.setHasFixedSize(true);
        privateKeyRecyclerview = findViewById(R.id.activity_crypto_privateKey_recyclerView);
        privateKeyRecyclerview.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._24sdp), (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        privateKeyRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        privateKeyRecyclerview.setHasFixedSize(true);

        publicKeyEditText = findViewById(R.id.activity_crypto_publicKey_editText);
        privateKeyEditText = findViewById(R.id.activity_crypto_privateKey_editText);
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

            saveKeys();

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
        if (authViewModel.getPublicKey().equals("")) {
            publicKeyEditText.setHint(getResources().getString(R.string.CryptoPublicKey));
        } else {
            publicKey = authViewModel.getPublicKey();
            publicKeyEditText.setText(publicKey);
        }

        if (authViewModel.getPrivateKey().equals("")) {
            privateKeyEditText.setHint(getResources().getString(R.string.CryptoPrivateKey));
        } else {
            privateKey = authViewModel.getPrivateKey();
            privateKeyEditText.setText(privateKey);
        }

        try {
            publicListAdapter.setList(cryptoViewModel.getAll("public"), "CryptoPublic");
            privateListAdapter.setList(cryptoViewModel.getAll("private"), "CryptoPrivate");
            publicKeyRecyclerview.setAdapter(publicListAdapter);
            privateKeyRecyclerview.setAdapter(privateListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveKeys() {
        publicKey = publicKeyEditText.getText().toString().trim();
        privateKey = privateKeyEditText.getText().toString().trim();

        authViewModel.setPublicKey(publicKey);
        authViewModel.setPrivateKey(privateKey);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);

        saveKeys();
    }

}