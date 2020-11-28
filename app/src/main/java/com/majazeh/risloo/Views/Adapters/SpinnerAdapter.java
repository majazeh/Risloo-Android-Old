package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.CreateCaseActivity;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.EditCaseActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.SamplesActivity;

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

    public SpinnerAdapter(Activity activity) {
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
                    holder.titleTextView.setText(model.get("title").toString());
                    break;
                case "roomReferences":
                case "references":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.titleTextView.setText(user.get("name").toString());
                    break;
                case "phones":
                    holder.titleTextView.setText(model.get("name").toString());
                    break;
                case "scalesFilter":
                case "roomsFilter":
                case "statusFilter":
                    holder.titleTextView.setText(model.get("title").toString());
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.deleteImageView.setOnClickListener(v -> {
            holder.deleteImageView.setClickable(false);
            handler.postDelayed(() -> holder.deleteImageView.setClickable(true), 300);

            try {
                if (method.equals("scalesFilter") || method.equals("roomsFilter") || method.equals("statusFilter")) {
                    if (model.get("id").toString().equals(((SamplesActivity) Objects.requireNonNull(activity)).scale)) {
                        ((SamplesActivity) Objects.requireNonNull(activity)).scale = "";
                    } else if (model.get("id").toString().equals(((SamplesActivity) Objects.requireNonNull(activity)).room)) {
                        ((SamplesActivity) Objects.requireNonNull(activity)).room = "";
                    } else if (model.get("id").toString().equals(((SamplesActivity) Objects.requireNonNull(activity)).status)) {
                        ((SamplesActivity) Objects.requireNonNull(activity)).status = "";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            removeValue(i);

            if (values.size() == 0) {
                switch (method) {
                    case "scales":
                        // Reset Scales
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleTextView.setVisibility(View.VISIBLE);

                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setText("");
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setVisibility(View.GONE);
                        break;
                    case "roomReferences":
                        // Reset RoomReferences
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceTextView.setVisibility(View.VISIBLE);

                        // Reset Count
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setEnabled(true);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setFocusableInTouchMode(true);
                        break;
                    case "phones":
                        // Reset Phones
                        if (theory.equals("CreateCenter")) {
                            ((CreateCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                        } else if (theory.equals("EditCenter")) {
                            ((EditCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "scalesFilter":
                    case "roomsFilter":
                    case "statusFilter":
                        ((SamplesActivity) Objects.requireNonNull(activity)).relaunchData();
                        break;
                    case "references":
                        // Reset References
                        if (theory.equals("CreateCase")) {
                            ((CreateCaseActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);
                        } else if (theory.equals("EditCase")) {
                            ((EditCaseActivity) Objects.requireNonNull(activity)).referenceTextView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            } else {
                switch (method) {
                    case "scales":
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleCountTextView.setText(String.valueOf(values.size()));
                        break;
                    case "scalesFilter":
                    case "roomsFilter":
                    case "statusFilter":
                        ((SamplesActivity) Objects.requireNonNull(activity)).relaunchData();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setValue(ArrayList<Model> values, ArrayList<String> ids, String method, String theory) {
        this.values = values;
        this.ids = ids;
        this.method = method;
        this.theory = theory;
        notifyDataSetChanged();
    }

    public ArrayList<Model> getValues() {
        return values;
    }

    public ArrayList<String> getIds(){
        return ids;
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

    public class SpinnerHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView deleteImageView;

        public SpinnerHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_spinner_textView);
            deleteImageView = view.findViewById(R.id.single_item_spinner_imageView);
        }
    }

}