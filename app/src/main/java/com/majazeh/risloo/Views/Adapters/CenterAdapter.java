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

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterHolder> {

    // Vars
    private ArrayList<Model> centers;

    // Objects
    private Activity activity;
    private Handler handler;

    public CenterAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_center_single_item, viewGroup, false);

        initializer(view);

        return new CenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int i) {


    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setCenter(ArrayList<Model> centers) {
        this.centers = centers;
        notifyDataSetChanged();
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, requestTextView, descriptionTextView, principalTextView;
        public ImageView editImageView, peopleImageVIew, expandImageView;
        public RecyclerView addressRecyclerView, phoneRecyclerView;

        public CenterHolder(View view) {
            super(view);
//            subjectTextView = view.findViewById(R.id.activity_question_single_item_subject_textView);
//            answerTextView = view.findViewById(R.id.activity_question_single_item_answer_textView);
//            expandImageView = view.findViewById(R.id.activity_question_single_item_expand_imageView);
         }
    }

}