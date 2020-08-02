package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrerequisiteAdapter extends RecyclerView.Adapter<PrerequisiteAdapter.PrerequisiteHolder> {

    // Vars
    private String type = "", option = "";
    private ArrayList prerequisites;

    // Objects
    private Activity activity;
    private JSONArray jsonArray;

    public PrerequisiteAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PrerequisiteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_prerequisite_single_item, viewGroup, false);

        initializer(view);

        return new PrerequisiteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrerequisiteHolder holder, int i) {
        JSONObject item = (JSONObject) prerequisites.get(i);

        try {
            if (item.getJSONObject("answer").getString("type").equals("number")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);
                holder.arrowImageView.setVisibility(View.GONE);

                holder.typeEditText.setHint(((JSONObject) prerequisites.get(i)).getString("text"));
                holder.typeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

                holder.typeEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        type = holder.typeEditText.getText().toString().trim();

                        try {
                            jsonArray.put(i + 1, new JSONObject().put("index",i+1).put("answer", type));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) { }

                });

            } else if (item.getJSONObject("answer").getString("type").equals("text")) {
                holder.typeEditText.setVisibility(View.VISIBLE);
                holder.optionSpinner.setVisibility(View.GONE);
                holder.arrowImageView.setVisibility(View.GONE);

                holder.typeEditText.setHint(((JSONObject) prerequisites.get(i)).getString("text"));
                holder.typeEditText.setInputType(InputType.TYPE_CLASS_TEXT);

                holder.typeEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        type = holder.typeEditText.getText().toString().trim();

                        try {
                            jsonArray.put(i + 1, new JSONObject().put("index",i+1).put("answer", type));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) { }

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
                            ((TextView)view.findViewById(R.id.spinner_background_textView)).setText("");
                            ((TextView)view.findViewById(R.id.spinner_background_textView)).setHint(getItem(getCount()));
                        } else {
                            ((TextView)view.findViewById(R.id.spinner_background_textView)).setText(getItem(position));
                            ((TextView)view.findViewById(R.id.spinner_background_textView)).setHint("");
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

                holder.optionSpinner.setAdapter(adapter);
                holder.optionSpinner.setSelection(adapter.getCount());
                holder.optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (adapter.getCount() != position) {
                            option = parent.getItemAtPosition(position).toString();

                            try {
                                jsonArray.put(i + 1, new JSONObject().put("index",i+1).put("answer", option));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }

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
        jsonArray = new JSONArray();
    }

    public void setPrerequisite(ArrayList prerequisites) {
        this.prerequisites = prerequisites;
        notifyDataSetChanged();
    }

    public ArrayList<ArrayList> answers() throws JSONException {
        ArrayList<ArrayList> arrayList = new ArrayList<>();
        for (int i = 1; i < jsonArray.length(); i++) {
            ArrayList list= new ArrayList();
            list.add(jsonArray.getJSONObject(i).get("index"));
            list.add(jsonArray.getJSONObject(i).get("answer"));
            arrayList.add(list);
        }
        return arrayList;
    }

    public class PrerequisiteHolder extends RecyclerView.ViewHolder {

        public EditText typeEditText;
        public Spinner optionSpinner;
        public ImageView arrowImageView;

        public PrerequisiteHolder(View view) {
            super(view);
            typeEditText = view.findViewById(R.id.fragment_prerequisite_single_item_type_editText);
            optionSpinner = view.findViewById(R.id.fragment_prerequisite_single_item_option_spinner);
            arrowImageView = view.findViewById(R.id.fragment_prerequisite_single_item_arrow_imageView);
        }
    }

}