package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Sample;
import com.majazeh.risloo.R;

import java.util.ArrayList;

public class PFPAdapter extends RecyclerView.Adapter<PFPAdapter.PFPHolder> {

    // Vars
    private ArrayList<Sample> answers;

    // Objects
    private Activity activity;
    private Handler handler;

    public PFPAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PFPHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_fp_single_item, viewGroup, false);

        initializer(view);

        return new PFPHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PFPHolder holder, int i) {

//        try {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//                holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_quartz_border_ripple);
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            doWork();
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setAnswer(ArrayList<Sample> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    private void doWork() {

    }

    public class PFPHolder extends RecyclerView.ViewHolder {

        public ImageView answerImageView;

        public PFPHolder(View view) {
            super(view);
            answerImageView = view.findViewById(R.id.fragment_fp_single_item_answer_imageView);
        }
    }

}