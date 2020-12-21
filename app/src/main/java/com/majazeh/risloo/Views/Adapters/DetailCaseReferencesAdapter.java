package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
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
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.DetailCaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailCaseReferencesAdapter extends RecyclerView.Adapter<DetailCaseReferencesAdapter.DetailCaseReferencesHolder> {

    // Vars
    private ArrayList<Model> references;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailCaseReferencesAdapter(@NonNull Activity activity) {
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

            Intent createSampleIntent = (new Intent(activity, CreateSampleActivity.class));

            createSampleIntent.putExtra("loaded", true);
            createSampleIntent.putExtra("room_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomId);
            createSampleIntent.putExtra("room_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomName);
            createSampleIntent.putExtra("room_title", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomTitle);
            createSampleIntent.putExtra("case_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseId);
            createSampleIntent.putExtra("case_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseName);
            createSampleIntent.putExtra("session_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).sessionId);
            createSampleIntent.putExtra("session_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).sessionName);
            createSampleIntent.putExtra("clients", ((DetailCaseActivity) Objects.requireNonNull(activity)).clients);

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                createSampleIntent.putExtra("client_id", model.get("id").toString());
            }

            // User
            if (model.attributes.has("user") && !model.attributes.isNull("user")) {
                JSONObject user = (JSONObject) model.get("user");

                holder.nameTextView.setText(user.get("name").toString());
            }

            holder.createTextView.setOnClickListener(v -> {
                holder.createTextView.setClickable(false);
                handler.postDelayed(() -> holder.createTextView.setClickable(true), 250);

                activity.startActivityForResult(createSampleIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
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