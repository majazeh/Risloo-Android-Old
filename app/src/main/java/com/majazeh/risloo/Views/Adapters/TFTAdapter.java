package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Sample;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;
import com.majazeh.risloo.Views.Ui.Activities.AuthActivity;
import com.majazeh.risloo.Views.Ui.Activities.SampleActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class TFTAdapter extends RecyclerView.Adapter<TFTAdapter.TFTHolder> {

    // Vars
    private ArrayList<String> answers;

    private SampleViewModel viewModel;
    // Objects
    private Activity activity;
    private Handler handler;

    private SharedPreferences sharedPreferences;

    public TFTAdapter(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
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
            holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_quartz_border_ripple);
        }
        holder.answerTextView.setText(answers.get(i).toString());


        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            doWork(i);
        });

    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    private void initializer(View view) {
        handler = new Handler();
        sharedPreferences = activity.getSharedPreferences("STORE", Context.MODE_PRIVATE);
    }

    public void setAnswer(ArrayList<String> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    private void doWork(int position) {
        try {

            JSONArray jsonArray = viewModel.readFromCache(sharedPreferences.getString("sampleId", ""));
            jsonArray.getJSONObject(viewModel.getCurrentIndex()).put("index", viewModel.getCurrentIndex());
            jsonArray.getJSONObject(viewModel.getCurrentIndex()).put("answer", position);
            viewModel.writeToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
            //////////////////////////////////////////////////////////////////////////////////////////////////
           if (viewModel.next() == null){
               activity.finish();
           }
            ((SampleActivity) Objects.requireNonNull(activity)).showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
            viewModel.insertToLocalData(viewModel.getCurrentIndex(), position);
            viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class TFTHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLinearLayout;
        public TextView numberTextView, answerTextView;

        public TFTHolder(View view) {
            super(view);
            rootLinearLayout = view.findViewById(R.id.fragment_ft_single_item_root_linearLayout);
            numberTextView = view.findViewById(R.id.fragment_ft_single_item_number_textView);
            answerTextView = view.findViewById(R.id.fragment_ft_single_item_answer_textView);
        }
    }

}