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
    private ArrayList<Model> subListSmall;

    // Objects
    private Activity activity;

    public SubListSmallAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubListSmallHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.sublist_small_single_item, viewGroup, false);
        return new SubListSmallHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListSmallHolder holder, int i) {

        try {
            holder.itemTextView.setText(subListSmall.get(i).get("title").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return subListSmall.size();
    }

    public void setSubListSmall(ArrayList<Model> subListSmall) {
        this.subListSmall = subListSmall;
        notifyDataSetChanged();
    }

    public class SubListSmallHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;

        public SubListSmallHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.sublist_small_single_item_textView);
        }
    }

}