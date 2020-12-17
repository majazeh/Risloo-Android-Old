package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    // Vars
    private ArrayList<Model> users;

    // Objects
    private Activity activity;
    private Handler handler;

    public UsersAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_users, viewGroup, false);

        initializer(view);

        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int i) {
        Model model = users.get(i);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setUser(ArrayList<Model> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public class UsersHolder extends RecyclerView.ViewHolder {

//        public TextView serialTextView;
//        public RecyclerView referenceRecyclerView;
//        public LinearLayout roomLinearLayout, referenceLinearLayout;

        public UsersHolder(View view) {
            super(view);
//            serialTextView = view.findViewById(R.id.single_item_cases_serial_textView);
//            roomTextView = view.findViewById(R.id.single_item_cases_room_textView);
//            referenceRecyclerView = view.findViewById(R.id.single_item_cases_reference_recyclerView);
//            editTextView = view.findViewById(R.id.single_item_cases_edit_textView);
//            roomLinearLayout = view.findViewById(R.id.single_item_cases_room_linearLayout);
//            referenceLinearLayout = view.findViewById(R.id.single_item_cases_reference_linearLayout);
        }
    }

}