package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.ViewModels.ExplodeViewModel;
import com.majazeh.risloo.Views.Activities.AboutUsActivity;
import com.majazeh.risloo.Views.Activities.CallUsActivity;
import com.majazeh.risloo.Views.Activities.SettingActivity;
import com.majazeh.risloo.Views.Activities.QuestionActivity;
import com.majazeh.risloo.Views.Dialogs.SocialDialog;
import com.majazeh.risloo.Views.Activities.TermConditionActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MoreHolder> {

    // ViewModels
    private ExplodeViewModel viewModel;

    // Vars
    private ArrayList<Model> settings;

    // Objects
    private Activity activity;
    private Handler handler;
    private SocialDialog socialDialog;

    // Widgets
    private Dialog noUpdateDialog, availableUpdateDialog;
    private TextView noUpdateDialogTitle, noUpdateDialogDescription, noUpdateDialogConfirm, availableUpdateDialogTitle, availableUpdateDialogDescription, availableUpdateDialogPositive, availableUpdateDialogNegative;

    public SettingAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_setting, viewGroup, false);

        initializer(view);

        return new MoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreHolder holder, int i) {
        Model model = settings.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_white_ripple_quartz);
            }

            if (i != settings.size() - 1) {
                holder.titleTextView.setText(model.get("title").toString());
            } else {
                if (viewModel.hasUpdate()) {
                    holder.titleTextView.setText(activity.getResources().getString(R.string.SettingUpdate));
                    holder.updateTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.titleTextView.setText(currentVersion());
                    holder.updateTextView.setVisibility(View.INVISIBLE);
                }

                holder.lineView.setVisibility(View.GONE);
            }

            holder.avatarImageView.setImageDrawable((Drawable) model.get("image"));
            holder.avatarImageView.setBackground((Drawable) model.get("drawable"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

            doWork(i);
        });

    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    private void initializer(View view) {
        viewModel = new ViewModelProvider((FragmentActivity) activity).get(ExplodeViewModel.class);

        socialDialog = new SocialDialog(activity);

        handler = new Handler();
    }

    public void setSetting(ArrayList<Model> settings) {
        this.settings = settings;
        notifyDataSetChanged();
    }

    private void doWork(int position) {
        switch (position) {
            case 0:
                activity.startActivity(new Intent(activity, AboutUsActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 1:
                activity.startActivity(new Intent(activity, QuestionActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 2:
                activity.startActivity(new Intent(activity, CallUsActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 3:
                activity.startActivity(new Intent(activity, TermConditionActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 4:
                socialDialog.show(((SettingActivity)activity).getSupportFragmentManager(), "socialBottomSheet");
                break;
            case 5:
                IntentManager.share(activity, activity.getResources().getString(R.string.SettingShareLink), activity.getResources().getString(R.string.SettingShareChooser));
                break;
            case 6:
                IntentManager.googlePlay(activity);
                break;
            case 7:
                initDialog();

                detector();

                listener();

                if (viewModel.hasUpdate()) {
                    availableUpdateDialogTitle.setText(newVersion());
                    availableUpdateDialog.show();
                } else {
                    noUpdateDialogTitle.setText(currentVersion());
                    noUpdateDialog.show();
                }
                break;
        }
    }

    private void initDialog() {
        noUpdateDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(noUpdateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        noUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noUpdateDialog.setContentView(R.layout.dialog_note);
        noUpdateDialog.setCancelable(true);

        availableUpdateDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(availableUpdateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        availableUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        availableUpdateDialog.setContentView(R.layout.dialog_action);
        availableUpdateDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams1 = new WindowManager.LayoutParams();
        layoutParams1.copyFrom(noUpdateDialog.getWindow().getAttributes());
        layoutParams1.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        noUpdateDialog.getWindow().setAttributes(layoutParams1);

        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(availableUpdateDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        availableUpdateDialog.getWindow().setAttributes(layoutParams2);

        noUpdateDialogTitle = noUpdateDialog.findViewById(R.id.dialog_note_title_textView);
        noUpdateDialogDescription = noUpdateDialog.findViewById(R.id.dialog_note_description_textView);
        noUpdateDialogDescription.setText(activity.getResources().getString(R.string.SettingNoUpdateDialogDescription));
        noUpdateDialogConfirm = noUpdateDialog.findViewById(R.id.dialog_note_confirm_textView);
        noUpdateDialogConfirm.setText(activity.getResources().getString(R.string.SettingNoUpdateDialogConfirm));

        availableUpdateDialogTitle = availableUpdateDialog.findViewById(R.id.dialog_action_title_textView);
        availableUpdateDialogDescription = availableUpdateDialog.findViewById(R.id.dialog_action_description_textView);
        availableUpdateDialogDescription.setText(activity.getResources().getString(R.string.SettingAvailableUpdateDialogDescription));
        availableUpdateDialogPositive = availableUpdateDialog.findViewById(R.id.dialog_action_positive_textView);
        availableUpdateDialogPositive.setText(activity.getResources().getString(R.string.SettingAvailableUpdateDialogPositive));
        availableUpdateDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        availableUpdateDialogNegative = availableUpdateDialog.findViewById(R.id.dialog_action_negative_textView);
        availableUpdateDialogNegative.setText(activity.getResources().getString(R.string.SettingAvailableUpdateDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            noUpdateDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);

            availableUpdateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            availableUpdateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        availableUpdateDialogPositive.setOnClickListener(v -> {
            availableUpdateDialogPositive.setClickable(false);
            handler.postDelayed(() -> availableUpdateDialogPositive.setClickable(true), 300);
            availableUpdateDialog.dismiss();

            IntentManager.googlePlay(activity);
        });

        availableUpdateDialogNegative.setOnClickListener(v -> {
            availableUpdateDialogNegative.setClickable(false);
            handler.postDelayed(() -> availableUpdateDialogNegative.setClickable(true), 300);
            availableUpdateDialog.dismiss();
        });

        noUpdateDialogConfirm.setOnClickListener(v -> {
            noUpdateDialogConfirm.setClickable(false);
            handler.postDelayed(() -> noUpdateDialogConfirm.setClickable(true), 300);
            noUpdateDialog.dismiss();
        });

        noUpdateDialog.setOnCancelListener(dialog -> noUpdateDialog.dismiss());

        availableUpdateDialog.setOnCancelListener(dialog -> availableUpdateDialog.dismiss());
    }

    private String currentVersion() {
        return activity.getResources().getString(R.string.SettingVersion) + " " + viewModel.currentVersion();
    }

    private String newVersion() {
        return activity.getResources().getString(R.string.SettingVersion) + " " + viewModel.newVersion() + " " + activity.getResources().getString(R.string.SettingArrived);
    }

    public class MoreHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public TextView titleTextView, updateTextView;
        public View lineView;

        public MoreHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.single_item_setting_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_setting_title_textView);
            updateTextView = view.findViewById(R.id.single_item_setting_update_textView);
            lineView = view.findViewById(R.id.single_item_setting_line_view);
        }
    }

}