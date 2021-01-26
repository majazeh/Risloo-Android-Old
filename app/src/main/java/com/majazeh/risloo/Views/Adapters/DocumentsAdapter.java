package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.DocumentsActivity;

import java.util.ArrayList;
import java.util.Objects;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.DocumentsHolder> {

    // Vars
    private ArrayList<Model> documents;

    // Objects
    private Activity activity;
    private Handler handler;

    public DocumentsAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DocumentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_documents, viewGroup, false);

        initializer(view);

        return new DocumentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsHolder holder, int i) {
        Model model = documents.get(i);

//        try {
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    private void clearProgress() {
        if (((DocumentsActivity) Objects.requireNonNull(activity)).pagingProgressBar.isShown()) {
            ((DocumentsActivity) Objects.requireNonNull(activity)).loading = false;
            ((DocumentsActivity) Objects.requireNonNull(activity)).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public void setDocument(ArrayList<Model> documents) {
        this.documents = documents;
        notifyDataSetChanged();
    }

    public class DocumentsHolder extends RecyclerView.ViewHolder {

        // TODO : Type Code

        public DocumentsHolder(View view) {
            super(view);
            // TODO : Type Code
        }
    }

}
