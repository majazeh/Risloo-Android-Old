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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    // Vars
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

            switch (method) {
                case "getPersonalClinic":
                    holder.titleTextView.setText(model.get("name").toString());

                    if (((CreateCenterActivity) Objects.requireNonNull(activity)).manager.equals(model.get("id").toString()))
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    else
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    break;
                case "getCounselingCenter":
                    holder.titleTextView.setText(model.get("name").toString());

                    if (theory.equals("CreateCenter")) {
                        if (((CreateCenterActivity) Objects.requireNonNull(activity)).manager.equals(model.get("id").toString()))
                            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                         else
                            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    } else if (theory.equals("EditCenter")) {
                        if (((EditCenterActivity) Objects.requireNonNull(activity)).managerId.equals(model.get("id").toString()))
                            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        else
                            holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    }
                    break;
                case "getScales":
                    holder.titleTextView.setText(model.get("title").toString());

                    if (((CreateSampleActivity) Objects.requireNonNull(activity)).scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString()))
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    else
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    break;
                case "getRooms":
                    JSONObject manager = (JSONObject) model.get("manager");
                    holder.titleTextView.setText(manager.get("name").toString());

                    if (((CreateSampleActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString()))
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    else
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    break;
                case "getCases":
                    StringBuilder name = new StringBuilder();
                    JSONArray client = (JSONArray) model.get("clients");

                    for (int j = 0; j < client.length(); j++) {
                        JSONObject object = client.getJSONObject(j);
                        JSONObject user = object.getJSONObject("user");

                        if (j == client.length() - 1)
                            name.append(user.getString("name"));
                        else
                            name.append(user.getString("name")).append(" - ");
                    }

                    if (!name.toString().equals("")) {
                        JSONObject casse = new JSONObject().put("name", name);
                        holder.titleTextView.setText(casse.get("name").toString());
                    }

                    if (((CreateSampleActivity) Objects.requireNonNull(activity)).casse.equals(model.get("id").toString()))
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    else
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    break;

                case "getReferences":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.titleTextView.setText(user.get("name").toString());

                    if (((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceRecyclerViewAdapter.getIds().contains(model.get("id").toString()))
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    else
                        holder.titleTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

            switch (theory) {
                case "CreateCenter":
                    ((CreateCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "EditCenter":
                    ((EditCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "CreateSample":
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
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