package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.ViewModels.SessionViewModel;
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
import com.majazeh.risloo.Views.Activities.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    // ViewModels
    private SessionViewModel sessionViewModel;

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
                            setActive("textView", null, ((CreateCenterActivity) Objects.requireNonNull(activity)).managerId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditCenter":
                            setActive("textView", null, ((EditCenterActivity) Objects.requireNonNull(activity)).managerId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Avatar
                    if (model.attributes.has("avatar") && !model.attributes.isNull("avatar") && model.attributes.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                        JSONObject avatar = (JSONObject) model.get("avatar");
                        JSONObject medium = (JSONObject) avatar.get("medium");

                        Picasso.get().load(medium.get("url").toString()).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.White).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.VISIBLE);
                        holder.charTextView.setText(StringManager.firstChars(holder.nameTextView.getText().toString()));
                    }
                    holder.avatarFrameLayout.setVisibility(View.VISIBLE);
                    break;

                case "getScales":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "CreateSample":
                            setActive("recyclerView", ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleRecyclerViewAdapter.getIds(), null, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getRooms":
                    JSONObject manager = (JSONObject) model.get("manager");
                    holder.nameTextView.setText(manager.get("name").toString());

                    switch (theory) {
                        case "Samples":
                            setActive("textView", null, ((SamplesActivity) Objects.requireNonNull(activity)).room, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateSample":
                            setActive("textView", null, ((CreateSampleActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateCase":
                            setActive("textView", null, ((CreateCaseActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditCase":
                            setActive("textView", null, ((EditCaseActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateSession":
                            setActive("textView", null, ((CreateSessionActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditSession":
                            setActive("textView", null, ((EditSessionActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateUser":
                            setActive("textView", null, ((CreateUserActivity) Objects.requireNonNull(activity)).roomId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    JSONObject center = (JSONObject) model.get("center");
                    JSONObject detail = (JSONObject) center.get("detail");

                    holder.titleTextView.setText(detail.get("title").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Avatar
                    if (manager.has("avatar") && !manager.isNull("avatar") && manager.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                        JSONObject avatar = (JSONObject) manager.get("avatar");
                        JSONObject medium = (JSONObject) avatar.get("medium");

                        Picasso.get().load(medium.get("url").toString()).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.White).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.VISIBLE);
                        holder.charTextView.setText(StringManager.firstChars(holder.nameTextView.getText().toString()));
                    }
                    holder.avatarFrameLayout.setVisibility(View.VISIBLE);
                    break;

                case "getCenters":
                    JSONObject detail2 = (JSONObject) model.get("detail");
                    holder.nameTextView.setText(detail2.get("title").toString());

                    switch (theory) {
                        case "CreateRoom":
                            setActive("textView", null, ((CreateRoomActivity) Objects.requireNonNull(activity)).centerId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Avatar
                    if (detail2.has("avatar") && !detail2.isNull("avatar") && detail2.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                        JSONObject avatar = (JSONObject) detail2.get("avatar");
                        JSONObject medium = (JSONObject) avatar.get("medium");

                        Picasso.get().load(medium.get("url").toString()).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.White).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.VISIBLE);
                        holder.charTextView.setText(StringManager.firstChars(holder.nameTextView.getText().toString()));
                    }
                    holder.avatarFrameLayout.setVisibility(View.VISIBLE);
                    break;

                case "getPsychologies":
                    JSONObject creator = (JSONObject) model.get("creator");
                    holder.nameTextView.setText(creator.get("name").toString());

                    switch (theory) {
                        case "CreateRoom":
                            setActive("textView", null, ((CreateRoomActivity) Objects.requireNonNull(activity)).psychologyId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Avatar
                    if (creator.has("avatar") && !creator.isNull("avatar") && creator.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                        JSONObject avatar = (JSONObject) creator.get("avatar");
                        JSONObject medium = (JSONObject) avatar.get("medium");

                        Picasso.get().load(medium.get("url").toString()).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.White).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.VISIBLE);
                        holder.charTextView.setText(StringManager.firstChars(holder.nameTextView.getText().toString()));
                    }
                    holder.avatarFrameLayout.setVisibility(View.VISIBLE);
                    break;

                case "getReferences":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.nameTextView.setText(user.get("name").toString());

                    switch (theory) {
                        case "CreateSample":
                            setActive("recyclerView", ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds(), null, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateCase":
                            setActive("recyclerView", ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds(), null, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditCase":
                            setActive("recyclerView", ((EditCaseActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds(), null, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateUser":
                            setActive("recyclerView", ((CreateUserActivity) Objects.requireNonNull(activity)).referenceRecyclerViewAdapter.getIds(), null, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    // Avatar
                    if (user.has("avatar") && !user.isNull("avatar") && user.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                        JSONObject avatar = (JSONObject) user.get("avatar");
                        JSONObject medium = (JSONObject) avatar.get("medium");

                        Picasso.get().load(medium.get("url").toString()).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.White).placeholder(R.color.White).into(holder.avatarImageView);

                        holder.charTextView.setVisibility(View.VISIBLE);
                        holder.charTextView.setText(StringManager.firstChars(holder.nameTextView.getText().toString()));
                    }
                    holder.avatarFrameLayout.setVisibility(View.VISIBLE);
                    break;

                case "getCases":
                    StringBuilder name = new StringBuilder();
                    JSONArray clients = (JSONArray) model.get("clients");

                    for (int j = 0; j < clients.length(); j++) {
                        JSONObject client = (JSONObject) clients.get(j);
                        JSONObject clientUser = (JSONObject) client.get("user");

                        if (j == clients.length() - 1) {
                            name.append(clientUser.get("name").toString());
                        } else {
                            name.append(clientUser.get("name").toString()).append(" - ");
                        }
                    }
                    holder.nameTextView.setText(name.toString());

                    switch (theory) {
                        case "CreateSample":
                            setActive("textView", null, ((CreateSampleActivity) Objects.requireNonNull(activity)).caseId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "CreateSession":
                            setActive("textView", null, ((CreateSessionActivity) Objects.requireNonNull(activity)).caseId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditSession":
                            setActive("textView", null, ((EditSessionActivity) Objects.requireNonNull(activity)).caseId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());

                    // Sessions
                    if (model.attributes.has("sessions") && !model.attributes.isNull("sessions") && model.attributes.get("sessions").getClass().getName().equals("org.json.JSONArray")) {
                        JSONArray sessions = (JSONArray) model.get("sessions");

                        if (sessions.length() != 0) {
                            for (int j = 0; j < 1; j++) {
                                JSONObject session = (JSONObject) sessions.get(j);

                                String faStatus =sessionViewModel.getFAStatus(session.get("status").toString());
                                holder.titleTextView.append("\n" + faStatus);

                                String startedAtTime = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(session.get("started_at").toString())));
                                String startedAtDate = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(session.get("started_at").toString()))));
                                holder.titleTextView.append("\n" + startedAtDate + "  |  " + startedAtTime);
                            }
                        }
                    }
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getSessions":
                    String faStatus = sessionViewModel.getFAStatus(model.get("status").toString());
                    holder.nameTextView.setText(faStatus);

                    switch (theory) {
                        case "CreateSample":
                            setActive("textView", null, ((CreateSampleActivity) Objects.requireNonNull(activity)).sessionId, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setText(model.get("id").toString());
                    holder.titleTextView.setVisibility(View.VISIBLE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getStatus":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateSession":
                            setActive("textView", null, ((CreateSessionActivity) Objects.requireNonNull(activity)).statusId, model.get("en_title").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "EditSession":
                            setActive("textView", null, ((EditSessionActivity) Objects.requireNonNull(activity)).statusId, model.get("en_title").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getPositions":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateUser":
                            setActive("textView", null, ((CreateUserActivity) Objects.requireNonNull(activity)).positionId, model.get("en_title").toString(), holder.nameTextView, holder.itemView);
                            break;
                        case "Users":
                            setActive("textView", null, ((UsersActivity) Objects.requireNonNull(activity)).userPositionId, model.get("en_title").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getEncryptions":
                    holder.nameTextView.setText(model.get("fa_title").toString());

                    switch (theory) {
                        case "CreateReport":
                            setActive("textView", null, ((CreateReportActivity) Objects.requireNonNull(activity)).encryptionId, model.get("en_title").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getScalesFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "Samples":
                            setActive("textView", null, ((SamplesActivity) Objects.requireNonNull(activity)).scale, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getStatusFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    switch (theory) {
                        case "Samples":
                            setActive("textView", null, ((SamplesActivity) Objects.requireNonNull(activity)).status, model.get("id").toString(), holder.nameTextView, holder.itemView);
                            break;
                    }

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;

                case "getURLs":
                    holder.nameTextView.setText(model.get("title").toString());

                    setActive("textView", null, "+", "-", holder.nameTextView, holder.itemView);

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
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
                    case "Users":
                        ((UsersActivity) Objects.requireNonNull(activity)).observeSearchAdapter(model, method);
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
        sessionViewModel = new ViewModelProvider((FragmentActivity) activity).get(SessionViewModel.class);

        handler = new Handler();
    }

    private void setActive(String widget, ArrayList<String> inputList, String inputValue, String outputValue, TextView textView, View itemView) {
        switch (widget) {
            case "textView":
                if (inputValue.equals(outputValue)) {
                    textView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                    else
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                } else {
                    textView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                    else
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                }
                break;
            case "recyclerView":
                if (inputList.contains(outputValue)) {
                    textView.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p_ripple_primary);
                    else
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_primary5p);
                } else {
                    textView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude_ripple_quartz);
                    else
                        itemView.setBackgroundResource(R.drawable.draw_rectangle_solid_solitude);
                }
                break;
        }
    }

    public void setValues(ArrayList<Model> values, String method, String theory) {
        this.values = values;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        public TextView charTextView, nameTextView, titleTextView;
        public ImageView avatarImageView;
        public FrameLayout avatarFrameLayout;

        public SearchHolder(View view) {
            super(view);
            charTextView = view.findViewById(R.id.single_item_search_char_textView);
            nameTextView = view.findViewById(R.id.single_item_search_name_textView);
            titleTextView = view.findViewById(R.id.single_item_search_title_textView);
            avatarImageView = view.findViewById(R.id.single_item_search_avatar_imageView);
            avatarFrameLayout = view.findViewById(R.id.single_item_search_avatar_frameLayout);
        }
    }

}