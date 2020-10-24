package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.ImageActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.CenterHolder> {

    // ViewModels
    private CenterViewModel viewModel;

    // Vars
    private int position = -1;
    private String check = "";
    private ArrayList<Model> centers;
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
            Intent editIntent = (new Intent(activity, EditCenterActivity.class));
            Intent imageIntent = (new Intent(activity, ImageActivity.class));

            editIntent.putExtra("id", (String) model.get("id"));
            editIntent.putExtra("type", (String) model.get("type"));

            int createdAt = (int) model.get("created_at");

            switch (createdAt % 16) {
                case 0:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_0);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient0);
                    break;
                case 1:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_1);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient1);
                    break;
                case 2:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_2);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient2);
                    break;
                case 3:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_3);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient3);
                    break;
                case 4:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_4);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient4);
                    break;
                case 5:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_5);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient5);
                    break;
                case 6:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_6);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient6);
                    break;
                case 7:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_7);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient7);
                    break;
                case 8:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_8);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient8);
                    break;
                case 9:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_9);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient9);
                    break;
                case 10:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_nero5p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Nero));

                    holder.gradientImageView.setImageResource(R.drawable.gra_10);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient10);
                    break;
                case 11:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_11);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient11);
                    break;
                case 12:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_12);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient12);
                    break;
                case 13:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_13);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient13);
                    break;
                case 14:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_14);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient14);
                    break;
                case 15:
                    holder.expandImageView.setBackgroundResource(R.drawable.draw_oval_solid_white15p);
                    ImageViewCompat.setImageTintList(holder.expandImageView, AppCompatResources.getColorStateList(activity, R.color.Solitude));

                    holder.gradientImageView.setImageResource(R.drawable.gra_15);
                    holder.expandLinearLayout.setBackgroundResource(R.color.Gradient15);
                    break;
            }

            JSONObject details = (JSONObject) model.get("detail");

            if (!details.isNull("title")) {
                if (model.get("type").equals("counseling_center")) {
                    editIntent.putExtra("title", details.getString("title"));
                }
                imageIntent.putExtra("title", details.getString("title"));
                holder.titleTextView.setText(details.getString("title"));
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
                holder.subTitleTextView.setText(String.valueOf(details.getString("title").charAt(0)) + String.valueOf(details.getString("title").substring(details.getString("title").lastIndexOf(" ") + 1).charAt(0)));
            }

            JSONObject manager = (JSONObject) model.get("manager");
            if (!manager.isNull("name")) {
                editIntent.putExtra("manager_id",manager.getString("id"));
                editIntent.putExtra("manager", manager.getString("name"));
                holder.managerLinearLayout.setVisibility(View.VISIBLE);
                holder.managerTextView.setText(manager.getString("name"));
            } else {
                holder.managerLinearLayout.setVisibility(View.GONE);
            }

            if (!details.isNull("description")) {
                editIntent.putExtra("description", details.getString("description"));
                holder.descriptionLinearLayout.setVisibility(View.VISIBLE);
                holder.descriptionTextView.setText(details.getString("description"));
            } else {
                holder.descriptionLinearLayout.setVisibility(View.GONE);
            }

            if (!details.isNull("address")) {
                editIntent.putExtra("address", details.getString("address"));
                holder.addressLinearLayout.setVisibility(View.VISIBLE);
                holder.addressTextView.setText(details.getString("address"));
            } else {
                holder.addressLinearLayout.setVisibility(View.GONE);
            }

            if (!details.isNull("phone_numbers")) {
                JSONArray phoneNumbers = details.getJSONArray("phone_numbers");
                editIntent.putExtra("phone_numbers", String.valueOf(details.getJSONArray("phone_numbers")));

                ArrayList<String> phones = new ArrayList<>();
                for (int j = 0; j < phoneNumbers.length(); j++) {
                    phones.add(phoneNumbers.getString(j));
                }

                PhoneAdapter adapter = new PhoneAdapter(activity);
                adapter.setPhone(phones);

                if (holder.phoneRecyclerView.getAdapter() == null) {
                    holder.phoneRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", (int) activity.getResources().getDimension(R.dimen._4sdp), (int) activity.getResources().getDimension(R.dimen._2sdp), (int) activity.getResources().getDimension(R.dimen._4sdp)));
                    holder.phoneRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    holder.phoneRecyclerView.setHasFixedSize(false);
                }

                holder.phoneRecyclerView.setAdapter(adapter);
                holder.phoneLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.phoneLinearLayout.setVisibility(View.GONE);
            }

            JSONObject item = model.attributes;

            if (item.isNull("acceptation")) {
                holder.requestTextView.setText(activity.getResources().getString(R.string.CenterRequest));
                holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.White));

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
                } else {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary);
                }

                if (sharedPreferences.getBoolean("hasAccess", false)) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        holder.editImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                    }
                } else {
                    holder.editImageView.setVisibility(View.GONE);
                }

            } else {
                JSONObject acceptation = (JSONObject) model.get("acceptation");

                if (acceptation.getString("position").equals("manager")) {
                    holder.requestTextView.setText(activity.getResources().getString(R.string.CenterOwner));
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.Nero));
                } else {
                    if (acceptation.isNull("kicked_at")) {
                        if (acceptation.isNull("accepted_at")) {
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CenterAwaiting));
                        } else {
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CenterAccepted));
                        }
                    } else {
                        holder.requestTextView.setText(activity.getResources().getString(R.string.CenterKicked));
                    }
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                }

                holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude);

                if (sharedPreferences.getBoolean("hasAccess", false) || acceptation.getString("position").equals("manager")) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        holder.editImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                    }
                } else {
                    holder.editImageView.setVisibility(View.GONE);
                }

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
                handler.postDelayed(() -> holder.itemView.setClickable(true), 300);

                if (expands.get(i)) {
                    expands.put(i, false);
                } else {
                    expands.put(i, true);
                }

                notifyDataSetChanged();
            });

            holder.requestTextView.setOnClickListener(v -> {
                holder.requestTextView.setClickable(false);
                handler.postDelayed(() -> holder.requestTextView.setClickable(true), 300);

                position = i;

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

            holder.avatarImageView.setOnClickListener(v -> {
                holder.avatarImageView.setClickable(false);
                handler.postDelayed(() -> holder.avatarImageView.setClickable(true), 300);

                if (!details.isNull("title") && !details.isNull("avatar")) {
                    activity.startActivity(imageIntent);
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
                handler.postDelayed(() -> holder.editImageView.setClickable(true), 300);

                activity.startActivityForResult(editIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
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
        return centers.size();
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();
    }

    public void setCenter(ArrayList<Model> centers, HashMap<Integer, Boolean> expands, String check, CenterViewModel viewModel) {
        this.centers = centers;
        this.expands = expands;
        this.check = check;
        this.viewModel = viewModel;
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
        Objects.requireNonNull(requestDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        requestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestDialog.setContentView(R.layout.dialog_action);
        requestDialog.setCancelable(true);
        progressDialog = new Dialog(activity, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
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
            requestDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            requestDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener(String clinicId) {
        requestDialogPositive.setOnClickListener(v -> {
            requestDialogPositive.setClickable(false);
            handler.postDelayed(() -> requestDialogPositive.setClickable(true), 300);
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
            handler.postDelayed(() -> requestDialogNegative.setClickable(true), 300);
            requestDialog.dismiss();
        });

        requestDialog.setOnCancelListener(dialog -> requestDialog.dismiss());
    }

    public void observeWork() {
        CenterRepository.workState.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (CenterRepository.work.equals("request")) {
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
                                JSONObject acceptation = data.getJSONObject(data.length() - 1).getJSONObject("acceptation");
                                if (acceptation.isNull("kicked_at")) {
                                    if (!acceptation.isNull("accepted_at")) {
                                        centers.add(new Model(data.getJSONObject(data.length() - 1)));
                                    }
                                }
                                notifyItemChanged(position);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        CenterRepository.workState.removeObserver(this);
                    }
                }
            }

        });
    }

    public class CenterHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, subTitleTextView, requestTextView, managerTextView, descriptionTextView, addressTextView;
        public RecyclerView phoneRecyclerView;
        public ImageView gradientImageView, editImageView, peopleImageView, expandImageView;
        public LinearLayout expandLinearLayout, managerLinearLayout, descriptionLinearLayout, addressLinearLayout, phoneLinearLayout;

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
            managerTextView = view.findViewById(R.id.single_item_center_manager_textView);
            managerLinearLayout = view.findViewById(R.id.single_item_center_manager_linearLayout);
            descriptionTextView = view.findViewById(R.id.single_item_center_description_textView);
            descriptionLinearLayout = view.findViewById(R.id.single_item_center_description_linearLayout);
            addressTextView = view.findViewById(R.id.single_item_center_address_textView);
            addressLinearLayout = view.findViewById(R.id.single_item_center_address_linearLayout);
            phoneRecyclerView = view.findViewById(R.id.single_item_center_phone_recyclerView);
            phoneLinearLayout = view.findViewById(R.id.single_item_center_phone_linearLayout);
        }
    }

}