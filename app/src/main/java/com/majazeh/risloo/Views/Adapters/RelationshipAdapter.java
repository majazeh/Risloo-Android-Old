package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RelationshipAdapter extends RecyclerView.Adapter<RelationshipAdapter.RelationshipHolder> {

    // Vars
    private ArrayList<Model> relationships;

    // Objects
    private Activity activity;
    private Handler handler;

    public RelationshipAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RelationshipHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_relationship_single_item, viewGroup, false);

        initializer(view);

        return new RelationshipHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelationshipHolder holder, int i) {


    }

    @Override
    public int getItemCount() {
        return relationships.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setRelationship(ArrayList<Model> relationships) {
        this.relationships = relationships;
        notifyDataSetChanged();
    }

    public class RelationshipHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, requestTextView, descriptionTextView, principalTextView;
        public ImageView editImageView, peopleImageVIew, expandImageView;
        public RecyclerView addressRecyclerView, phoneRecyclerView;

        public RelationshipHolder(View view) {
            super(view);
//            subjectTextView = view.findViewById(R.id.activity_question_single_item_subject_textView);
//            answerTextView = view.findViewById(R.id.activity_question_single_item_answer_textView);
//            expandImageView = view.findViewById(R.id.activity_question_single_item_expand_imageView);
         }
    }

}