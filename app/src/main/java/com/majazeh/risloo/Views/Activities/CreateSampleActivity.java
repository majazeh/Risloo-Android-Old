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
    private String room = "", casse = "", count = "";
    private ArrayList<String> scales, roomReferences, caseReferences;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    private Spinner scaleSpinner, roomSpinner, caseSpinner, roomReferenceSpinner;
    private RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView;
    private LinearLayout roomLinearLayout, caseLinearLayout;
    private TextView scaleTextView, roomReferenceTextView;
    private EditText roomReferenceEditText;
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

        scales = new ArrayList<>();
        roomReferences = new ArrayList<>();
        caseReferences = new ArrayList<>();

        handler = new Handler();

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        scaleSpinner = findViewById(R.id.activity_create_sample_scale_spinner);
        roomSpinner = findViewById(R.id.activity_create_sample_room_spinner);
        caseSpinner = findViewById(R.id.activity_create_sample_case_spinner);
        roomReferenceSpinner = findViewById(R.id.activity_create_sample_room_reference_spinner);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3", (int) getResources().getDimension(R.dimen._12sdp)));
        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scaleRecyclerView.setHasFixedSize(false);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_room_reference_recyclerView);
        roomReferenceRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3", (int) getResources().getDimension(R.dimen._12sdp)));
        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomReferenceRecyclerView.setHasFixedSize(true);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout", (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);

        roomLinearLayout = findViewById(R.id.activity_create_sample_room_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_create_sample_case_linearLayout);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        roomReferenceTextView = findViewById(R.id.activity_create_sample_room_reference_textView);

        roomReferenceEditText = findViewById(R.id.activity_create_sample_room_reference_editText);

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

        scaleSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (SampleRepository.scales.size() == 0) {
                    doWork("getScales", "");
                }
            }
            return false;
        });

        roomSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (SampleRepository.roomsTitle.size() == 0) {
                    doWork("getRooms", "");
                }
            }
            return false;
        });

        caseSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                doWork("getCases", room);
            }
            return false;
        });

        roomReferenceSpinner.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                doWork("getReferences", room);
            }
            return false;
        });

        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (scaleSpinner.getCount() != position) {
                    if (!scales.contains(parent.getItemAtPosition(position).toString())) {
                        scales.add(parent.getItemAtPosition(position).toString());
                        setRecyclerView(scales, scaleRecyclerView, "spinner");
                    }

                    if (scales.size() == 1) {
                        scaleTextView.setVisibility(View.GONE);
                        scaleRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (roomSpinner.getCount() != position) {
                    room = String.valueOf(SampleRepository.roomsId.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        caseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (caseSpinner.getCount() != position) {
                    casse = parent.getItemAtPosition(position).toString();
                    // TODO : Get Case References List From ViewModel and Call SetRecyclerView
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roomReferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (roomReferenceSpinner.getCount() != position) {
                    if (!roomReferences.contains(parent.getItemAtPosition(position).toString())) {
                        roomReferences.add(parent.getItemAtPosition(position).toString());
                        setRecyclerView(roomReferences, roomReferenceRecyclerView, "spinner");
                    }

                    if (roomReferences.size() == 1) {
                        roomReferenceTextView.setVisibility(View.GONE);
                        roomReferenceRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        roomReferenceEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                roomReferenceEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                roomReferenceEditText.setCursorVisible(true);
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

    private void setSpinner(ArrayList<String> arrayList, Spinner spinner, String type) {
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
        for (int i = 0; i < arrayList.size(); i++) {
            adapter.add(arrayList.get(i));
        }
        if (type.equals("room")) {
            adapter.add(getResources().getString(R.string.CreateSampleRoom));
        } else if (type.equals("case")) {
            adapter.add(getResources().getString(R.string.CreateSampleCase));
        } else {
            adapter.add("");
        }
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
    }

    private void setRecyclerView(ArrayList<String> arrayList, RecyclerView recyclerView, String type) {
        if (type.equals("spinner")) {
            spinnerAdapter.setReference(arrayList);
            recyclerView.setAdapter(spinnerAdapter);
        } else if (type.equals("checkbox")) {
            checkBoxAdapter.setReference(arrayList);
            recyclerView.setAdapter(checkBoxAdapter);
        }
    }

    private void checkInput() {

    }

    private void clearData() {

    }

    private void doWork(String method, String roomId) {
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
                case "getCases":
                    // TODO : Show Progress
                    viewModel.cases(roomId);
                    break;
                case "getReferences":
                    // TODO : Show Progress
                    viewModel.references(roomId);
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
                    setSpinner(SampleRepository.scales, scaleSpinner, "scale");

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
                    setSpinner(SampleRepository.roomsTitle, roomSpinner, "room");

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
                    setSpinner(SampleRepository.cases, caseSpinner, "case");

                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work == "getReferences") {
                if (integer == 1) {
                    setSpinner(SampleRepository.references, roomReferenceSpinner, "reference");

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