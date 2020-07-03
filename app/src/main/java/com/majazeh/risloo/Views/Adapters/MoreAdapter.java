package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.majazeh.risloo.Entities.More;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Views.Ui.AboutUsActivity;
import com.majazeh.risloo.Views.Ui.CallUsActivity;
import com.majazeh.risloo.Views.Ui.QuestionActivity;
import com.majazeh.risloo.Views.Ui.TermsConditionsActivity;

import org.json.JSONException;

import java.util.ArrayList;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreHolder> {

    private Activity activity;
    private ArrayList<More> mores;

    public MoreAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_more_single_item, viewGroup, false);
        return new MoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreHolder holder, int i) {

        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.rootLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_snow_ripple);
            }

            holder.titleTextView.setText(mores.get(i).get("title").toString());
            holder.avatarImageView.setImageDrawable((Drawable) mores.get(i).get("image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);

            Handler handler = new Handler();
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            doWork(i);

            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return mores.size();
    }

    public void setMore(ArrayList<More> mores) {
        this.mores = mores;
        notifyDataSetChanged();
    }

    public void doWork(int position) {
        IntentCaller intentCaller = new IntentCaller();

        switch (position) {
            case 0:
                activity.startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case 1:
                activity.startActivity(new Intent(activity, QuestionActivity.class));
                break;
            case 2:
                activity.startActivity(new Intent(activity, CallUsActivity.class));
                break;
            case 3:
                activity.startActivity(new Intent(activity, TermsConditionsActivity.class));
                break;
            case 4:

                break;
            case 5:
                intentCaller.share(activity, activity.getResources().getString(R.string.MoreShareLink), activity.getResources().getString(R.string.MoreShareChooser));
                break;
            case 6:
                intentCaller.rate(activity);
                break;
            case 7:

                break;
        }
    }

    public class MoreHolder extends RecyclerView.ViewHolder {

        public LinearLayout rootLinearLayout;
        public TextView titleTextView;
        public ImageView avatarImageView;

        public MoreHolder(View view) {
            super(view);
            rootLinearLayout = view.findViewById(R.id.activity_more_single_item_root_linearLayout);
            titleTextView = view.findViewById(R.id.activity_more_single_item_title_textView);
            avatarImageView = view.findViewById(R.id.activity_more_single_item_avatar_imageView);
        }
    }

}