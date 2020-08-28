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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.majazeh.risloo.Views.Adapters.CheckBoxAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONException;

import java.util.ArrayList;

public class CreateSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private SpinnerAdapter spinnerAdapter;
    private CheckBoxAdapter checkBoxAdapter;

    // Vars
    private String room, Case, count;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    private LinearLayout roomLinearLayout, caseLinearLayout;
    private EditText countEditText;
    private Spinner scaleSpinner, roomSpinner, referenceSpinner, caseSpinner;
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

        spinnerAdapter = new SpinnerAdapter(this);
        checkBoxAdapter = new CheckBoxAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        roomLinearLayout = findViewById(R.id.activity_create_sample_room_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_create_sample_case_linearLayout);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleSpinner = findViewById(R.id.activity_create_sample_scale_spinner);
        roomSpinner = findViewById(R.id.activity_create_sample_room_spinner);
        referenceSpinner = findViewById(R.id.activity_create_sample_reference_spinner);
        caseSpinner = findViewById(R.id.activity_create_sample_case_spinner);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3", (int) getResources().getDimension(R.dimen._12sdp)));
        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scaleRecyclerView.setHasFixedSize(false);
        scaleRecyclerView.setAdapter(spinnerAdapter);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
        roomReferenceRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3", (int) getResources().getDimension(R.dimen._12sdp)));
        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomReferenceRecyclerView.setHasFixedSize(true);
        roomReferenceRecyclerView.setAdapter(spinnerAdapter);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout", (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);
        caseReferenceRecyclerView.setAdapter(checkBoxAdapter);

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

        scaleSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
               doWork("getScales");
            }
            return false;
        });

        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (scaleSpinner.getCount() != position) {
                    String value = parent.getItemAtPosition(position).toString();
                    // TODO : Set RecyclerView With "Value"
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roomSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                doWork("getRooms");
            }
            return false;
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (roomSpinner.getCount() != position) {
                    room = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        referenceSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                doWork("getRoomUsers");
            }
            return false;
        });

        referenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (referenceSpinner.getCount() != position) {
                    String value = parent.getItemAtPosition(position).toString();
                    // TODO : Set RecyclerView With "Value"
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        caseSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                doWork("getCases");
            }
            return false;
        });

        caseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (caseSpinner.getCount() != position) {
                    Case = parent.getItemAtPosition(position).toString();
                }
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
                        roomLinearLayout.setVisibility(View.VISIBLE);
                        caseLinearLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        roomLinearLayout.setVisibility(View.GONE);
                        caseLinearLayout.setVisibility(View.VISIBLE);
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

    private void setSpinner(ArrayList<String> arrayList, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_background) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                if (position == getCount()) {
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setText("");
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint(getItem(getCount()));
                } else {
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setText(getItem(position));
                    ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint("");
                }

                return view;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

        };
        for (int i = 0; i < 5; i++) {
            adapter.add("Parameter");
        }
        adapter.add("hint");
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    private void setRecyclerView(String value, RecyclerView recyclerView) {
        if (value == "spinner") {
//            spinnerAdapter.setReference();
        } else if (value == "checkbox"){
//            checkBoxAdapter.setReference();
        }
    }

    private void checkInput() {

    }

    private void clearData() {

    }

    private void doWork(String method) {
        try {
            switch (method) {
                case "getScales":
                    // TODO : Show Progress
                    viewModel.scales();
                    break;
                case "getRooms":
                    // TODO : Show Progress
                    viewModel.rooms();
                    break;
                case "getRoomUsers":
                    // TODO : Show Progress
//                    viewModel.roomsUsers();
                    break;
                case "getCases":
                    // TODO : Show Progress
//                    viewModel.cases();
                    break;
                case "create":
//                    progressDialog.show();
//                    viewModel.create();
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work == "getScales") {
                if (integer == 1) {
                    setSpinner(SampleRepository.scales, scaleSpinner);

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work == "getRooms") {
                if (integer == 1) {
                    setSpinner(SampleRepository.roomsTitle, roomSpinner);

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work == "getRoomsUsers") {
                if (integer == 1) {
                    setSpinner(SampleRepository.roomUsers, referenceSpinner);

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work == "getCases") {
                if (integer == 1) {
                    setSpinner(SampleRepository.cases, caseSpinner);

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work == "create") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
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