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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private JSONObject details;

    // Widgets
    private Dialog requestDialog, progressDialog;
    private TextView requestDialogTitle, requestDialogDescription, requestDialogPositive, requestDialogNegative;

    public CenterAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_center, viewGroup, false);

        initializer(view);

        return new CenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int i) {

        try {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                holder.requestTextView.setBackgroundResource(R.drawable.draw_4sdp_primary_ripple);
                holder.editImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
                holder.peopleImageView.setBackgroundResource(R.drawable.draw_4sdp_solitude_ripple);
            }

            int createdAt = (int) centers.get(i).get("created_at");

            switch (createdAt % 16) {
                case 0:
                    holder.gradientImageView.setImageResource(R.drawable.gra_0);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient0);
                    break;
                case 1:
                    holder.gradientImageView.setImageResource(R.drawable.gra_1);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient1);
                    break;
                case 2:
                    holder.gradientImageView.setImageResource(R.drawable.gra_2);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient2);
                    break;
                case 3:
                    holder.gradientImageView.setImageResource(R.drawable.gra_3);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient3);
                    break;
                case 4:
                    holder.gradientImageView.setImageResource(R.drawable.gra_4);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient4);
                    break;
                case 5:
                    holder.gradientImageView.setImageResource(R.drawable.gra_5);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient5);
                    break;
                case 6:
                    holder.gradientImageView.setImageResource(R.drawable.gra_6);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient6);
                    break;
                case 7:
                    holder.gradientImageView.setImageResource(R.drawable.gra_7);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient7);
                    break;
                case 8:
                    holder.gradientImageView.setImageResource(R.drawable.gra_8);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient8);
                    break;
                case 9:
                    holder.gradientImageView.setImageResource(R.drawable.gra_9);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient9);
                    break;
                case 10:
                    holder.gradientImageView.setImageResource(R.drawable.gra_10);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient10);
                    break;
                case 11:
                    holder.gradientImageView.setImageResource(R.drawable.gra_11);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient11);
                    break;
                case 12:
                    holder.gradientImageView.setImageResource(R.drawable.gra_12);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient12);
                    break;
                case 13:
                    holder.gradientImageView.setImageResource(R.drawable.gra_13);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient13);
                    break;
                case 14:
                    holder.gradientImageView.setImageResource(R.drawable.gra_14);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient14);
                    break;
                case 15:
                    holder.gradientImageView.setImageResource(R.drawable.gra_15);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient15);
                    break;
            }

            details = (JSONObject) centers.get(i).get("detail");

            if (!details.isNull("avatar")) {
                JSONObject avatar = details.getJSONObject("avatar");
                JSONObject medium = avatar.getJSONObject("medium");

                Picasso.get().load(medium.getString("url")).placeholder(R.color.White).into(holder.avatarImageView);
            } else {
                Picasso.get().load(R.color.Solitude).placeholder(R.color.White).into(holder.avatarImageView);
            }

            JSONObject manager = (JSONObject) centers.get(i).get("manager");

            holder.titleTextView.setText(details.getString("title"));
            holder.descriptionTextView.setText(details.getString("description"));
            holder.principalTextView.setText(manager.getString("name"));
            holder.addressTextView.setText(details.getString("address"));

            JSONArray phoneNumbers = details.getJSONArray("phone_numbers");

            ArrayList phones = new ArrayList<String>();
            for (int j = 0; j < phoneNumbers.length(); j++) {
                phones.add(phoneNumbers.get(j));
            }

            if (position == -1) {
                adapter.setPhone(phones);

                holder.phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout3", (int) activity.getResources().getDimension(R.dimen._8sdp)));
                holder.phoneRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                holder.phoneRecyclerView.setHasFixedSize(false);
                holder.phoneRecyclerView.setAdapter(adapter);
            }

            expandedCenters.add(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (position == -1) {
            holder.expandLinearLayout.setVisibility(View.GONE);
        } else {
            if (position == i) {
                if (!expandedCenters.get(i)) {
                    holder.expandLinearLayout.setVisibility(View.VISIBLE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_up));

                    expandedCenters.set(i, true);
                } else {
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            } else {
                if (!expandedCenters.get(i)) {
                    holder.expandLinearLayout.setVisibility(View.GONE);
                    holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));

                    expandedCenters.set(i, false);
                }
            }
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);

            position = i;

            notifyDataSetChanged();
        });

        holder.requestTextView.setOnClickListener(v -> {
            holder.requestTextView.setClickable(false);
            handler.postDelayed(() -> holder.requestTextView.setClickable(true), 500);

            try {
                doWork(centers.get(i).get("id").toString(), holder.titleTextView.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        holder.editImageView.setOnClickListener(v -> {
            holder.editImageView.setClickable(false);
            handler.postDelayed(() -> holder.editImageView.setClickable(true), 500);

            activity.startActivityForResult(new Intent(activity, EditCenterActivity.class), 100);
            activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        holder.peopleImageView.setOnClickListener(v -> {
            holder.peopleImageView.setClickable(false);
            handler.postDelayed(() -> holder.peopleImageView.setClickable(true), 500);

            // TODO : See What This Function Do And Then Add The Code
        });
    }

    @Override
    public int getItemCount() {
        return centers.size();
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

    private void doWork(String clinicId, String title) {
        initDialog(title);

        detector();

        listener(clinicId);

        requestDialog.show();
    }

    private void initDialog(String title) {
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
        requestDialogTitle.setText(activity.getResources().getString(R.string.CenterRequestDialogTitle) + " " + title);
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

    private void listener(String clinicId) {
        requestDialogPositive.setOnClickListener(v -> {
            requestDialogPositive.setClickable(false);
            handler.postDelayed(() -> requestDialogPositive.setClickable(true), 500);
            requestDialog.dismiss();

            try {
                progressDialog.show();
                viewModel.request(clinicId);
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        requestDialogNegative.setOnClickListener(v -> {
            requestDialogNegative.setClickable(false);
            handler.postDelayed(() -> requestDialogNegative.setClickable(true), 500);
            requestDialog.dismiss();
        });

        requestDialog.setOnCancelListener(dialog -> requestDialog.dismiss());
    }

    public void observeWork() {
        CenterRepository.workState.observeForever(integer -> {
            if (CenterRepository.work == "request") {
                if (integer == 1) {
                    // Do Nothing

                    progressDialog.dismiss();
                    Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    CenterRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, requestTextView, descriptionTextView, principalTextView, addressTextView;
        public RecyclerView phoneRecyclerView;
        public ImageView gradientImageView, editImageView, peopleImageView, expandImageView;
        public LinearLayout expandLinearLayout;

        public CenterHolder(View view) {
            super(view);
            gradientImageView = view.findViewById(R.id.single_item_center_gradient_imageView);
            avatarImageView = view.findViewById(R.id.single_center_item_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_center_title_textView);
            requestTextView = view.findViewById(R.id.single_item_center_request_textView);
            descriptionTextView = view.findViewById(R.id.single_center_item_description_textView);
            principalTextView = view.findViewById(R.id.single_item_center_principal_textView);
            addressTextView = view.findViewById(R.id.single_item_center_address_textView);
            phoneRecyclerView = view.findViewById(R.id.single_item_center_recyclerView);
            editImageView = view.findViewById(R.id.single_item_center_edit_imageView);
            peopleImageView = view.findViewById(R.id.single_item_center_people_imageView);
            expandImageView = view.findViewById(R.id.single_item_center_expand_imageView);
            expandLinearLayout = view.findViewById(R.id.single_item_center_expand_linearLayout);
        }
    }

}