package com.majazeh.risloo.Models.Items;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthItems {

    // Vars
    private final ArrayList<Model> items = new ArrayList<>();

    // Objects
    private final Application application;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public AuthItems(@NonNull Application application) {
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

    public String userName() {
        if (!sharedPreferences.getString("userName", "").equals("")) {
            return sharedPreferences.getString("userName", "");
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
            switch (sharedPreferences.getString("type", "")) {
                case "psychology":
                    return application.getApplicationContext().getResources().getString(R.string.AuthTypePsychology);
                case "clinic_center":
                    return application.getApplicationContext().getResources().getString(R.string.AuthTypeClinicCenter);
                case "operator":
                    return application.getApplicationContext().getResources().getString(R.string.AuthTypeOperator);
                case "admin":
                    return application.getApplicationContext().getResources().getString(R.string.AuthTypeAdmin);
                case "client":
                    return application.getApplicationContext().getResources().getString(R.string.AuthTypeClient);
                default:
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
            return DateManager.gregorianToJalali(sharedPreferences.getString("birthday", ""));
        }
        return "";
    }

    public String avatar() {
        if (!sharedPreferences.getString("avatar", "").equals("")) {
            return sharedPreferences.getString("avatar", "");
        }
        return "";
    }

    public String publicKey() {
        if (!sharedPreferences.getString("public_key", "").equals("")) {
            return sharedPreferences.getString("public_key", "");
        }
        return "";
    }

    public String privateKey() {
        if (!sharedPreferences.getString("private_key", "").equals("")) {
            return sharedPreferences.getString("private_key", "");
        }
        return "";
    }

    public void setPublicKey(String key) {
        editor.putString("public_key", key);
        editor.apply();
    }

    public void setPrivateKey(String key) {
        editor.putString("private_key", key);
        editor.apply();
    }

    private JSONArray data() throws JSONException {
        JSONArray data = new JSONArray();
        if (!userId().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthUserId)).put("subTitle", userId()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_code_light, null)));
        }
        if (!userName().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthUserName)).put("subTitle", userName()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_at_light, null)));
        }
        if (!name().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthName)).put("subTitle", name()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_user_light, null)));
        }
//        if (!type().equals("")) {
//            if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypePsychology))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image",ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_stethoscope_light, null)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeClinicCenter))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_hospital_light, null)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeOperator))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_headset_light, null)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeAdmin))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_headset_light, null)));
//            } else if (type().equals(application.getApplicationContext().getResources().getString(R.string.AuthTypeClient))) {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_pills_light, null)));
//            } else {
//                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthType)).put("subTitle", type()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_pills_light, null)));
//            }
//        }
        if (!mobile().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthMobile)).put("subTitle", mobile()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_mobile_light, null)));
        }
        if (!email().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthEmail)).put("subTitle", email()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_email_light, null)));
        }
        if (!gender().equals("")) {
            if (gender().equals(application.getApplicationContext().getResources().getString(R.string.AuthGenderMale))) {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_male_light, null)));
            } else if (gender().equals(application.getApplicationContext().getResources().getString(R.string.AuthGenderFemale))) {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_female_light, null)));
            } else {
                data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthGender)).put("subTitle", gender()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_gender_light, null)));
            }
        }
        if (!birthday().equals("")) {
            data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.AuthBirthday)).put("subTitle", birthday()).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_calendar_light, null)));
        }
        return data;
    }

}