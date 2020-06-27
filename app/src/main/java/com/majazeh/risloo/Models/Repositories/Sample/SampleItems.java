package com.majazeh.risloo.Models.Repositories.Sample;

import com.majazeh.risloo.Entities.Sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleItems {

    private int index = 0;
    private Sample currentItem;
    private ArrayList<Sample> items = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> localData = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();

    public SampleItems(JSONArray items) throws JSONException {
        for (int i = 0; i < items.length(); i++) {
            this.items.add(new Sample((JSONObject) items.get(i)));
        }
        currentItem = this.items.get(index);
    }

    public void insertToLocalData(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);

        localData.add(arrayList);
    }

    public void insertToRemoteData(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);

        remoteData.add(arrayList);
    }

    public void insertRemoteDataToLocalData() {
        for (int i = 0; i < remoteData.size(); i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(remoteData.get(i));

            localData.add(arrayList);
        }
        remoteData.clear();
    }

    public ArrayList localData() {
        return localData;
    }

    public ArrayList remoteData() {
        return remoteData;
    }

    public Sample item(int i) {
        return items.get(i);
    }

    public Sample next() {
        index++;
        currentItem = items.get(index);
        return currentItem;
    }

    public Sample prev() {
        index--;
        currentItem = items.get(index);
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

}