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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SamplesAdapter extends RecyclerView.Adapter<SamplesAdapter.SamplesHolder> {

    // Vars
    private int position = -1;
    private ArrayList<Model> samples;

    // Objects
    private Activity activity;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private Dialog startDialog;
    private TextView startDialogTitle, startDialogDescription, startDialogPositive, startDialogNegative;

    public SamplesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SamplesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_samples, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return new SamplesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SamplesHolder holder, int i) {
        Model model = samples.get(i);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_24sdp_solid_white_ripple_quartz);

            holder.startTextView.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }

        try {
            holder.serialTextView.setText(model.get("id").toString());

            JSONObject scale = (JSONObject) model.get("scale");

            holder.scaleTextView.setText(scale.get("title").toString());
            switch ((String) model.get("status")) {
                case "open":
                    holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusOpen));
                    holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    holder.startFrameLayout.setVisibility(View.VISIBLE);
                    break;
                case "seald":
                    holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusSeald));
                    holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    holder.startFrameLayout.setVisibility(View.VISIBLE);
                    break;
                case "scoring":
                    holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusScoring));
                    holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.MoonYellow));
                    holder.startFrameLayout.setVisibility(View.GONE);
                    break;
                case "closed":
                    holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusClosed));
                    holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
                    holder.startFrameLayout.setVisibility(View.GONE);
                    break;
                default:
                    holder.statusTextView.setText(model.get("status").toString());
                    holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
                    holder.startFrameLayout.setVisibility(View.GONE);
                    break;
            }

            if (!model.attributes.isNull("client")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.SamplesReference));
                holder.referenceTextView.setText(client.get("name").toString());
            } else {
                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.SamplesCode));
                holder.referenceTextView.setText(model.get("code").toString());
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

        holder.itemView.setOnClickListener(v -> {
            if (sharedPreferences.getString("access", "").equals("true")) {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 500);
                try {
                    activity.startActivity(new Intent(activity, DetailSampleActivity.class).putExtra("id", (String) model.get("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            }
        });

        holder.startTextView.setOnClickListener(v -> {
            holder.startTextView.setClickable(false);
            handler.postDelayed(() -> holder.startTextView.setClickable(true), 500);
            startDialog.show();

            position = i;
        });

    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        handler = new Handler();

        startDialog = new Dialog(activity, R.style.DialogTheme);
        startDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startDialog.setContentView(R.layout.dialog_action);
        startDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(startDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(layoutParams);

        startDialogTitle = startDialog.findViewById(R.id.dialog_action_title_textView);
        startDialogTitle.setText(activity.getResources().getString(R.string.SamplesStartDialogTitle));
        startDialogDescription = startDialog.findViewById(R.id.dialog_action_description_textView);
        startDialogDescription.setText(activity.getResources().getString(R.string.SamplesStartDialogDescription));
        startDialogPositive = startDialog.findViewById(R.id.dialog_action_positive_textView);
        startDialogPositive.setText(activity.getResources().getString(R.string.SamplesStartDialogPositive));
        startDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        startDialogNegative = startDialog.findViewById(R.id.dialog_action_negative_textView);
        startDialogNegative.setText(activity.getResources().getString(R.string.SamplesStartDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            startDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        startDialogPositive.setOnClickListener(v -> {
            startDialogPositive.setClickable(false);
            handler.postDelayed(() -> startDialogPositive.setClickable(true), 500);
            startDialog.dismiss();

            doWork(position);
        });

        startDialogNegative.setOnClickListener(v -> {
            startDialogNegative.setClickable(false);
            handler.postDelayed(() -> startDialogNegative.setClickable(true), 500);
            startDialog.dismiss();
        });

        startDialog.setOnCancelListener(dialog -> startDialog.dismiss());
    }

    public void setSamples(ArrayList<Model> samples) {
        this.samples = samples;
        notifyDataSetChanged();


    }

    private void doWork(int position) {
        try {
            editor.putString("sampleId", samples.get(position).get("id").toString());
            editor.apply();

            activity.startActivity(new Intent(activity, SampleActivity.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class SamplesHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, scaleTextView, statusTextView, startTextView, referenceHintTextView, referenceTextView, caseTextView, roomTextView;
        public LinearLayout referenceLinearLayout, caseLinearLayout, roomLinearLayout;
        public FrameLayout startFrameLayout;

        public SamplesHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_samples_serial_textView);
            scaleTextView = view.findViewById(R.id.single_item_samples_scale_textView);
            statusTextView = view.findViewById(R.id.single_item_samples_status_textView);
            startTextView = view.findViewById(R.id.single_item_samples_start_textView);
            referenceHintTextView = view.findViewById(R.id.single_item_samples_reference_hint_textView);
            referenceTextView = view.findViewById(R.id.single_item_samples_reference_textView);
            caseTextView = view.findViewById(R.id.single_item_samples_case_textView);
            roomTextView = view.findViewById(R.id.single_item_samples_room_textView);
            referenceLinearLayout = view.findViewById(R.id.single_item_samples_reference_linearLayout);
            caseLinearLayout = view.findViewById(R.id.single_item_samples_case_linearLayout);
            roomLinearLayout = view.findViewById(R.id.single_item_samples_room_linearLayout);
            startFrameLayout = view.findViewById(R.id.single_item_samples_start_frameLayout);
        }
    }

}