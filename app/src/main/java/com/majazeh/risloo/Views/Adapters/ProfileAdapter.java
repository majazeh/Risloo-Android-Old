package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Profile;
import com.majazeh.risloo.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {

    // Vars
    private ArrayList<Profile> profiles;

    // Objects
    private Activity activity;
    private Handler handler;

    public ProfileAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_profile_single_item, viewGroup, false);

        initializer(view);

        return new ProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int i) {

        // TODO : Fill Here

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setProfile(ArrayList<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    public class ProfileHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public TextView titleTextView, subTitleTextView;

        public ProfileHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.activity_profile_single_item_avatar_imageView);
            titleTextView = view.findViewById(R.id.activity_profile_single_item_title_textView);
            subTitleTextView = view.findViewById(R.id.activity_profile_single_item_subTitle_textView);
        }
    }

}