package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailCaseReferencesAdapter extends RecyclerView.Adapter<DetailCaseReferencesAdapter.DetailCaseReferencesHolder> {

    // Vars
    private ArrayList<Model> references;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailCaseReferencesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailCaseReferencesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_case_references, viewGroup, false);

        initializer(view);

        return new DetailCaseReferencesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCaseReferencesHolder holder, int i) {
        Model model = references.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.createTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            }

            // Get Duration
            if (model.attributes.has("user") && !model.attributes.isNull("user")) {
                JSONObject user = (JSONObject) model.get("user");

                holder.nameTextView.setText(user.get("name").toString());
            }

            holder.createTextView.setOnClickListener(v -> {
                holder.createTextView.setClickable(false);
                handler.postDelayed(() -> holder.createTextView.setClickable(true), 300);

                // TODO : Call Create Reference Intent
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

    public class DetailCaseReferencesHolder extends RecyclerView.ViewHolder {

        public TextView createTextView, nameTextView;

        public DetailCaseReferencesHolder(View view) {
            super(view);
            createTextView = view.findViewById(R.id.single_item_detail_case_references_create_textView);
            nameTextView = view.findViewById(R.id.single_item_detail_case_references_name_textView);
        }
    }

}