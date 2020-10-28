package com.majazeh.risloo.Utils.Generators;

import android.util.Log;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FilterGenerator {

    public Object filter(JSONObject filters, String property) {

        try {
            ArrayList<Model> arrayList = new ArrayList<>();
            JSONObject allowed = filters.getJSONObject("allowed");
            switch (allowed.get(property).getClass().getName()) {
                case "org.json.JSONObject":
                    JSONObject data = allowed.getJSONObject(property);
                    Iterator<String> keys = data.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        JSONObject content = new JSONObject();
                        content.put("id", key);
                        content.put("title", data.getString(key));
                        arrayList.add(new Model(content));
                    }
                    return arrayList;

                case "org.json.JSONArray":
                    for (int i = 0; i < allowed.getJSONArray(property).length(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", "S" + i+1);
                        jsonObject.put("title", allowed.getJSONArray(property).get(i));
                        arrayList.add(new Model(jsonObject));
                    }
                    return arrayList;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}