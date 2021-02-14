package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.BitmapManager;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Views.Activities.EditAccountActivity;
import com.majazeh.risloo.Views.Activities.ImageActivity;
import com.majazeh.risloo.Views.Dialogs.ImageDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAvatarFragment extends Fragment {

    // Vars
    public String avatarFilePath = "";

    // Objects
    private Activity activity;
    private ImageDialog imageDialog;
    public Bitmap avatarBitmap;

    // Widgets
    public CircleImageView avatarCircleImageView;
    private ImageView avatarImageView;
    private Button editButton;

    public EditAvatarFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_edit_avatar, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        imageDialog = new ImageDialog(getActivity());
        imageDialog.setType("EditAvatar");

        avatarCircleImageView = view.findViewById(R.id.edit_avatar_circleImageView);

        avatarImageView = view.findViewById(R.id.edit_avatar_imageView);

        editButton = view.findViewById(R.id.edit_avatar_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            avatarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_border_quartz_ripple_quartz);

            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        avatarCircleImageView.setOnClickListener(v -> {
            avatarCircleImageView.setClickable(false);
            ((EditAccountActivity) getActivity()).handler.postDelayed(() -> avatarCircleImageView.setClickable(true), 250);

            navigator();
        });

        avatarImageView.setOnClickListener(v -> {
            avatarImageView.setClickable(false);
            ((EditAccountActivity) getActivity()).handler.postDelayed(() -> avatarImageView.setClickable(true), 250);

            imageDialog.show(getActivity().getSupportFragmentManager(), "imageBottomSheet");
        });

        editButton.setOnClickListener(v -> {
            if (avatarBitmap == null) {
                ExceptionGenerator.getException(false, 0, null, "PictureException");
                Toast.makeText(getActivity(), ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            } else {
                doWork();
            }
        });
    }

    private void setData() {
        if (((EditAccountActivity) getActivity()).authViewModel.getAvatar().equals("")) {
            avatarCircleImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_user_circle_solid, null));
        } else {
            Picasso.get().load(((EditAccountActivity) getActivity()).authViewModel.getAvatar()).placeholder(R.color.Solitude).into(avatarCircleImageView);
        }
    }

    private void doWork() {
        FileManager.writeBitmapToCache(getActivity(), BitmapManager.modifyOrientation(avatarBitmap, avatarFilePath), "image");

        try {
            ((EditAccountActivity) getActivity()).progressDialog.show();

            ((EditAccountActivity) getActivity()).authViewModel.editAvatar();
            ((EditAccountActivity) getActivity()).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void navigator() {
        if (!((EditAccountActivity) getActivity()).authViewModel.getName().equals("") && !((EditAccountActivity) getActivity()).authViewModel.getAvatar().equals("")) {
            Intent imageIntent = (new Intent(getActivity(), ImageActivity.class));

            imageIntent.putExtra("title", ((EditAccountActivity) getActivity()).authViewModel.getName());
            if (avatarBitmap == null) {
                imageIntent.putExtra("bitmap", false);
                imageIntent.putExtra("image", ((EditAccountActivity) getActivity()).authViewModel.getAvatar());
            } else {
                imageIntent.putExtra("bitmap", true);
                imageIntent.putExtra("path", avatarFilePath);

                FileManager.writeBitmapToCache(getActivity(), avatarBitmap, "image");
            }

            startActivity(imageIntent);
        }
    }

}