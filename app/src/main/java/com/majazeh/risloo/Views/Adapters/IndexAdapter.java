package com.majazeh.risloo.Views.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import com.majazeh.risloo.Views.Activities.SampleActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexHolder> {

    // Vars
    private ArrayList<String> indexes;
    private ArrayList<String> answers;
    private SampleViewModel viewModel;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private Dialog dialog;

    public IndexAdapter(Activity activity, SampleViewModel viewModel, Dialog dialog) {
        this.activity = activity;
        this.viewModel = viewModel;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public IndexHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_navigate_single_item, viewGroup, false);

        initializer(view);

        return new IndexHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IndexHolder holder, int i) {

        holder.numberTextView.setText(String.valueOf(Integer.parseInt(indexes.get(i))+1));

        if (viewModel.getCurrentIndex() == i){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow_border_primary_ripple);
            } else {
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow_border_primary);
            }

            holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.Nero));
        }else {
            if (!answers.get(i).equals("")) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_primary20p_ripple);
                } else {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_primary20p);
                }

                holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow_ripple_quartz);
                } else {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_snow);
                }

                holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);
            dialog.dismiss();

            viewModel.goToIndex(i);
            try {
                ((SampleActivity) Objects.requireNonNull(activity)).showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return indexes.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setIndex(JSONArray jsonArray) {
        ArrayList indexes = new ArrayList<Integer>();
        ArrayList answers = new ArrayList<Boolean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                indexes.add(jsonArray.getJSONObject(i).getString("index"));
                answers.add(jsonArray.getJSONObject(i).getString("answer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.indexes = indexes;
        this.answers = answers;
        notifyDataSetChanged();
    }

    public class IndexHolder extends RecyclerView.ViewHolder {

        public TextView numberTextView;

        public IndexHolder(View view) {
            super(view);
            numberTextView = view.findViewById(R.id.dialog_navigate_single_item_number_textView);
        }
    }

}