package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleViewModel extends AndroidViewModel {

    // Repositories
    private SampleRepository repository;

    public SampleViewModel(@NonNull Application application, String sampleId) throws JSONException {
        super(application);

        repository = new SampleRepository(application, sampleId);
    }

    public SampleViewModel(@NonNull Application application) {
        super(application);

        repository = new SampleRepository(application);
    }

    public void getSample(String sampleId) throws JSONException {
        repository.getSample(sampleId);
    }

    public void sendAnswers(String sampleId) throws JSONException {
        repository.sendAnswers(sampleId);
    }

    public void closeSample() throws JSONException {
        repository.closeSample();
    }

    public void sendPre(ArrayList arrayList) throws JSONException {
        repository.sendPre(arrayList);
    }

    public String getTitle() throws JSONException {
        return repository.json().getString("title");
    }

    public String getDescription() throws JSONException {
        return repository.json().getJSONObject("data").getString("description");
    }

    public ArrayList getPrerequisite() {
        ArrayList arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = repository.json().getJSONObject("data").getJSONArray("prerequisite");
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList<String> getOptions(int index) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < getAnswer(index).getJSONArray("options").length(); i++) {
            arrayList.add((String) getAnswer(index).getJSONArray("options").get(i));
        }
        return arrayList;
    }

    public JSONObject getAnswer(int index) throws JSONException {
        return (JSONObject) repository.items().item(index).get("answer");
    }

    public ArrayList getItems() {
        return repository.items().items();
    }

    public Model getItem(int index) {
        return repository.items().item(index);
    }

    public Model getNext() {
        return repository.items().next();
    }

    public Model getPrev() {
        return repository.items().prev();
    }

    public Model goToIndex(int index) {
        return repository.items().goToIndex(index);
    }

    public void setIndex(int index) {
        repository.items().setIndex(index);
    }

    public int getIndex() {
        return repository.items().getIndex();
    }

    public int getSize() {
        return repository.items().size();
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        repository.insertToLocal(index, answer);
    }

    /*
         ---------- Save ----------
    */

    public void saveToExternal(JSONArray jsonArray, String fileName) {
        repository.saveToExternal(jsonArray, fileName);
    }

    public void saveAnswerToCache(JSONArray jsonArray, String fileName) {
        repository.saveAnswerToCache(jsonArray, fileName);
    }

    public void savePrerequisiteToCache(JSONArray jsonArray, String fileName) {
        repository.savePrerequisiteToCache(jsonArray, fileName);
    }

    /*
         ---------- Read ----------
    */

    public JSONArray readAnswerFromCache(String fileName) {
        return repository.readAnswerFromCache(fileName);
    }

    public JSONArray readPrerequisiteFromCache(String fileName) {
        return repository.readPrerequisiteFromCache(fileName);
    }

    /*
         ---------- Check ----------
    */

    public boolean hasAnswerStorage(String fileName) {
        return repository.hasAnswerStorage(fileName);
    }

    public boolean havePrerequisiteStorage(String fileName) {
        return repository.havePrerequisiteStorage(fileName);
    }

    public void checkAnswerStorage(String fileName) {
        repository.checkAnswerStorage(fileName);
    }

    public void checkPrerequisiteStorage(String fileName) {
        repository.checkPrerequisiteStorage(fileName);
    }

    public void deleteStorage(String fileName) {
        repository.deleteAnswerStorage(fileName);
    }

    /*
         ---------- Check ----------
    */

    public ArrayList<Model> getStorageFiles() {
        return repository.storageFiles();
    }

    /*
         ---------- Answer ----------
    */

    public int answeredPosition(String fileName, int index) {
        return repository.answeredPosition(fileName, index);
    }

    public int answeredSize(String fileName) {
        return repository.answeredSize(fileName);
    }

    public int firstUnanswered(String fileName) {
        return repository.firstUnanswered(fileName);
    }

}