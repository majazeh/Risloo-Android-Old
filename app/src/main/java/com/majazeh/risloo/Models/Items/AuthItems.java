package com.majazeh.risloo.Models.Items;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthItems {

    // Vars
    private ArrayList<Model> items = new ArrayList<>();

    // Objects
    private Application application;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthItems(Application application) {
        this.application = application;

        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();
    }

    public ArrayList<Model> items() throws JSONException {
        items.clear();

        for (int i = 0; i < data().length(); i++) {
            items.add(new Model(data().getJSONObject(i)));
        }

        return items;
    }

    public boolean hasAccess() {
        return sharedPreferences.getBoolean("hasAccess", false);
    }

    public String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return sharedPreferences.getString("token", "");
        }
        return "";
    }

    public String userId() {
        if (!sharedPreferences.getString("userId", "").equals("")) {
            return sharedPreferences.getString("userId", "");
        }
        return "";
    }

    public String name() {
        if (!sharedPreferences.getString("name", "").equals("")) {
            return sharedPreferences.getString("name", "");
        }
        return "";
    }

    public String type() {
        if (!sharedPreferences.getString("type", "").equals("")) {
            if (sharedPreferences.getString("type", "").equals("psychology")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypePsychology);
            } else if (sharedPreferences.getString("type", "").equals("clinic_center")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypeClinicCenter);
            } else if (sharedPreferences.getString("type", "").equals("operator")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypeOperator);
            } else if (sharedPreferences.getString("type", "").equals("admin")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypeAdmin);
            } else if (sharedPreferences.getString("type", "").equals("client")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypeClient);
            } else {
                return application.getApplicationContext().getResources().getString(R.string.AuthTypeDefault);
            }
        }
        return "";
    }

    public String mobile() {
        if (!sharedPreferences.getString("mobile", "").equals("")) {
            return sharedPreferences.getString("mobile", "");
        }
        return "";
    }

    public String email() {
        if (!sharedPreferences.getString("email", "").equals("")) {
            return sharedPreferences.getString("email", "");
        }
        return "";
    }

    public String gender() {
        if (!sharedPreferences.getString("gender", "").equals("")) {
            if (sharedPreferences.getString("gender", "").equals("male")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthGenderMale);
            } else if (sharedPreferences.getString("gender", "").equals("female")) {
                return application.getApplicationContext().getResources().getString(R.string.AuthGenderFemale);
            } else {
                return application.getApplicationContext().getResources().getString(R.string.AuthGenderDefault);
            }
        }
        return "";
    }

    public String birthday() {
        if (!sharedPreferences.getString("birthday", "").equals("")) {
            return StringManager.gregorianToJalali(sharedPreferences.getString("birthday", ""));
        }
        return "";
    }

    public String avatar() {
        if (!sharedPreferences.getString("avatar", "").equals("")) {
            return sharedPreferences.getString("avatar", "");
        }
        return "";
    }

    private JSONArray data() throws JSONException {
        JSONArray data = new JSONArray();
        if (!userId().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthUserId)).put("subTitle", userId()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_code_light)));
        }
        if (!name().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthName)).put("subTitle", name()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_user_light)));
        }
//        if (!type().equals("")) {
//            if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypePsychology))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_stethoscope_light)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeClinicCenter))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_hospital_light)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeOperator))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset_light)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeAdmin))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset_light)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeClient))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_pills_light)));
//            } else {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_pills_light)));
//            }
//        }
        if (!mobile().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthMobile)).put("subTitle", mobile()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile_light)));
        }
        if (!email().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthEmail)).put("subTitle", email()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_email_light)));
        }
        if (!gender().equals("")) {
            if (gender().equals(application.getApplicationContext().getResources().getString(R.string.AuthGenderMale))) {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_male_light)));
            } else if (gender().equals(application.getApplicationContext().getResources().getString(R.string.AuthGenderFemale))) {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_female_light)));
            } else {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_gender_light)));
            }
        }
        if (!birthday().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthBirthday)).put("subTitle", birthday()).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_calendar_light)));
        }
        return data;
    }

}