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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ArchiveHolder> {

    // Vars
    private ArrayList<Model> archives;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private TextView continueDialogTitle, continueDialogDescription, continueDialogPositive, continueDialogNegative;
    private Dialog continueDialog;

    public ArchiveAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArchiveHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_archive_single_item, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return new ArchiveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchiveHolder holder, int i) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_snow_ripple);
        }

        try {
            holder.serialTextView.setText(archives.get(i).get("serial").toString());
            holder.statusTextView.setText(archives.get(i).get("status").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);
            continueDialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return archives.size();
    }

    private void initializer(View view) {
        handler = new Handler();

        continueDialog = new Dialog(activity, R.style.DialogTheme);
        continueDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        continueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        continueDialog.setContentView(R.layout.dialog_action);
        continueDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(continueDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        continueDialog.getWindow().setAttributes(layoutParams);

        continueDialogTitle = continueDialog.findViewById(R.id.dialog_action_title_textView);
        continueDialogTitle.setText(activity.getResources().getString(R.string.ArchiveContinueDialogTitle));
        continueDialogDescription = continueDialog.findViewById(R.id.dialog_action_description_textView);
        continueDialogDescription.setText(activity.getResources().getString(R.string.ArchiveContinueDialogDescription));
        continueDialogPositive = continueDialog.findViewById(R.id.dialog_action_positive_textView);
        continueDialogPositive.setText(activity.getResources().getString(R.string.ArchiveContinueDialogPositive));
        continueDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        continueDialogNegative = continueDialog.findViewById(R.id.dialog_action_negative_textView);
        continueDialogNegative.setText(activity.getResources().getString(R.string.ArchiveContinueDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            continueDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            continueDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        continueDialogPositive.setOnClickListener(v -> {
            continueDialogPositive.setClickable(false);
            handler.postDelayed(() -> continueDialogPositive.setClickable(true), 1000);
            continueDialog.dismiss();

            doWork();
        });

        continueDialogNegative.setOnClickListener(v -> {
            continueDialogNegative.setClickable(false);
            handler.postDelayed(() -> continueDialogNegative.setClickable(true), 1000);
            continueDialog.dismiss();
        });

        continueDialog.setOnCancelListener(dialog -> continueDialog.dismiss());
    }

    public void setArchive(ArrayList<Model> archives) {
        this.archives = archives;
        notifyDataSetChanged();
    }

    private void doWork() {

    }

    public class ArchiveHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLinearLayout;
        public TextView serialTextView, statusTextView, continueTextView;

        public ArchiveHolder(View view) {
            super(view);
            rootLinearLayout = view.findViewById(R.id.activity_archive_single_item_root_linearLayout);
            serialTextView = view.findViewById(R.id.activity_archive_single_item_serial_textView);
            statusTextView = view.findViewById(R.id.activity_archive_single_item_status_textView);
            continueTextView = view.findViewById(R.id.activity_archive_single_item_continue_textView);
        }
    }

}
