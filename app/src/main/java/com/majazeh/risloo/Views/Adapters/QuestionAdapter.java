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

import com.majazeh.risloo.Entities.Question;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {

    // Vars
    private int position = -1;
    private ArrayList<Question> questions;
    private ArrayList<Boolean> expandedQuestions = new ArrayList<>();

    // Objects
    private Activity activity;
    private Handler handler;

    public QuestionAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_question_single_item, viewGroup, false);

        initializer(view);

        return new QuestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int i) {

        try {
            if (questions.get(i).get("important").equals(true)) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_primary_ripple);
                } else {
                    holder.rootLinearLayout.setBackgroundResource(R.color.Primary);
                }
                holder.subjectTextView.setTextColor(activity.getResources().getColor(R.color.White));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.White));
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solitude_ripple);
                } else {
                    holder.rootLinearLayout.setBackgroundResource(R.color.Solitude);
                }
                holder.subjectTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Grey));
            }

            holder.subjectTextView.setText(questions.get(i).get("subject").toString());
            holder.answerTextView.setText(questions.get(i).get("answer").toString());

            expandedQuestions.add(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (position == -1) {
            holder.answerTextView.setVisibility(View.GONE);
        } else {
            if (position == i) {
                if (!expandedQuestions.get(i)){
                    holder.answerTextView.setVisibility(View.VISIBLE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_minus));

                    expandedQuestions.set(i, true);
                } else {
                    holder.answerTextView.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_plus));

                    expandedQuestions.set(i, false);
                }
            } else {
                if (!expandedQuestions.get(i)){
                    holder.answerTextView.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_plus));

                    expandedQuestions.set(i, false);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            position = i;

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

    public void setQuestion(ArrayList<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLinearLayout;
        public TextView subjectTextView, answerTextView;
        public ImageView expandImageView;

        public QuestionHolder(View view) {
            super(view);
            rootLinearLayout = view.findViewById(R.id.activity_question_single_item_root_linearLayout);
            subjectTextView = view.findViewById(R.id.activity_question_single_item_subject_textView);
            answerTextView = view.findViewById(R.id.activity_question_single_item_answer_textView);
            expandImageView = view.findViewById(R.id.activity_question_single_item_expand_imageView);
        }
    }

}