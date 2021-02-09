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
    private ArrayList<Model> list;

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
        Model model = list.get(i);

        try {
            holder.titleTextView.setText((String) model.get("title"));
            holder.descriptionTextView.setText((String) model.get("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<Model> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class SubListBigHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;

        public SubListBigHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.item_sublist_big_title_textView);
            descriptionTextView = view.findViewById(R.id.item_sublist_big_description_textView);
        }
    }

}