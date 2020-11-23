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
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Views.Activities.DetailCaseActivity;
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

    public DetailCaseSessionsAdapter(Activity activity) {
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
            Intent editIntent = (new Intent(activity, EditSessionActivity.class));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            }

            editIntent.putExtra("id", (String) model.get("id"));

            editIntent.putExtra("room_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomId);
            editIntent.putExtra("room_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomName);
            editIntent.putExtra("room_title", ((DetailCaseActivity) Objects.requireNonNull(activity)).roomTitle);
            editIntent.putExtra("case_id", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseId);
            editIntent.putExtra("case_name", ((DetailCaseActivity) Objects.requireNonNull(activity)).caseName);

            // Get Status
            if (model.attributes.has("status") && !model.attributes.isNull("status")) {
                String enStatus = model.get("status").toString();
                String faStatus = ((DetailCaseActivity) Objects.requireNonNull(activity)).sessionViewModel.getFAStatus(model.get("status").toString());

                editIntent.putExtra("en_status", enStatus);
                editIntent.putExtra("fa_status", faStatus);

                holder.statusTextView.setText(faStatus);
            }

            // Get Duration
            if (model.attributes.has("duration") && !model.attributes.isNull("duration")) {
                editIntent.putExtra("period", model.get("duration").toString());

                holder.durationTextView.setText(model.get("duration").toString() + " " + activity.getResources().getString(R.string.DetailCaseSessionMinute));
            }

            // Get Start
            if (model.attributes.has("started_at") && !model.attributes.isNull("started_at")) {
                String date = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString()))));
                String time = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString())));

                editIntent.putExtra("time", time);
                editIntent.putExtra("date", date);

                holder.startTextView.setText(date + "\n" + time);
            }

            holder.editTextView.setOnClickListener(v -> {
                holder.editTextView.setClickable(false);
                handler.postDelayed(() -> holder.editTextView.setClickable(true), 300);

                activity.startActivityForResult(editIntent, 100);
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

        public TextView startTextView, durationTextView, statusTextView, editTextView;

        public DetailCaseSessionsHolder(View view) {
            super(view);
            startTextView = view.findViewById(R.id.single_item_detail_case_sessions_start_textView);
            durationTextView = view.findViewById(R.id.single_item_detail_case_sessions_duration_textView);
            statusTextView = view.findViewById(R.id.single_item_detail_case_sessions_status_textView);
            editTextView = view.findViewById(R.id.single_item_detail_case_sessions_edit_textView);
        }
    }

}