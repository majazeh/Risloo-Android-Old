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
import com.majazeh.risloo.Views.Activities.CreateCaseActivity;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.CreateRoomActivity;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.CreateSessionActivity;
import com.majazeh.risloo.Views.Activities.CreateReportActivity;
import com.majazeh.risloo.Views.Activities.CreateUserActivity;
import com.majazeh.risloo.Views.Activities.DetailSampleActivity;
import com.majazeh.risloo.Views.Activities.EditCaseActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.EditSessionActivity;
import com.majazeh.risloo.Views.Activities.SamplesActivity;

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

    public SearchAdapter(@NonNull Activity activity) {
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

                    switch (theory) {
                        case "CreateCenter":
                            if (((CreateCenterActivity) Objects.requireNonNull(activity)).managerId.equals(model.get("id").toString())) {
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

                        case "EditCenter":
                            if (((EditCenterActivity) Objects.requireNonNull(activity)).managerId.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getScales":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).scaleRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
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
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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
                            if (((CreateCaseActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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

                        case "EditCase":
                            if (((EditCaseActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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
                            if (((CreateSessionActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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

                        case "EditSession":
                            if (((EditSessionActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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

                        case "CreateUser":
                            if (((CreateUserActivity) Objects.requireNonNull(activity)).roomId.equals(model.get("id").toString())) {
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
                    }

                    JSONObject center = (JSONObject) model.get("center");
                    JSONObject detail = (JSONObject) center.get("detail");

                    holder.titleTextView.setText(detail.get("title").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getCenters":
                    JSONObject detail2 = (JSONObject) model.get("detail");
                    holder.nameTextView.setText(detail2.get("title").toString());

                    switch (theory) {
                        case "CreateRoom":
                            if (((CreateRoomActivity) Objects.requireNonNull(activity)).centerId.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getPsychologies":
                    JSONObject creator = (JSONObject) model.get("creator");
                    holder.nameTextView.setText(creator.get("name").toString());

                    switch (theory) {
                        case "CreateRoom":
                            if (((CreateRoomActivity) Objects.requireNonNull(activity)).psychologyId.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getReferences":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.nameTextView.setText(user.get("name").toString());

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
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
                            if (((CreateCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
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

                        case "EditCase":
                            if (((EditCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
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

                        case "CreateUser":
                            if (((CreateUserActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds().contains(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getSessions":
                    String faStatus = ((CreateSampleActivity) Objects.requireNonNull(activity)).sessionViewModel.getFAStatus(model.get("status").toString());

                    holder.nameTextView.setText(faStatus);

                    switch (theory) {
                        case "CreateSample":
                            if (((CreateSampleActivity) Objects.requireNonNull(activity)).sessionId.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);
                    break;
                case "getStatus":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateSession":
                            if (((CreateSessionActivity) Objects.requireNonNull(activity)).statusId.equals(model.get("en_title").toString())) {
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

                        case "EditSession":
                            if (((EditSessionActivity) Objects.requireNonNull(activity)).statusId.equals(model.get("en_title").toString())) {
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
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getPositions":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateUser":
                            if (((CreateUserActivity) Objects.requireNonNull(activity)).positionId.equals(model.get("en_title").toString())) {
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
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getEncryptionTypes":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateReport":
                            if (((CreateReportActivity) Objects.requireNonNull(activity)).encryptionTypeId.equals(model.get("en_title").toString())) {
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
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getScalesFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "Samples":
                            if (((SamplesActivity) Objects.requireNonNull(activity)).scale.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getStatusFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "Samples":
                            if (((SamplesActivity) Objects.requireNonNull(activity)).status.equals(model.get("id").toString())) {
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
                    }

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
                case "getURLs":
                    holder.nameTextView.setText(model.get("title").toString());

                    holder.nameTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                        holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                    else
                        holder.itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);

                    holder.titleTextView.setVisibility(View.GONE);
                    break;
            }

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

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
                    case "CreateCase":
                        ((CreateCaseActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "EditCase":
                        ((EditCaseActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "CreateSession":
                        ((CreateSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "EditSession":
                        ((EditSessionActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "CreateRoom":
                        ((CreateRoomActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "CreateUser":
                        ((CreateUserActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
                        break;
                    case "CreateReport":
                        ((CreateReportActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
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
        handler = new Handler();
    }

    public void setValues(ArrayList<Model> values, String method, String theory) {
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