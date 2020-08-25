package com.majazeh.risloo.Views.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.AccountAdapter;
import com.majazeh.risloo.Views.Adapters.ArchiveAdapter;
//import com.majazeh.risloo.Views.Adapters.ReferenceCheckBoxAdapter;

import org.json.JSONException;

public class CreateSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
//    private ReferenceCheckBoxAdapter checkBoxAdapter;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    private ViewFlipper typeViewFlipper;
    private EditText countEditText;
    private Spinner roomSpinner, caseSpinner;
    private RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView;
    private Button createButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_sample);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

//        checkBoxAdapter = new ReferenceCheckBoxAdapter(this);
//        checkBoxAdapter.setReferences();

        handler = new Handler();

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        typeViewFlipper = findViewById(R.id.activity_create_sample_type_viewFlipper);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        roomSpinner = findViewById(R.id.activity_create_sample_room_spinner);
        caseSpinner = findViewById(R.id.activity_create_sample_case_spinner);

//        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
//        scaleRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout2", (int) getResources().getDimension(R.dimen._4sdp)));
//        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        scaleRecyclerView.setHasFixedSize(false);
//        scaleRecyclerView.setAdapter(adapter);
//        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
//        roomReferenceRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout2", (int) getResources().getDimension(R.dimen._4sdp)));
//        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        roomReferenceRecyclerView.setHasFixedSize(true);
//        roomReferenceRecyclerView.setAdapter(adapter);
//        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
//        caseReferenceRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout", (int) getResources().getDimension(R.dimen._4sdp)));
//        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        caseReferenceRecyclerView.setHasFixedSize(true);
//        caseReferenceRecyclerView.setAdapter(checkBoxAdapter);

        createButton = findViewById(R.id.activity_create_sample_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            createButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        caseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        typeViewFlipper.showNext();
                        break;
                    case 1:
                        typeViewFlipper.showPrevious();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        countEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                countEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                countEditText.setCursorVisible(true);
            }
            return false;
        });

        createButton.setOnClickListener(v -> {
//            if () {
//                checkInput();
//            } else {
//                clearData();
//
//                try {
//                    progressDialog.show();
//                    viewModel.create(scale, room, case, reference);
//                    observeWork();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        });
    }

    private void checkInput() {

    }

    private void clearData() {

    }

    private void observeWork() {
        SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work == "create") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}