package com.majazeh.risloo.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    // Objects
    public ArrayList arrayMap = new ArrayList();
    public JSONObject attributes = new JSONObject();

    public Main(JSONObject attribute) throws JSONException {
        Iterator keys = attribute.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();

            arrayMap.add(key);
            attributes.put(key, attribute.get(key));
        }
    }

    public Object get(String key) throws JSONException {
        if (arrayMap.contains(key)) {
            return attributes.get(key);
        } else {
            return null;
        }
    }

    public void set(String key, Object value) throws JSONException {
        attributes.put(key, value);
    }

}