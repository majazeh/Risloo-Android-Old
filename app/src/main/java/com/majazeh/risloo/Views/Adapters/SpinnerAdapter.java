package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.SpinnerHolder> {

    // Vars
    private String type;
    private ArrayList<Model> values = new ArrayList<>();
    private ArrayList<String> valuesId = new ArrayList<>();

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

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.spinnerLinearLayout.setBackgroundResource(R.drawable.draw_4sdp_solid_snow_ripple_quartz);
            holder.deleteImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
        }

        try {
            switch (type) {
                case "scale":
                    holder.titleTextView.setText(String.valueOf(model.get("title")));
                    break;
                case "roomReference":
                    JSONObject user = (JSONObject) model.get("user");
                    holder.titleTextView.setText(String.valueOf(user.get("name")));
                    break;
                case "phoneCreate":
                case "phoneEdit":
                    holder.titleTextView.setText(String.valueOf(model.get("title")));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 300);
        });

        holder.deleteImageView.setOnClickListener(v -> {
            holder.deleteImageView.setClickable(false);
            handler.postDelayed(() -> holder.deleteImageView.setClickable(true), 300);

            removeValue(i);
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setValue(ArrayList<Model> values, String type) {
        this.values = values;
        this.type = type;
        notifyDataSetChanged();

        if (type.equals("phoneCreate")) {
            for (int i = 0; i < values.size(); i++) {
                try {
                    valuesId.add((String) values.get(i).get("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < values.size(); i++) {
                try {
                    valuesId.add((String) values.get(i).get("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setValuesId(ArrayList<String> valuesId){
        this.valuesId = valuesId;
    }

    public ArrayList<Model> getValues() {
        return values;
    }

    public ArrayList<String> getValuesId(){
        return valuesId;
    }

    private void removeValue(int position) {
        values.remove(position);
        valuesId.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position);

        if (values.size() == 0) {
            switch (type) {
                case "scale":
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleTextView.setVisibility(View.VISIBLE);
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleSpinner.setAdapter(null);
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).setSpinner(SampleRepository.scales, ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleSpinner, "scale");
                    break;
                case "roomReference":
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceTextView.setVisibility(View.VISIBLE);
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setVisibility(View.VISIBLE);
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceSpinner.setAdapter(null);
                    ((CreateSampleActivity) Objects.requireNonNull(activity)).setSpinner(SampleRepository.references, ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceSpinner, "reference");
                    break;
                case "phoneCreate":
                    ((CreateCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                    break;
                case "phoneEdit":
                    ((EditCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public class SpinnerHolder extends RecyclerView.ViewHolder {

        public LinearLayout spinnerLinearLayout;
        public TextView titleTextView;
        public ImageView deleteImageView;

        public SpinnerHolder(View view) {
            super(view);
            spinnerLinearLayout = view.findViewById(R.id.single_item_spinner_linearLayout);
            titleTextView = view.findViewById(R.id.single_item_spinner_textView);
            deleteImageView = view.findViewById(R.id.single_item_spinner_imageView);
        }
    }

}