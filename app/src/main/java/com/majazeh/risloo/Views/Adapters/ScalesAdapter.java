package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.CreateSampleActivity;
import com.majazeh.risloo.Views.Activities.ScalesActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ScalesAdapter extends RecyclerView.Adapter<ScalesAdapter.ScalesHolder> {

    // Vars
    private ArrayList<Model> scales;

    // Objects
    private Activity activity;
    private Handler handler;

    public ScalesAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ScalesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_scales, viewGroup, false);

        initializer(view);

        return new ScalesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScalesHolder holder, int i) {
        Model model = scales.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.createTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            }

            Intent createSampleIntent = (new Intent(activity, CreateSampleActivity.class));

            holder.serialTextView.setText(model.get("id").toString());
            createSampleIntent.putExtra("id", model.get("id").toString());

            if (!model.get("title").toString().equals("")) {
                holder.scaleTextView.setText(model.get("title").toString());
                createSampleIntent.putExtra("title", model.get("title").toString());
            }

            if (!model.get("edition").toString().equals("")) {
                holder.editTextView.setText(model.get("edition").toString());
                holder.editLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.editLinearLayout.setVisibility(View.GONE);
            }

            if (!model.get("version").toString().equals("")) {
                holder.versionTextView.setText(model.get("version").toString());
                holder.versionLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.versionLinearLayout.setVisibility(View.GONE);
            }


            holder.createTextView.setOnClickListener(v -> {
                holder.createTextView.setClickable(false);
                handler.postDelayed(() -> holder.createTextView.setClickable(true), 300);

                activity.startActivity(createSampleIntent.putExtra("loaded", ((ScalesActivity) Objects.requireNonNull(activity)).finished));
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return scales.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setScales(ArrayList<Model> scales) {
        this.scales = scales;
        notifyDataSetChanged();
    }

    public class ScalesHolder extends RecyclerView.ViewHolder {

        public TextView scaleTextView, serialTextView, createTextView, editTextView, versionTextView;
        public LinearLayout editLinearLayout, versionLinearLayout;

        public ScalesHolder(View view) {
            super(view);
            scaleTextView = view.findViewById(R.id.single_item_scales_title_textView);
            serialTextView = view.findViewById(R.id.single_item_scales_serial_textView);
            createTextView = view.findViewById(R.id.single_item_scales_create_textView);
            editTextView = view.findViewById(R.id.single_item_scales_edit_textView);
            versionTextView = view.findViewById(R.id.single_item_scales_version_textView);
            editLinearLayout = view.findViewById(R.id.single_item_scales_edit_linearLayout);
            versionLinearLayout = view.findViewById(R.id.single_item_scales_version_linearLayout);
        }
    }

}
