package com.majazeh.risloo.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Widgets.ItemTouchRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.ArchiveAdapter;

public class ArchiveActivity extends AppCompatActivity implements ItemTouchRecyclerView.OnItemTouchListener {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private ArchiveAdapter adapter;

    // Objects
    private Handler handler;
    private Typeface danaBold;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TextView countTextView;
    private RecyclerView archiveRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_archive);

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
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        adapter = new ArchiveAdapter(this);

        handler = new Handler();

        danaBold = ResourcesCompat.getFont(this, R.font.dana_bold);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        coordinatorLayout = findViewById(R.id.activity_archive);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.ArchiveTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        countTextView = findViewById(R.id.activity_archive_count_textView);

        archiveRecyclerView = findViewById(R.id.activity_archive_recyclerView);
        archiveRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        archiveRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        archiveRecyclerView.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchRecyclerView(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(archiveRecyclerView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });
    }

    private void setData() {
        if (hasArchive()) {
            countTextView.setText(viewModel.getArchive().size() + " " + getResources().getString(R.string.ArchiveCount));

            adapter.setArchive(viewModel.getArchive());
            archiveRecyclerView.setAdapter(adapter);
        } else {
            finish();
        }
    }

    private boolean hasArchive() {
        return viewModel.getArchive() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                setData();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ArchiveAdapter.ArchiveHolder) {

            Model archive = adapter.getArchive(viewHolder.getAdapterPosition());
            int index = viewHolder.getAdapterPosition();

            adapter.removeArchive(index);

            if (hasArchive()) {
                countTextView.setText(viewModel.getArchive().size() - 1 + " " + getResources().getString(R.string.ArchiveCount));
            }

            handler.postDelayed(() -> {
                viewModel.delete(sharedPreferences.getString("sampleId", ""));

                if (!hasArchive()) {
                    finish();
                }
            }, 4000);

            Snackbar snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.ArchiveDeleted), 4000);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.Primary5P));
            snackbar.setAction(getResources().getString(R.string.ArchiveRestore), view -> {
                adapter.restoreArchive(archive, index);
                handler.removeCallbacksAndMessages(null);

                if (hasArchive()) {
                    countTextView.setText(viewModel.getArchive().size() + " " + getResources().getString(R.string.ArchiveCount));
                }
            });

            TextView messageTextView = snackbar.getView().findViewById(R.id.snackbar_text);
            TextView actionTextView = snackbar.getView().findViewById(R.id.snackbar_action);

            messageTextView.setTypeface(danaBold);
            actionTextView.setTypeface(danaBold);

            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._11ssp));
            actionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._11ssp));

            messageTextView.setTextColor(getResources().getColor(R.color.Mischka));
            actionTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));

            snackbar.show();
        }
    }

}