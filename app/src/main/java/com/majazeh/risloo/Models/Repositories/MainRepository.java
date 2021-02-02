package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Items.AuthItems;
import com.majazeh.risloo.Utils.Generators.FilterGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainRepository {

    // Objects
    public Application application;
    private FilterGenerator filterGenerator;
    public static JSONObject meta;
    private SharedPreferences sharedPreferences;

    public MainRepository() {

    }

    public MainRepository(Application application) {
        this.application = application;

        filterGenerator = new FilterGenerator();
        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
    }

    public void handlerFilters() {
        for (Field field : this.getClass().getFields()) {
            if (field.getName().length() >= 7 && field.getName().endsWith("Filter")) {
                String key = field.getName().substring(0, field.getName().length() - 6);
                try {
                    this.getClass().getDeclaredField(key + "Filter").set(getClass(), filterGenerator.getFilter(this.meta.getJSONObject("filters"), key));
                } catch (IllegalAccessException | NoSuchFieldException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addSuggest(Model model, String module) throws JSONException {
        addSuggest(model, 1, module);
    }

    public void addSuggest(Model model, Integer rank, String module) throws JSONException {
        String mainModule = module.substring(0, module.indexOf("ViewModel"));
        JSONObject jsonObject;
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "suggest" + mainModule) == null) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "suggest" + mainModule);
        }
        if (!jsonObject.has((String) model.get("id"))) {
            model.set("local_ranking", 0);
            jsonObject.put((String) model.get("id"), model.attributes);
        }
        model.set("local_ranking", jsonObject.getJSONObject((String) model.get("id")).getInt("local_ranking") + rank);

        jsonObject.put((String) model.get("id"), model.attributes);
        FileManager.writeObjectToCache(application.getApplicationContext(), jsonObject, "suggest" + mainModule);
    }

    public ArrayList<Model> getSuggest(String module) throws JSONException {
        String mainModule = module.substring(0, module.indexOf("ViewModel"));
        JSONObject jsonObject;
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "suggest" + mainModule) == null) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "suggest" + mainModule);
        }
        ArrayList<Model> arrayList = new ArrayList<>();
        ArrayList<Model> suggestList = new ArrayList<>();
        if (jsonObject.length() > 10) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                arrayList.add(new Model(jsonObject.getJSONObject(keys.next())));
            }
            Collections.sort(arrayList, (o1, o2) -> {
                try {
                    return Integer.compare(o2.attributes.getInt("local_ranking"), o1.attributes.getInt("local_ranking"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return 0;
                }
            });
            for (Model model :
                    arrayList) {
                suggestList.add(model);

            }
        } else {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                suggestList.add(new Model(jsonObject.getJSONObject(keys.next())));
            }
        }
        return suggestList;
    }


    public boolean admin() {
        if (sharedPreferences.getString("type", "").equals("admin")) {
            return true;
        } else
            return false;
    }

    public boolean roomManager(Model data) throws JSONException {
        JSONObject room;
        if (data.get("room") != null) {
            room = (JSONObject) data.get("room");
        } else if (data.get("case") != null) {
           JSONObject cases = (JSONObject) data.get("case");
            room = (JSONObject) cases.get("room");
        } else {
            room = data.attributes;
        }
        JSONObject center = (JSONObject) room.get("center");
        if (!center.isNull("acceptation")) {
            JSONObject roomManager = room.getJSONObject("manager");
            if (roomManager.getString("id").equals(sharedPreferences.getString("userId", ""))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean roomManager() {
        if (sharedPreferences.getString("roomManager", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean centerManager(Model data) throws JSONException {
        JSONObject center;
        if (data.get("room") != null) {
            JSONObject room = (JSONObject) data.get("room");
            center = room.getJSONObject("center");
        } else {
            center = data.attributes;
        }
        if (!center.isNull("acceptation")) {
            JSONObject centerManager = center.getJSONObject("acceptation");
            if (centerManager.getString("position").equals("manager")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean centerManager() {
        if (sharedPreferences.getString("centerManager", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean centerOwner(Model data) throws JSONException {
        JSONObject center;
        if (data.get("room") != null) {
            JSONObject room = (JSONObject) data.get("room");
            center = room.getJSONObject("center");
        } else {
            center = data.attributes;
        }
        if (!center.isNull("manager")) {
            JSONObject centerManager = center.getJSONObject("manager");
            if (centerManager.getString("id").equals(sharedPreferences.getString("userId", ""))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean centerOwner() {
        if (sharedPreferences.getString("centerOwner", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean roomAccess() {
        if (sharedPreferences.getString("createRoomAccess", "").equals("true")) {
            return true;
        } else
            return false;

    }

    public boolean currentAuth(String id){
        if (sharedPreferences.getString("userId", "").equals(id))
            return true;
        else
            return false;

    }

    public boolean centerOperator() {
        if (sharedPreferences.getString("centerOperator", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean centerOperator(Model data) throws JSONException {
        JSONObject center;
        if (data.get("room") != null) {
            JSONObject room = (JSONObject) data.get("room");
            center = room.getJSONObject("center");
        } else if (data.get("case") != null) {
            JSONObject cases = (JSONObject) data.get("case");
            JSONObject room = (JSONObject) cases.get("room");
            center = room.getJSONObject("center");
        } else {
            center = data.attributes;
        }
        if (!center.isNull("acceptation")) {
            JSONObject acceptation = center.getJSONObject("acceptation");
            if (acceptation.getString("position").equals("operator")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean sessionClient(Model data) throws JSONException {
        JSONObject client = (JSONObject) data.get("client");
        if (client.getString("id").equals(sharedPreferences.getString("userId", ""))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean operator() {
        if (sharedPreferences.getString("operator", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean psychologist(Model data) throws JSONException {
        JSONObject center;
        if (data.get("room") != null) {
            JSONObject room = (JSONObject) data.get("room");
            center = room.getJSONObject("center");
        } else if (data.get("case") != null) {
            JSONObject cases = (JSONObject) data.get("case");
            JSONObject room = (JSONObject) cases.get("room");
            center = room.getJSONObject("center");
        } else {
            center = data.attributes;
        }
        if (!center.isNull("acceptation")) {

            JSONObject acceptation = center.getJSONObject("acceptation");
            if (acceptation.getString("position").equals("psychologist")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean psychologist() {
        if (sharedPreferences.getString("psychologist", "").equals("true")) {
            return true;
        } else
            return false;
    }

    public boolean client(Model data) throws JSONException {
        JSONObject client = (JSONObject) data.get("client");
        if (client.getString("id").equals(sharedPreferences.getString("userId", ""))) {
            return true;
        } else {
            return false;
        }
    }

}