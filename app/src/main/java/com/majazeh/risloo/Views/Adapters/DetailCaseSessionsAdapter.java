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
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Views.Activities.DetailCaseActivity;
import com.majazeh.risloo.Views.Activities.DetailSessionActivity;
import com.majazeh.risloo.Views.Activities.EditSessionActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class DetailCaseSessionsAdapter extends RecyclerView.Adapter<DetailCaseSessionsAdapter.DetailCaseSessionsHolder> {

    // Vars
    private ArrayList<Model> sessions;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailCaseSessionsAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailCaseSessionsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_case_sessions, viewGroup, false);

        initializer(view);

        return new DetailCaseSessionsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailCaseSessionsHolder holder, int i) {
        Model model = sessions.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.linkImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
                holder.editImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            }

            Intent detailSessionIntent = (new Intent(activity, DetailSessionActivity.class));
            Intent editSessionIntent = (new Intent(activity, EditSessionActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id") && !model.attributes.get("id").equals("")) {
                detailSessionIntent.putExtra("id", model.get("id").toString());
                editSessionIntent.putExtra("id", model.get("id").toString());

                holder.idTextView.setText(model.get("id").toString());
            }

            editSessionIntent.putExtra("room_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomId);
            editSessionIntent.putExtra("room_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomName);
            editSessionIntent.putExtra("room_title", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomTitle);
            editSessionIntent.putExtra("case_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseId);
            editSessionIntent.putExtra("case_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseName);

            // StartedAt
            if (model.attributes.has("started_at") && !model.attributes.isNull("started_at") && !model.attributes.get("started_at").equals("")) {
                String startedAtDate = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString()))));
                String startedAtTime = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString())));

                editSessionIntent.putExtra("started_at_time", startedAtTime);
                editSessionIntent.putExtra("started_at_date", startedAtDate);

                holder.startedAtTextView.setText(startedAtDate + "\n" + startedAtTime);
            }

            // Duration
            if (model.attributes.has("duration") && !model.attributes.isNull("duration") && !model.attributes.get("duration").equals("")) {
                editSessionIntent.putExtra("duration", model.get("duration").toString());

                holder.durationTextView.setText(model.get("duration").toString() + " " + activity.getResources().getString(R.string.DetailCaseSessionMinute));
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status") && !model.attributes.get("status").equals("")) {
                String enStatus = model.get("status").toString();
                String faStatus = ((DetailCaseActivity) Objects.requireNonNull(activity)).sessionViewModel.getFAStatus(model.get("status").toString());

                editSessionIntent.putExtra("en_status", enStatus);
                editSessionIntent.putExtra("fa_status", faStatus);

                holder.statusTextView.setText(faStatus);
            }

            // Edit Session Access
            if (((DetailCaseActivity) Objects.requireNonNull(activity)).authViewModel.editSession(model)) {
                holder.editImageView.setVisibility(View.VISIBLE);
            } else {
                holder.editImageView.setVisibility(View.GONE);
            }

            holder.editImageView.setOnClickListener(v -> {
                holder.editImageView.setClickable(false);
                handler.postDelayed(() -> holder.editImageView.setClickable(true), 250);

                activity.startActivityForResult(editSessionIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

            // Detail Session Access
            if (((DetailCaseActivity) Objects.requireNonNull(activity)).authViewModel.detailSession(model)) {
                holder.linkImageView.setVisibility(View.VISIBLE);
            } else {
                holder.linkImageView.setVisibility(View.GONE);
            }

            holder.linkImageView.setOnClickListener(v -> {
                holder.linkImageView.setClickable(false);
                handler.postDelayed(() -> holder.linkImageView.setClickable(true), 250);

                activity.startActivityForResult(detailSessionIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setSession(ArrayList<Model> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    public class DetailCaseSessionsHolder extends RecyclerView.ViewHolder {

        public TextView idTextView, startedAtTextView, durationTextView, statusTextView;
        public ImageView linkImageView, editImageView;

        public DetailCaseSessionsHolder(View view) {
            super(view);
            idTextView = view.findViewById(R.id.single_item_detail_case_sessions_id_textView);
            startedAtTextView = view.findViewById(R.id.single_item_detail_case_sessions_started_at_textView);
            durationTextView = view.findViewById(R.id.single_item_detail_case_sessions_duration_textView);
            statusTextView = view.findViewById(R.id.single_item_detail_case_sessions_status_textView);
            linkImageView = view.findViewById(R.id.single_item_detail_case_sessions_link_imageView);
            editImageView = view.findViewById(R.id.single_item_detail_case_sessions_edit_imageView);
        }
    }

}