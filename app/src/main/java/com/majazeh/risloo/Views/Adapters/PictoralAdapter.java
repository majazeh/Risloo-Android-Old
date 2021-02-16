package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictoralAdapter extends RecyclerView.Adapter<PictoralAdapter.PictoralHolder> {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private int position = -1;
    private boolean clicked;
    private ArrayList<String> answers;

    // Objects
    private Activity activity;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PictoralAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PictoralHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_pictoral, viewGroup, false);

        initializer(view);

        return new PictoralHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PictoralHolder holder, int i) {
        String answer = answers.get(i);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_quartz_ripple_solitude);
        }

        Picasso.get().load(answer).placeholder(R.color.Gray50).into(holder.answerImageView);

        if (position == -1) {
            holder.answerImageView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

            holder.itemView.setEnabled(true);
            holder.itemView.setClickable(true);
        } else {
            if (position == i + 1) {
                holder.answerImageView.setBackgroundResource(R.drawable.draw_16sdp_border_primary);

                if (clicked) {
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.itemView.setEnabled(true);
                    holder.itemView.setClickable(true);
                }
            } else {
                if (clicked) {
                    holder.answerImageView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

                    holder.itemView.setAlpha((float) 0.3);
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.answerImageView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

                    holder.itemView.setAlpha(1);
                    holder.itemView.setEnabled(true);
                    holder.itemView.setClickable(true);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            handler.postDelayed(() -> doWork(i), 500);

            position = i + 1;
            clicked = true;

            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();
    }

    public void setAnswer(ArrayList<String> answers, int position, SampleViewModel viewModel) {
        this.answers = answers;
        this.position = position;
        this.viewModel = viewModel;
        notifyDataSetChanged();
    }

    private void doWork(int position) {

    }

    public class PictoralHolder extends RecyclerView.ViewHolder {

        public ImageView answerImageView;

        public PictoralHolder(View view) {
            super(view);
            answerImageView = view.findViewById(R.id.single_item_pictoral_answer_imageView);
        }
    }

}