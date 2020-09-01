package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxHolder> {

    // Vars
    private ArrayList<Model> references;
    private ArrayList<String> checks;

    // Objects
    private Activity activity;
    private Handler handler;

    public CheckBoxAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CheckBoxHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_checkbox, viewGroup, false);

        initializer(view);

        return new CheckBoxHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxHolder holder, int i) {
        try {
            JSONObject user = (JSONObject) references.get(i).get("user");

            holder.titleCheckBox.setText(user.getString("name"));

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 500);

                if (holder.titleCheckBox.isChecked()) {
                    try {
                        checks.add(user.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    checks.remove(String.valueOf(i));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return references.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setReference(ArrayList<Model> references) {
        this.references = references;
        notifyDataSetChanged();
    }

    public void setChecks(ArrayList<String> checks){
        this.checks = checks;
    }

    public ArrayList<String> getChecks() {
        return checks;
    }

    public ArrayList<Model> getReferences() {
        return references;
    }

    public class CheckBoxHolder extends RecyclerView.ViewHolder {

        public CheckBox titleCheckBox;

        public CheckBoxHolder(View view) {
            super(view);
            titleCheckBox = view.findViewById(R.id.single_item_checkbox_title_checkBox);
        }
    }

}