package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
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

    public ArchiveAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArchiveHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_archive_single_item, viewGroup, false);

        initializer(view);

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
    }

    @Override
    public int getItemCount() {
        return archives.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setArchive(ArrayList<Model> archives) {
        this.archives = archives;
        notifyDataSetChanged();
    }

    public class ArchiveHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLinearLayout;
        public TextView serialTextView, dateTextView, statusTextView;
        public ImageView avatarImageView;

        public ArchiveHolder(View view) {
            super(view);
            rootLinearLayout = view.findViewById(R.id.activity_archive_single_item_root_linearLayout);
            serialTextView = view.findViewById(R.id.activity_archive_single_item_serial_textView);
            dateTextView = view.findViewById(R.id.activity_archive_single_item_date_textView);
            statusTextView = view.findViewById(R.id.activity_archive_single_item_status_textView);
            avatarImageView = view.findViewById(R.id.activity_archive_single_item_avatar_imageView);
        }
    }

}
