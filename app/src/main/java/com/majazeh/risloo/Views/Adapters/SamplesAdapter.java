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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;
import com.majazeh.risloo.Views.Activities.SamplesActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SamplesAdapter extends RecyclerView.Adapter<SamplesAdapter.SamplesHolder> {

    // Vars
    private ArrayList<Model> samples;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private Dialog startDialog;
    private TextView startDialogTitle, startDialogDescription, startDialogPositive, startDialogNegative;

    public SamplesAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SamplesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_samples, viewGroup, false);

        initializer(view);

        return new SamplesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SamplesHolder holder, int i) {
        Model model = samples.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.startTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
                holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_snow_ripple_quartz);
            }

            Intent detailSampleIntent = (new Intent(activity, DetailSampleActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id") && !model.attributes.get("id").equals("")) {
                detailSampleIntent.putExtra("id", model.get("id").toString());

                holder.serialTextView.setText(model.get("id").toString());
            }

            // Scale
            if (model.attributes.has("scale") && !model.attributes.isNull("scale") && !model.attributes.get("scale").equals("")) {
                JSONObject scale = (JSONObject) model.get("scale");

                holder.scaleTextView.setText(scale.get("title").toString());
            }

            // Version
            if (model.attributes.has("version") && !model.attributes.isNull("version") && !model.attributes.get("version").equals("")) {
                holder.scaleTextView.append(" " + model.get("version").toString());
            }

            // Edition
            if (model.attributes.has("edition") && !model.attributes.isNull("edition") && !model.attributes.get("edition").equals("")) {
                holder.scaleTextView.append(" " + model.get("edition").toString());
            }

            // Status
            if (model.attributes.has("status") && !model.attributes.isNull("status") && !model.attributes.get("status").equals("")) {
                switch (model.get("status").toString()) {
                    case "seald":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusSeald));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));

                        if (((SamplesActivity) Objects.requireNonNull(activity)).authViewModel.openSample(model)) {
                            holder.startTextView.setVisibility(View.VISIBLE);
                        } else {
                            holder.startTextView.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case "open":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusOpen));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));

                        if (((SamplesActivity) Objects.requireNonNull(activity)).authViewModel.openSample(model)) {
                            holder.startTextView.setVisibility(View.VISIBLE);
                        } else {
                            holder.startTextView.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case "closed":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusClosed));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));

                        holder.startTextView.setVisibility(View.INVISIBLE);
                        break;

                    case "scoring":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusScoring));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.MoonYellow));

                        holder.startTextView.setVisibility(View.INVISIBLE);
                        break;

                    case "craeting_files":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusCreatingFiles));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.MoonYellow));

                        holder.startTextView.setVisibility(View.INVISIBLE);
                        break;

                    case "done":
                        holder.statusTextView.setText(activity.getResources().getString(R.string.SamplesStatusDone));
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));

                        holder.startTextView.setVisibility(View.INVISIBLE);
                        break;

                    default:
                        holder.statusTextView.setText(model.get("status").toString());
                        holder.statusTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(holder.statusImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));

                        holder.startTextView.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            // Reference
            if (model.attributes.has("client") && !model.attributes.isNull("client") && !model.attributes.get("client").equals("")) {
                JSONObject client = (JSONObject) model.get("client");

                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.SamplesReference));
                holder.referenceHintImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_user_light));

                holder.referenceTextView.setText(client.get("name").toString());
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else if (model.attributes.has("code") && !model.attributes.isNull("code")) {
                holder.referenceHintTextView.setText(activity.getResources().getString(R.string.SamplesCode));
                holder.referenceHintImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_hashtag_light));

                holder.referenceTextView.setText(model.get("code").toString());
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            // Case
            if (model.attributes.has("case") && !model.attributes.isNull("case") && !model.attributes.get("case").equals("")) {
                JSONObject casse = (JSONObject) model.get("case");

                holder.caseTextView.setText(casse.get("id").toString());
                holder.caseLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.caseLinearLayout.setVisibility(View.GONE);
            }

            // Room
            if (model.attributes.has("room") && !model.attributes.isNull("room") && !model.attributes.get("room").equals("")) {
                JSONObject room = (JSONObject) model.get("room");

                JSONObject manager = (JSONObject) room.get("manager");

                holder.roomTitleTextView.setText(manager.get("name").toString());

                JSONObject center = (JSONObject) room.get("center");
                JSONObject detail = (JSONObject) center.get("detail");

                holder.roomTypeTextView.setText(detail.get("title").toString());

                // Avatar
                if (manager.has("avatar") && !manager.isNull("avatar") && manager.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                    JSONObject avatar = manager.getJSONObject("avatar");
                    JSONObject medium = avatar.getJSONObject("medium");

                    Picasso.get().load(medium.get("url").toString()).placeholder(R.color.Solitude).into(holder.roomAvatarImageView);

                    holder.roomSubTitleTextView.setVisibility(View.GONE);
                } else {
                    Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(holder.roomAvatarImageView);

                    holder.roomSubTitleTextView.setVisibility(View.VISIBLE);
                    holder.roomSubTitleTextView.setText(StringManager.firstChars(holder.roomTitleTextView.getText().toString()));
                }

                holder.roomLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.roomLinearLayout.setVisibility(View.GONE);
            }

            // Sample Access
            if (((SamplesActivity) Objects.requireNonNull(activity)).authViewModel.openSample(model)) {
                holder.startTextView.setVisibility(View.VISIBLE);
            } else {
                holder.startTextView.setVisibility(View.GONE);
            }

            holder.startTextView.setOnClickListener(v -> {
                holder.startTextView.setClickable(false);
                handler.postDelayed(() -> holder.startTextView.setClickable(true), 250);

                showDialog(holder.serialTextView.getText().toString());
            });

            // Sample Detail Access
            if (((SamplesActivity) Objects.requireNonNull(activity)).authViewModel.openDetailSample(model)) {
                holder.itemView.setEnabled(true);
            } else {
                holder.itemView.setEnabled(false);
            }

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(detailSampleIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void showDialog(String sampleId) {
        initDialog();

        detector();

        listener(sampleId);

        startDialog.show();
    }

    private void initDialog() {
        startDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(startDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        startDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        startDialog.setContentView(R.layout.dialog_action);
        startDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsStartDialog = new WindowManager.LayoutParams();
        layoutParamsStartDialog.copyFrom(startDialog.getWindow().getAttributes());
        layoutParamsStartDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsStartDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        startDialog.getWindow().setAttributes(layoutParamsStartDialog);

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

    private void listener(String sampleId) {
        startDialogPositive.setOnClickListener(v -> {
            startDialogPositive.setClickable(false);
            handler.postDelayed(() -> startDialogPositive.setClickable(true), 250);
            startDialog.dismiss();

            doWork(sampleId);
        });

        startDialogNegative.setOnClickListener(v -> {
            startDialogNegative.setClickable(false);
            handler.postDelayed(() -> startDialogNegative.setClickable(true), 250);
            startDialog.dismiss();
        });

        startDialog.setOnCancelListener(dialog -> startDialog.dismiss());
    }

    private void doWork(String sampleId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();

        editor.putString("sampleId", sampleId);
        editor.apply();

        clearProgress();

        activity.startActivityForResult(new Intent(activity, SampleActivity.class), 100);
    }

    public void setSample(ArrayList<Model> samples) {
        this.samples = samples;
        notifyDataSetChanged();
    }

    public void clearProgress() {
        if (((SamplesActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((SamplesActivity) Objects.requireNonNull(activity)).loading = false;
            ((SamplesActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public class SamplesHolder extends RecyclerView.ViewHolder {

        public TextView scaleTextView, serialTextView, roomTitleTextView, roomSubTitleTextView, roomTypeTextView, statusTextView, startTextView, referenceHintTextView, referenceTextView, caseTextView;
        public ImageView roomAvatarImageView, statusImageView, referenceHintImageView;
        public LinearLayout referenceLinearLayout, caseLinearLayout, roomLinearLayout;

        public SamplesHolder(View view) {
            super(view);
            scaleTextView = view.findViewById(R.id.single_item_samples_scale_textView);
            serialTextView = view.findViewById(R.id.single_item_samples_serial_textView);
            roomAvatarImageView = view.findViewById(R.id.single_item_samples_room_avatar_imageView);
            roomTitleTextView = view.findViewById(R.id.single_item_samples_room_title_textView);
            roomSubTitleTextView = view.findViewById(R.id.single_item_samples_room_subtitle_textView);
            roomTypeTextView = view.findViewById(R.id.single_item_samples_room_type_textView);
            statusTextView = view.findViewById(R.id.single_item_samples_status_textView);
            statusImageView = view.findViewById(R.id.single_item_samples_status_imageView);
            startTextView = view.findViewById(R.id.single_item_samples_start_textView);
            referenceHintTextView = view.findViewById(R.id.single_item_samples_reference_hint_textView);
            referenceHintImageView = view.findViewById(R.id.single_item_samples_reference_hint_imageView);
            referenceTextView = view.findViewById(R.id.single_item_samples_reference_textView);
            caseTextView = view.findViewById(R.id.single_item_samples_case_textView);
            referenceLinearLayout = view.findViewById(R.id.single_item_samples_reference_linearLayout);
            caseLinearLayout = view.findViewById(R.id.single_item_samples_case_linearLayout);
            roomLinearLayout = view.findViewById(R.id.single_item_samples_room_linearLayout);
        }
    }

}