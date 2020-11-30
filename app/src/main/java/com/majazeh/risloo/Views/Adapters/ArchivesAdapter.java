package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.SampleActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ArchivesAdapter extends RecyclerView.Adapter<ArchivesAdapter.ArchivesHolder> {

    // Vars
    private ArrayList<Model> archives;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private Dialog continueDialog;
    private TextView continueDialogTitle, continueDialogDescription, continueDialogPositive, continueDialogNegative;

    public ArchivesAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArchivesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_archives, viewGroup, false);

        initializer(view);

        return new ArchivesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivesHolder holder, int i) {
        Model model = archives.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.continueTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            }

            holder.serialTextView.setText(model.get("id").toString());

            if (!model.attributes.isNull("scale")) {
                JSONObject scale = (JSONObject) model.get("scale");

                holder.scaleTextView.setText(scale.get("title").toString());

                switch ((String) model.get("status")) {
                    case "open":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.ArchivesStatusOpen));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));

                        break;
                    default:
                        holder.statusTextView.setText(model.get("status").toString());
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));

                        holder.continueTextView.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            if (!model.attributes.isNull("client")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.ArchivesReference));
                holder.referenceHintImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_user_light));
                holder.referenceTextView.setText(client.get("name").toString());
            } else if (!model.attributes.isNull("code")){
                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.ArchivesCode));
                holder.referenceHintImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_hashtag_light));
                holder.referenceTextView.setText(model.get("code").toString());
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            if (!model.attributes.isNull("case")) {
                JSONObject Case = (JSONObject) model.get("case");

                holder.caseTextView.setText(Case.get("name").toString());
            } else {
                holder.caseLinearLayout.setVisibility(View.GONE);
            }

            if (!model.attributes.isNull("room")) {
                JSONObject room = (JSONObject) model.get("room");
                JSONObject center = (JSONObject) room.get("center");
                JSONObject detail = (JSONObject) center.get("detail");

                holder.roomTextView.setText(detail.get("title").toString());
            } else {
                holder.roomLinearLayout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.continueTextView.setOnClickListener(v -> {
            holder.continueTextView.setClickable(false);
            handler.postDelayed(() -> holder.continueTextView.setClickable(true), 300);
            continueDialog.show();

            try {
                showDialog(model.get("id").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return archives.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void showDialog(String sampleId) {
        initDialog();

        detector();

        listener(sampleId);

        continueDialog.show();
    }

    private void initDialog() {
        continueDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(continueDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        continueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        continueDialog.setContentView(R.layout.dialog_action);
        continueDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsContinueDialog = new WindowManager.LayoutParams();
        layoutParamsContinueDialog.copyFrom(continueDialog.getWindow().getAttributes());
        layoutParamsContinueDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsContinueDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        continueDialog.getWindow().setAttributes(layoutParamsContinueDialog);

        continueDialogTitle = continueDialog.findViewById(R.id.dialog_action_title_textView);
        continueDialogTitle.setText(activity.getResources().getString(R.string.ArchivesContinueDialogTitle));
        continueDialogDescription = continueDialog.findViewById(R.id.dialog_action_description_textView);
        continueDialogDescription.setText(activity.getResources().getString(R.string.ArchivesContinueDialogDescription));
        continueDialogPositive = continueDialog.findViewById(R.id.dialog_action_positive_textView);
        continueDialogPositive.setText(activity.getResources().getString(R.string.ArchivesContinueDialogPositive));
        continueDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        continueDialogNegative = continueDialog.findViewById(R.id.dialog_action_negative_textView);
        continueDialogNegative.setText(activity.getResources().getString(R.string.ArchivesContinueDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            continueDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            continueDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener(String sampleId) {
        continueDialogPositive.setOnClickListener(v -> {
            continueDialogPositive.setClickable(false);
            handler.postDelayed(() -> continueDialogPositive.setClickable(true), 250);
            continueDialog.dismiss();

            doWork(sampleId);
        });

        continueDialogNegative.setOnClickListener(v -> {
            continueDialogNegative.setClickable(false);
            handler.postDelayed(() -> continueDialogNegative.setClickable(true), 250);
            continueDialog.dismiss();
        });

        continueDialog.setOnCancelListener(dialog -> continueDialog.dismiss());
    }

    private void doWork(String sampleId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();

        editor.putString("sampleId", sampleId);
        editor.apply();

        activity.startActivityForResult(new Intent(activity, SampleActivity.class),100);
    }

    public void setArchive(ArrayList<Model> archives) {
        this.archives = archives;
        notifyDataSetChanged();
    }

    public Model getArchive(int position) {
        return archives.get(position);
    }

    public void removeArchive(int position) {
        archives.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreArchive(Model archive, int position) {
        archives.add(position, archive);
        notifyItemInserted(position);
    }

    public class ArchivesHolder extends RecyclerView.ViewHolder {

        public FrameLayout backGroundView;
        public LinearLayout foreGroundView;
        public TextView scaleTextView, serialTextView, statusTextView, continueTextView, referenceHintTextView, referenceTextView, caseTextView, roomTextView;
        public ImageView statusImageView, referenceHintImageView;
        public LinearLayout referenceLinearLayout, caseLinearLayout, roomLinearLayout;

        public ArchivesHolder(View view) {
            super(view);
            backGroundView = view.findViewById(R.id.single_item_archives_backGroundView);
            foreGroundView = view.findViewById(R.id.single_item_archive_foreGroundView);
            scaleTextView = view.findViewById(R.id.single_item_archives_scale_textView);
            serialTextView = view.findViewById(R.id.single_item_archives_serial_textView);
            statusTextView = view.findViewById(R.id.single_item_archives_status_textView);
            statusImageView = view.findViewById(R.id.single_item_archives_status_imageView);
            continueTextView = view.findViewById(R.id.single_item_archives_continue_textView);
            referenceHintTextView = view.findViewById(R.id.single_item_archives_reference_hint_textView);
            referenceHintImageView = view.findViewById(R.id.single_item_archives_reference_hint_imageView);
            referenceTextView = view.findViewById(R.id.single_item_archives_reference_textView);
            caseTextView = view.findViewById(R.id.single_item_archives_case_textView);
            roomTextView = view.findViewById(R.id.single_item_archives_room_textView);
            referenceLinearLayout = view.findViewById(R.id.single_item_archives_reference_linearLayout);
            caseLinearLayout = view.findViewById(R.id.single_item_archives_case_linearLayout);
            roomLinearLayout = view.findViewById(R.id.single_item_archives_room_linearLayout);
        }
    }

}