package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    // Vars
    private ArrayList<Model> accounts;

    // Objects
    private Activity activity;
    private Handler handler;

    public AccountAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_account_single_item, viewGroup, false);

        initializer(view);

        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int i) {
        try {
            holder.titleTextView.setText(accounts.get(i).get("title").toString());
            holder.subTitleTextView.setText(accounts.get(i).get("subTitle").toString());
            holder.avatarImageView.setImageDrawable((Drawable) accounts.get(i).get("image"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setAccount(ArrayList<Model> accounts) {
        this.accounts = accounts;
        notifyDataSetChanged();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public TextView titleTextView, subTitleTextView;

        public AccountHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.activity_account_single_item_avatar_imageView);
            titleTextView = view.findViewById(R.id.activity_account_single_item_title_textView);
            subTitleTextView = view.findViewById(R.id.activity_account_single_item_subTitle_textView);
        }
    }

}