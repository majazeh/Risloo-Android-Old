package com.majazeh.risloo.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainModel {
    public ArrayList ArrayMap = new ArrayList();
    public JSONObject attributes = new JSONObject();

    public MainModel(JSONObject attributes) throws JSONException {
                Iterator keys = attributes.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            ArrayMap.add(key);
            this.attributes.put(key, attributes.get(key));
        }
    }

    public Object get(String key) throws JSONException {
        if (ArrayMap.indexOf(key) >= 0) {
            return this.attributes.get(key);
        }
        return null;
    }
}
