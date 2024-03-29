package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Items.SettingItems;

import org.json.JSONException;

import java.util.ArrayList;

public class SettingRepository extends MainRepository {

    // Objects
    private final SettingItems items;

    public SettingRepository(@NonNull Application application) throws JSONException {
        super(application);

        items = new SettingItems(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return items.items();
    }

}