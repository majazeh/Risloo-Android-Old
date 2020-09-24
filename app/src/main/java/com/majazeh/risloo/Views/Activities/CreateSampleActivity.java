package com.majazeh.risloo.Views.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.CheckBoxAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CreateSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private SpinnerAdapter scaleAdapter, referenceAdapter;
    private CheckBoxAdapter checkBoxAdapter;

    // Vars
    private String room = "", casse = "", count = "";
    private boolean scaleException = false, roomException = false, roomReferenceException = false, caseException = false, caseReferenceException = false;

    // Widgets
    private Toolbar toolbar;
    private TabLayout typeTabLayout;
    public Spinner scaleSpinner, roomSpinner, caseSpinner, roomReferenceSpinner;
    public TextView scaleTextView, roomTextView, caseTextView, roomReferenceTextView, caseReferenceTextView;
    public EditText countEditText;
    private RecyclerView scaleRecyclerView, roomReferenceRecyclerView, caseReferenceRecyclerView;
    private ProgressBar scaleProgressBar, roomProgressBar, caseProgressBar, roomReferenceProgressBar;
    private ImageView roomImageView, caseImageView;
    private CardView roomReferenceCardView;
    private LinearLayout scaleLinearLayout, roomLinearLayout, caseLinearLayout, roomReferenceLinearLayout;
    private FrameLayout roomFrameLayout, caseFrameLayout;
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
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        scaleAdapter = new SpinnerAdapter(this);
        referenceAdapter = new SpinnerAdapter(this);
        checkBoxAdapter = new CheckBoxAdapter(this);

        toolbar = findViewById(R.id.activity_create_sample_toolbar);

        typeTabLayout = findViewById(R.id.activity_create_sample_type_tabLayout);

        scaleSpinner = findViewById(R.id.activity_create_sample_scale_spinner);
        roomSpinner = findViewById(R.id.activity_create_sample_room_spinner);
        caseSpinner = findViewById(R.id.activity_create_sample_case_spinner);
        roomReferenceSpinner = findViewById(R.id.activity_create_sample_clinic_reference_spinner);

        scaleTextView = findViewById(R.id.activity_create_sample_scale_textView);
        roomTextView = findViewById(R.id.activity_create_sample_room_textView);
        caseTextView = findViewById(R.id.activity_create_sample_case_textView);
        roomReferenceTextView = findViewById(R.id.activity_create_sample_clinic_reference_textView);
        caseReferenceTextView = findViewById(R.id.activity_create_sample_case_reference_textView);

        countEditText = findViewById(R.id.activity_create_sample_count_editText);

        scaleRecyclerView = findViewById(R.id.activity_create_sample_scale_recyclerView);
        scaleRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        scaleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scaleRecyclerView.setHasFixedSize(true);
        roomReferenceRecyclerView = findViewById(R.id.activity_create_sample_clinic_reference_recyclerView);
        roomReferenceRecyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", 0, (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        roomReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        roomReferenceRecyclerView.setHasFixedSize(true);
        caseReferenceRecyclerView = findViewById(R.id.activity_create_sample_case_reference_recyclerView);
        caseReferenceRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._12sdp), (int) getResources().getDimension(R.dimen._3sdp), (int) getResources().getDimension(R.dimen._12sdp)));
        caseReferenceRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        caseReferenceRecyclerView.setHasFixedSize(true);

        scaleProgressBar = findViewById(R.id.activity_create_sample_scale_progressBar);
        roomProgressBar = findViewById(R.id.activity_create_sample_room_progressBar);
        caseProgressBar = findViewById(R.id.activity_create_sample_case_progressBar);
        roomReferenceProgressBar = findViewById(R.id.activity_create_sample_clinic_reference_progressBar);

        roomImageView = findViewById(R.id.activity_create_sample_room_imageView);
        caseImageView = findViewById(R.id.activity_create_sample_case_imageView);

        roomReferenceCardView = findViewById(R.id.activity_create_sample_clinic_reference_cardView);

        scaleLinearLayout = findViewById(R.id.activity_create_sample_scale_linearLayout);
        roomLinearLayout = findViewById(R.id.activity_create_sample_clinic_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_create_sample_case_linearLayout);
        roomReferenceLinearLayout = findViewById(R.id.activity_create_sample_clinic_reference_linearLayout);

        roomFrameLayout = findViewById(R.id.activity_create_sample_room_frameLayout);
        caseFrameLayout = findViewById(R.id.activity_create_sample_case_frameLayout);

        createButton = findViewById(R.id.activity_create_sample_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            scaleSpinner.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
            roomReferenceSpinner.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
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
                if (!countEditText.hasFocus()) {
                    clearInput(countEditText);
                }

                switch (tab.getPosition()) {
                    case 0:
                        roomLinearLayout.setVisibility(View.VISIBLE);
                        caseLinearLayout.setVisibility(View.GONE);

                        // Reset Case
                        if (SampleRepository.cases.size() != 0) {
                            casse = "";
                            SampleRepository.cases.clear();
                            caseSpinner.setAdapter(null);
                            caseTextView.setVisibility(View.VISIBLE);
                        }

                        // Reset CaseReferences
                        if (checkBoxAdapter.getValues() != null) {
                            checkBoxAdapter.setChecks(new ArrayList<>());
                            caseReferenceTextView.setVisibility(View.GONE);

                            checkBoxAdapter.getValues().clear();
                            checkBoxAdapter.notifyDataSetChanged();
                        }

                        break;
                    case 1:
                        roomLinearLayout.setVisibility(View.GONE);
                        caseLinearLayout.setVisibility(View.VISIBLE);

                        // Reset RoomReferences
                        if (SampleRepository.references.size() != 0) {
                            SampleRepository.references.clear();
                            roomReferenceSpinner.setAdapter(null);
                            roomReferenceTextView.setVisibility(View.VISIBLE);

                            referenceAdapter.getValues().clear();
                            referenceAdapter.notifyDataSetChanged();
                        }

                        // Reset Count
                        if (countEditText.length() != 0) {
                            count = "";
                            countEditText.getText().clear();
                            countEditText.setVisibility(View.VISIBLE);
                        }

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
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (scaleException) {
                    clearException("scale");
                }

                if (!countEditText.hasFocus()) {
                    clearInput(countEditText);
                }

                if (SampleRepository.scales.size() == 0) {
                    getData("getScales", "");
                }
            }
            return false;
        });

        roomSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (roomException) {
                    clearException("room");
                }

                if (!countEditText.hasFocus()) {
                    clearInput(countEditText);
                }

                if (SampleRepository.rooms.size() == 0) {
                    getData("getRooms", "");
                }
            }
            return false;
        });

        caseSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!room.isEmpty()) {
                    if (caseException) {
                        clearException("case");
                    }

                    if (SampleRepository.cases.size() == 0) {
                        getData("getCases", room);
                    }
                } else {
                    Toast.makeText(this, "لطفا اول اتاق درمانی انتخاب کنید", Toast.LENGTH_SHORT).show();
                }

                if (!countEditText.hasFocus()) {
                    clearInput(countEditText);
                }
            }
            return false;
        });

        roomReferenceSpinner.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!room.isEmpty()) {
                    if (roomReferenceException) {
                        clearException("roomReference");
                    }

                    if (SampleRepository.references.size() == 0) {
                        getData("getReferences", room);
                    }
                } else {
                    Toast.makeText(this, "لطفا اول اتاق درمانی انتخاب کنید", Toast.LENGTH_SHORT).show();
                }

                if (!countEditText.hasFocus()) {
                    clearInput(countEditText);
                }
            }
            return false;
        });

        scaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (scaleSpinner.getCount() != position) {
                    if (!scaleAdapter.getValues().contains(parent.getItemAtPosition(position).toString())) {
                        scaleAdapter.getValues().add(SampleRepository.scales.get(position));
                        setRecyclerView(scaleAdapter.getValues(), scaleRecyclerView, "scale");
                    }

                    if (scaleAdapter.getValues().size() == 1) {
                        scaleTextView.setVisibility(View.GONE);
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
                    try {
                        room = String.valueOf(SampleRepository.rooms.get(position).get("id"));

                        countEditText.setFocusableInTouchMode(true);

                        // Reset Case
                        if (SampleRepository.cases.size() != 0) {
                            casse = "";
                            SampleRepository.cases.clear();
                            caseSpinner.setAdapter(null);
                            caseTextView.setVisibility(View.VISIBLE);
                        }

                        // Reset RoomReferences
                        if (SampleRepository.references.size() != 0) {
                            SampleRepository.references.clear();
                            roomReferenceSpinner.setAdapter(null);
                            roomReferenceTextView.setVisibility(View.VISIBLE);

                            referenceAdapter.getValues().clear();
                            referenceAdapter.notifyDataSetChanged();
                        }

                        // Reset CaseReferences
                        if (checkBoxAdapter.getValues() != null) {
                            checkBoxAdapter.setChecks(new ArrayList<>());
                            caseReferenceTextView.setVisibility(View.GONE);

                            checkBoxAdapter.getValues().clear();
                            checkBoxAdapter.notifyDataSetChanged();
                        }

                        // Reset Count
                        if (countEditText.length() != 0) {
                            count = "";
                            countEditText.setText(count);
                            countEditText.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    try {
                        casse = String.valueOf(SampleRepository.cases.get(position).get("id"));

                        ArrayList<Model> cases = new ArrayList<>();
                        JSONArray clients = (JSONArray) SampleRepository.cases.get(position).get("clients");
                        for (int i = 0; i < clients.length(); i++) {
                            JSONObject object = (JSONObject) clients.get(i);
                            cases.add(new Model(object));
                        }

                        setRecyclerView(cases, caseReferenceRecyclerView, "checkbox");

                        caseReferenceTextView.setVisibility(View.VISIBLE);
                        caseReferenceRecyclerView.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    if (!referenceAdapter.getValues().contains(parent.getItemAtPosition(position).toString())) {
                        referenceAdapter.getValues().add(SampleRepository.references.get(position));
                        setRecyclerView(referenceAdapter.getValues(), roomReferenceRecyclerView, "roomReference");
                    }

                    if (referenceAdapter.getValues().size() == 1) {
                        roomReferenceTextView.setVisibility(View.GONE);
                        countEditText.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!room.isEmpty()) {
                    if (!countEditText.hasFocus()) {
                        selectInput(countEditText);
                    }
                } else {
                    Toast.makeText(this, "لطفا اول اتاق درمانی انتخاب کنید", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        countEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (countEditText.length() == 1) {
                    roomReferenceCardView.setVisibility(View.GONE);
                } else if (countEditText.length() == 0) {
                    roomReferenceCardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createButton.setOnClickListener(v -> {
            if (!countEditText.hasFocus()) {
                clearInput(countEditText);
            }

            if (scaleAdapter.getValues().size() == 0) {
                errorView("scale");
            }
            if (room.isEmpty()) {
                errorView("room");
            }

            if (scaleException) {
                clearException("scale");
            }
            if (roomException) {
                clearException("room");
            }
            if (caseException) {
                clearException("case");
            }
            if (roomReferenceException) {
                clearException("roomReference");
            }
            if (caseReferenceException) {
                clearException("caseReference");
            }

            if (scaleAdapter.getValues().size() != 0 && !room.isEmpty()) {
                doWork();
            }
        });
    }

    public void setSpinner(ArrayList<Model> arrayList, Spinner spinner, String type) {
        if (type.equals("scale") || type.equals("reference")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_null) {

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    if (position == getCount()) {
                        ((TextView) view.findViewById(R.id.spinner_null_textView)).setText("");
                        try {
                            if (type.equals("scale")) {
                                ((TextView) view.findViewById(R.id.spinner_null_textView)).setHint(String.valueOf(arrayList.get(position).get("title")));
                            } else {
                                JSONObject user = (JSONObject) arrayList.get(position).get("user");
                                ((TextView) view.findViewById(R.id.spinner_null_textView)).setHint(String.valueOf(user.get("name")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (type.equals("scale")) {
                                ((TextView) view.findViewById(R.id.spinner_null_textView)).setText(String.valueOf(arrayList.get(position).get("title")));
                            } else {
                                JSONObject user = (JSONObject) arrayList.get(position).get("user");
                                ((TextView) view.findViewById(R.id.spinner_null_textView)).setText(String.valueOf(user.get("name")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((TextView) view.findViewById(R.id.spinner_null_textView)).setHint("");
                    }

                    return view;
                }

                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }

            };
            try {
                if (type.equals("scale")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        adapter.add((String) arrayList.get(i).get("title"));
                    }
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        JSONObject user = (JSONObject) arrayList.get(i).get("user");
                        adapter.add((String) user.get("name"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);

            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getCount());

        } else if (type.equals("room") || type.equals("case")){
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
            try {
                if (type.equals("room")) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        JSONObject manager = (JSONObject) arrayList.get(i).get("manager");
                        adapter.add((String) manager.get("name"));
                    }
                } else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        ArrayList<String> list = new ArrayList<>();
                        StringBuilder title = new StringBuilder();
                        JSONArray clients = (JSONArray) arrayList.get(i).get("clients");

                        for (int j = 0; j < clients.length(); j++) {
                            JSONObject object = clients.getJSONObject(j);
                            JSONObject user = object.getJSONObject("user");

                            if (j == clients.length() - 1) {
                                title.append(user.getString("name"));
                            } else {
                                title.append(user.getString("name")).append(" - ");
                            }

                            list.add(user.getString("name"));
                        }

                        if (!title.toString().equals("")) {
                            adapter.add(title.toString());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type.equals("room")) {
                adapter.add(getResources().getString(R.string.CreateSampleRoom));
                roomTextView.setVisibility(View.GONE);
            } else {
                adapter.add(getResources().getString(R.string.CreateSampleCase));
                caseTextView.setVisibility(View.GONE);
            }
            adapter.setDropDownViewResource(R.layout.spinner_dropdown);

            spinner.setAdapter(adapter);
            spinner.setSelection(adapter.getCount());

        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String type) {
        switch (type) {
            case "scale":
                scaleAdapter.setValue(arrayList, type);
                recyclerView.setAdapter(scaleAdapter);
                break;
            case "roomReference":
                referenceAdapter.setValue(arrayList, type);
                recyclerView.setAdapter(referenceAdapter);
                break;
            case "checkbox":
                checkBoxAdapter.setValue(arrayList);
                recyclerView.setAdapter(checkBoxAdapter);
                break;
        }
    }

    private void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    private void errorView(String type) {
        if (type.equals("scale")) {
            scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        } else if (type.equals("room")) {
            roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
    }

    private void clearException(String type) {
        switch (type) {
            case "scale":
                scaleException = false;
                scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "room":
                roomException = false;
                roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "case":
                caseException = false;
                caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "roomReference":
                roomReferenceException = false;
                roomReferenceLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
            case "caseReference":
                caseReferenceException = false;
                caseReferenceRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void getData(String method, String roomId) {
        try {
            switch (method) {
                case "getScales":
                    scaleProgressBar.setVisibility(View.VISIBLE);
                    scaleSpinner.setClickable(false);

                    viewModel.scales();
                    break;
                case "getRooms":
                    roomProgressBar.setVisibility(View.VISIBLE);
                    roomImageView.setVisibility(View.GONE);
                    roomSpinner.setClickable(false);

                    viewModel.rooms();
                    break;
                case "getCases":
                    caseProgressBar.setVisibility(View.VISIBLE);
                    caseImageView.setVisibility(View.GONE);
                    caseSpinner.setClickable(false);

                    viewModel.cases(roomId);
                    break;
                case "getReferences":
                    roomReferenceProgressBar.setVisibility(View.VISIBLE);
                    roomReferenceSpinner.setClickable(false);

                    viewModel.references(roomId);
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        count = countEditText.getText().toString().trim();

        try {
            progressDialog.show();
            viewModel.create(scaleAdapter.getValuesId(), room, casse, referenceAdapter.getValuesId(), checkBoxAdapter.getChecks(), count);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SampleRepository.workStateCreate.observe((LifecycleOwner) this, integer -> {
            switch (SampleRepository.work) {
                case "create":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);
                        finish();

                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        observeException();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getScales":
                    if (integer == 1) {
                        setSpinner(SampleRepository.scales, scaleSpinner, "scale");

                        scaleProgressBar.setVisibility(View.GONE);
                        scaleSpinner.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        scaleProgressBar.setVisibility(View.GONE);
                        scaleSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        scaleProgressBar.setVisibility(View.GONE);
                        scaleSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getRooms":
                    if (integer == 1) {
                        setSpinner(SampleRepository.rooms, roomSpinner, "room");

                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomSpinner.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        roomProgressBar.setVisibility(View.GONE);
                        roomImageView.setVisibility(View.VISIBLE);
                        roomSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getCases":
                    if (integer == 1) {
                        setSpinner(SampleRepository.cases, caseSpinner, "case");

                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseSpinner.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        caseProgressBar.setVisibility(View.GONE);
                        caseImageView.setVisibility(View.VISIBLE);
                        caseSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "getReferences":
                    if (integer == 1) {
                        setSpinner(SampleRepository.references, roomReferenceSpinner, "reference");

                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceSpinner.setClickable(true);
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        roomReferenceProgressBar.setVisibility(View.GONE);
                        roomReferenceSpinner.setClickable(true);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateCreate.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("create")) {
            try {
                if (!ExceptionManager.errors.isNull("scale_id")) {
                    scaleLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("scale_id"), Toast.LENGTH_SHORT).show();
                    scaleException = true;
                }
                if (!ExceptionManager.errors.isNull("room_id")) {
                    roomFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("gender"), Toast.LENGTH_SHORT).show();
                    roomException = true;
                }
                if (!ExceptionManager.errors.isNull("case_id")) {
                    caseFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("gender"), Toast.LENGTH_SHORT).show();
                    caseException = true;
                }
                if (!ExceptionManager.errors.isNull("client_id")) {
                    if (typeTabLayout.getSelectedTabPosition() == 0) {
                        roomReferenceLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                        Toast.makeText(this, "" + ExceptionManager.errors.getString("gender"), Toast.LENGTH_SHORT).show();
                        roomReferenceException = true;
                    } else if (typeTabLayout.getSelectedTabPosition() == 1) {
                        caseReferenceRecyclerView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                        Toast.makeText(this, "" + ExceptionManager.errors.getString("gender"), Toast.LENGTH_SHORT).show();
                        caseReferenceException = true;
                    }
                }
                if (!ExceptionManager.errors.isNull("count")) {
                    countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("count"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}