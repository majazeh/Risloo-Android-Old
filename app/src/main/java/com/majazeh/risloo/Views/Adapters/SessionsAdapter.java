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

import java.util.ArrayList;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.SessionsHolder> {

    // Vars
    private ArrayList<Model> sessions;

    // Objects
    private Activity activity;
    private Handler handler;

    public SessionsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SessionsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_sessions, viewGroup, false);

        initializer(view);

        return new SessionsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsHolder holder, int i) {
        Model model = sessions.get(i);

//        try {
//            // TODO : Place Code Here
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setSessions(ArrayList<Model> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    public class SessionsHolder extends RecyclerView.ViewHolder {

        public TextView serialTextView, roomTextView, caseTextView, referenceTextView, startTextView, periodTextView, statusTextView, editTextView;
        public LinearLayout roomLinearLayout, caseLinearLayout, referenceLinearLayout, startLinearLayout, periodLinearLayout, statusLinearLayout;

        public SessionsHolder(View view) {
            super(view);
            serialTextView = view.findViewById(R.id.single_item_sessions_serial_textView);
            roomTextView = view.findViewById(R.id.single_item_sessions_room_textView);
            caseTextView = view.findViewById(R.id.single_item_sessions_case_textView);
            referenceTextView = view.findViewById(R.id.single_item_sessions_reference_textView);
            startTextView = view.findViewById(R.id.single_item_sessions_start_textView);
            periodTextView = view.findViewById(R.id.single_item_sessions_period_textView);
            statusTextView = view.findViewById(R.id.single_item_sessions_status_textView);
            editTextView = view.findViewById(R.id.single_item_sessions_edit_textView);
            roomLinearLayout = view.findViewById(R.id.single_item_sessions_room_linearLayout);
            caseLinearLayout = view.findViewById(R.id.single_item_sessions_case_linearLayout);
            referenceLinearLayout = view.findViewById(R.id.single_item_sessions_reference_linearLayout);
            startLinearLayout = view.findViewById(R.id.single_item_sessions_start_linearLayout);
            periodLinearLayout = view.findViewById(R.id.single_item_sessions_period_linearLayout);
            statusLinearLayout = view.findViewById(R.id.single_item_sessions_status_linearLayout);
        }
    }

}
