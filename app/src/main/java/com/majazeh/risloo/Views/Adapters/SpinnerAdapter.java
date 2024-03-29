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
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Views.Activities.CreateCaseActivity;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.CreateUserActivity;
import com.majazeh.risloo.Views.Activities.EditCaseActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.SamplesActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.SpinnerHolder> {

    // Vars
    private String method, theory;
    private ArrayList<Model> values = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();

    // Objects
    private Activity activity;
    private Handler handler;

    public SpinnerAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SpinnerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_spinner, viewGroup, false);

        initializer(view);

        return new SpinnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerHolder holder, int i) {
        Model model = values.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.deleteImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
            }

            switch (method) {
                case "scales":
                    holder.nameTextView.setText(model.get("title").toString());

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;
                case "references":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.nameTextView.setText(user.get("name").toString());

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
                case "phones":
                    holder.nameTextView.setText(model.get("name").toString());

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;
                case "scalesFilter":
                case "roomsFilter":
                case "statusFilter":
                    holder.nameTextView.setText(model.get("title").toString());

                    holder.titleTextView.setVisibility(View.GONE);

                    holder.avatarFrameLayout.setVisibility(View.GONE);
                    break;
            }

            holder.deleteImageView.setOnClickListener(v -> {
                holder.deleteImageView.setClickable(false);
                handler.postDelayed(() -> holder.deleteImageView.setClickable(true), 250);

                try {
                    if (method.equals("scalesFilter") || method.equals("roomsFilter") || method.equals("statusFilter")) {
                        if (((SamplesActivity) Objects.requireNonNull(activity)).scale.equals(model.get("id").toString())) {
                            ((SamplesActivity) Objects.requireNonNull(activity)).scale = "";
                        } else if (((SamplesActivity) Objects.requireNonNull(activity)).room.equals(model.get("id").toString())) {
                            ((SamplesActivity) Objects.requireNonNull(activity)).room = "";
                        } else if (((SamplesActivity) Objects.requireNonNull(activity)).status.equals(model.get("id").toString())) {
                            ((SamplesActivity) Objects.requireNonNull(activity)).status = "";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                removeValue(i);

                if (method.equals("scalesFilter") || method.equals("roomsFilter") || method.equals("statusFilter")) {
                    ((SamplesActivity) Objects.requireNonNull(activity)).relaunchData();
                }

                if (values.size() == 0) {
                    switch (method) {
                        case "scales":
                            ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleTextView.setVisibility(View.VISIBLE);

                            ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setText("");
                            ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setVisibility(View.GONE);
                            break;

                        case "references":
                            switch (theory) {
                                case "Samples":
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);

                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText("");
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.GONE);

                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setEnabled(true);
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setFocusableInTouchMode(true);
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                                    break;
                                case "CreateCase":
                                    ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);

                                    ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText("");
                                    ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.GONE);
                                    break;
                                case "EditCase":
                                    ((EditCaseActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);

                                    ((EditCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText("");
                                    ((EditCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.GONE);
                                    break;
                                case "CreateUser":
                                    ((CreateUserActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);

                                    ((CreateUserActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText("");
                                    ((CreateUserActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.GONE);
                                    break;
                            }
                            break;

                        case "phones":
                            switch (theory) {
                                case "CreateCenter":
                                    ((CreateCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                                    break;
                                case "EditCenter":
                                    ((EditCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (method) {
                        case "scales":
                            ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setText(String.valueOf(values.size()));
                            ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setVisibility(View.VISIBLE);
                            break;

                        case "references":
                            switch (theory) {
                                case "Samples":
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText(String.valueOf(values.size()));
                                    ((CreateSampleActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.VISIBLE);
                                    break;
                                case "CreateCase":
                                    ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText(String.valueOf(values.size()));
                                    ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.VISIBLE);
                                    break;
                                case "EditCase":
                                    ((EditCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText(String.valueOf(values.size()));
                                    ((EditCaseActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.VISIBLE);
                                    break;
                                case "CreateUser":
                                    ((CreateUserActivity) Objects.requireNonNull(activity)).referenceCountTextView.setText(String.valueOf(values.size()));
                                    ((CreateUserActivity) Objects.requireNonNull(activity)).referenceCountTextView.setVisibility(View.VISIBLE);
                                    break;
                            }
                            break;
                    }
                }
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

    public void setValues(ArrayList<Model> values, ArrayList<String> ids, String method, String theory) {
        this.values = values;
        this.ids = ids;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public void clearValues() {
        values.clear();
        ids.clear();
        notifyDataSetChanged();
    }

    public void removeValue(int position) {
        values.remove(position);
        ids.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void replaceValue(int position, Model model) {
        try {
            values.set(position, model);
            ids.set(position, model.get("id").toString());
            notifyItemChanged(position);
            notifyItemRangeChanged(position, getItemCount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Model> getValues() {
        return values;
    }

    public ArrayList<String> getIds(){
        return ids;
    }

    public class SpinnerHolder extends RecyclerView.ViewHolder {

        public TextView charTextView, nameTextView, titleTextView;
        public ImageView deleteImageView, avatarImageView;
        public FrameLayout avatarFrameLayout;

        public SpinnerHolder(View view) {
            super(view);
            charTextView = view.findViewById(R.id.single_item_spinner_char_textView);
            nameTextView = view.findViewById(R.id.single_item_spinner_name_textView);
            titleTextView = view.findViewById(R.id.single_item_spinner_title_textView);
            deleteImageView = view.findViewById(R.id.single_item_spinner_delete_imageView);
            avatarImageView = view.findViewById(R.id.single_item_spinner_avatar_imageView);
            avatarFrameLayout = view.findViewById(R.id.single_item_spinner_avatar_frameLayout);
        }
    }

}