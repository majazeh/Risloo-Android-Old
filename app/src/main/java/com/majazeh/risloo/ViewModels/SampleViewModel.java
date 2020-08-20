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
import java.util.HashMap;

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

    /*
         ---------- Voids ----------
    */

    public void sample(String sampleId) throws JSONException {
        repository.sample(sampleId);
    }

    public void samples() throws JSONException {
        repository.samples();
    }

    public void close(String sampleId) throws JSONException {
        repository.close(sampleId);
    }

    public void delete(String sampleId) {
        repository.delete(sampleId);
    }

    public void sendAnswers(String sampleId) throws JSONException {
        repository.sendAnswers(sampleId);
    }

    public void sendPrerequisite(HashMap parameters) throws JSONException {
        repository.sendPrerequisite(parameters);
    }

    /*
         ---------- Write ----------
    */

    public void writeSampleAnswerToExternal(JSONArray jsonArray, String fileName) {
        repository.writeSampleAnswerToExternal(jsonArray, fileName);
    }

    public void writeSampleAnswerToCache(JSONArray jsonArray, String fileName) {
        repository.writeSampleAnswerToCache(jsonArray, fileName);
    }

    public void writePrerequisiteAnswerToCache(JSONObject jsonObject, String fileName) {
        repository.writePrerequisiteAnswerToCache(jsonObject, fileName);
    }

    /*
         ---------- Read ----------
    */

    public JSONArray readSampleAnswerFromCache(String fileName) {
        return repository.readSampleAnswerFromCache(fileName);
    }

    public JSONObject readPrerequisiteAnswerFromCache(String fileName) {
        return repository.readPrerequisiteAnswerFromCache(fileName);
    }

    /*
         ---------- Check ----------
    */

    public boolean hasSampleAnswerStorage(String fileName) {
        return repository.hasAnswerStorage(fileName);
    }

    public boolean hasPrerequisiteAnswerStorage(String fileName) {
        return repository.hasPrerequisiteStorage(fileName);
    }

    /*
         ---------- Delete ----------
    */










































    ///////////////////////////////////////////////////////////////////

    public boolean showPrerequisite(String fileName){
     return repository.showPrerequisite(fileName);
    }

    public void checkAnswerStorage(String fileName) {
        repository.checkAnswerStorage(fileName);
    }

    ///////////////////////////////////////////////////////////////////




























    /*
         ---------- Sample ----------
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

    public JSONObject getAnswer(int index) {
        return repository.getAnswer(index);
    }

    public ArrayList getOptions(int index) {
        return repository.getOptions(index);
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

    /*
         ---------- Ints ----------
    */

    public int answeredPosition(String fileName, int index) {
        return repository.answeredPosition(fileName, index);
    }

    public int answeredSize(String fileName) {
        return repository.answeredSize(fileName);
    }

    public int firstUnAnswered(String fileName) {
        return repository.firstUnAnswered(fileName);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll(){
        return repository.getAll();
    }

    public ArrayList<Model> getArchive() {
        return repository.getArchive();
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        repository.insertToLocal(index, answer);
    }

}