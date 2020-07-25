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

import android.graphics.Typeface;
import android.os.Bundle;
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
    private Typeface danaBold;

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
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        adapter = new ArchiveAdapter(this);
        if (hasArchive()) {
            adapter.setArchive(viewModel.getStorageFiles());
        }

        danaBold = ResourcesCompat.getFont(this, R.font.dana_bold);

        coordinatorLayout = findViewById(R.id.activity_archive);

        toolbar = findViewById(R.id.activity_archive_toolbar);

        countTextView = findViewById(R.id.activity_archive_count_textView);
        if (hasArchive()) {
            countTextView.setText(viewModel.getStorageFiles().size() + " " + getResources().getString(R.string.ArchiveCount));
        }

        recyclerView = findViewById(R.id.activity_archive_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._18sdp)));
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
        return viewModel.getStorageFiles() != null;
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

            Snackbar snackbar = Snackbar.make(coordinatorLayout, getResources().getString(R.string.ArchiveDeleted), Snackbar.LENGTH_SHORT);
            snackbar.setAction(getResources().getString(R.string.ArchiveRestore), view -> adapter.restoreArchive(archive, index));
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.Primary5P));

            TextView messageTextView = snackbar.getView().findViewById(R.id.snackbar_text);
            TextView actionTextView = snackbar.getView().findViewById(R.id.snackbar_action);

            messageTextView.setTypeface(danaBold);
            actionTextView.setTypeface(danaBold);

            messageTextView.setTextSize(getResources().getDimension(R.dimen._4ssp));
            actionTextView.setTextSize(getResources().getDimension(R.dimen._4ssp));

            messageTextView.setTextColor(getResources().getColor(R.color.Mischka));
            actionTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));

            snackbar.show();
        }
    }

}