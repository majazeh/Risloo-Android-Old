package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONException;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    // Vars
    private ArrayList<Model> accounts;

    // Objects
    private Activity activity;

    public AccountAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_account, viewGroup, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, int i) {
        Model model = accounts.get(i);

        try {
            holder.titleTextView.setText(model.get("title").toString());
            holder.subTitleTextView.setText(model.get("subTitle").toString());
            holder.avatarImageView.setImageDrawable((Drawable) model.get("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return accounts.size();
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
            avatarImageView = view.findViewById(R.id.single_item_account_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_account_title_textView);
            subTitleTextView = view.findViewById(R.id.single_item_account_subTitle_textView);
        }
    }

}