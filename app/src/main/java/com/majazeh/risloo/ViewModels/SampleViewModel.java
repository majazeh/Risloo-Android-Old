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

    public SampleViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new SampleRepository(application);
    }

    /*
         ---------- Voids ----------
    */

    public void sample(String sampleId) throws JSONException {
        repository.sample(sampleId);
    }

    public void samples(String scalesQ, String statusQ, String roomQ) throws JSONException {
        repository.samples(scalesQ,statusQ,roomQ);
    }

    public void sendAnswers(String sampleId) throws JSONException {
        repository.sendAnswers(sampleId);
    }

    public void sendAllAnswers(String sampleId) throws JSONException {
        repository.sendAllAnswers(sampleId);
    }

    public void sendPrerequisite(String sampleId, ArrayList prerequisites) throws JSONException {
        repository.sendPrerequisite(sampleId, prerequisites);
    }

    public void create(ArrayList scales, String room, String cases, ArrayList roomReferences, ArrayList caseReferences, String count) throws JSONException {
        repository.create(scales, room, cases, roomReferences, caseReferences, count);
    }

    public void close(String sampleId) throws JSONException {
        repository.close(sampleId);
    }

    public void score(String sampleId) throws JSONException {
        repository.score(sampleId);
    }

    public void delete(String sampleId) {
        repository.delete(sampleId);
    }

    public void scales(String q) throws JSONException {
        repository.scales(q);
    }

    public void rooms(String q) throws JSONException {
        repository.rooms(q);
    }

    public void cases(String roomId, String q) throws JSONException {
        repository.cases(roomId, q);
    }

    public void references(String roomId, String q) throws JSONException {
        repository.references(roomId, q);
    }

    public void general(String sampleId) throws JSONException {
        repository.general(sampleId);
    }

    public void scores(String sampleId) throws JSONException {
        repository.scores(sampleId);
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        repository.insertToLocal(index, answer);
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

    public void writePrerequisiteAnswerToCache(JSONArray jsonArray, String fileName) {
        repository.writePrerequisiteAnswerToCache(jsonArray, fileName);
    }

    /*
         ---------- Read ----------
    */

    public JSONObject readSampleFromCache(String fileName) {
        return repository.readSampleFromCache(fileName);
    }

    public JSONArray readSampleAnswerFromCache(String fileName) {
        return repository.readSampleAnswerFromCache(fileName);
    }

    public JSONArray readPrerequisiteAnswerFromCache(String fileName) {
        return repository.readPrerequisiteAnswerFromCache(fileName);
    }

    public JSONObject readSampleDetailFromCache(String fileName) {
        return repository.readSampleDetailFromCache(fileName);
    }

    /*
         ---------- Check ----------
    */

    public boolean hasSampleAnswerStorage(String fileName) {
        return repository.hasSampleAnswerStorage(fileName);
    }

    public boolean checkSampleAnswerStorage(String fileName) {
        return repository.checkSampleAnswerStorage(fileName);
    }

    public boolean hasPrerequisiteAnswerStorage(String fileName) {
        return repository.hasPrerequisiteAnswerStorage(fileName);
    }

    public boolean checkPrerequisiteAnswerStorage(String fileName) {
        return repository.checkPrerequisiteAnswerStorage(fileName);
    }

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

    public Model getItem(int index) {
        return repository.getItem(index);
    }

    public JSONObject getAnswer(int index) {
        return repository.getAnswer(index);
    }

    public ArrayList getOptions(int index) {
        return repository.getOptions(index);
    }

    public String getType(int index) {
        return repository.getType(index);
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
         ---------- Urls ----------
    */

    public String getSvgScore(String sampleId) {
        return repository.getSvgScore(sampleId);
    }

    public String getPngScore(String sampleId) {
        return repository.getPngScore(sampleId);
    }

    public String getHtmlScore(String sampleId) {
        return repository.getHtmlScore(sampleId);
    }

    public String getPdfScore(String sampleId) {
        return repository.getPdfScore(sampleId);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getArchive() {
        return repository.getArchive();
    }

}