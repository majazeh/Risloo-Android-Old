package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailSessionPracticesAdapter extends RecyclerView.Adapter<DetailSessionPracticesAdapter.DetailSessionPracticesHolder> {

    // Vars
    private ArrayList<Model> practices;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailSessionPracticesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailSessionPracticesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_session_practices, viewGroup, false);

        initializer(view);

        return new DetailSessionPracticesHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DetailSessionPracticesHolder holder, int i) {
        Model model = practices.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.attachmentTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
                holder.homeworkTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_snow_border_quartz_ripple_quartz);
            }

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                holder.idTextView.setText(model.get("id").toString());
            }

            // Tittle
            if (model.attributes.has("title") && !model.attributes.isNull("title")) {
                holder.titleTextView.setText(model.get("title").toString());
            }

            // Content
            if (model.attributes.has("content") && !model.attributes.isNull("content")) {
                holder.contentTextView.setText(model.get("content").toString());
            }

//            // Note
//            if (model.attributes.has("note") && !model.attributes.isNull("note")) {
//                holder.noteTextView.setText(model.get("note").toString());
//            }

            // Attachment
            if (model.attributes.has("attachments") && !model.attributes.isNull("attachments")) {
                holder.attachmentTextView.setVisibility(View.VISIBLE);
            } else {
                holder.attachmentTextView.setVisibility(View.GONE);
            }

            // Homework
            if (model.attributes.has("homework") && !model.attributes.isNull("homework")) {
                holder.homeworkTextView.setText(activity.getResources().getString(R.string.DetailSessionPracticeHomeWorkDownload));
            } else {
                holder.homeworkTextView.setText(activity.getResources().getString(R.string.DetailSessionPracticeHomeWorkSend));
            }

            holder.attachmentTextView.setOnClickListener(v -> {
                holder.attachmentTextView.setClickable(false);
                handler.postDelayed(() -> holder.attachmentTextView.setClickable(true), 250);

                if (model.attributes.has("attachments") && !model.attributes.isNull("attachments")) {
                    try {
                        JSONObject attachments = (JSONObject) model.get("attachments");
                        JSONObject original = (JSONObject) attachments.get("original");

                        if (PermissionManager.storagePermission(activity)) {
                            IntentManager.download(activity, original.get("url").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.homeworkTextView.setOnClickListener(view -> {
                holder.homeworkTextView.setClickable(false);
                handler.postDelayed(() -> holder.homeworkTextView.setClickable(true), 250);

                if (model.attributes.has("homework") && !model.attributes.isNull("homework")) {
                    try {
                        JSONObject homework = (JSONObject) model.get("homework");

                        if (PermissionManager.storagePermission(activity)) {
                            IntentManager.download(activity, homework.get("url").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                }
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

    public void setPractice(ArrayList<Model> practices) {
        this.practices = practices;
        notifyDataSetChanged();
    }

    public class DetailSessionPracticesHolder extends RecyclerView.ViewHolder {

        public TextView idTextView, titleTextView, contentTextView, noteTextView, attachmentTextView, homeworkTextView;

        public DetailSessionPracticesHolder(@NonNull View view) {
            super(view);
            idTextView = view.findViewById(R.id.single_item_detail_session_practices_id_textView);
            titleTextView = view.findViewById(R.id.single_item_detail_session_practices_title_textView);
            contentTextView = view.findViewById(R.id.single_item_detail_session_practices_content_textView);
            noteTextView = view.findViewById(R.id.single_item_detail_session_practices_note_textView);
            attachmentTextView = view.findViewById(R.id.single_item_detail_session_practices_attachment_textView);
            homeworkTextView = view.findViewById(R.id.single_item_detail_session_practices_homework_textView);
        }
    }

}