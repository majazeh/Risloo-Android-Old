package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterHolder> {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private PhoneAdapter adapter;

    // Vars
    private int position = -1;
    private ArrayList<Model> centers;
    private ArrayList<Boolean> expandedCenters = new ArrayList<>();

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private TextView requestDialogTitle, requestDialogDescription, requestDialogPositive, requestDialogNegative;
    private Dialog requestDialog, progressDialog;

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

//        try {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            holder.requestTextView.setBackgroundResource(R.drawable.draw_4sdp_primary_ripple);
            holder.editImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
            holder.peopleImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
        }

        holder.mainRelativeLayout.setBackgroundResource(R.color.Accent);
        holder.expandLinearLayout.setBackgroundResource(R.color.Snow);

        Picasso.get().load(R.drawable.soc_facebook).placeholder(R.color.White).into(holder.avatarImageView);

        holder.titleTextView.setText("مرکز درمانی بقیه الله اعظم");
        holder.descriptionTextView.setText("هم به خانه ای می روهند و را برای اداره ای.");
        holder.principalTextView.setText("دکتر روانشناس");
        holder.addressTextView.setText("قم / خیابان صفاییه / کوچه 20 / پلاک 20");

        if (position == -1) {
            //        adapter.setPhone();

            holder.phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3",(int) activity.getResources().getDimension(R.dimen._8sdp)));
            holder.phoneRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            holder.phoneRecyclerView.setHasFixedSize(false);
            holder.phoneRecyclerView.setAdapter(adapter);
        }

        expandedCenters.add(false);

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        if (position == -1) {
            holder.expandLinearLayout.setVisibility(View.GONE);
        } else {
            if (position == i) {
                if (!expandedCenters.get(i)){
                    holder.expandLinearLayout.setVisibility(View.VISIBLE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_up));

                    expandedCenters.set(i, true);
                } else {
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            } else {
                if (!expandedCenters.get(i)){
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 1000);

            position = i;

            notifyDataSetChanged();
        });

        holder.requestTextView.setOnClickListener(v -> {
            holder.requestTextView.setClickable(false);
            handler.postDelayed(() -> holder.requestTextView.setClickable(true), 1000);

            doWork(i);
        });

        holder.editImageView.setOnClickListener(v -> {
            holder.editImageView.setClickable(false);
            handler.postDelayed(() -> holder.editImageView.setClickable(true), 1000);

            activity.startActivityForResult(new Intent(activity, EditCenterActivity.class), 100);
            activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        holder.peopleImageView.setOnClickListener(v -> {
            holder.peopleImageView.setClickable(false);
            handler.postDelayed(() -> holder.peopleImageView.setClickable(true), 1000);

            // TODO : See What This Function Do And Then Add The Code
        });

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of((FragmentActivity) activity).get(CenterViewModel.class);

        adapter = new PhoneAdapter(activity);

        handler = new Handler();
    }

    public void setCenter(ArrayList<Model> centers) {
        this.centers = centers;
        notifyDataSetChanged();
    }

    private void doWork(int position) {
        initDialog(position);

        detector();

        listener(position);

        requestDialog.show();
    }

    private void initDialog(int position) {
        requestDialog = new Dialog(activity, R.style.DialogTheme);
        requestDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        requestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestDialog.setContentView(R.layout.dialog_action);
        requestDialog.setCancelable(true);

        progressDialog = new Dialog(activity, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(requestDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        requestDialog.getWindow().setAttributes(layoutParams);

        requestDialogTitle = requestDialog.findViewById(R.id.dialog_action_title_textView);
        requestDialogTitle.setText(activity.getResources().getString(R.string.CenterRequestDialogTitle) + " " + "مرکز درمانی بوقی");
        requestDialogDescription = requestDialog.findViewById(R.id.dialog_action_description_textView);
        requestDialogDescription.setText(activity.getResources().getString(R.string.CenterRequestDialogDescription));
        requestDialogPositive = requestDialog.findViewById(R.id.dialog_action_positive_textView);
        requestDialogPositive.setText(activity.getResources().getString(R.string.CenterRequestDialogPositive));
        requestDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        requestDialogNegative = requestDialog.findViewById(R.id.dialog_action_negative_textView);
        requestDialogNegative.setText(activity.getResources().getString(R.string.CenterRequestDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            requestDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            requestDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener(int position) {
        requestDialogPositive.setOnClickListener(v -> {
            requestDialogPositive.setClickable(false);
            handler.postDelayed(() -> requestDialogPositive.setClickable(true), 1000);
            requestDialog.dismiss();

//            try {
//                progressDialog.show();
//                viewModel.request();
//                observeWork();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        });

        requestDialogNegative.setOnClickListener(v -> {
            requestDialogNegative.setClickable(false);
            handler.postDelayed(() -> requestDialogNegative.setClickable(true), 1000);
            requestDialog.dismiss();
        });

        requestDialog.setOnCancelListener(dialog -> requestDialog.dismiss());
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public RelativeLayout mainRelativeLayout;
        public CircleImageView avatarImageView;
        public TextView titleTextView, requestTextView, descriptionTextView, principalTextView, addressTextView;
        public RecyclerView phoneRecyclerView;
        public ImageView editImageView, peopleImageView, expandImageView;
        public LinearLayout expandLinearLayout;

        public CenterHolder(View view) {
            super(view);
            mainRelativeLayout = view.findViewById(R.id.activity_center_single_item_main_relativeLayout);
            avatarImageView = view.findViewById(R.id.activity_center_single_item_avatar_imageView);
            titleTextView = view.findViewById(R.id.activity_center_single_item_title_textView);
            requestTextView = view.findViewById(R.id.activity_center_single_item_request_textView);
            descriptionTextView = view.findViewById(R.id.activity_center_single_item_description_textView);
            principalTextView = view.findViewById(R.id.activity_center_single_item_principal_textView);
            addressTextView = view.findViewById(R.id.activity_center_single_item_address_textView);
            phoneRecyclerView = view.findViewById(R.id.activity_center_single_item_recyclerView);
            editImageView = view.findViewById(R.id.activity_center_single_item_edit_imageView);
            peopleImageView = view.findViewById(R.id.activity_center_single_item_people_imageView);
            expandImageView = view.findViewById(R.id.activity_center_single_item_expand_imageView);
            expandLinearLayout = view.findViewById(R.id.activity_center_single_item_expand_linearLayout);
         }
    }

}