package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.jsibbold.zoomage.ZoomageView;
import com.majazeh.risloo.Models.Managers.BitmapManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    // Vars
    private String title = "", path = "", image = "";
    private boolean bitmap = false;

    // Objects
    private Bundle extras;

    // Widgets
    private Toolbar toolbar;
    private ZoomageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_image);

        initializer();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.defaultWindow(this, R.color.Nero, R.color.Nero);
    }

    private void initializer() {
        extras = getIntent().getExtras();

        toolbar = findViewById(R.id.activity_image_toolbar);

        imageView = findViewById(R.id.activity_image_imageView);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setData() {
        title = extras.getString("title");
        bitmap = extras.getBoolean("bitmap");

        toolbar.setTitle(title);

        if (!bitmap) {
            image = extras.getString("image");
            Picasso.get().load(image).placeholder(R.color.Nero).into(imageView);
        } else {
            path = extras.getString("path");
            if (path.equals("")) {
                imageView.setImageBitmap(FileManager.readBitmapFromCache(this, "image"));
            } else {
                imageView.setImageBitmap(BitmapManager.modifyOrientation(FileManager.readBitmapFromCache(this, "image"), path));
            }
            FileManager.deleteFileFromCache(this, "image");
        }
    }

}