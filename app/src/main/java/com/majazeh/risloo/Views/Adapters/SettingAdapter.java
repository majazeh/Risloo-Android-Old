package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.SettingActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingHolder> {

    // Vars
    private ArrayList<Model> settings;

    // Objects
    private Activity activity;
    private Handler handler;

    public SettingAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SettingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_setting, viewGroup, false);

        initializer(view);

        return new SettingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingHolder holder, int i) {
        Model model = settings.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_white_ripple_quartz);
            }

            if (i != settings.size() - 1) {
                holder.titleTextView.setText((String) model.get("title"));
            } else {
                if (((SettingActivity) Objects.requireNonNull(activity)).explodeViewModel.hasUpdate()) {
                    holder.titleTextView.setText(activity.getResources().getString(R.string.SettingUpdate));
                    holder.updateTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.titleTextView.setText(((SettingActivity) Objects.requireNonNull(activity)).explodeViewModel.currentVersionFa());
                    holder.updateTextView.setVisibility(View.INVISIBLE);
                }

                holder.separatorView.setVisibility(View.GONE);
            }

            holder.avatarImageView.setImageDrawable((Drawable) model.get("image"));
            holder.avatarImageView.setBackground((Drawable) model.get("drawable"));

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                ((SettingActivity) Objects.requireNonNull(activity)).navigator(i);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setSettings(ArrayList<Model> settings) {
        this.settings = settings;
        notifyDataSetChanged();
    }

    public class SettingHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public TextView titleTextView, updateTextView;
        public View separatorView;

        public SettingHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.item_setting_avatar_imageView);
            titleTextView = view.findViewById(R.id.item_setting_title_textView);
            updateTextView = view.findViewById(R.id.item_setting_update_textView);
            separatorView = view.findViewById(R.id.item_setting_separator_view);
        }
    }

}