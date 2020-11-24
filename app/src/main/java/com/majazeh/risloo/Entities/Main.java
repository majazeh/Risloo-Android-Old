package com.majazeh.risloo.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Main {

    // Objects
    public ArrayList arrayMap = new ArrayList();
    public JSONObject attributes = new JSONObject();

    public Main(JSONObject attributes) throws JSONException {
        Iterator keys = attributes.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            arrayMap.add(key);
            this.attributes.put(key, attributes.get(key));
        }
    }

    public Object get(String key) throws JSONException {
        if (arrayMap.indexOf(key) >= 0)
            return this.attributes.get(key);
        else
            return null;
    }

    public void set(String key, Object value) throws JSONException {
        attributes.put(key, value);
    }

}