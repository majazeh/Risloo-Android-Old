package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.List;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.SubListHolder> {

    // Vars
    private ArrayList<List> subList;

    // Objects
    private Activity activity;

    public SubListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.sublist_single_item, viewGroup, false);
        return new SubListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListHolder holder, int i) {

        try {
            holder.itemTextView.setText(subList.get(i).get("title").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

    public void setSubList(ArrayList<List> subList) {
        this.subList = subList;
        notifyDataSetChanged();
    }

    public class SubListHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;

        public SubListHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.sublist_single_item_textView);
        }
    }

}