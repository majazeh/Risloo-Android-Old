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

public class SubListBigAdapter extends RecyclerView.Adapter<SubListBigAdapter.SubListBigHolder> {

    // Vars
    private ArrayList<Model> lists;

    // Objects
    private Activity activity;

    public SubListBigAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubListBigHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_sublist_big, viewGroup, false);
        return new SubListBigHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListBigHolder holder, int i) {
        Model model = lists.get(i);

        try {
            holder.titleTextView.setText(model.get("title").toString());
            holder.descriptionTextView.setText(model.get("description").toString());
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

    public class SubListBigHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;

        public SubListBigHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_sublist_big_title_textView);
            descriptionTextView = view.findViewById(R.id.single_item_sublist_big_description_textView);
        }
    }

}