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
                holder.spinnerLinearLayout.setBackgroundResource(R.drawable.draw_4sdp_solid_snow_ripple_quartz);
                holder.deleteImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
            }

            holder.titleTextView.setText(model.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.deleteImageView.setOnClickListener(v -> {
            holder.deleteImageView.setClickable(false);
            handler.postDelayed(() -> holder.deleteImageView.setClickable(true), 300);

            values.remove(i);
            ids.remove(i);
            notifyItemRemoved(i);
            notifyItemChanged(i);

            if (values.size() == 0) {
                switch (method) {
                    case "scales":
                        // Reset Scales
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleDialogRecyclerView.setAdapter(null);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleTextView.setVisibility(View.VISIBLE);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).setRecyclerView(SampleRepository.scales, ((CreateSampleActivity) Objects.requireNonNull(activity)).scaleDialogRecyclerView, "getScales");
                        break;
                    case "roomReferences":
                        // Reset RoomReferences
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceDialogRecyclerView.setAdapter(null);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceTextView.setVisibility(View.VISIBLE);
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).setRecyclerView(SampleRepository.references, ((CreateSampleActivity) Objects.requireNonNull(activity)).roomReferenceDialogRecyclerView, "getReferences");

                        // Reset Count
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).count = "";
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.getText().clear();
                        ((CreateSampleActivity) Objects.requireNonNull(activity)).countEditText.setVisibility(View.VISIBLE);
                        break;
                    case "phones":
                        // Reset Phones
                        if (theory.equals("CreateCenter")) {
                            ((CreateCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                        } else if (theory.equals("EditCenter")) {
                            ((EditCenterActivity) Objects.requireNonNull(activity)).phoneTextView.setVisibility(View.VISIBLE);
                        }
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