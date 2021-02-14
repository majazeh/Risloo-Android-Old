package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Utils.Managers.ParamsManager;
import com.majazeh.risloo.Utils.Widgets.SingleNumberPicker;
import com.majazeh.risloo.Views.Activities.EditAccountActivity;

import org.json.JSONException;

public class EditPersonalFragment extends Fragment {

    // Vars
    private String name = "", username = "", mobile = "", email = "", birthday = "", gender = "", status = "", type = "";
    private int year, month, day;
    public boolean birthdayException = false, genderException = false, statusException = false, typeException = false;

    // Objects
    private Activity activity;

    // Widgets
    public EditText nameEditText, usernameEditText, mobileEditText, emailEditText;
    public TextView birthdayTextView;
    public TabLayout genderTabLayout, statusTabLayout, typeTabLayout;
    private Button editButton;
    private Dialog birthdayDialog;
    private SingleNumberPicker yearNumberPicker, monthNumberPicker, dayNumberPicker;
    private TextView birthdayDialogPositive, birthdayDialogNegative;

    public EditPersonalFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_edit_personal, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setData();

        setCustomPicker();

        return view;
    }

    private void initializer(View view) {
        nameEditText = view.findViewById(R.id.edit_personal_name_editText);
        usernameEditText = view.findViewById(R.id.edit_personal_username_editText);
        mobileEditText = view.findViewById(R.id.edit_personal_mobile_editText);
        emailEditText = view.findViewById(R.id.edit_personal_email_editText);

        birthdayTextView = view.findViewById(R.id.edit_personal_birthday_textView);

        genderTabLayout = view.findViewById(R.id.edit_personal_gender_tabLayout);
        statusTabLayout = view.findViewById(R.id.edit_personal_status_tabLayout);
        typeTabLayout = view.findViewById(R.id.edit_personal_type_tabLayout);

        editButton = view.findViewById(R.id.edit_personal_button);

        birthdayDialog = new Dialog(getActivity(), R.style.DialogTheme);
        birthdayDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        birthdayDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        birthdayDialog.setContentView(R.layout.dialog_date);
        birthdayDialog.setCancelable(true);
        birthdayDialog.getWindow().setAttributes(ParamsManager.set(birthdayDialog));

        yearNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_year_NumberPicker);
        monthNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_month_NumberPicker);
        dayNumberPicker = birthdayDialog.findViewById(R.id.dialog_date_day_NumberPicker);

        birthdayDialogPositive = birthdayDialog.findViewById(R.id.dialog_date_positive_textView);
        birthdayDialogNegative = birthdayDialog.findViewById(R.id.dialog_date_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            birthdayDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            birthdayDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(nameEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(nameEditText);
                }
            }
            return false;
        });

        usernameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!usernameEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(usernameEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(usernameEditText);
                }
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(mobileEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(mobileEditText);
                }
            }
            return false;
        });

        emailEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!emailEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(emailEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(emailEditText);
                }
            }
            return false;
        });

        birthdayTextView.setOnTouchListener((v, event) -> {
            birthdayTextView.setClickable(false);
            ((EditAccountActivity) getActivity()).handler.postDelayed(() -> birthdayTextView.setClickable(true), 250);

            if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
            }

            if (birthdayException) {
                birthdayException = ((EditAccountActivity) getActivity()).exceptionManager.clear(birthdayTextView);
            }

            birthdayDialog.show();
            return false;
        });

        monthNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (picker == monthNumberPicker) {
                if (newVal <= 6) {
                    dayNumberPicker.setMaxValue(31);
                } else {
                    dayNumberPicker.setMaxValue(30);
                }
            }
        });

        genderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                    ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                }

                if (genderException) {
                    genderException = ((EditAccountActivity) getActivity()).exceptionManager.clear(genderTabLayout);
                }

                switch (tab.getPosition()) {
                    case 0:
                        gender = "male";
                        break;
                    case 1:
                        gender = "female";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        statusTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                    ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                }

                if (statusException) {
                    statusException = ((EditAccountActivity) getActivity()).exceptionManager.clear(statusTabLayout);
                }

                switch (tab.getPosition()) {
                    case 0:
                        status = "active";
                        break;
                    case 1:
                        status = "waiting";
                        break;
                    case 2:
                        status = "closed";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        typeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                    ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                }

                if (typeException) {
                    typeException = ((EditAccountActivity) getActivity()).exceptionManager.clear(typeTabLayout);
                }

                switch (tab.getPosition()) {
                    case 0:
                        type = "admin";
                        break;
                    case 1:
                        type = "psychology";
                        break;
                    case 2:
                        type = "operator";
                        break;
                    case 3:
                        type = "clinic_center";
                        break;
                    case 4:
                        type = "client";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        editButton.setOnClickListener(v -> {
            if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
            }

            if (nameEditText.length() == 0) {
                ((EditAccountActivity) getActivity()).controlEditText.error(getActivity(), nameEditText);
            }
            if (usernameEditText.length() == 0) {
                ((EditAccountActivity) getActivity()).controlEditText.error(getActivity(), usernameEditText);
            }
            if (mobileEditText.length() == 0) {
                ((EditAccountActivity) getActivity()).controlEditText.error(getActivity(), mobileEditText);
            }

            if (nameEditText.length() != 0 && usernameEditText.length() != 0 && mobileEditText.length() != 0) {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), nameEditText);
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), usernameEditText);
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), mobileEditText);

                doWork();
            }
        });

        birthdayDialogPositive.setOnClickListener(v -> {
            birthdayDialogPositive.setClickable(false);
            ((EditAccountActivity) getActivity()).handler.postDelayed(() -> birthdayDialogPositive.setClickable(true), 250);
            birthdayDialog.dismiss();

            year = yearNumberPicker.getValue();
            month = monthNumberPicker.getValue();
            day = dayNumberPicker.getValue();

            if (month < 10) {
                if (day < 10)
                    birthday = year + "-" + "0" + month + "-" + "0" + day;
                else
                    birthday = year + "-" + "0" + month + "-" + day;
            } else {
                if (day < 10)
                    birthday = year + "-" + month + "-" + "0" + day;
                else
                    birthday = year + "-" + month + "-" + day;
            }

            birthdayTextView.setText(birthday);
        });

        birthdayDialogNegative.setOnClickListener(v -> {
            birthdayDialogNegative.setClickable(false);
            ((EditAccountActivity) getActivity()).handler.postDelayed(() -> birthdayDialogNegative.setClickable(true), 250);
            birthdayDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });

        birthdayDialog.setOnCancelListener(dialog -> {
            birthdayDialog.dismiss();

            yearNumberPicker.setValue(year);
            monthNumberPicker.setValue(month);
            dayNumberPicker.setValue(day);
        });
    }

    private void setData() {
        if (((EditAccountActivity) getActivity()).authViewModel.getName().equals("")) {
            nameEditText.setHint(getResources().getString(R.string.EditPersonalNameHint));
        } else {
            name = ((EditAccountActivity) getActivity()).authViewModel.getName();
            nameEditText.setText(name);
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getUsername().equals("")) {
            usernameEditText.setHint(getResources().getString(R.string.EditPersonalUsernameHint));
        } else {
            username = ((EditAccountActivity) getActivity()).authViewModel.getUsername();
            usernameEditText.setText(username);
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getMobile().equals("")) {
            mobileEditText.setHint(getResources().getString(R.string.EditPersonalMobileHint));
        } else {
            mobile = ((EditAccountActivity) getActivity()).authViewModel.getMobile();
            mobileEditText.setText(mobile);
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getEmail().equals("")) {
            emailEditText.setHint(getResources().getString(R.string.EditPersonalEmailHint));
        } else {
            email = ((EditAccountActivity) getActivity()).authViewModel.getEmail();
            emailEditText.setText(email);
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getBirthday().equals("")) {
            birthday = getResources().getString(R.string.AuthBirthdayDefault);
            birthdayTextView.setText(birthday);
        } else {
            birthday = ((EditAccountActivity) getActivity()).authViewModel.getBirthday();
            birthdayTextView.setText(birthday);
        }

        year = Integer.parseInt(DateManager.dateToString("yyyy", DateManager.stringToDate("yyyy-MM-dd", birthday)));
        month = Integer.parseInt(DateManager.dateToString("MM", DateManager.stringToDate("yyyy-MM-dd", birthday)));
        day = Integer.parseInt(DateManager.dateToString("dd", DateManager.stringToDate("yyyy-MM-dd", birthday)));

        if (((EditAccountActivity) getActivity()).authViewModel.getGender().equals(getActivity().getResources().getString(R.string.EditPersonalGenderFemale))) {
            gender = "female";
            genderTabLayout.getTabAt(1).select();
        } else {
            gender = "male";
            genderTabLayout.getTabAt(0).select();
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getStatus().equals(getActivity().getResources().getString(R.string.EditPersonalStatusWaiting))) {
            status = "waiting";
            statusTabLayout.getTabAt(1).select();
        } else if (((EditAccountActivity) getActivity()).authViewModel.getStatus().equals(getActivity().getResources().getString(R.string.EditPersonalStatusClosed))) {
            status = "closed";
            statusTabLayout.getTabAt(2).select();
        } else {
            status = "active";
            statusTabLayout.getTabAt(0).select();
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getType().equals(getActivity().getResources().getString(R.string.EditPersonalTypePsychology))) {
            type = "psychology";
            typeTabLayout.getTabAt(1).select();
        } else if (((EditAccountActivity) getActivity()).authViewModel.getType().equals(getActivity().getResources().getString(R.string.EditPersonalTypeOperator))) {
            type = "operator";
            typeTabLayout.getTabAt(2).select();
        } else if (((EditAccountActivity) getActivity()).authViewModel.getType().equals(getActivity().getResources().getString(R.string.EditPersonalTypeClinicCenter))) {
            type = "clinic_center";
            typeTabLayout.getTabAt(3).select();
        } else if (((EditAccountActivity) getActivity()).authViewModel.getType().equals(getActivity().getResources().getString(R.string.EditPersonalTypeClient))) {
            type = "client";
            typeTabLayout.getTabAt(4).select();
        } else {
            type = "admin";
            typeTabLayout.getTabAt(0).select();
        }
    }

    private void setCustomPicker() {
        yearNumberPicker.setMinValue(1300);
        yearNumberPicker.setMaxValue(2100);
        yearNumberPicker.setValue(year);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(month);
        monthNumberPicker.setDisplayedValues(new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"});

        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        dayNumberPicker.setValue(day);
    }

    private void doWork() {
        name = nameEditText.getText().toString().trim();
        username = usernameEditText.getText().toString().trim();
        mobile = mobileEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();

        try {
            ((EditAccountActivity) getActivity()).progressDialog.show();

            ((EditAccountActivity) getActivity()).authViewModel.editPersonal(name, username, mobile, email, DateManager.jalaliToGregorian(birthday), gender, status, type);
            ((EditAccountActivity) getActivity()).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}