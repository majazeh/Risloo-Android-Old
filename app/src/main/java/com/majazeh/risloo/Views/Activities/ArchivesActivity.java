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

import android.content.Intent;
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
import com.majazeh.risloo.Views.Adapters.ArchivesAdapter;

import org.json.JSONException;

public class ArchivesActivity extends AppCompatActivity implements ItemTouchRecyclerView.OnItemTouchListener {

    // ViewModels
    private SampleViewModel sampleViewModel;

    // Adapters
    private ArchivesAdapter archivesAdapter;

    // Objects
    private Handler handler;
    private Typeface danaBold;

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

        setContentView(R.layout.activity_archives);

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
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        archivesAdapter = new ArchivesAdapter(this);

        handler = new Handler();

        danaBold = ResourcesCompat.getFont(this, R.font.dana_bold);

        coordinatorLayout = findViewById(R.id.activity_archives);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.ArchivesTitle));

        countTextView = findViewById(R.id.activity_archives_count_textView);

        archiveRecyclerView = findViewById(R.id.activity_archives_recyclerView);
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
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });
    }

    private void setData() {
        if (sampleViewModel.getArchive() != null) {
            countTextView.setText(sampleViewModel.getArchive().size() + " " + getResources().getString(R.string.ArchivesCount));

            archivesAdapter.setArchive(sampleViewModel.getArchive());
            archiveRecyclerView.setAdapter(archivesAdapter);
        } else {
            finish();
        }
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ArchivesAdapter.ArchivesHolder) {
            int index = viewHolder.getAdapterPosition();

            Model archive = archivesAdapter.getArchive(index);

            archivesAdapter.removeArchive(index);
            handler.postDelayed(() -> {
                try {
                    sampleViewModel.delete(archive.get("id").toString());

                    if (sampleViewModel.getArchive() == null) {
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, 4000);

            if (sampleViewModel.getArchive() != null) {
                countTextView.setText(sampleViewModel.getArchive().size() - 1 + " " + getResources().getString(R.string.ArchivesCount));
            }

            Snackbar snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.ArchivesDeleted), 4000);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.Primary5P));
            snackbar.setAction(getResources().getString(R.string.ArchivesRestore), view -> {
                archivesAdapter.restoreArchive(archive, index);
                handler.removeCallbacksAndMessages(null);

                if (sampleViewModel.getArchive() != null) {
                    countTextView.setText(sampleViewModel.getArchive().size() + " " + getResources().getString(R.string.ArchivesCount));
                }
            });

            TextView messageTextView = snackbar.getView().findViewById(R.id.snackbar_text);
            TextView actionTextView = snackbar.getView().findViewById(R.id.snackbar_action);

            messageTextView.setTypeface(danaBold);
            actionTextView.setTypeface(danaBold);

            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._11ssp));
            actionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._11ssp));

            messageTextView.setTextColor(getResources().getColor(R.color.Gray300));
            actionTextView.setTextColor(getResources().getColor(R.color.Risloo800));

            snackbar.show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}