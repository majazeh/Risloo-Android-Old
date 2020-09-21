package com.majazeh.risloo.Views.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PrerequisiteAdapter extends RecyclerView.Adapter<PrerequisiteAdapter.PrerequisiteHolder> {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String result = "";
    private ArrayList prerequisites;
    private JSONArray answers;
    public HashMap answer;

    // Objects
    private Activity activity;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    public EditText inputEditText;

    public PrerequisiteAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PrerequisiteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_prerequisite, viewGroup, false);

        initializer(view);

        return new PrerequisiteHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PrerequisiteHolder holder, int i) {
        JSONObject item = (JSONObject) prerequisites.get(i);

        try {
            if (item.getJSONObject("answer").getString("type").equals("number")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);
                holder.arrowImageView.setVisibility(View.GONE);

                holder.typeEditText.setText(answers.getJSONArray(i).getString(1));
                holder.typeEditText.setHint(((JSONObject) prerequisites.get(i)).getString("text"));
                holder.typeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

                holder.typeEditText.setOnTouchListener((v, event) -> {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        if (!holder.typeEditText.hasFocus()) {
                            if (inputEditText != null && inputEditText.hasFocus()) {
                                clearInput(inputEditText);
                            }

                            selectInput(holder.typeEditText);
                            focusInput(holder.typeEditText);
                        }
                    }
                    return false;
                });

                holder.typeEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        result = holder.typeEditText.getText().toString().trim();
                        getAnswers(i + 1, result);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                });

            } else if (item.getJSONObject("answer").getString("type").equals("text")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);
                holder.arrowImageView.setVisibility(View.GONE);

                holder.typeEditText.setText(answers.getJSONArray(i).getString(1));
                holder.typeEditText.setHint(((JSONObject) prerequisites.get(i)).getString("text"));
                holder.typeEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                holder.typeEditText.setOnTouchListener((v, event) -> {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        if (!holder.typeEditText.hasFocus()) {
                            if (inputEditText != null && inputEditText.hasFocus()) {
                                clearInput(inputEditText);
                            }

                            selectInput(holder.typeEditText);
                            focusInput(holder.typeEditText);
                        }
                    }
                    return false;
                });

                holder.typeEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        result = holder.typeEditText.getText().toString().trim();
                        getAnswers(i + 1, result);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                });

            } else if (item.getJSONObject("answer").getString("type").equals("select")) {
                holder.typeEditText.setVisibility(View.GONE);
                holder.optionSpinner.setVisibility(View.VISIBLE);
                holder.arrowImageView.setVisibility(View.VISIBLE);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_background) {

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
                for (int j = 0; j < item.getJSONObject("answer").getJSONArray("options").length(); j++) {
                    adapter.add((String) item.getJSONObject("answer").getJSONArray("options").get(j));
                }
                adapter.add(((JSONObject) prerequisites.get(i)).getString("text"));
                adapter.setDropDownViewResource(R.layout.spinner_dropdown);

                int count;
                if (!answers.getJSONArray(i).getString(1).isEmpty()) {
                    count = (Integer.parseInt(answers.getJSONArray(i).getString(1))) - 1;
                } else {
                    count = adapter.getCount();
                }

                holder.optionSpinner.setAdapter(adapter);
                holder.optionSpinner.setSelection(count);

                holder.optionSpinner.setOnTouchListener((v, event) -> {
                    if (MotionEvent.ACTION_UP == event.getAction()) {
                        if (inputEditText != null && inputEditText.hasFocus()) {
                            clearInput(inputEditText);
                        }
                    }
                    return false;
                });

                holder.optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (adapter.getCount() != position) {
                            int pos = position + 1;
                            result = String.valueOf(pos);
                            getAnswers(i + 1, result);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return prerequisites.size();
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();

        answer = new HashMap();
    }

    public void setPrerequisite(ArrayList prerequisites, JSONArray answers, SampleViewModel viewModel) {
        this.prerequisites = prerequisites;
        this.answers = answers;
        this.viewModel = viewModel;
        notifyDataSetChanged();
    }

    public void getAnswers(int index, String result) {
        try {
            JSONArray jsonArray = viewModel.readPrerequisiteAnswerFromCache(sharedPreferences.getString("sampleId", ""));
            jsonArray.put(index, new JSONArray().put(String.valueOf(index)).put(result));

            viewModel.writePrerequisiteAnswerToCache(jsonArray, sharedPreferences.getString("sampleId", ""));

            answer.put(index,result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void focusInput(EditText input) {
        this.inputEditText = input;
    }

    private void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    public void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public class PrerequisiteHolder extends RecyclerView.ViewHolder {

        public EditText typeEditText;
        public Spinner optionSpinner;
        public ImageView arrowImageView;

        public PrerequisiteHolder(View view) {
            super(view);
            typeEditText = view.findViewById(R.id.single_item_prerequisite_type_editText);
            optionSpinner = view.findViewById(R.id.single_item_prerequisite_option_spinner);
            arrowImageView = view.findViewById(R.id.single_item_prerequisite_arrow_imageView);
        }
    }

}