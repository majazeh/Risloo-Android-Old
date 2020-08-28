package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxHolder> {

    // Vars
    private ArrayList<String> references;
    private HashMap<String, String> checks;

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

        holder.titleCheckBox.setText("عنوان");

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);

            if (holder.titleCheckBox.isChecked()) {
//                checks.put(String.valueOf(i), references.get(i));
            } else {
//                checks.remove(String.valueOf(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setReference(ArrayList<String> references, HashMap<String, String> checks) {
        this.references = references;
        this.checks = checks;
        notifyDataSetChanged();
    }

    public class CheckBoxHolder extends RecyclerView.ViewHolder {

        public CheckBox titleCheckBox;

        public CheckBoxHolder(View view) {
            super(view);
            titleCheckBox = view.findViewById(R.id.single_item_checkbox_title_checkBox);
        }
    }

}