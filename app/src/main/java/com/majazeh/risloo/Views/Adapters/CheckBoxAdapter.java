package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxHolder> {

    // Vars
    private ArrayList<Model> values;
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
        Model model = values.get(i);

        try {
            JSONObject user = (JSONObject) model.get("user");

            holder.titleCheckBox.setText(user.getString("name"));

            holder.titleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

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
        return values.size();
    }

    private void initializer(View view) {
        handler = new Handler();
        checks = new ArrayList<>();
    }

    public void setValue(ArrayList<Model> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    public void setChecks(ArrayList<String> checks){
        this.checks = checks;
    }

    public ArrayList<Model> getValues() {
        return values;
    }

    public ArrayList<String> getChecks() {
        return checks;
    }

    public class CheckBoxHolder extends RecyclerView.ViewHolder {

        public CheckBox titleCheckBox;

        public CheckBoxHolder(View view) {
            super(view);
            titleCheckBox = view.findViewById(R.id.single_item_checkbox_title_checkBox);
        }
    }

}