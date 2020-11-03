package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Context;
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
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.SampleActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class OptionalAdapter extends RecyclerView.Adapter<OptionalAdapter.OptionalHolder> {

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

    public OptionalAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public OptionalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_optional, viewGroup, false);

        initializer(view);

        return new OptionalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionalHolder holder, int i) {
        String answer = answers.get(i);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_white_border_quartz_ripple_solitude);
        }

        holder.answerTextView.setText(answer);

        if (position == -1) {
            holder.numberTextView.setText("");
            holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_solitude);

            holder.itemView.setEnabled(true);
            holder.itemView.setClickable(true);
        } else {
            if (position == i + 1) {
                holder.numberTextView.setText(String.valueOf(i + 1));
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_primary);

                if (clicked) {
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.itemView.setEnabled(true);
                    holder.itemView.setClickable(true);
                }
            } else {
                if (clicked) {
                    holder.numberTextView.setText("");
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_solitude);

                    holder.itemView.setAlpha((float) 0.3);
                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);
                } else {
                    holder.numberTextView.setText("");
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_snow);

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
        try {
            JSONArray jsonArray = FileManager.readArrayFromCache(activity, "Answers" + "/" + sharedPreferences.getString("sampleId", ""));

            jsonArray.getJSONObject(viewModel.getIndex()).put("index", viewModel.getIndex());
            jsonArray.getJSONObject(viewModel.getIndex()).put("answer", position + 1);

            FileManager.writeArrayToCache(activity, jsonArray, "Answers" + "/" + sharedPreferences.getString("sampleId", ""));


            if (viewModel.getNext() == null) {
                if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) == -1) {
                    viewModel.insertToLocal(viewModel.getIndex() + 1, position + 1);
                    viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));

                    ((SampleActivity) Objects.requireNonNull(activity)).setProgress();

                    ((SampleActivity) Objects.requireNonNull(activity)).closeDialog.show();
                    return;
                }
                viewModel.setIndex(viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")));
            }

            viewModel.insertToLocal(viewModel.getIndex(), position + 1);
            viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));

            ((SampleActivity) Objects.requireNonNull(activity)).showFragment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class OptionalHolder extends RecyclerView.ViewHolder {

        public TextView numberTextView, answerTextView;

        public OptionalHolder(View view) {
            super(view);
            numberTextView = view.findViewById(R.id.single_item_optional_number_textView);
            answerTextView = view.findViewById(R.id.single_item_optional_answer_textView);
        }
    }

}