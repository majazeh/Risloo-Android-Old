package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.ItemHelper;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.ArchiveAdapter;

public class ArchiveActivity extends AppCompatActivity implements ItemHelper.RecyclerItemTouchHelperListener {

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
    private Toolbar toolbar;
    private TextView countTextView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_archive);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        adapter = new ArchiveAdapter(this);
        if (hasArchive()) {
            adapter.setArchive(viewModel.getArchive());
        }

        handler = new Handler();

        danaBold = ResourcesCompat.getFont(this, R.font.dana_bold);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        coordinatorLayout = findViewById(R.id.activity_archive);

        toolbar = findViewById(R.id.activity_archive_toolbar);

        countTextView = findViewById(R.id.activity_archive_count_textView);
        if (hasArchive()) {
            countTextView.setText(viewModel.getArchive().size() + " " + getResources().getString(R.string.ArchiveCount));
        }

        recyclerView = findViewById(R.id.activity_archive_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._18sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });
    }

    private boolean hasArchive() {
        return viewModel.getArchive() != null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!hasArchive()) {
            finish();
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