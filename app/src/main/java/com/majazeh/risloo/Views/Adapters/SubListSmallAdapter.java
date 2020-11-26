package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;

public class SubListSmallAdapter extends RecyclerView.Adapter<SubListSmallAdapter.SubListSmallHolder> {

    // Vars
    private ArrayList<Model> lists;

    // Objects
    private Activity activity;

    public SubListSmallAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubListSmallHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_sublist_small, viewGroup, false);
        return new SubListSmallHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListSmallHolder holder, int i) {
        Model model = lists.get(i);

        try {
            holder.titleTextView.setText(model.get("title").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setList(ArrayList<Model> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public class SubListSmallHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public SubListSmallHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_sublist_small_title_textView);
        }
    }

}