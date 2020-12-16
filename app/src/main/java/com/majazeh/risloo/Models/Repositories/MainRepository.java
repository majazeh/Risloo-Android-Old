package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
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

    public MainRepository(Application application) {
        this.application = application;
        filterGenerator = new FilterGenerator();
        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
    }

    public MainRepository() {

    }

    public void handlerFilters() {
        for (Field field : this.getClass().getFields()) {
            if (field.getName().length() >= 7 && field.getName().endsWith("Filter")) {
                String key = field.getName().substring(0, field.getName().length() - 6);
                try {
                    this.getClass().getDeclaredField(key + "Filter").set(getClass(), filterGenerator.getFilter(this.meta.getJSONObject("filters"), key));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void addSuggest(Model model,String module) throws JSONException {
        addSuggest(model, 1,module);
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

    public boolean admin(){
        if (sharedPreferences.getString("type","").equals("admin")){
            return true;
        }else
            return false;
    }

}