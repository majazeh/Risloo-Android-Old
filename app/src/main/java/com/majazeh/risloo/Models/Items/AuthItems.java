package com.majazeh.risloo.Models.Items;

import android.app.Application;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

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

    public AuthItems(Application application, SharedPreferences sharedPreferences) throws JSONException {
        this.application = application;
        this.sharedPreferences = sharedPreferences;

        for (int i = 0; i < data().length(); i++) {
            items.add(new Model(data().getJSONObject(i)));
        }
    }

    public ArrayList<Model> items(){
        return items;
    }

    public String account() {
        if (!sharedPreferences.getString("type", "").equals("null")) {
            return sharedPreferences.getString("type", "");
        }
        return "";
    }

    public String avatar() {
        if (!sharedPreferences.getString("avatar", "").equals("null")) {
            return sharedPreferences.getString("avatar", "");
        }
        return "";
    }

    public String name() {
        if (!sharedPreferences.getString("name", "").equals("null")) {
            return sharedPreferences.getString("name", "");
        }
        return "کاربر تجربی";
    }

    public String type() {
        if (!sharedPreferences.getString("type", "").equals("null")) {
            if (sharedPreferences.getString("type", "").equals("psychology")) {
                return "مشاور";
            } else if (sharedPreferences.getString("type", "").equals("clinic_center")) {
                return "مرکز درمانی";
            } else if (sharedPreferences.getString("type", "").equals("operator")) {
                return "اپراتور";
            } else if (sharedPreferences.getString("type", "").equals("admin")) {
                return "ادمین";
            } else if (sharedPreferences.getString("type", "").equals("client")) {
                return "مراجع";
            }
        }
        return "مراجع";
    }

    public String mobile() {
        if (!sharedPreferences.getString("mobile", "").equals("null")) {
            return sharedPreferences.getString("mobile", "");
        }
        return "+989000000000";
    }

    public String email() {
        if (!sharedPreferences.getString("email", "").equals("null")) {
            return sharedPreferences.getString("email", "");
        }
        return "نامشخص";
    }

    public String gender() {
        if (!sharedPreferences.getString("gender", "").equals("null")) {
            if (sharedPreferences.getString("gender", "").equals("male")) {
                return "مرد";
            } else if (sharedPreferences.getString("gender", "").equals("female")) {
                return "زن";
            }
        }
        return "نامشخص";
    }

    public String birthday() {
        if (!sharedPreferences.getString("birthday", "").equals("null")) {
            return sharedPreferences.getString("birthday", "");
        }
        return "نامشخص";
    }

    private JSONArray data() throws JSONException {
        JSONArray data = new JSONArray();
        if (!sharedPreferences.getString("name", "").equals("null")) {
            data.put(new JSONObject().put("title", "نام").put("subTitle", sharedPreferences.getString("name", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_user_light)));
        }
        if (!sharedPreferences.getString("type", "").equals("null")) {
            if (sharedPreferences.getString("type", "").equals("psychology")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مشاور").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_stethoscope_light)));
            } else if (sharedPreferences.getString("type", "").equals("clinic_center")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مرکز درمانی").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_hospital_light)));
            } else if (sharedPreferences.getString("type", "").equals("operator")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "اپراتور").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset_light)));
            } else if (sharedPreferences.getString("type", "").equals("admin")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "ادمین").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset_light)));
            } else if (sharedPreferences.getString("type", "").equals("client")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مراجع").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_pills_light)));
            }
        }
        if (!sharedPreferences.getString("mobile", "").equals("null")) {
            data.put(new JSONObject().put("title", "شماره همراه").put("subTitle", sharedPreferences.getString("mobile", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile_light)));
        }
        if (!sharedPreferences.getString("email", "").equals("null")) {
            data.put(new JSONObject().put("title", "ایمیل").put("subTitle", sharedPreferences.getString("email", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_message_light)));
        }
        if (!sharedPreferences.getString("gender", "").equals("null")) {
            if (sharedPreferences.getString("gender", "").equals("male")) {
                data.put(new JSONObject().put("title", "جنسیت").put("subTitle", "مرد").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_male_light)));
            } else if (sharedPreferences.getString("gender", "").equals("female")) {
                data.put(new JSONObject().put("title", "جنسیت").put("subTitle", "زن").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_female_light)));
            }
        }
        if (!sharedPreferences.getString("birthday", "").equals("null")) {
            data.put(new JSONObject().put("title", "تاریخ تولد").put("subTitle", sharedPreferences.getString("birthday", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_calendar_light)));
        }
        return data;
    }

}