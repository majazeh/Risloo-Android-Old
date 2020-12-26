package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Views.Activities.DetailSessionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailSessionPracticesAdapter extends RecyclerView.Adapter<DetailSessionPracticesAdapter.DetailSessionPracticesHolder> {

    // Vars
    private int position;
    private Model model;
    private ArrayList<Model> practices;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private Dialog progressDialog;

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
                    if (PermissionManager.filePermission(activity)) {
                        IntentManager.file(activity);
                    }

                    position = i;
                    this.model = model;
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

        progressDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    public void doWork(String sessionId, String attachment) {
        try {
            progressDialog.show();

            ((DetailSessionActivity) Objects.requireNonNull(activity)).sessionViewModel.createHomework(sessionId, model.get("id").toString(), attachment);
            observeWork(position, model, sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(int position, Model model, String sessionId) {
        SessionRepository.workState.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (SessionRepository.work.equals("createHomework")) {
                    if (integer == 1) {

                        try {
                            JSONObject allJsonObject = FileManager.readObjectFromCache(activity.getApplicationContext(), "practices" + "/" + sessionId);
                            JSONArray allPractices = (JSONArray) allJsonObject.get("data");

                            for (int i = 0; i < allPractices.length(); i++) {
                                JSONObject user = allPractices.getJSONObject(i);

                                if (model.get("id").toString().equals(user.get("id").toString())) {
                                    Model changedModel = new Model(user);

                                    replacePractice(position, changedModel);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SessionRepository.workState.removeObserver(this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SessionRepository.workState.removeObserver(this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SessionRepository.workState.removeObserver(this);
                    }
                }
            }
        });
    }

    public void setPractice(ArrayList<Model> practices) {
        this.practices = practices;
        notifyDataSetChanged();
    }

    public void replacePractice(int position, Model model) {
        practices.set(position, model);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, getItemCount());
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