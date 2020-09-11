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

import java.util.ArrayList;

public class TFPAdapter extends RecyclerView.Adapter<TFPAdapter.TFPHolder> {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private int position = -1;
    private boolean clickedItem;
    private ArrayList<String> answers;

    // Objects
    private Activity activity;
    private Handler handler;
    private SharedPreferences sharedPreferences;

    public TFPAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public TFPHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_fp_single_item, viewGroup, false);

        initializer(view);

        return new TFPHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TFPHolder holder, int i) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_quartz_ripple_solitude);
        }

        // TODO : Check State

        holder.itemView.setOnClickListener(v -> {
            handler.postDelayed(() -> doWork(i), 500);

            position = i;

            clickedItem = true;

            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

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

    public class TFPHolder extends RecyclerView.ViewHolder {

        public ImageView answerImageView;

        public TFPHolder(View view) {
            super(view);
            answerImageView = view.findViewById(R.id.fragment_fp_single_item_answer_imageView);
        }
    }

}