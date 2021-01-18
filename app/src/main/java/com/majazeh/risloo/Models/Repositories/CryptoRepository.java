package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CryptoRepository extends MainRepository {

    // Objects
    private final JSONArray publicItems, privateItems;

    public CryptoRepository(@NonNull Application application) throws JSONException {
        super(application);

        publicItems = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "PublicKey.json"));
        privateItems = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "PrivateKey.json"));
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll(String type) throws JSONException {
        ArrayList<Model> items = new ArrayList<>();
        if (type.equals("public")) {
            for (int i = 0; i < this.publicItems.length(); i++) {
                items.add(new Model(this.publicItems.getJSONObject(i)));
            }
        } else {
            for (int i = 0; i < this.privateItems.length(); i++) {
                items.add(new Model(this.privateItems.getJSONObject(i)));
            }
        }
        return items;
    }

    public ArrayList<Model> getSubset(String type, int index) throws JSONException {
        JSONArray subsets;

        if (type.equals("public")) {
            subsets = publicItems.getJSONObject(index).getJSONArray("items");
        } else {
            subsets = privateItems.getJSONObject(index).getJSONArray("items");
        }

        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < subsets.length(); i++) {
            items.add(new Model(subsets.getJSONObject(i)));
        }
        return items;
    }

}