package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneHolder> {

    // Vars
    private ArrayList<String> phones;

    // Objects
    private Activity activity;
    private Handler handler;

    public PhoneAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PhoneHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_phone, viewGroup, false);

        initializer(view);

        return new PhoneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneHolder holder, int i) {
        String phone = phones.get(i);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.titleTextView.setBackgroundResource(R.drawable.draw_4sdp_solid_white_ripple_quartz);
        }

        holder.titleTextView.setText(phone);

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

            IntentManager.phone(activity, phone);
        });
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setPhone(ArrayList<String> phones) {
        this.phones = phones;
        notifyDataSetChanged();
    }

    public class PhoneHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public PhoneHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_phone_textView);
        }
    }

}