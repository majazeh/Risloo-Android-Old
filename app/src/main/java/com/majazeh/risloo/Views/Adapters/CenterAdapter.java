package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterHolder> {

    // Vars
    private int position = -1;
    private ArrayList<Model> centers;
    private ArrayList<Boolean> expandedCenters = new ArrayList<>();

    // Objects
    private Activity activity;
    private Handler handler;

    public CenterAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_center_single_item, viewGroup, false);

        initializer(view);

        return new CenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int i) {

        holder.itemView.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.requestTextView.setBackgroundResource(R.drawable.draw_4sdp_primary_ripple);
            holder.editImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
            holder.peopleImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
        }

        expandedCenters.add(false);

        if (position == -1) {
            holder.expandLinearLayout.setVisibility(View.GONE);
        } else {
            if (position == i) {
                if (!expandedCenters.get(i)){
                    holder.expandLinearLayout.setVisibility(View.VISIBLE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_up));

                    expandedCenters.set(i, true);
                } else {
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            } else {
                if (!expandedCenters.get(i)){
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            position = i;

            notifyDataSetChanged();
        });

        holder.requestTextView.setOnClickListener(v -> {
            holder.requestTextView.setClickable(false);
            handler.postDelayed(() -> holder.requestTextView.setClickable(true), 1000);


        });

        holder.editImageView.setOnClickListener(v -> {
            holder.editImageView.setClickable(false);
            handler.postDelayed(() -> holder.editImageView.setClickable(true), 1000);

            activity.startActivityForResult(new Intent(activity, EditCenterActivity.class), 100);
            activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        holder.peopleImageView.setOnClickListener(v -> {
            holder.peopleImageView.setClickable(false);
            handler.postDelayed(() -> holder.peopleImageView.setClickable(true), 1000);


        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setCenter(ArrayList<Model> centers) {
        this.centers = centers;
        notifyDataSetChanged();
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, requestTextView, descriptionTextView;
        public ImageView editImageView, peopleImageView, expandImageView;
        public LinearLayout expandLinearLayout;

        public CenterHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.activity_center_single_item_avatar_imageView);
            titleTextView = view.findViewById(R.id.activity_center_single_item_title_textView);
            descriptionTextView = view.findViewById(R.id.activity_center_single_item_description_textView);
            requestTextView = view.findViewById(R.id.activity_center_single_item_request_textView);
            editImageView = view.findViewById(R.id.activity_center_single_item_edit_imageView);
            peopleImageView = view.findViewById(R.id.activity_center_single_item_people_imageView);
            expandImageView = view.findViewById(R.id.activity_center_single_item_expand_imageView);
            expandLinearLayout = view.findViewById(R.id.activity_center_single_item_expand_linearLayout);
         }
    }

}