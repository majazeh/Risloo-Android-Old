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

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneHolder> {

    // Vars
    private ArrayList<String> phones;

    // Objects
    private Activity activity;

    public PhoneAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PhoneHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_phone, viewGroup, false);
        return new PhoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneHolder holder, int i) {
        holder.itemTextView.setText(phones.get(i));
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    public void setPhone(ArrayList<String> phones) {
        this.phones = phones;
        notifyDataSetChanged();
    }

    public class PhoneHolder extends RecyclerView.ViewHolder {

        public TextView itemTextView;

        public PhoneHolder(View view) {
            super(view);
            itemTextView = view.findViewById(R.id.single_item_phone_textView);
        }
    }

}