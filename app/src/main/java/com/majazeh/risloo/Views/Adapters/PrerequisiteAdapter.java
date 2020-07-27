package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PrerequisiteAdapter extends RecyclerView.Adapter<PrerequisiteAdapter.PrerequisiteHolder> {

    // Vars
    private ArrayList prerequisites;

    // Objects
    private Activity activity;
    private Handler handler;
    private JSONObject jsonObject;
    ArrayList<ArrayList> arrayList;

    public PrerequisiteAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PrerequisiteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_prerequisite_single_item, viewGroup, false);
        initializer(view);

        return new PrerequisiteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrerequisiteHolder holder, int i) {
        JSONObject item = (JSONObject) prerequisites.get(i);
        try {
            holder.title.setText(((JSONObject) prerequisites.get(i)).getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (item.getJSONObject("answer").getString("type").equals("number")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);

                holder.typeEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                holder.typeEditText.setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        Toast.makeText(activity, holder.typeEditText.getText(), Toast.LENGTH_SHORT).show();
                        ArrayList arrayList1 = new ArrayList<>();
                        arrayList1.add(i+1);
                        arrayList1.add(holder.typeEditText.getText().toString());
                        arrayList.add(arrayList1);
                        return true;
                    }
                    return false;
                });
            } else if (item.getJSONObject("answer").getString("type").equals("text")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);

                holder.typeEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                holder.typeEditText.setOnKeyListener((v, keyCode, event) -> {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        String text = holder.typeEditText.getText().toString();
                        ArrayList arrayList1 = new ArrayList<>();
                        arrayList1.add(i+1);
                        arrayList1.add(text);
                        arrayList.add(arrayList1);
                        return true;
                    }
                    return false;
                });
            } else if (item.getJSONObject("answer").getString("type").equals("select")){
                holder.typeEditText.setVisibility(View.GONE);
                holder.optionSpinner.setVisibility(View.VISIBLE);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_background);
                for (int j = -1; j < item.getJSONObject("answer").getJSONArray("options").length(); j++) {
                    if (j == -1) {
                        adapter.add("");
                    } else {
                        adapter.add((String) item.getJSONObject("answer").getJSONArray("options").get(j));
                    }
                }
                adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                holder.optionSpinner.setAdapter(adapter);
                holder.optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();
                        ArrayList arrayList1 = new ArrayList<>();
                        arrayList1.add(i+1);
                        arrayList1.add(text);
                        arrayList.add(arrayList1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else{

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
        arrayList = new ArrayList<>();

        handler = new Handler();

        jsonObject = new JSONObject();
    }

    public void setPrerequisite(ArrayList prerequisites) {
        this.prerequisites = prerequisites;
        notifyDataSetChanged();
    }

    public class PrerequisiteHolder extends RecyclerView.ViewHolder {

        public EditText typeEditText;
        public Spinner optionSpinner;
        public TextView title;

        public PrerequisiteHolder(View view) {
            super(view);
            typeEditText = view.findViewById(R.id.activity_prerequisite_single_item_type_editText);
            optionSpinner = view.findViewById(R.id.activity_prerequisite_single_item_option_spinner);
            title = view.findViewById(R.id.activity_prerequisite_single_item_title);
        }
    }

    public ArrayList answers(){
        return arrayList;
    }

}