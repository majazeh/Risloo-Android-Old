package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.OutroActivity;
import com.majazeh.risloo.Views.Activities.SampleActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class TFTAdapter extends RecyclerView.Adapter<TFTAdapter.TFTHolder> {

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

    public TFTAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public TFTHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_ft_single_item, viewGroup, false);

        initializer(view);

        return new TFTHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TFTHolder holder, int i) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_18sdp_quartz_border_ripple);
        }

        holder.answerTextView.setText(answers.get(i));

        if (position == -1) {
            holder.numberTextView.setText("");
            holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solitude);

            holder.itemView.setEnabled(true);
            holder.itemView.setClickable(true);
        } else {
            if (position == i + 1) {
                holder.numberTextView.setText(String.valueOf(i + 1));
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow_border_primary);

                if (clickedItem) {
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.itemView.setEnabled(true);
                    holder.itemView.setClickable(true);
                }
            } else {
                if (clickedItem) {
                    holder.numberTextView.setText("");
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solitude);

                    holder.itemView.setAlpha((float) 0.3);
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.numberTextView.setText("");
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow);

                    holder.itemView.setAlpha(1);
                    holder.itemView.setEnabled(true);
                    holder.itemView.setClickable(true);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            handler.postDelayed(() -> doWork(i), 500);
            position = i + 1;

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
        try {
            JSONArray jsonArray = viewModel.readAnswerFromCache(sharedPreferences.getString("sampleId", ""));
            jsonArray.getJSONObject(viewModel.getIndex()).put("index", viewModel.getIndex());
            jsonArray.getJSONObject(viewModel.getIndex()).put("answer", position + 1);
            viewModel.saveAnswerToCache(jsonArray, sharedPreferences.getString("sampleId", ""));

            if (viewModel.getNext() == null) {
                if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) == -1) {
                    viewModel.insertToLocal(viewModel.getIndex() + 1, position + 1);
                    viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));
                    activity.startActivity(new Intent(activity, OutroActivity.class));
                    activity.finish();
                    return;
                }
                viewModel.setIndex(viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")));
            }
            try {
                ((SampleActivity) Objects.requireNonNull(activity)).showFragment();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            viewModel.insertToLocal(viewModel.getIndex(), position + 1);
            viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class TFTHolder extends RecyclerView.ViewHolder {

        public TextView numberTextView, answerTextView;

        public TFTHolder(View view) {
            super(view);
            numberTextView = view.findViewById(R.id.fragment_ft_single_item_number_textView);
            answerTextView = view.findViewById(R.id.fragment_ft_single_item_answer_textView);
        }
    }

}