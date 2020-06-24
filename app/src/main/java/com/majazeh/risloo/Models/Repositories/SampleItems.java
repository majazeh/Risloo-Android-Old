package com.majazeh.risloo.Models.Repositories;

import com.majazeh.risloo.Entities.Sample.Sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleItems {
    private int index = 0;
    private Sample current;
    private ArrayList<Sample> items = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> data = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> sending_data = new ArrayList<>();

    public void setData(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);
        data.add(arrayList);
    }

    public void setSending_data(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);
        sending_data.add(arrayList);
    }

    public ArrayList data() {
        return data;
    }

    public ArrayList sending_data() {
        return sending_data;
    }

    public void putSDtoD() {
        for (int i = 0; i < sending_data.size(); i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(sending_data.get(i));
            data.add(arrayList);
        }
        sending_data.clear();
    }

    public SampleItems(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            this.items.add(new Sample((JSONObject) items.get(i)));
        }
        current = this.items.get(index);
    }

    public Sample next() {
        index++;
        current = items.get(index);
        return current;
    }

    public Sample prev() {
        index--;
        current = items.get(index);
        return current;
    }

    public Sample goToIndex(int i) {
        index = i;
        current = items.get(index);
        return current;
    }

    public Sample getItem(int i) {
        return items.get(i);
    }

    public int getCurrentIndex() {
        return index;
    }

    public int getSize() {
        return items.size();
    }
}
