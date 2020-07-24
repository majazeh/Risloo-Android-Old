package com.majazeh.risloo.Models.Items;

import com.majazeh.risloo.Entities.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleItems {

    // Vars
    private int index = 0;
    private ArrayList<Model> items = new ArrayList<>();

    public SampleItems(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            this.items.add(new Model((JSONObject) items.get(i)));
        }
    }

    public ArrayList<Model> items(){
        return items;
    }

    public Model item(int i) {
        return items.get(i);
    }

    public Model next() {
        if (index+1==size()){
            return null;
        }
        index++;
        return items.get(index);
    }

    public Model prev() {
        if (index<=0) {
            return null;
        }
        index--;
        return items.get(index);
    }

    public Model goToIndex(int i) {
        index = i;
        return items.get(index);
    }

    public void setIndex(int i){
        index = i;
    }

    public int getIndex() {
        return index;
    }

    public int size() {
        return items.size();
    }

}