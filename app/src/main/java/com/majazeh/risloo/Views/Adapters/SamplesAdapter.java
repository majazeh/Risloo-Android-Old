package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SamplesAdapter extends RecyclerView.Adapter<SamplesAdapter.SamplesHolder> {

    // Vars
    private ArrayList<Model> samples;

    // Objects
    private Activity activity;
    private Handler handler;

    public SamplesAdapter(Activity activity) {
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

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_24sdp_white_ripple);
        }

        try {
            holder.serialTextView.setText(samples.get(i).get("id").toString());
            holder.statusTextView.setText(samples.get(i).get("status").toString());

            JSONObject scale = (JSONObject) samples.get(i).get("scale");

            holder.scaleTextView.setText(scale.getString("title"));

            holder.referenceTextView.setText("احمد مهرانی");
            holder.caseTextView.setText("RS966666W");
            holder.roomTextView.setText("دکتر جهانیان");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);
        });

    }

    @Override
    public int getItemCount() {
        return samples.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setSamples(ArrayList<Model> samples) {
        this.samples = samples;
        notifyDataSetChanged();
    }

    public class SamplesHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, scaleTextView, statusTextView, referenceTextView, caseTextView, roomTextView;

        public SamplesHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_samples_serial_textView);
            scaleTextView = view.findViewById(R.id.single_item_samples_scale_textView);
            statusTextView = view.findViewById(R.id.single_item_samples_status_textView);
            referenceTextView = view.findViewById(R.id.single_item_samples_reference_textView);
            caseTextView = view.findViewById(R.id.single_item_samples_case_textView);
            roomTextView = view.findViewById(R.id.single_item_samples_room_textView);
        }
    }

}