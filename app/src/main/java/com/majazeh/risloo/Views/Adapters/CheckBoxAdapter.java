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

import java.util.ArrayList;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxHolder> {

    // Vars
    private String method, theory;
    private ArrayList<Model> values = new ArrayList<>();
    private ArrayList<String> checks = new ArrayList<>();

    // Objects
    private Activity activity;
    private Handler handler;

    public CheckBoxAdapter(@NonNull Activity activity) {
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
            holder.titleCheckBox.setText(model.get("name").toString());

            holder.titleCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                try {
                    if (holder.titleCheckBox.isChecked()) {
                        checks.add(model.get("id").toString());
                    } else {
                        checks.remove(String.valueOf(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    }

    public void setValues(ArrayList<Model> values, ArrayList<String> checks, String method, String theory) {
        this.values = values;
        this.checks = checks;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public void clearValues() {
        values.clear();
        checks.clear();
        notifyDataSetChanged();
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
            titleCheckBox = view.findViewById(R.id.single_item_checkbox_checkBox);
        }
    }

}