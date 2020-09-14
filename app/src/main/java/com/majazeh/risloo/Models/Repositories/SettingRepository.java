package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Items.SettingItems;

import org.json.JSONException;

import java.util.ArrayList;

public class SettingRepository extends MainRepository {

    // Items
    private SettingItems settingItems;

    public SettingRepository(Application application) throws JSONException {
        super(application);

        settingItems = new SettingItems(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return settingItems.items();
    }

}