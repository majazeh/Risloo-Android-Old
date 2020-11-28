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
            Intent editIntent = (new Intent(activity, EditSessionActivity.class));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            }

            editIntent.putExtra("id", (String) model.get("id"));
            holder.serialTextView.setText(model.get("id").toString());

            // Get Case
            if (model.attributes.has("case") && !model.attributes.isNull("case")) {
                JSONObject casse = (JSONObject) model.get("case");
                editIntent.putExtra("case_id", (String) casse.get("id"));

                ArrayList<Model> cases = new ArrayList<>();

                StringBuilder name = new StringBuilder();
                JSONArray client = (JSONArray) casse.get("clients");

                for (int j = 0; j < client.length(); j++) {
                    JSONObject object = client.getJSONObject(j);
                    JSONObject user = object.getJSONObject("user");

                    cases.add(new Model(user));

                    if (j == client.length() - 1)
                        name.append(user.getString("name"));
                    else
                        name.append(user.getString("name")).append(" - ");
                }

                if (!name.toString().equals("")) {
                    editIntent.putExtra("case_name", name.toString());
                }

                holder.caseTextView.setText(casse.getString("id"));
                holder.caseLinearLayout.setVisibility(View.VISIBLE);

                JSONObject room = (JSONObject) casse.get("room");
                editIntent.putExtra("room_id", (String) room.get("id"));

                JSONObject manager = (JSONObject) room.get("manager");
                if (!manager.isNull("name")) {
                    editIntent.putExtra("room_name", (String) manager.get("name"));
                }

                JSONObject center = (JSONObject) room.get("center");

                if (center.has("detail") && !center.isNull("detail")) {
                    JSONObject details = (JSONObject) center.get("detail");
                    editIntent.putExtra("room_title", (String) details.get("title"));

                    holder.roomTextView.setText(details.getString("title"));
                    holder.roomLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    holder.roomLinearLayout.setVisibility(View.GONE);
                }

            } else {
                holder.caseLinearLayout.setVisibility(View.GONE);
            }

            // get Reference
            if (model.attributes.has("client") && !model.attributes.isNull("client")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceTextView.setText(client.getString("name"));
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            // Get Status
            if (model.attributes.has("status") && !model.attributes.isNull("status")) {
                String enStatus = model.get("status").toString();
                String faStatus = ((SessionsActivity) Objects.requireNonNull(activity)).sessionViewModel.getFAStatus(model.get("status").toString());

                editIntent.putExtra("en_status", enStatus);
                editIntent.putExtra("fa_status", faStatus);

                holder.statusTextView.setText(faStatus);
                holder.statusLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.statusLinearLayout.setVisibility(View.GONE);
            }

            // Get Duration
            if (model.attributes.has("duration") && !model.attributes.isNull("duration")) {
                editIntent.putExtra("period", model.get("duration").toString());

                holder.durationTextView.setText(model.get("duration").toString() + " " + activity.getResources().getString(R.string.SessionsMinute));
                holder.durationLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.durationLinearLayout.setVisibility(View.GONE);
            }

            // Get Start
            if (model.attributes.has("started_at") && !model.attributes.isNull("started_at")) {
                String date = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString()))));
                String time = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(model.get("started_at").toString())));

                editIntent.putExtra("time", time);
                editIntent.putExtra("date", date);

                holder.startedAtTextView.setText(date + "\n" + time);
                holder.startedAtLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.startedAtLinearLayout.setVisibility(View.GONE);
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