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

    public void sendPre(ArrayList<ArrayList> arrayList) throws JSONException {
        repository.sendPrerequisite(arrayList);
    }

     /*
         ---------- Get ----------
    */

    public String getDescription() {
        return repository.getDescription();
    }

    public ArrayList getPrerequisite() {
        return repository.getPrerequisite();
    }

    public ArrayList getItems() {
        return repository.getItems();
    }

    public Model getItem(int index) {
        return repository.getItem(index);
    }

    public Model getNext() {
        return repository.getNext();
    }

    public Model getPrev() {
        return repository.getPrev();
    }

    public Model goToIndex(int index) {
        return repository.goToIndex(index);
    }

    public void setIndex(int index) {
        repository.setIndex(index);
    }

    public int getIndex() {
        return repository.getIndex();
    }

    public int getSize() {
        return repository.getSize();
    }

    public ArrayList<String> getOptions(int index) {
        return repository.getOptions(index);
    }

    public JSONObject getAnswer(int index) {
        return repository.getAnswer(index);
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