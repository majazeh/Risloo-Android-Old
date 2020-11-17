package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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

import org.json.JSONException;

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

//        try {
//            // TODO : Place Code Here
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
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

        public TextView serialTextView, roomTextView;
        public RecyclerView referencesRecyclerView;
        public LinearLayout roomLinearLayout, referencesLinearLayout;

        public CasesHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_cases_serial_textView);
            roomTextView = view.findViewById(R.id.single_item_cases_room_textView);
            referencesRecyclerView = view.findViewById(R.id.single_item_cases_references_recyclerView);
            roomLinearLayout = view.findViewById(R.id.single_item_cases_room_linearLayout);
            referencesLinearLayout = view.findViewById(R.id.single_item_cases_references_linearLayout);
        }
    }

}
