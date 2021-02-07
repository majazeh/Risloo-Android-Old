package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class FAQuestionAdapter extends RecyclerView.Adapter<FAQuestionAdapter.FAQuestionHolder> {

    // Vars
    private ArrayList<Model> faQuestions;
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;
    private Handler handler;

    public FAQuestionAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public FAQuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_fa_question, viewGroup, false);

        initializer(view);

        return new FAQuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQuestionHolder holder, int i) {
        Model model = faQuestions.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_snow_ripple_quartz);
            }

            if (model.get("important").equals(true)) {
                holder.subjectTextView.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.PrimaryDark, null));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));
            } else {
                holder.subjectTextView.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.Grey, null));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Grey));
            }

            holder.subjectTextView.setText((String) model.get("subject"));
            holder.answerTextView.setText((String) model.get("answer"));

            if (expands.get(i)) {
                holder.answerTextView.setVisibility(View.VISIBLE);
                holder.expandImageView.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_chevron_up, null));
            }else{
                holder.answerTextView.setVisibility(View.GONE);
                holder.expandImageView.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.ic_chevron_down, null));
            }

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                doWork(i);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return faQuestions.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void doWork(int position) {
        expands.put(position, !expands.get(position));
        notifyDataSetChanged();
    }

    public void setFAQuestions(ArrayList<Model> faQuestions, HashMap<Integer, Boolean> expands) {
        this.faQuestions = faQuestions;
        this.expands = expands;
        notifyDataSetChanged();
    }

    public class FAQuestionHolder extends RecyclerView.ViewHolder {

        public TextView subjectTextView, answerTextView;
        public ImageView expandImageView;

        public FAQuestionHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.subject_textView);
            answerTextView = view.findViewById(R.id.answer_textView);
            expandImageView = view.findViewById(R.id.expand_imageView);
        }
    }

}