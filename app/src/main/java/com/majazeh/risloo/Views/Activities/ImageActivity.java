package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsibbold.zoomage.ZoomageView;
import com.majazeh.risloo.Utils.Managers.BitmapManager;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    // Vars
    private String title = "", path = "", image = "";
    private boolean bitmap = false;

    // Objects
    private Bundle extras;
    private Handler handler;

    // Widgets
    private ConstraintLayout toolbarConstraintLayout;
    private ImageView toolbarImageView, toolbarDownloadImageView;
    private TextView toolbarTextView;
    private ZoomageView avatarZoomageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_image);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.darkShowSystemUI(this);
        windowDecorator.darkSetSystemUIColor(this, getResources().getColor(R.color.Gray900), getResources().getColor(R.color.Gray900));
    }

    private void initializer() {
        extras = getIntent().getExtras();

        handler = new Handler();

        toolbarConstraintLayout = findViewById(R.id.activity_image_toolbar);
        toolbarConstraintLayout.setBackgroundColor(getResources().getColor(R.color.Gray900));

        toolbarImageView = findViewById(R.id.component_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.icon_angle_right_light);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.White));
        toolbarDownloadImageView = findViewById(R.id.component_toolbar_secondary_imageView);
        toolbarDownloadImageView.setImageResource(R.drawable.icon_download_light);
        ImageViewCompat.setImageTintList(toolbarDownloadImageView, AppCompatResources.getColorStateList(this, R.color.White));

        toolbarTextView = findViewById(R.id.component_toolbar_textView);
        toolbarTextView.setTextColor(getResources().getColor(R.color.White));

        avatarZoomageView = findViewById(R.id.activity_image_zoomageView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_gray900_ripple_white);
            toolbarDownloadImageView.setBackgroundResource(R.drawable.draw_oval_solid_gray900_ripple_white);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
        });

        toolbarDownloadImageView.setOnClickListener(v -> {
            toolbarDownloadImageView.setClickable(false);
            handler.postDelayed(() -> toolbarDownloadImageView.setClickable(true), 250);

            if (PermissionManager.storagePermission(this)) {
                IntentManager.download(this, image);
            }
        });
    }

    private void setData() {
        if (extras.getString("title") != null) {
            title = extras.getString("title");
        }
        if (extras.getString("path") != null) {
            path = extras.getString("path");
        }
        if (extras.getString("image") != null) {
            image = extras.getString("image");
        }
        if (extras.getBoolean("bitmap")) {
            bitmap = extras.getBoolean("bitmap");
        }

        if (!title.equals("")) {
            toolbarTextView.setText(title);
        }

        if (!bitmap) {
            Picasso.get().load(image).placeholder(R.color.Gray900).into(avatarZoomageView);
            toolbarDownloadImageView.setVisibility(View.VISIBLE);
        } else {
            avatarZoomageView.setImageBitmap(BitmapManager.modifyOrientation(FileManager.readBitmapFromCache(this, "image"), path));
            FileManager.deleteFileFromCache(this, "image");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.download(this, image);
            }
        }
    }

}