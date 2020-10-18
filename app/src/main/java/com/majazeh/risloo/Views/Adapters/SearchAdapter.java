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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    // Vars
    private int position = -1;
    private String method, theory;
    private ArrayList<Model> values;

    // Objects
    private Activity activity;
    private Handler handler;

    public SearchAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_search, viewGroup, false);

        initializer(view);

        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int i) {
        Model model = values.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.titleTextView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
            }

            holder.titleTextView.setText(model.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (position == i) {
            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        } else {
            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

            position = i;

            switch (theory) {
                case "CreateCenter":
                    ((CreateCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(holder.titleTextView.getText().toString(), position, method);
                    break;
                case "EditCenter":
                    ((EditCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(holder.titleTextView.getText().toString(), position, method);
                    break;
                case "CreateSample":
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).observeSearchAdapter(holder.titleTextView.getText().toString(), position, method);
                    break;
            }

            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setValue(ArrayList<Model> values, String method, String theory) {
        this.values = values;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;

        public SearchHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_search_textView);
        }
    }

}