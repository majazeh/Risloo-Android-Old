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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterHolder> {

    // ViewModels
    private CenterViewModel viewModel;

    // Vars
    private String check = "";
    private ArrayList<Model> centers;
    private HashMap<Integer, Boolean> expands;
    private int position = -1;

    // Objects
    private Activity activity;
    private Handler handler;

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
        Model model = centers.get(i);

        try {
            int createdAt = (int) model.get("created_at");

            switch (createdAt % 16) {
                case 0:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_0);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient0);
                    break;
                case 1:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_1);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient1);
                    break;
                case 2:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_2);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient2);
                    break;
                case 3:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_3);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient3);
                    break;
                case 4:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_4);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient4);
                    break;
                case 5:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_5);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient5);
                    break;
                case 6:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_6);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient6);
                    break;
                case 7:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_7);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient7);
                    break;
                case 8:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_8);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient8);
                    break;
                case 9:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_9);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient9);
                    break;
                case 10:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_10);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient10);
                    break;
                case 11:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_11);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient11);
                    break;
                case 12:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_12);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient12);
                    break;
                case 13:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_13);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient13);
                    break;
                case 14:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_14);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient14);
                    break;
                case 15:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_15);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient15);
                    break;
            }

            JSONObject manager = (JSONObject) model.get("manager");

            if (!manager.isNull("name")) {
                holder.principalTextView.setText(manager.getString("name"));
            } else {
                holder.principalLinearLayout.setVisibility(View.GONE);
            }

            JSONObject details = (JSONObject) model.get("detail");

            holder.titleTextView.setText(details.getString("title"));

            if (!details.isNull("avatar")) {
                JSONObject avatar = details.getJSONObject("avatar");
                JSONObject medium = avatar.getJSONObject("medium");

                Picasso.get().load(medium.getString("url")).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.VISIBLE);
                holder.subTitleTextView.setText(String.valueOf(details.getString("title").charAt(0)) + String.valueOf(details.getString("title").substring(details.getString("title").lastIndexOf(" ") + 1).charAt(0)));
            }

            if (!details.isNull("description")) {
                holder.descriptionTextView.setText(details.getString("description"));
            } else {
                holder.descriptionLinearLayout.setVisibility(View.GONE);
            }

            if (!details.isNull("address")) {
                holder.addressTextView.setText(details.getString("address"));
            } else {
                holder.addressLinearLayout.setVisibility(View.GONE);
            }

            if (!details.isNull("phone_numbers")) {
                JSONArray phoneNumbers = details.getJSONArray("phone_numbers");

                ArrayList phones = new ArrayList<String>();
                for (int j = 0; j < phoneNumbers.length(); j++) {
                    phones.add(phoneNumbers.get(j));
                }

                PhoneAdapter adapter = new PhoneAdapter(activity);
                adapter.setPhone(phones);

                if (holder.phoneRecyclerView.getAdapter() == null) {
                    holder.phoneRecyclerView.addItemDecoration(new ItemDecorator("horizontalLinearLayout2", (int) activity.getResources().getDimension(R.dimen._4sdp)));
                    holder.phoneRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    holder.phoneRecyclerView.setHasFixedSize(false);
                }
                holder.phoneRecyclerView.setAdapter(adapter);

            } else {
                holder.phoneLinearLayout.setVisibility(View.GONE);
            }

            JSONObject item = model.attributes;

            if (item.isNull("acceptation")) {
                holder.requestTextView.setText(activity.getResources().getString(R.string.CenterRequest));
                holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.White));

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_primary_ripple);
                } else {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_primary);
                }

            } else {
                JSONObject acceptation = (JSONObject) model.get("acceptation");

                if (acceptation.getString("position").equals("manager")) {
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.White));
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_primary);

                    holder.requestTextView.setText(activity.getResources().getString(R.string.CenterManager));
                } else {
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solitude);

                    if (acceptation.isNull("kicked_at")) {
                        if (acceptation.isNull("accepted_at")) {
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CenterAwaiting));
                        } else {
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CenterAccepted));
                        }
                    } else {
                        holder.requestTextView.setText(activity.getResources().getString(R.string.CenterKicked));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (expands.get(i)) {
            holder.expandLinearLayout.setVisibility(View.VISIBLE);
            holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_up));
        } else {
            holder.expandLinearLayout.setVisibility(View.GONE);
            holder.expandImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_chevron_down));
        }

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setClickable(false);
            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);

            if (expands.get(i)) {
                expands.put(i, false);
            } else {
                expands.put(i, true);
            }

            notifyDataSetChanged();
        });

        holder.requestTextView.setOnClickListener(v -> {
            holder.requestTextView.setClickable(false);
            handler.postDelayed(() -> holder.requestTextView.setClickable(true), 500);

            position = i;

            JSONObject item = model.attributes;

            if (item.isNull("acceptation")) {
                try {
                    doWork(model.get("id").toString(), holder.titleTextView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (expands.get(i)) {
                    expands.put(i, false);
                } else {
                    expands.put(i, true);
                }

                notifyDataSetChanged();
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

        handler = new Handler();
    }

    public void setCenter(ArrayList<Model> centers, HashMap<Integer, Boolean> expands, String check) {
        this.centers = centers;
        this.expands = expands;
        this.check = check;
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
        CenterRepository.workState.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (CenterRepository.work == "request") {
                    if (integer == 1) {
                        Model item = centers.get(position);

                        if (check.equals("all")) {
                            JSONObject jsonObject = FileManager.readObjectFromCache(activity.getApplicationContext(), "centers", "all");
                            try {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject response = data.getJSONObject(i);
                                    if (item.get("id").equals(response.getString("id"))) {
                                        item = new Model(response);
                                        centers.remove(position);
                                        centers.add(position, item);
                                    }
                                }
                                notifyItemChanged(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            JSONObject jsonObject = FileManager.readObjectFromCache(activity.getApplicationContext(), "centers", "my");
                            try {
                                JSONArray data = jsonObject.getJSONArray("data");
                                JSONObject acceptation = data.getJSONObject(data.length()-1).getJSONObject("acceptation");
                                if (acceptation.isNull("kicked_at")) {
                                    if (!acceptation.isNull("accepted_at")) {
                                        centers.add(new Model(data.getJSONObject(data.length()-1)));
                                    }
                                }
                                notifyItemChanged(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                        Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    }
                }
            }

        });
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, subTitleTextView, requestTextView, principalTextView, descriptionTextView, addressTextView;
        public RecyclerView phoneRecyclerView;
        public ImageView gradientImageView, editImageView, peopleImageView, expandImageView;
        public LinearLayout expandLinearLayout, principalLinearLayout, descriptionLinearLayout, addressLinearLayout, phoneLinearLayout;

        public CenterHolder(View view) {
            super(view);
            gradientImageView = view.findViewById(R.id.single_item_center_gradient_imageView);
            avatarImageView = view.findViewById(R.id.single_item_center_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_center_title_textView);
            subTitleTextView = view.findViewById(R.id.single_item_center_subtitle_textView);
            requestTextView = view.findViewById(R.id.single_item_center_request_textView);
            editImageView = view.findViewById(R.id.single_item_center_edit_imageView);
            peopleImageView = view.findViewById(R.id.single_item_center_people_imageView);
            expandImageView = view.findViewById(R.id.single_item_center_expand_imageView);
            expandLinearLayout = view.findViewById(R.id.single_item_center_expand_linearLayout);
            principalTextView = view.findViewById(R.id.single_item_center_principal_textView);
            principalLinearLayout = view.findViewById(R.id.single_item_center_principal_linearLayout);
            descriptionTextView = view.findViewById(R.id.single_item_center_description_textView);
            descriptionLinearLayout = view.findViewById(R.id.single_item_center_description_linearLayout);
            addressTextView = view.findViewById(R.id.single_item_center_address_textView);
            addressLinearLayout = view.findViewById(R.id.single_item_center_address_linearLayout);
            phoneRecyclerView = view.findViewById(R.id.single_item_center_phone_recyclerView);
            phoneLinearLayout = view.findViewById(R.id.single_item_center_phone_linearLayout);
        }
    }

}