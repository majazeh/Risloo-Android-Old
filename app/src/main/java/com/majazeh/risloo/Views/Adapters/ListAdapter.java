package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    // Vars
    private ArrayList<String> list;

    // Objects
    private Activity activity;

    public ListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_list_single_item, viewGroup, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int i) {
        holder.itemTextView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ListHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;

        public ListHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.activity_list_single_item_textView);
        }
    }

}