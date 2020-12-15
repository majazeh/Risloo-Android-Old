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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Activities.CentersActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;
import com.majazeh.risloo.Views.Activities.ImageActivity;
import com.majazeh.risloo.Views.Fragments.AllCentersFragment;
import com.majazeh.risloo.Views.Fragments.MyCentersFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.CentersHolder> {

    // ViewModels
    private AuthViewModel authViewModel;
    private CenterViewModel centerViewModel;

    // Vars
    private String type = "";
    private ArrayList<Model> centers;
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private Dialog requestDialog, progressDialog;
    private TextView requestDialogTitle, requestDialogDescription, requestDialogPositive, requestDialogNegative;

    public CentersAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public CentersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_centers, viewGroup, false);

        initializer(view);

        return new CentersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersHolder holder, int i) {
        Model model = centers.get(i);

        try {
            Intent imageIntent = (new Intent(activity, ImageActivity.class));
            Intent editCenterIntent = (new Intent(activity, EditCenterActivity.class));

            // ID
            if (model.attributes.has("id") && !model.attributes.isNull("id")) {
                editCenterIntent.putExtra("id", model.get("id").toString());
            }

            // Manager
            if (model.attributes.has("manager") && !model.attributes.isNull("manager")) {
                JSONObject manager = (JSONObject) model.get("manager");
                editCenterIntent.putExtra("manager_id", manager.get("id").toString());
                editCenterIntent.putExtra("manager_name", manager.get("name").toString());

                holder.managerLinearLayout.setVisibility(View.VISIBLE);
                holder.managerTextView.setText(manager.getString("name"));
            } else {
                holder.managerLinearLayout.setVisibility(View.GONE);
            }

            // Acceptation
            if (model.attributes.has("acceptation") && !model.attributes.isNull("acceptation")) {
                JSONObject acceptation = (JSONObject) model.get("acceptation");

                if (acceptation.get("position").toString().equals("manager")) {
                    holder.requestTextView.setText(activity.getResources().getString(R.string.CentersOwner));
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.Nero));
                } else {
                    if (acceptation.has("kicked_at") && !acceptation.isNull("kicked_at")) {
                        holder.requestTextView.setText(activity.getResources().getString(R.string.CentersKicked));
                    } else {
                        if (acceptation.has("accepted_at") && !acceptation.isNull("accepted_at"))
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CentersAccepted));
                        else
                            holder.requestTextView.setText(activity.getResources().getString(R.string.CentersAwaiting));
                    }
                    holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.Grey));
                }
                holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude);

                if (authViewModel.hasAccess() || acceptation.get("position").toString().equals("manager")) {
                    holder.editImageView.setVisibility(View.VISIBLE);
                    holder.usersImageView.setVisibility(View.VISIBLE);

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        holder.editImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                        holder.usersImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                    }
                } else {
                    holder.editImageView.setVisibility(View.GONE);
                    holder.usersImageView.setVisibility(View.GONE);
                }

            } else {
                holder.requestTextView.setText(activity.getResources().getString(R.string.CentersRequest));
                holder.requestTextView.setTextColor(activity.getResources().getColor(R.color.White));

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
                } else {
                    holder.requestTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary);
                }

                if (authViewModel.hasAccess()) {
                    holder.editImageView.setVisibility(View.VISIBLE);
                    holder.usersImageView.setVisibility(View.VISIBLE);

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        holder.editImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                        holder.usersImageView.setBackgroundResource(R.drawable.draw_8sdp_solid_solitude_ripple_quartz);
                    }
                } else {
                    holder.editImageView.setVisibility(View.GONE);
                    holder.usersImageView.setVisibility(View.GONE);
                }
            }

            // Type
            if (model.attributes.has("type") && !model.attributes.isNull("type")) {
                editCenterIntent.putExtra("type", model.get("type").toString());
            }

            JSONObject detail = (JSONObject) model.get("detail");

            // Title
            if (detail.has("title") && !detail.isNull("title")) {
                imageIntent.putExtra("title", detail.get("title").toString());
                editCenterIntent.putExtra("title", detail.get("title").toString());

                holder.titleTextView.setText(detail.get("title").toString());
            }

            // Description
            if (detail.has("description") && !detail.isNull("description")) {
                editCenterIntent.putExtra("description", detail.get("description").toString());

                holder.descriptionTextView.setText(detail.get("description").toString());
                holder.descriptionLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.descriptionLinearLayout.setVisibility(View.GONE);
            }

            // Address
            if (detail.has("address") && !detail.isNull("address")) {
                editCenterIntent.putExtra("address", detail.get("address").toString());

                holder.addressTextView.setText(detail.get("address").toString());
                holder.addressLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.addressLinearLayout.setVisibility(View.GONE);
            }

            // Phone
            if (detail.has("phone_numbers") && !detail.isNull("phone_numbers")) {
                JSONArray phoneNumbers = (JSONArray) detail.get("phone_numbers");
                editCenterIntent.putExtra("phone_numbers", detail.get("phone_numbers").toString());

                ArrayList<String> phones = new ArrayList<>();
                for (int j = 0; j < phoneNumbers.length(); j++) {
                    phones.add(phoneNumbers.getString(j));
                }

                PhoneAdapter phoneAdapter = new PhoneAdapter(activity);
                phoneAdapter.setPhone(phones);

                if (holder.phoneRecyclerView.getAdapter() == null) {
                    holder.phoneRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", (int) activity.getResources().getDimension(R.dimen._4sdp), (int) activity.getResources().getDimension(R.dimen._2sdp), (int) activity.getResources().getDimension(R.dimen._4sdp)));
                    holder.phoneRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    holder.phoneRecyclerView.setHasFixedSize(true);
                }

                holder.phoneRecyclerView.setAdapter(phoneAdapter);
                holder.phoneLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.phoneLinearLayout.setVisibility(View.GONE);
            }

            // Avatar
            if (detail.has("avatar") && !detail.isNull("avatar")) {
                JSONObject avatar = (JSONObject) detail.get("avatar");
                JSONObject medium = (JSONObject) avatar.get("medium");

                imageIntent.putExtra("bitmap", false);
                imageIntent.putExtra("image", medium.getString("url"));

                Picasso.get().load(medium.get("url").toString()).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.GONE);
            } else {
                Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(holder.avatarImageView);

                holder.subTitleTextView.setVisibility(View.VISIBLE);
                holder.subTitleTextView.setText(StringManager.firstChars(detail.get("title").toString()));
            }

            // CreatedAt
            if (model.attributes.has("created_at") && !model.attributes.isNull("created_at")) {
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
                handler.postDelayed(() -> holder.itemView.setClickable(true), 250);

                expands.put(i, !expands.get(i));
                notifyDataSetChanged();
            });

            holder.requestTextView.setOnClickListener(v -> {
                holder.requestTextView.setClickable(false);
                handler.postDelayed(() -> holder.requestTextView.setClickable(true), 250);

                if (!model.attributes.isNull("acceptation")) {
                    expands.put(i, !expands.get(i));
                    notifyDataSetChanged();
                } else {
                    if (type.equals("all")) {
                        showDialog(i, model, holder.titleTextView.getText().toString());
                    } else {
                        ExceptionGenerator.getException(false, 0, null, "CantRequestException");
                        Toast.makeText(activity, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.avatarImageView.setOnClickListener(v -> {
                holder.avatarImageView.setClickable(false);
                handler.postDelayed(() -> holder.avatarImageView.setClickable(true), 250);

                if (!detail.isNull("title") && !detail.isNull("avatar")) {
                    clearProgress();

                    activity.startActivity(imageIntent);
                } else {
                    expands.put(i, !expands.get(i));
                    notifyDataSetChanged();
                }
            });

            holder.editImageView.setOnClickListener(v -> {
                holder.editImageView.setClickable(false);
                handler.postDelayed(() -> holder.editImageView.setClickable(true), 250);

                clearProgress();

                activity.startActivityForResult(editCenterIntent, 100);
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
            });

            holder.usersImageView.setOnClickListener(v -> {
                holder.usersImageView.setClickable(false);
                handler.postDelayed(() -> holder.usersImageView.setClickable(true), 250);

                clearProgress();

                // TODO : Call Index Users
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
        authViewModel = ((CentersActivity) Objects.requireNonNull(activity)).authViewModel;
        centerViewModel = ((CentersActivity) Objects.requireNonNull(activity)).centerViewModel;

        handler = new Handler();
    }

    private void showDialog(int position, Model model, String title) {
        initDialog(title);

        detector();

        listener(position, model);

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

        WindowManager.LayoutParams layoutParamsRequestDialog = new WindowManager.LayoutParams();
        layoutParamsRequestDialog.copyFrom(requestDialog.getWindow().getAttributes());
        layoutParamsRequestDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsRequestDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        requestDialog.getWindow().setAttributes(layoutParamsRequestDialog);

        requestDialogTitle = requestDialog.findViewById(R.id.dialog_action_title_textView);
        requestDialogTitle.setText(activity.getResources().getString(R.string.CentersRequestDialogTitle) + " " + title);
        requestDialogDescription = requestDialog.findViewById(R.id.dialog_action_description_textView);
        requestDialogDescription.setText(activity.getResources().getString(R.string.CentersRequestDialogDescription));
        requestDialogPositive = requestDialog.findViewById(R.id.dialog_action_positive_textView);
        requestDialogPositive.setText(activity.getResources().getString(R.string.CentersRequestDialogPositive));
        requestDialogPositive.setTextColor(activity.getResources().getColor(R.color.PrimaryDark));
        requestDialogNegative = requestDialog.findViewById(R.id.dialog_action_negative_textView);
        requestDialogNegative.setText(activity.getResources().getString(R.string.CentersRequestDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            requestDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            requestDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener(int position, Model model) {
        requestDialogPositive.setOnClickListener(v -> {
            requestDialogPositive.setClickable(false);
            handler.postDelayed(() -> requestDialogPositive.setClickable(true), 250);
            requestDialog.dismiss();

            doWork(position, model);
        });

        requestDialogNegative.setOnClickListener(v -> {
            requestDialogNegative.setClickable(false);
            handler.postDelayed(() -> requestDialogNegative.setClickable(true), 250);
            requestDialog.dismiss();
        });

        requestDialog.setOnCancelListener(dialog -> requestDialog.dismiss());
    }

    private void doWork(int position, Model model) {
        try {
            progressDialog.show();

            centerViewModel.request(model.get("id").toString());
            observeWork(position, model);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(int position, Model model) {
        CenterRepository.workState.observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (CenterRepository.work.equals("request")) {
                    if (integer == 1) {

                        try {
                            JSONObject allJsonObject = FileManager.readObjectFromCache(activity.getApplicationContext(), "centers" + "/" + "all");
                            JSONArray allCenters = (JSONArray) allJsonObject.get("data");

                            for (int i = 0; i < allCenters.length(); i++) {
                                JSONObject center = allCenters.getJSONObject(i);

                                if (model.get("id").toString().equals(center.get("id").toString())) {
                                    Model changedModel = new Model(center);

                                    replaceCenter(position, changedModel);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void setCenter(ArrayList<Model> centers, HashMap<Integer, Boolean> expands, String type) {
        this.centers = centers;
        this.expands = expands;
        this.type = type;
        notifyDataSetChanged();
    }

    public void replaceCenter(int position, Model model) {
        centers.set(position, model);
        expands.put(position, false);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    private void clearProgress() {
        Fragment allFragment = ((CentersActivity) Objects.requireNonNull(activity)).tabCentersAdapter.allFragment;
        if (((AllCentersFragment) allFragment).pagingProgressBar.isShown()) {
            ((CentersActivity) Objects.requireNonNull(activity)).loadingAll = false;
            ((AllCentersFragment) allFragment).pagingProgressBar.setVisibility(View.GONE);
        }
        Fragment myFragment = ((CentersActivity) Objects.requireNonNull(activity)).tabCentersAdapter.myFragment;
        if (((MyCentersFragment) myFragment).pagingProgressBar.isShown()) {
            ((CentersActivity) Objects.requireNonNull(activity)).loadingMy = false;
            ((MyCentersFragment) myFragment).pagingProgressBar.setVisibility(View.GONE);
        }
    }

    public class CentersHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatarImageView;
        public TextView titleTextView, subTitleTextView, requestTextView, managerTextView, descriptionTextView, addressTextView;
        public RecyclerView phoneRecyclerView;
        public ImageView gradientImageView, editImageView, usersImageView, expandImageView;
        public LinearLayout expandLinearLayout, managerLinearLayout, descriptionLinearLayout, addressLinearLayout, phoneLinearLayout;

        public CentersHolder(View view) {
            super(view);
            avatarImageView = view.findViewById(R.id.single_item_centers_avatar_imageView);
            titleTextView = view.findViewById(R.id.single_item_centers_title_textView);
            subTitleTextView = view.findViewById(R.id.single_item_centers_subtitle_textView);
            requestTextView = view.findViewById(R.id.single_item_centers_request_textView);
            gradientImageView = view.findViewById(R.id.single_item_centers_gradient_imageView);
            editImageView = view.findViewById(R.id.single_item_centers_edit_imageView);
            usersImageView = view.findViewById(R.id.single_item_centers_users_imageView);
            expandImageView = view.findViewById(R.id.single_item_centers_expand_imageView);
            expandLinearLayout = view.findViewById(R.id.single_item_centers_expand_linearLayout);
            managerTextView = view.findViewById(R.id.single_item_centers_manager_textView);
            managerLinearLayout = view.findViewById(R.id.single_item_centers_manager_linearLayout);
            descriptionTextView = view.findViewById(R.id.single_item_centers_description_textView);
            descriptionLinearLayout = view.findViewById(R.id.single_item_centers_description_linearLayout);
            addressTextView = view.findViewById(R.id.single_item_centers_address_textView);
            addressLinearLayout = view.findViewById(R.id.single_item_centers_address_linearLayout);
            phoneRecyclerView = view.findViewById(R.id.single_item_centers_phone_recyclerView);
            phoneLinearLayout = view.findViewById(R.id.single_item_centers_phone_linearLayout);
        }
    }

}