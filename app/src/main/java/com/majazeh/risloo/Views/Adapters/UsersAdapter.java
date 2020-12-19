package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    // Vars
    private ArrayList<Model> users;

    // Objects
    private Activity activity;
    private Handler handler;

    public UsersAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_users, viewGroup, false);

        initializer(view);

        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int i) {
        Model model = users.get(i);

        try {

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                holder.serialTextView.setText(model.get("id").toString());
            }

            // Creator
            if (model.attributes.has("creator") && !model.attributes.isNull("creator")) {
                JSONObject creator = (JSONObject) model.get("creator");

                holder.creatorTextView.setText(creator.get("name").toString());
                holder.creatorLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.creatorLinearLayout.setVisibility(View.GONE);
            }

            // Reference
            if (model.attributes.has("user") && !model.attributes.isNull("user")) {
                JSONObject user = (JSONObject) model.get("user");

                holder.referenceTextView.setText(user.get("name").toString());
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            // AcceptedAt
            if (model.attributes.has("accepted_at") && !model.attributes.isNull("accepted_at")) {
                String acceptedAtTime = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(model.get("accepted_at").toString())));
                String acceptedAtDate = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(model.get("accepted_at").toString()))));

                holder.acceptedAtTextView.setText(acceptedAtDate + "\n" + acceptedAtTime);
                holder.acceptedAtLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.acceptedAtLinearLayout.setVisibility(View.GONE);
            }

            // Status
            if (model.attributes.has("position") && !model.attributes.isNull("position")) {
                holder.statusTextView.setText(model.get("position").toString());
                holder.statusLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.statusLinearLayout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setUser(ArrayList<Model> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class UsersHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, referenceTextView, creatorTextView, acceptedAtTextView, statusTextView;
        public LinearLayout referenceLinearLayout, creatorLinearLayout, acceptedAtLinearLayout, statusLinearLayout;

        public UsersHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_users_serial_textView);
            referenceTextView = view.findViewById(R.id.single_item_users_reference_textView);
            creatorTextView = view.findViewById(R.id.single_item_users_creator_textView);
            acceptedAtTextView = view.findViewById(R.id.single_item_users_accepted_at_textView);
            statusTextView = view.findViewById(R.id.single_item_users_status_textView);
            referenceLinearLayout = view.findViewById(R.id.single_item_users_reference_linearLayout);
            creatorLinearLayout = view.findViewById(R.id.single_item_users_creator_linearLayout);
            acceptedAtLinearLayout = view.findViewById(R.id.single_item_users_accepted_at_linearLayout);
            statusLinearLayout = view.findViewById(R.id.single_item_users_status_linearLayout);
        }
    }

}