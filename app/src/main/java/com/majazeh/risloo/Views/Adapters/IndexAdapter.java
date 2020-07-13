package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Index;
import com.majazeh.risloo.R;

import java.util.ArrayList;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexHolder> {

    // Vars
    private ArrayList<Index> Indexes;

    // Objects
    private Activity activity;

    public IndexAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public IndexHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_navigate_single_item, viewGroup, false);
        return new IndexHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IndexHolder holder, int i) {

        // TODO : Fill Here

    }

    @Override
    public int getItemCount() {
        return Indexes.size();
    }

    public void setIndex(ArrayList<Index> Indexes) {
        this.Indexes = Indexes;
        notifyDataSetChanged();
    }

    public class IndexHolder extends RecyclerView.ViewHolder {

        // TODO : Fill Here

        public IndexHolder(View view) {
            super(view);
            // TODO : Fill Here
        }
    }

}