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

public class SubListBigAdapter extends RecyclerView.Adapter<SubListBigAdapter.SubListBigHolder> {

    // Vars
    private ArrayList<List> subListBig;

    // Objects
    private Activity activity;

    public SubListBigAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubListBigHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.sublist_big_single_item, viewGroup, false);
        return new SubListBigHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListBigHolder holder, int i) {

        try {
            holder.titleTextView.setText(subListBig.get(i).get("title").toString());
            holder.descriptionTextView.setText(subListBig.get(i).get("description").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return subListBig.size();
    }

    public void setSubListBig(ArrayList<List> subListBig) {
        this.subListBig = subListBig;
        notifyDataSetChanged();
    }

    public class SubListBigHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;

        public SubListBigHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.subList_big_single_item_title_textView);
            descriptionTextView = view.findViewById(R.id.subList_big_single_item_description_textView);
        }
    }

}