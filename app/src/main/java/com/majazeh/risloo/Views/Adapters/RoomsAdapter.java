package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
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
import com.majazeh.risloo.Views.Activities.ImageActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsHolder> {

    // Vars
    private ArrayList<Model> rooms;

    // Objects
    private Activity activity;
    private Handler handler;

    public RoomsAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RoomsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_rooms, viewGroup, false);

        initializer(view);

        return new RoomsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsHolder holder, int i) {
        Model model = rooms.get(i);

        try {
            Intent imageIntent = (new Intent(activity, ImageActivity.class));

            int createdAt = (int) model.get("created_at");

            switch (createdAt % 16) {
                case 0:
                    holder.gradientImageView.setImageResource(R.drawable.gra_0);
                    break;
                case 1:
                    holder.gradientImageView.setImageResource(R.drawable.gra_1);
                    break;
                case 2:
                    holder.gradientImageView.setImageResource(R.drawable.gra_2);
                    break;
                case 3:
                    holder.gradientImageView.setImageResource(R.drawable.gra_3);
                    break;
                case 4:
                    holder.gradientImageView.setImageResource(R.drawable.gra_4);
                    break;
                case 5:
                    holder.gradientImageView.setImageResource(R.drawable.gra_5);
                    break;
                case 6:
                    holder.gradientImageView.setImageResource(R.drawable.gra_6);
                    break;
                case 7:
                    holder.gradientImageView.setImageResource(R.drawable.gra_7);
                    break;
                case 8:
                    holder.gradientImageView.setImageResource(R.drawable.gra_8);
                    break;
                case 9:
                    holder.gradientImageView.setImageResource(R.drawable.gra_9);
                    break;
                case 10:
                    holder.gradientImageView.setImageResource(R.drawable.gra_10);
                    break;
                case 11:
                    holder.gradientImageView.setImageResource(R.drawable.gra_11);
                    break;
                case 12:
                    holder.gradientImageView.setImageResource(R.drawable.gra_12);
                    break;
                case 13:
                    holder.gradientImageView.setImageResource(R.drawable.gra_13);
                    break;
                case 14:
                    holder.gradientImageView.setImageResource(R.drawable.gra_14);
                    break;
                case 15:
                    holder.gradientImageView.setImageResource(R.drawable.gra_15);
                    break;
            }

            JSONObject manager = (JSONObject) model.get("manager");

            if (!manager.isNull("name")) {
                imageIntent.putExtra("title", manager.get("name").toString());
                holder.titleTextView.setText(manager.get("name").toString());
            }

            JSONObject center = (JSONObject) model.get("center");
            JSONObject details = (JSONObject) center.get("detail");

            if (!details.isNull("title")) {
                if (center.getString("type").equals("personal_clinic")) {
                    holder.typeTextView.setText(activity.getResources().getString(R.string.RoomsPersonalClinic));
                } else {
                    holder.typeTextView.setText(details.getString("title"));
                }
            }

            if (!details.isNull("avatar")) {
                JSONObject avatar = details.getJSONObject("avatar");
                JSONObject medium = avatar.getJSONObject("medium");

                imageIntent.putExtra("bitmap", false);
                imageIntent.putExtra("image", medium.getString("url"));

                Picasso.get().load(medium.getString("url")).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.VISIBLE);
                holder.subTitleTextView.setText(String.valueOf(manager.getString("name").charAt(0)) + String.valueOf(manager.getString("name").substring(manager.getString("name").lastIndexOf(" ") + 1).charAt(0)));
            }

            holder.avatarImageView.setOnClickListener(v -> {
                holder.avatarImageView.setClickable(false);
                handler.postDelayed(() -> holder.avatarImageView.setClickable(true), 300);

                if (!manager.isNull("name") && !details.isNull("avatar")) {
                    activity.startActivity(imageIntent);
                }
            });

            holder.peopleImageView.setOnClickListener(v -> {
                holder.peopleImageView.setClickable(false);
                handler.postDelayed(() -> holder.peopleImageView.setClickable(true), 300);

                // TODO : See What This Function Do And Then Add The Code
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    private void initializer(View view) {
        handler = new Handler();
    }

    public void setRoom(ArrayList<Model> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    public class RoomsHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, subTitleTextView, typeTextView;
        public ImageView gradientImageView, peopleImageView;

        public RoomsHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.single_item_rooms_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_rooms_title_textView);
            subTitleTextView = view.findViewById(R.id.single_item_rooms_subtitle_textView);
            typeTextView = view.findViewById(R.id.single_item_rooms_type_textView);
            gradientImageView = view.findViewById(R.id.single_item_rooms_gradient_imageView);
            peopleImageView = view.findViewById(R.id.single_item_rooms_people_imageView);
        }
    }

}