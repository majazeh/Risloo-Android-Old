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

    public SampleViewModel(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        repository = new SampleRepository(application, testUniqueId);
    }

    public SampleViewModel(@NonNull Application application) {
        super(application);
        repository = new SampleRepository(application);
    }

    public void insertToLocalData(int item, int answer) {
        repository.insertToLocalData(item, answer);
    }

    public String getTitle() throws JSONException {
        return repository.json().getString("title");
    }

    public String getDescription() throws JSONException {
        return repository.json().getString("description");
    }

    public int getVersion() throws JSONException {
        return repository.json().getInt("version");
    }

    public String getEdition() throws JSONException {
        return repository.json().getString("edition");
    }

    public String getEditionVersion() throws JSONException {
        return repository.json().getString("edition_version");
    }

    public String getFiller() throws JSONException {
        return repository.json().getString("filler");
    }

    public Model getItem(int index) {
        return repository.items().item(index);
    }

    public Model next() {
        return repository.items().next();
    }

    public Model prev() {
        return repository.items().prev();
    }

    public Model goToIndex(int index) {
        return repository.items().goToIndex(index);
    }

    public int getCurrentIndex() {
        return repository.items().currentIndex();
    }

    public int getSize() {
        return repository.items().size();
    }

    public ArrayList getItems() {
        return repository.items().getAll();
    }

    public void setIndex(int index) {
        repository.items().setCurrentIndex(index);
    }

    public ArrayList files() {
        return repository.files();
    }

    //Answers
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public JSONObject getAnswer(int index) throws JSONException {
        return (JSONObject) repository.items().item(index).get("answer");
    }

    public ArrayList<String> getOptions(int index) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < getAnswer(index).getJSONArray("options").length(); i++) {
            arrayList.add((String) getAnswer(index).getJSONArray("options").get(i));
        }
        return arrayList;
    }

    public void sendAnswers(String UniqueId) throws JSONException {
        repository.sendAnswers(UniqueId);
    }

    public boolean hasStorage(String fileName) {
        return repository.hasStorage( fileName);
    }

    public void deleteStorage(String fileName) {
        repository.deleteStorage(fileName);
    }

    public int answerSize(String fileName) {
        return repository.answerSize(fileName);
    }

    public int answerPosition(String fileName, int index) {
        return repository.answerPosition(fileName, index);
    }

    public int getLastUnAnswer(String fileName) {
        return repository.lastUnAnswer( fileName);
    }

    public void writeAnswerToCache(JSONArray jsonArray, String fileName) {
        repository.writeToCache(jsonArray,  fileName);
    }

    public JSONArray readAnswerFromCache(String fileName) {
        return repository.readAnswerFromCache( fileName);
    }

    public void saveAnswerToExternal(JSONArray jsonArray, String fileName) {
        repository.saveToExternal(jsonArray, fileName);
    }

}