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

public class ZoomageAdapter extends RecyclerView.Adapter<ZoomageAdapter.ZoomageHolder> {

    // Vars
    private ArrayList<Model> zoomages;

    // Objects
    private Activity activity;
    private Handler handler;

    public ZoomageAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ZoomageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_zoomage, viewGroup, false);

        initializer(view);

        return new ZoomageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoomageHolder holder, int i) {
        Model model = zoomages.get(i);

        try {
            Picasso.get().load(model.get("url").toString()).placeholder(R.color.Solitude).into(holder.zoomageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.zoomageView.setOnClickListener(v -> {
            holder.zoomageView.setClickable(false);
            handler.postDelayed(() -> holder.zoomageView.setClickable(true), 300);

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
    }

    @Override
    public int getItemCount() {
        return zoomages.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setZoomages(ArrayList<Model> zoomages) {
        this.zoomages = zoomages;
        notifyDataSetChanged();
    }

    public class ZoomageHolder extends RecyclerView.ViewHolder {

        public ZoomageView zoomageView;

        public ZoomageHolder(View view) {
            super(view);
            zoomageView = view.findViewById(R.id.single_item_zoomage);
        }
    }

}