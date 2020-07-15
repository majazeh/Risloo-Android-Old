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

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityHolder> {

    // Vars
    private ArrayList<String> facilities;

    // Objects
    private Activity activity;

    public FacilityAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public FacilityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_about_us_facility_single_item, viewGroup, false);
        return new FacilityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityHolder holder, int i) {
        holder.facilityTextView.setText(facilities.get(i));
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    public void setFacility(ArrayList<String> facilities) {
        this.facilities = facilities;
        notifyDataSetChanged();
    }

    public class FacilityHolder extends RecyclerView.ViewHolder {

        public TextView facilityTextView;

        public FacilityHolder(View view) {
            super(view);
            facilityTextView = view.findViewById(R.id.activity_about_us_facility_single_item_textView);
        }
    }

}