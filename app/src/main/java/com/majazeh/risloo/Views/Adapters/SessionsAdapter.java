package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Views.Activities.DetailSessionActivity;
import com.majazeh.risloo.Views.Activities.EditSessionActivity;
import com.majazeh.risloo.Views.Activities.SessionsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionsHolder> {

    // Vars
    private ArrayList<Model> sessions;

    // Objects
    private Activity activity;
    private Handler handler;

    public SessionsAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SessionsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_sessions, viewGroup, false);

        initializer(view);

        return new SessionsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsHolder holder, int i) {
        Model model = sessions.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
                holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_snow_ripple_quartz);
            }

            Intent editSessionIntent = (new Intent(activity, EditSessionActivity.class));
            Intent detailSessionIntent = (new Intent(activity, DetailSessionActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id") && !model.attributes.get("id").equals("")) {
                editSessionIntent.putExtra("id", model.get("id").toString());
                detailSessionIntent.putExtra("id", model.get("id").toString());

                holder.serialTextView.setText(model.get("id").toString());
            }

            // Case
            if (model.attributes.has("case") && !model.attributes.isNull("case") && !model.attributes.get("case").equals("")) {
                JSONObject casse = (JSONObject) model.get("case");
                editSessionIntent.putExtra("case_id", casse.get("id").toString());

                holder.caseTextView.setText(casse.get("id").toString());
                holder.caseLinearLayout.setVisibility(View.VISIBLE);

                StringBuilder name = new StringBuilder();
                JSONArray clients = (JSONArray) casse.get("clients");

                for (int j = 0; j < clients.length(); j++) {
                    JSONObject client = (JSONObject) clients.get(j);
                    JSONObject user = (JSONObject) client.get("user");

                    if (j == clients.length() - 1) {
                        name.append(user.get("name").toString());
                    } else {
                        name.append(user.get("name").toString()).append(" - ");
                    }
                }

                editSessionIntent.putExtra("case_name", name.toString());

                // Room
                if (casse.has("room") && !casse.isNull("room")) {
                    JSONObject room = (JSONObject) casse.get("room");
                    editSessionIntent.putExtra("room_id", room.get("id").toString());

                    JSONObject manager = (JSONObject) room.get("manager");
                    editSessionIntent.putExtra("room_name", manager.get("name").toString());

                    JSONObject center = (JSONObject) room.get("center");
                    JSONObject detail = (JSONObject) center.get("detail");
                    editSessionIntent.putExtra("room_title", detail.get("title").toString());

                    holder.roomTextView.setText(detail.get("title").toString());
                    holder.roomLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.roomLinearLayout.setVisibility(View.GONE);
                }
            } else {
                holder.caseLinearLayout.setVisibility(View.GONE);
            }

            // Reference
            if (model.attributes.has("client") && !model.attributes.isNull("client") && !model.attributes.get("client").equals("")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceTextView.setText(client.get("name").toString());
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            // StartedAt
            if (model.attributes.has("started_at") && !model.attributes.isNull("started_at") && !model.attributes.get("started_at").equals("")) {
                String startedAtTime = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString())));
                String startedAtDate = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString()))));

                editSessionIntent.putExtra("started_at_time", startedAtTime);
                editSessionIntent.putExtra("started_at_date", startedAtDate);

                holder.startedAtTextView.setText(startedAtDate + "\n" + startedAtTime);
                holder.startedAtLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.startedAtLinearLayout.setVisibility(View.GONE);
            }

            // Duration
            if (model.attributes.has("duration") && !model.attributes.isNull("duration") && !model.attributes.get("duration").equals("")) {
                editSessionIntent.putExtra("duration", model.get("duration").toString());

                holder.durationTextView.setText(model.get("duration").toString() + " " + activity.getResources().getString(R.string.SessionsMinute));
                holder.durationLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.durationLinearLayout.setVisibility(View.GONE);
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status") && !model.attributes.get("status").equals("")) {
                String enStatus = model.get("status").toString();
                String faStatus = ((SessionsActivity) Objects.requireNonNull(activity)).sessionViewModel.getFAStatus(model.get("status").toString());

                editSessionIntent.putExtra("en_status", enStatus);
                editSessionIntent.putExtra("fa_status", faStatus);

                holder.statusTextView.setText(faStatus);
                holder.statusLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.statusLinearLayout.setVisibility(View.GONE);
            }

//            // Edit Session Access
//            if (((SessionsActivity) Objects.requireNonNull(activity)).authViewModel.editSession(model)) {
//                holder.editTextView.setVisibility(View.VISIBLE);
//            } else {
//                holder.editTextView.setVisibility(View.GONE);
//            }

            holder.editTextView.setOnClickListener(v -> {
                holder.editTextView.setClickable(false);
                handler.postDelayed(() -> holder.editTextView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(editSessionIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

//            // Session Detail Access
//            if (((SessionsActivity) Objects.requireNonNull(activity)).authViewModel.openSessionDetail(model)) {
//                holder.itemView.setEnabled(true);
//            } else {
//                holder.itemView.setEnabled(false);
//            }

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(detailSessionIntent,100);
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

    private void clearProgress() {
        if (((SessionsActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((SessionsActivity) Objects.requireNonNull(activity)).loading = false;
            ((SessionsActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public class SessionsHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, roomTextView, caseTextView, referenceTextView, startedAtTextView, durationTextView, statusTextView, editTextView;
        public LinearLayout roomLinearLayout, caseLinearLayout, referenceLinearLayout, startedAtLinearLayout, durationLinearLayout, statusLinearLayout;

        public SessionsHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_sessions_serial_textView);
            roomTextView = view.findViewById(R.id.single_item_sessions_room_textView);
            caseTextView = view.findViewById(R.id.single_item_sessions_case_textView);
            referenceTextView = view.findViewById(R.id.single_item_sessions_reference_textView);
            startedAtTextView = view.findViewById(R.id.single_item_sessions_started_at_textView);
            durationTextView = view.findViewById(R.id.single_item_sessions_duration_textView);
            statusTextView = view.findViewById(R.id.single_item_sessions_status_textView);
            editTextView = view.findViewById(R.id.single_item_sessions_edit_textView);
            roomLinearLayout = view.findViewById(R.id.single_item_sessions_room_linearLayout);
            caseLinearLayout = view.findViewById(R.id.single_item_sessions_case_linearLayout);
            referenceLinearLayout = view.findViewById(R.id.single_item_sessions_reference_linearLayout);
            startedAtLinearLayout = view.findViewById(R.id.single_item_sessions_started_at_linearLayout);
            durationLinearLayout = view.findViewById(R.id.single_item_sessions_duration_linearLayout);
            statusLinearLayout = view.findViewById(R.id.single_item_sessions_status_linearLayout);
        }
    }

}