package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;

import java.util.ArrayList;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ReferenceHolder> {

    // Vars
    private ArrayList<String> references;

    // Objects
    private Activity activity;
    private Handler handler;

    public ReferenceAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReferenceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_reference, viewGroup, false);

        initializer(view);

        return new ReferenceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceHolder holder, int i) {
        holder.titleTextView.setText(references.get(i));
    }

    @Override
    public int getItemCount() {
        return references.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setReference(ArrayList<String> references) {
        this.references = references;
        notifyDataSetChanged();
    }

    public class ReferenceHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public ReferenceHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_reference_textView);
        }
    }

}