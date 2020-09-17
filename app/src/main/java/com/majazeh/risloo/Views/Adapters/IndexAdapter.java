package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private ArrayList<String> indexes;
    private ArrayList<String> answers;

    // Objects
    private Activity activity;
    private Handler handler;

    public IndexAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public IndexHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_index, viewGroup, false);

        initializer(view);

        return new IndexHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexHolder holder, int i) {

        holder.numberTextView.setText(String.valueOf(Integer.parseInt(indexes.get(i))+1));

        if (viewModel.getIndex() == i) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_primary_ripple_primary);
            } else {
                holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_primary);
            }

            holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.Nero));
        } else {
            if (!answers.get(i).equals("")) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_primary20p_ripple_primarydark);
                } else {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_primary20p);
                }

                holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
            } else {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_solitude_ripple_quartz);
                } else {
                    holder.numberTextView.setBackgroundResource(R.drawable.draw_oval_solid_solitude);
                }

                holder.numberTextView.setTextColor(activity.getResources().getColor(R.color.Mischka));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

            doWork(i);
        });
    }

    @Override
    public int getItemCount() {
        return indexes.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setIndex(JSONArray jsonArray, SampleViewModel viewModel) {
        ArrayList<String> indexes = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<>();

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
        this.viewModel = viewModel;
        notifyDataSetChanged();
    }

    private void doWork(int position) {
        viewModel.goToIndex(position);
        ((SampleActivity) Objects.requireNonNull(activity)).showFragment();
    }

    public class IndexHolder extends RecyclerView.ViewHolder {

        public TextView numberTextView;

        public IndexHolder(View view) {
            super(view);
            numberTextView = view.findViewById(R.id.single_item_index_textView);
        }
    }

}