package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.More;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoreRepository extends MainRepository {

    public MoreRepository(Application application) {
        super(application);
    }

    public ArrayList<More> getAll() {
        ArrayList<More> mores = new ArrayList<>();
        for (int i = 0; i < moreItems().length(); i++) {
            try {
                mores.add(new More(moreItems().getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return mores;
    }

    private JSONArray moreItems() {
        JSONArray moreItems = new JSONArray();
        try {
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreAboutUs)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_info)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreQuestions)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_question)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreCallUs)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreTermsConditions)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_gavel)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreFollow)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_heart)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreShare)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_share)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreRate)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_star)));
            moreItems.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.MoreVersion)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moreItems;
    }

}