package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;

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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.linkImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            }

            Intent detailSampleIntent = (new Intent(activity, DetailSampleActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id") && !model.attributes.get("id").equals("")) {
                detailSampleIntent.putExtra("id", model.get("id").toString());
            }

            // Title
            if (model.attributes.has("title") && !model.attributes.isNull("title") && !model.attributes.get("title").equals("")) {
                holder.titleTextView.setText(model.get("title").toString());
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status") && !model.attributes.get("status").equals("")) {
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
            if (model.attributes.has("session_id") && !model.attributes.isNull("session_id") && !model.attributes.get("session_id").equals("")) {
                holder.sessionIdTextView.setText(model.get("session_id").toString());
            }

            // Client
            if (model.attributes.has("client") && !model.attributes.isNull("client") && !model.attributes.get("client").equals("")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceTextView.setText(client.get("name").toString());
            }

//            if (((DetailCaseActivity) Objects.requireNonNull(activity)).authViewModel.caseDetails(new Model(FileManager.readObjectFromCache(activity, "caseDetail" + "/" + ((DetailCaseActivity) Objects.requireNonNull(activity)).caseId)))){
//                holder.createTextView.setVisibility(View.VISIBLE);
//            } else {
//               holder.createTextView.setVisibility(View.GONE);
//           }

            holder.linkImageView.setOnClickListener(v -> {
                holder.linkImageView.setClickable(false);
                handler.postDelayed(() -> holder.linkImageView.setClickable(true), 250);

                activity.startActivityForResult(detailSampleIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

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
        public ImageView linkImageView;

        public DetailCaseSamplesHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_detail_case_samples_title_textView);
            statusTextView = view.findViewById(R.id.single_item_detail_case_samples_status_textView);
            sessionIdTextView = view.findViewById(R.id.single_item_detail_case_samples_session_id_textView);
            referenceTextView = view.findViewById(R.id.single_item_detail_case_samples_reference_textView);
            linkImageView = view.findViewById(R.id.single_item_detail_case_samples_link_imageView);
        }
    }

}