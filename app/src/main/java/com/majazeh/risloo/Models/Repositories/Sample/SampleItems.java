package com.majazeh.risloo.Models.Repositories.Sample;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.majazeh.risloo.Entities.Sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleItems {

    private int index = 0;
    private Sample currentItem;
    private ArrayList<Sample> items = new ArrayList<>();

    public SampleItems(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            this.items.add(new Sample((JSONObject) items.get(i)));
        }
        currentItem = this.items.get(index);
    }

    public Sample item(int i) {
        return items.get(i);
    }

    public Sample next() {

        if (index+1==size()){

            return null;
        }
        index++;
            currentItem = items.get(index);
            return currentItem;
    }

    public Sample prev() {
        if (index>0) {
            index--;
            currentItem = items.get(index);
        }
            return currentItem;
    }

    public Sample goToIndex(int i) {
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

}