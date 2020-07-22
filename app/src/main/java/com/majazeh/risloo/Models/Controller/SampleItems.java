package com.majazeh.risloo.Models.Controller;

import android.util.Log;

import com.majazeh.risloo.Entities.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleItems {

    private int index = 0;
    private Model currentItem;
    private ArrayList<Model> items = new ArrayList<>();

    public SampleItems(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            this.items.add(new Model((JSONObject) items.get(i)));
        }
        currentItem = this.items.get(index);

    }

    public Model item(int i) {
        return items.get(i);
    }

    public Model next() {
        if (index+1==size()){
            return null;
        }
        index++;
        currentItem = items.get(index);
        return currentItem;
    }

    public Model prev() {
        if (index>0) {
            index--;
            currentItem = items.get(index);
        }
        return currentItem;
    }

    public Model goToIndex(int i) {
        index = i;
        currentItem = items.get(index);
        return currentItem;
    }

    public int currentIndex() {
        return index;
    }

    public int size() {
        return items.size();
    }

    public ArrayList getAll(){
        return items;
    }

    public void setCurrentIndex(int i){
        index = i;
    }

    public int userAnswer(int i){
        try {
            if (items.get(i).get("user_answered") != null)
           return Integer.parseInt(String.valueOf(items.get(i).get("user_answered")));
            else
                return -1;
        } catch (JSONException e) {
            e.printStackTrace();
            return  -1;
        }
    }
}