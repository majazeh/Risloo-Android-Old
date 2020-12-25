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
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Views.Activities.PracticesActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class PracticesAdapter extends RecyclerView.Adapter<PracticesAdapter.PracticeHolder> {

    // Vars
    private ArrayList<Model> practices;

    // Objects
    private Activity activity;
    private Handler handler;

    public PracticesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PracticeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_practices, viewGroup, false);

        initializer(view);

        return new PracticeHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PracticeHolder holder, int i) {
        Model model = practices.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.getPracticeTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            }

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                holder.serialTextView.setText(model.get("id").toString());
            }

            // Tittle
            if (model.attributes.has("title") && !model.attributes.isNull("title")) {

                holder.titleTextView.setText(model.get("title").toString());
                holder.titleLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.titleLinearLayout.setVisibility(View.GONE);
            }

            // Content
            if (model.attributes.has("content") && !model.attributes.isNull("content")) {

                holder.contentTextView.setText(model.get("content").toString());
                holder.contentLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.contentLinearLayout.setVisibility(View.GONE);
            }

            // Attachment
            if (model.attributes.has("attachments") && !model.attributes.isNull("attachments")) {
                holder.getPracticeTextView.setVisibility(View.VISIBLE);
            } else {
                holder.getPracticeTextView.setVisibility(View.GONE);
            }

            // Status
            if (model.attributes.has("homework") && !model.attributes.isNull("homework")) {
                holder.sendHomeworkTextView.setVisibility(View.GONE);
                holder.getHomeworkTextView.setVisibility(View.VISIBLE);
            } else {
                holder.sendHomeworkTextView.setVisibility(View.VISIBLE);
                holder.getHomeworkTextView.setVisibility(View.GONE);
            }

            holder.getPracticeTextView.setOnClickListener(v -> {
                holder.getPracticeTextView.setClickable(false);
                handler.postDelayed(() -> holder.getPracticeTextView.setClickable(true), 250);
                try {
                    JSONObject attachment = (JSONObject) model.get("attachments");
                    JSONObject original =  attachment.getJSONObject("original");
                if (PermissionManager.storagePermission(activity)) {
                    IntentManager.download(activity, original.getString("url"));
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            holder.getHomeworkTextView.setOnClickListener(view -> {
                holder.getHomeworkTextView.setClickable(false);
                handler.postDelayed(() -> holder.getHomeworkTextView.setClickable(true), 250);
                if (model.attributes.has("homework") && !model.attributes.isNull("homework")) {
                    try {
                        JSONObject homework = (JSONObject) model.get("homework");
                        if (PermissionManager.storagePermission(activity)) {
                            IntentManager.download(activity, homework.getString("url"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });

            holder.sendHomeworkTextView.setOnClickListener(v -> {
                holder.sendHomeworkTextView.setClickable(false);
                handler.postDelayed(() -> holder.sendHomeworkTextView.setClickable(true), 250);

                // TODO: get pic and send

            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return practices.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setPractices(ArrayList<Model> practices) {
        this.practices = practices;
        notifyDataSetChanged();
    }

    private void clearProgress() {
        if (((PracticesActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((PracticesActivity) Objects.requireNonNull(activity)).loading = false;
            ((PracticesActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public class PracticeHolder extends RecyclerView.ViewHolder {
        public TextView serialTextView, titleTextView, noteTextView, contentTextView, getPracticeTextView, sendHomeworkTextView,getHomeworkTextView;
        public LinearLayout titleLinearLayout, noteLinearLayout, contentLinearLayout;

        public PracticeHolder(@NonNull View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_practices_serial_textView);
            titleTextView = view.findViewById(R.id.single_item_practices_title_textView);
            noteTextView = view.findViewById(R.id.single_item_practices_note_textView);
            contentTextView = view.findViewById(R.id.single_item_practices_content_textView);
            getPracticeTextView = view.findViewById(R.id.single_item_practices_get_practice_textView);
            sendHomeworkTextView = view.findViewById(R.id.single_item_practices_send_homework_textView);
            getHomeworkTextView = view.findViewById(R.id.single_item_practices_get_homework_textView);
            titleLinearLayout = view.findViewById(R.id.single_item_practices_title_linearLayout);
            noteLinearLayout = view.findViewById(R.id.single_item_practices_note_linearLayout);
            contentLinearLayout = view.findViewById(R.id.single_item_practices_content_linearLayout);

        }
    }
}
