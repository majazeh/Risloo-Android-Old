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
import com.majazeh.risloo.Views.Activities.EditCaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.CasesHolder> {

    // Vars
    private ArrayList<Model> cases;

    // Objects
    private Activity activity;
    private Handler handler;

    public CasesAdapter(Activity activity) {
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
            Intent editIntent = (new Intent(activity, EditCaseActivity.class));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.editTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            }

            editIntent.putExtra("id", (String) model.get("id"));
            holder.serialTextView.setText(model.get("id").toString());

            JSONObject room = (JSONObject) model.get("room");
            editIntent.putExtra("room_id", (String) room.get("id"));

            JSONObject manager = (JSONObject) room.get("manager");
            if (!manager.isNull("name")) {
                editIntent.putExtra("room_name", (String) manager.get("name"));
            }

            JSONObject center = (JSONObject) room.get("center");
            JSONObject details = (JSONObject) center.get("detail");

            if (!details.isNull("title")) {
                editIntent.putExtra("room_title", (String) details.get("title"));

                holder.roomTextView.setText(details.getString("title"));
                holder.roomLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.roomLinearLayout.setVisibility(View.GONE);
            }

            JSONArray clients = (JSONArray) model.get("clients");
            if (clients.length() != 0) {
                editIntent.putExtra("clients", String.valueOf(clients));

                ArrayList<String> references = new ArrayList<>();

                for (int j = 0; j < clients.length(); j++) {
                    JSONObject client = (JSONObject) clients.get(j);
                    JSONObject user = (JSONObject) client.get("user");

                    if (!user.isNull("name")) {
                        references.add(user.get("name").toString());
                    }
                }

                ReferenceAdapter adapter = new ReferenceAdapter(activity);
                adapter.setReference(references);

                if (holder.referencesRecyclerView.getAdapter() == null) {
                    holder.referencesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, 0, 0));
                    holder.referencesRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                    holder.referencesRecyclerView.setHasFixedSize(false);
                }

                holder.referencesRecyclerView.setAdapter(adapter);
                holder.referencesLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.referencesLinearLayout.setVisibility(View.GONE);
            }

            JSONObject detail = (JSONObject) model.get("detail");
            if (!detail.isNull("chief_complaint")) {
                editIntent.putExtra("complaint", (String) detail.get("chief_complaint"));
            }

            holder.editTextView.setOnClickListener(v -> {
                holder.editTextView.setClickable(false);
                handler.postDelayed(() -> holder.editTextView.setClickable(true), 300);

                activity.startActivityForResult(editIntent, 100);
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

    public void setCases(ArrayList<Model> cases) {
        this.cases = cases;
        notifyDataSetChanged();
    }

    public class CasesHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, roomTextView, editTextView;
        public RecyclerView referencesRecyclerView;
        public LinearLayout roomLinearLayout, referencesLinearLayout;

        public CasesHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_cases_serial_textView);
            roomTextView = view.findViewById(R.id.single_item_cases_room_textView);
            referencesRecyclerView = view.findViewById(R.id.single_item_cases_references_recyclerView);
            editTextView = view.findViewById(R.id.single_item_cases_edit_textView);
            roomLinearLayout = view.findViewById(R.id.single_item_cases_room_linearLayout);
            referencesLinearLayout = view.findViewById(R.id.single_item_cases_references_linearLayout);
        }
    }

}
