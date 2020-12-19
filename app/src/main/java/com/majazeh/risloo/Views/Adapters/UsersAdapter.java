package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Views.Activities.UsersActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    // Vars
    private String type = "";
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

            // Position & Acceptation
            if (model.attributes.has("position") && !model.attributes.isNull("position")) {
                String enPosition = model.get("position").toString();
                String faPosition = ((UsersActivity) Objects.requireNonNull(activity)).centerViewModel.getFAPosition(model.get("position").toString());

                if (model.attributes.has("kicked_at") && !model.attributes.isNull("kicked_at")) {
                    holder.acceptationTextView.setText(activity.getResources().getString(R.string.UsersKicked));
                } else {
                    if (model.attributes.has("accepted_at") && !model.attributes.isNull("accepted_at"))
                        holder.acceptationTextView.setText(activity.getResources().getString(R.string.UsersAccepted));
                    else
                        holder.acceptationTextView.setText(activity.getResources().getString(R.string.UsersAwaiting));
                }

                holder.positionTextView.setText(faPosition);
                holder.positionLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.positionLinearLayout.setVisibility(View.GONE);
            }

            holder.positionFrameLayout.setOnClickListener(v -> {
                holder.positionFrameLayout.setClickable(false);
                handler.postDelayed(() -> holder.positionFrameLayout.setClickable(true), 250);

                if (type.equals("center")) {
//                    showDialog(model);
                }
            });

            holder.acceptTextView.setOnClickListener(v -> {
                holder.acceptTextView.setClickable(false);
                handler.postDelayed(() -> holder.acceptTextView.setClickable(true), 250);

                // TODO : Check & Insert
            });

            holder.suspendTextView.setOnClickListener(v -> {
                holder.suspendTextView.setClickable(false);
                handler.postDelayed(() -> holder.suspendTextView.setClickable(true), 250);

                // TODO : Check & Insert
            });

            holder.createTextView.setOnClickListener(v -> {
                holder.createTextView.setClickable(false);
                handler.postDelayed(() -> holder.createTextView.setClickable(true), 250);

                // TODO : Check & Insert
            });

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

    public void setUser(ArrayList<Model> users, String type) {
        this.users = users;
        this.type = type;
        notifyDataSetChanged();
    }

    public class UsersHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, referenceTextView, creatorTextView, acceptedAtTextView, positionTextView, acceptationTextView;
        public LinearLayout referenceLinearLayout, creatorLinearLayout, acceptedAtLinearLayout, positionLinearLayout;
        public LinearLayout positionFrameLayout;
        public ImageView positionImageView;
        public TextView acceptTextView, suspendTextView, createTextView;

        public UsersHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_users_serial_textView);
            referenceTextView = view.findViewById(R.id.single_item_users_reference_textView);
            creatorTextView = view.findViewById(R.id.single_item_users_creator_textView);
            acceptedAtTextView = view.findViewById(R.id.single_item_users_accepted_at_textView);
            positionTextView = view.findViewById(R.id.single_item_users_position_textView);
            acceptationTextView = view.findViewById(R.id.single_item_users_acceptation_textView);
            referenceLinearLayout = view.findViewById(R.id.single_item_users_reference_linearLayout);
            creatorLinearLayout = view.findViewById(R.id.single_item_users_creator_linearLayout);
            acceptedAtLinearLayout = view.findViewById(R.id.single_item_users_accepted_at_linearLayout);
            positionLinearLayout = view.findViewById(R.id.single_item_users_position_linearLayout);
            positionFrameLayout = view.findViewById(R.id.single_item_users_position_frameLayout);
            positionImageView = view.findViewById(R.id.single_item_users_position_imageView);
            acceptTextView = view.findViewById(R.id.single_item_users_accept_textView);
            suspendTextView = view.findViewById(R.id.single_item_users_suspend_textView);
            createTextView = view.findViewById(R.id.single_item_users_create_textView);
        }
    }

}