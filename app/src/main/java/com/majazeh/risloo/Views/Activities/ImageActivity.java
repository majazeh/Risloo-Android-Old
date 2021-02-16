package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarDownloadImageView;
    private TextView toolbarTextView;
    private ZoomageView imageView;

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

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Gray900));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.White));
        toolbarDownloadImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarDownloadImageView.setImageResource(R.drawable.ic_download_light);
        ImageViewCompat.setImageTintList(toolbarDownloadImageView, AppCompatResources.getColorStateList(this, R.color.White));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setTextColor(getResources().getColor(R.color.White));

        imageView = findViewById(R.id.activity_image_imageView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero_ripple_white);
            toolbarDownloadImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero_ripple_white);
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
        title = extras.getString("title");
        bitmap = extras.getBoolean("bitmap");

        toolbarTextView.setText(title);

        if (!bitmap) {
            image = extras.getString("image");
            Picasso.get().load(image).placeholder(R.color.Gray900).into(imageView);
            toolbarDownloadImageView.setVisibility(View.VISIBLE);
        } else {
            path = extras.getString("path");
            imageView.setImageBitmap(BitmapManager.modifyOrientation(FileManager.readBitmapFromCache(this, "image"), path));
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