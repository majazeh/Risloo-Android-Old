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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Views.Activities.CasesActivity;
import com.majazeh.risloo.Views.Activities.DetailCaseActivity;
import com.majazeh.risloo.Views.Activities.EditCaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.CasesHolder> {

    // Vars
    private ArrayList<Model> cases;

    // Objects
    private Activity activity;
    private Handler handler;

    public CasesAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CasesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_cases, viewGroup, false);

        initializer(view);

        return new CasesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CasesHolder holder, int i) {
        Model model = cases.get(i);

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
                holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_solid_snow_ripple_quartz);
            }

            Intent editCaseIntent = (new Intent(activity, EditCaseActivity.class));
            Intent detailCaseIntent = (new Intent(activity, DetailCaseActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                editCaseIntent.putExtra("id", model.get("id").toString());
                detailCaseIntent.putExtra("id", model.get("id").toString());

                holder.serialTextView.setText(model.get("id").toString());
            }

            // Room
            if (model.attributes.has("room") && !model.attributes.isNull("room")) {
                JSONObject room = (JSONObject) model.get("room");
                editCaseIntent.putExtra("room_id", room.get("id").toString());

                JSONObject manager = (JSONObject) room.get("manager");
                editCaseIntent.putExtra("room_name", manager.get("name").toString());

                JSONObject center = (JSONObject) room.get("center");
                JSONObject detail = (JSONObject) center.get("detail");
                editCaseIntent.putExtra("room_title", detail.get("title").toString());

                holder.roomTextView.setText(detail.getString("title"));
                holder.roomLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.roomLinearLayout.setVisibility(View.GONE);
            }

            // Reference
            JSONArray clients = (JSONArray) model.get("clients");
            editCaseIntent.putExtra("clients", model.get("clients").toString());

            if (clients.length() != 0) {
                ArrayList<String> references = new ArrayList<>();

                for (int j = 0; j < clients.length(); j++) {
                    JSONObject client = (JSONObject) clients.get(j);
                    JSONObject user = (JSONObject) client.get("user");

                    references.add(user.get("name").toString());
                }

                ReferenceAdapter referenceAdapter = new ReferenceAdapter(activity);
                referenceAdapter.setReference(references);

                if (holder.referenceRecyclerView.getAdapter() == null) {
                    holder.referenceRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, 0, 0));
                    holder.referenceRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    holder.referenceRecyclerView.setHasFixedSize(true);
                }

                holder.referenceRecyclerView.setAdapter(referenceAdapter);
                holder.referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referenceLinearLayout.setVisibility(View.GONE);
            }

            // Complaint
            if (model.attributes.has("detail") && !model.attributes.isNull("detail")) {
                JSONObject detail = (JSONObject) model.get("detail");
                editCaseIntent.putExtra("complaint", detail.get("chief_complaint").toString());
            }

            holder.editTextView.setOnClickListener(v -> {
                holder.editTextView.setClickable(false);
                handler.postDelayed(() -> holder.editTextView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(editCaseIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

            holder.itemView.setOnClickListener(v -> {
                holder.itemView.setClickable(false);
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(detailCaseIntent,100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cases.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void clearProgress() {
        if (((CasesActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((CasesActivity) Objects.requireNonNull(activity)).loading = false;
            ((CasesActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public void setCase(ArrayList<Model> cases) {
        this.cases = cases;
        notifyDataSetChanged();
    }

    public class CasesHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, roomTextView, editTextView;
        public RecyclerView referenceRecyclerView;
        public LinearLayout roomLinearLayout, referenceLinearLayout;

        public CasesHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_cases_serial_textView);
            roomTextView = view.findViewById(R.id.single_item_cases_room_textView);
            referenceRecyclerView = view.findViewById(R.id.single_item_cases_reference_recyclerView);
            editTextView = view.findViewById(R.id.single_item_cases_edit_textView);
            roomLinearLayout = view.findViewById(R.id.single_item_cases_room_linearLayout);
            referenceLinearLayout = view.findViewById(R.id.single_item_cases_reference_linearLayout);
        }
    }

}