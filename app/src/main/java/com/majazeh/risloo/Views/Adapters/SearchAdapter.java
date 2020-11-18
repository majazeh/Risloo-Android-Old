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
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.SamplesActivity;

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
            switch (method) {
                case "getManagers":
                    holder.nameTextView.setText(model.get("name").toString());

                    if (theory.equals("CreateCenter")) {
                        if (((CreateCenterActivity) Objects.requireNonNull(activity)).manager.equals(model.get("id").toString())) {
                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                        } else {
                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                        }
                    } else if (theory.equals("EditCenter")) {
                        if (((EditCenterActivity) Objects.requireNonNull(activity)).managerId.equals(model.get("id").toString())) {
                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                        } else {
                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                        }
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getScales":
                    holder.nameTextView.setText(model.get("title").toString());

                    if (((CreateSampleActivity) Objects.requireNonNull(activity)).scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                    } else {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getRooms":
                    JSONObject manager = (JSONObject) model.get("manager");
                    holder.nameTextView.setText(manager.get("name").toString());

                    switch (theory) {
                        case "Samples":
                            if (((SamplesActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;
                        case "CreateCase":
//                            if (((CreateCaseActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                        case "EditCase":
//                            if (((EditCaseActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                        case "CreateSession":
//                            if (((CreateSessionActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                        case "EditSession":
//                            if (((EditSessionActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                    }

                    JSONObject center = (JSONObject) model.get("center");
                    JSONObject detail = (JSONObject) center.get("detail");

                    holder.titleTextView.setText(detail.get("title").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
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
                        holder.nameTextView.setText(casse.get("name").toString());
                    }

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).casse.equals(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;
                        case "CreateSession":
//                            if (((CreateSessionActivity) Objects.requireNonNull(activity)).casse.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                        case "EditSession":
//                            if (((EditSessionActivity) Objects.requireNonNull(activity)).casseId.equals(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getReferences":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.nameTextView.setText(user.get("name").toString());

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                            } else {
                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                                else
                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                            }
                            break;
                        case "CreateCase":
//                            if (((CreateCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                        case "EditCase":
//                            if (((EditCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                            } else {
//                                holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
//                                else
//                                    holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                            }
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getStatus":
                    holder.nameTextView.setText(model.get("title").toString());

                    if (theory.equals("CreateSession")) {
//                        if (((CreateSessionActivity) Objects.requireNonNull(activity)).status.equals(model.get("id").toString())) {
//                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                        } else {
//                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                        }
                    } else if (theory.equals("EditSession")) {
//                        if (((EditSessionActivity) Objects.requireNonNull(activity)).statusId.equals(model.get("id").toString())) {
//                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
//                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
//                        } else {
//                            holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
//                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
//                        }
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getScalesFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    if (((SamplesActivity) Objects.requireNonNull(activity)).scale.equals(model.get("id").toString())) {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                    } else {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getStatusFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    if (((SamplesActivity) Objects.requireNonNull(activity)).status.equals(model.get("id").toString())) {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                    } else {
                        holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getURLs":
                    holder.nameTextView.setText(model.get("title").toString());

                    holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz); else holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

            switch (theory) {
                case "Samples":
                    ((SamplesActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "DetailSample":
                    ((DetailSampleActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "CreateSample":
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "CreateCenter":
                    ((CreateCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
                case "EditCenter":
                    ((EditCenterActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                    break;
//                case "CreateCase":
//                    ((CreateCaseActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
//                    break;
//                case "EditCase":
//                    ((EditCaseActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
//                    break;
//                case "CreateSession":
//                    ((CreateSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
//                    break;
//                case "EditSession":
//                    ((EditSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
//                    break;
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

        public TextView nameTextView, titleTextView;

        public SearchHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.single_item_search_name_textView);
            titleTextView = view.findViewById(R.id.single_item_search_title_textView);
        }
    }

}