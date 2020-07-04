package com.majazeh.risloo.Models.Repositories.Sample;

import java.util.ArrayList;

public class Samples {
    private ArrayList<ArrayList<Integer>> localData = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();

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

}
