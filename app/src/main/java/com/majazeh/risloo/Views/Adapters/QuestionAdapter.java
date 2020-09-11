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
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    // Vars
    private ArrayList<Model> questions;
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;
    private Handler handler;

    public QuestionAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_question, viewGroup, false);

        initializer(view);

        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int i) {

        try {
            if (questions.get(i).get("important").equals(true)) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_quartz_ripple_solitude);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                holder.subjectTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.PrimaryDark));
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_quartz_ripple_solitude);
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                holder.subjectTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Grey));
            }

            holder.subjectTextView.setText(questions.get(i).get("subject").toString());
            holder.answerTextView.setText(questions.get(i).get("answer").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (expands.get(i)){
            holder.answerTextView.setVisibility(View.VISIBLE);
            holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_up));
        }else{
            holder.answerTextView.setVisibility(View.GONE);
            holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);

            if (expands.get(i)){
                expands.put(i, false);
            }else{
                expands.put(i, true);
            }

            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setQuestion(ArrayList<Model> questions, HashMap<Integer, Boolean> expands) {
        this.questions = questions;
        this.expands = expands;
        notifyDataSetChanged();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        public TextView subjectTextView, answerTextView;
        public ImageView expandImageView;

        public QuestionHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.single_item_question_subject_textView);
            answerTextView = view.findViewById(R.id.single_item_question_answer_textView);
            expandImageView = view.findViewById(R.id.single_item_question_expand_imageView);
        }
    }

}