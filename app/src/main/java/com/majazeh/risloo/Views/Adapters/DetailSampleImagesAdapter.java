package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsibbold.zoomage.ZoomageView;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;
import com.majazeh.risloo.Views.Activities.ImageActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class DetailSampleImagesAdapter extends RecyclerView.Adapter<DetailSampleImagesAdapter.DetailSampleImagesHolder> {

    // Vars
    private ArrayList<Model> images;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailSampleImagesAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailSampleImagesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_sample_images, viewGroup, false);

        initializer(view);

        return new DetailSampleImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailSampleImagesHolder holder, int i) {
        Model model = images.get(i);

        try {
            Picasso.get().load(model.get("url").toString()).placeholder(R.color.Solitude).into(holder.zoomImageView);

            holder.zoomImageView.setOnClickListener(v -> {
                holder.zoomImageView.setClickable(false);
                handler.postDelayed(() -> holder.zoomImageView.setClickable(true), 250);

                try {
                    Intent intent = (new Intent(activity, ImageActivity.class));

                    intent.putExtra("title", ((DetailSampleActivity) Objects.requireNonNull(activity)).scaleTitle);
                    intent.putExtra("bitmap", false);
                    intent.putExtra("image", model.get("url").toString());

                    activity.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setImage(ArrayList<Model> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public class DetailSampleImagesHolder extends RecyclerView.ViewHolder {

        public ZoomageView zoomImageView;

        public DetailSampleImagesHolder(View view) {
            super(view);
            zoomImageView = view.findViewById(R.id.single_item_detail_sample_images);
        }
    }

}