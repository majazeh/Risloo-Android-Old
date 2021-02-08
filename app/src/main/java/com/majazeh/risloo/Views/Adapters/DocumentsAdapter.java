package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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
import com.majazeh.risloo.Views.Activities.DocumentsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.DocumentsHolder> {

    // Vars
    private ArrayList<Model> documents;

    // Objects
    private Activity activity;
    private Handler handler;

    public DocumentsAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DocumentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_documents, viewGroup, false);

        initializer(view);

        return new DocumentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsHolder holder, int i) {
        Model model = documents.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.acceptTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_islamicgreen_ripple_solitude);
                holder.cancelTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_violetred_ripple_solitude);
            }

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id") && !model.attributes.get("id").equals("")) {
                holder.serialTextView.setText(model.get("id").toString());
            }

            // Title
            if (model.attributes.has("title") && !model.attributes.isNull("title") && !model.attributes.get("title").equals("")) {
                holder.titleTextView.setText(model.get("title").toString());
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status") && !model.attributes.get("status").equals("")) {
                String enStatus = model.get("status").toString();
                String faStatus = ((DocumentsActivity) Objects.requireNonNull(activity)).documentViewModel.getFAStatus(model.get("status").toString());

                holder.statusTextView.setText(faStatus);
                holder.statusLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.statusLinearLayout.setVisibility(View.GONE);
            }

            // Description
            if (model.attributes.has("description") && !model.attributes.isNull("description") && !model.attributes.get("description").equals("")) {
                holder.descriptionTextView.setText(model.get("description").toString());
                holder.descriptionLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.descriptionLinearLayout.setVisibility(View.GONE);
            }

            // Note
            if (model.attributes.has("notic") && !model.attributes.isNull("notic") && !model.attributes.get("notic").equals("")) {
                holder.noteTextView.setText(model.get("notic").toString());
                holder.noteLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.noteLinearLayout.setVisibility(View.GONE);
            }

            // Attachments
            if (model.attributes.has("attachments") && !model.attributes.isNull("attachments") && model.attributes.get("attachments").getClass().getName().equals("org.json.JSONObject")) {
                JSONObject attachments = (JSONObject) model.get("attachments");
                JSONObject original = (JSONObject) attachments.get("original");

                holder.attachmentTextView.setText(original.get("id").toString());
                holder.attachmentLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.attachmentLinearLayout.setVisibility(View.GONE);
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

            holder.acceptTextView.setOnClickListener(v -> {
                holder.acceptTextView.setClickable(false);
                handler.postDelayed(() -> holder.acceptTextView.setClickable(true), 250);

//                doWork(i, model, "accept");
            });

            holder.cancelTextView.setOnClickListener(v -> {
                holder.cancelTextView.setClickable(false);
                handler.postDelayed(() -> holder.cancelTextView.setClickable(true), 250);

//                doWork(i, model, "cancel");
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void clearProgress() {
        if (((DocumentsActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((DocumentsActivity) Objects.requireNonNull(activity)).loading = false;
            ((DocumentsActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public void setDocument(ArrayList<Model> documents) {
        this.documents = documents;
        notifyDataSetChanged();
    }

    public class DocumentsHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, serialTextView, statusTextView, attachmentTextView, descriptionTextView, noteTextView;
        public LinearLayout statusLinearLayout, attachmentLinearLayout, descriptionLinearLayout, noteLinearLayout;
        public TextView acceptTextView, cancelTextView;

        public DocumentsHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_documents_title_textView);
            serialTextView = view.findViewById(R.id.single_item_documents_serial_textView);
            statusTextView = view.findViewById(R.id.single_item_documents_status_textView);
            attachmentTextView = view.findViewById(R.id.single_item_documents_attachment_textView);
            noteTextView = view.findViewById(R.id.single_item_documents_note_textView);
            descriptionTextView = view.findViewById(R.id.single_item_documents_description_textView);
            statusLinearLayout = view.findViewById(R.id.single_item_documents_status_linearLayout);
            attachmentLinearLayout = view.findViewById(R.id.single_item_documents_attachment_linearLayout);
            noteLinearLayout = view.findViewById(R.id.single_item_documents_note_linearLayout);
            descriptionLinearLayout = view.findViewById(R.id.single_item_documents_description_linearLayout);
            acceptTextView = view.findViewById(R.id.single_item_documents_accept_textView);
            cancelTextView = view.findViewById(R.id.single_item_documents_cancel_textView);
        }
    }

}