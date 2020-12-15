package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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

public class DetailCaseSamplesAdapter extends RecyclerView.Adapter<DetailCaseSamplesAdapter.DetailCaseSamplesHolder> {

    // Vars
    private ArrayList<Model> samples;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailCaseSamplesAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailCaseSamplesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_case_samples, viewGroup, false);

        initializer(view);

        return new DetailCaseSamplesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCaseSamplesHolder holder, int i) {
        Model model = samples.get(i);

        try {

            // Title
            if (model.attributes.has("title") && !model.attributes.isNull("title")) {
                holder.titleTextView.setText(model.get("title").toString());
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status")) {
                switch (model.get("status").toString()) {
                    case "seald":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusSeald));
                        break;

                    case "open":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusOpen));
                        break;

                    case "closed":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusClosed));
                        break;

                    case "scoring":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusScoring));
                        break;

                    case "craeting_files":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusCreatingFiles));
                        break;

                    case "done":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusDone));
                        break;

                    default:
                        holder.statusTextView.setText(model.get("status").toString());
                        break;
                }
            }

            // SessionID
            if (model.attributes.has("session_id") && !model.attributes.isNull("session_id")) {
                holder.sessionIdTextView.setText(model.get("session_id").toString());
            }

            // Client
            if (model.attributes.has("client") && !model.attributes.isNull("client")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceTextView.setText(client.get("name").toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setSample(ArrayList<Model> samples) {
        this.samples = samples;
        notifyDataSetChanged();
    }

    public class DetailCaseSamplesHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, statusTextView, sessionIdTextView, referenceTextView;

        public DetailCaseSamplesHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_detail_case_samples_title_textView);
            statusTextView = view.findViewById(R.id.single_item_detail_case_samples_status_textView);
            sessionIdTextView = view.findViewById(R.id.single_item_detail_case_samples_session_id_textView);
            referenceTextView = view.findViewById(R.id.single_item_detail_case_samples_reference_textView);
        }
    }

}