package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Utils.Generators.FilterGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class MainRepository {

    // Objects
    public Application application;
    private FilterGenerator filterGenerator;
    public static JSONObject meta;
    public MainRepository(Application application) {
        this.application = application;
        filterGenerator = new FilterGenerator();
    }

    public MainRepository() {

    }

    public void handlerFilters() {
        for (Field field : this.getClass().getFields()) {
            if (field.getName().length() >= 7 && field.getName().endsWith("Filter")){
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

}