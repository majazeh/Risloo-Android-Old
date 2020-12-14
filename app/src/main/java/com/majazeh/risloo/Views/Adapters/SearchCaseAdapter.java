package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.CreateSessionActivity;
import com.majazeh.risloo.Views.Activities.EditSessionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchCaseAdapter extends RecyclerView.Adapter<SearchCaseAdapter.SearchCaseHolder> {

    // ViewModels
    private SessionViewModel sessionViewModel;

    // Vars
    private String method, theory;
    private ArrayList<Model> values;

    // Objects
    private Activity activity;
    private Handler handler;

    public SearchCaseAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchCaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_search_case, viewGroup, false);

        initializer(view);

        return new SearchCaseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCaseHolder holder, int i) {
        Model model = values.get(i);

        try {
            switch (method) {
                case "getCases":
                    StringBuilder name = new StringBuilder();
                    JSONArray clients = (JSONArray) model.get("clients");

                    for (int j = 0; j < clients.length(); j++) {
                        JSONObject client = (JSONObject) clients.get(j);
                        JSONObject user = (JSONObject) client.get("user");

                        if (j == clients.length() - 1) {
                            name.append(user.get("name").toString());
                        } else {
                            name.append(user.get("name").toString()).append(" - ");
                        }
                    }

                    holder.nameTextView.setText(name.toString());

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).caseId.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;

                        case "CreateSession":
                            if (((CreateSessionActivity) Objects.requireNonNull(activity)).caseId.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;

                        case "EditSession":
                            if (((EditSessionActivity) Objects.requireNonNull(activity)).caseId.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Sessions
                    if (model.attributes.has("sessions") && !model.attributes.isNull("sessions")) {
                        JSONArray sessions = (JSONArray) model.get("sessions");

                        if (sessions.length() != 0) {
                            for (int j = 0; j < 1; j++) {
                                JSONObject session = (JSONObject) sessions.get(j);

                                String faStatus =sessionViewModel.getFAStatus(session.get("status").toString());

                                holder.statusTextView.setText(faStatus);
                                holder.statusTextView.setVisibility(View.VISIBLE);

                                String startedAtTime = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(session.get("started_at").toString())));
                                String startedAtDate = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(session.get("started_at").toString()))));

                                holder.startedAtTextView.setText(startedAtDate + "  |  " + startedAtTime);
                                holder.startedAtTextView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            holder.statusTextView.setVisibility(View.GONE);
                            holder.startedAtTextView.setVisibility(View.GONE);
                        }
                    } else {
                        holder.statusTextView.setVisibility(View.GONE);
                        holder.startedAtTextView.setVisibility(View.GONE);
                    }

                    break;
            }

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                switch (theory) {
                    case "CreateSample":
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "CreateSession":
                        ((CreateSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "EditSession":
                        ((EditSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                }

                notifyDataSetChanged();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private void initializer(View view) {
        sessionViewModel = new ViewModelProvider((FragmentActivity) activity).get(SessionViewModel.class);

        handler = new Handler();
    }

    public void setValues(ArrayList<Model> values, String method, String theory) {
        this.values = values;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public class SearchCaseHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView, titleTextView, statusTextView, startedAtTextView;

        public SearchCaseHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.single_item_search_case_name_textView);
            titleTextView = view.findViewById(R.id.single_item_search_case_title_textView);
            statusTextView = view.findViewById(R.id.single_item_search_case_status_textView);
            startedAtTextView = view.findViewById(R.id.single_item_search_case_started_at_textView);
        }
    }

}