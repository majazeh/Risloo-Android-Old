package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controllers.RelationshipController;
import com.majazeh.risloo.Models.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RelationshipRepository extends MainRepository {

    // Controllers
    private RelationshipController controller;

    // Managers
    private FileManager manager;

    public RelationshipRepository(Application application) throws JSONException {
        super(application);

        controller = new RelationshipController(application);

        manager = new FileManager();
    }

    public void relationships() throws JSONException {
        controller.work = "getAll";
        controller.workState.setValue(-1);
        controller.workManager("getAll");
    }

    public void myRelationships() throws JSONException {
        controller.work = "getMy";
        controller.workState.setValue(-1);
        controller.workManager("getMy");
    }

    public void request(String clinicId) throws JSONException {
        controller.clinicId = clinicId;
        controller.work = "request";
        controller.workState.setValue(-1);
        controller.workManager("request");
    }

    public ArrayList<Model> getAll() {
        ArrayList<Model> arrayList = new ArrayList<>();
        JSONObject jsonObject = manager.readFromCache(application.getApplicationContext(), "relationships", "all");
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() == 0) {
                return null;
            }
            for (int i = 0; i < data.length(); i++) {
                Model model = new Model(data.getJSONObject(i));
                arrayList.add(model);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Model> getMy() {
        ArrayList<Model> arrayList = new ArrayList<>();
        JSONObject jsonObject = manager.readFromCache(application.getApplicationContext(), "relationships", "my");
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() == 0) {
                return null;
            }
            for (int i = 0; i < data.length(); i++) {
                Model model = new Model(data.getJSONObject(i));
                arrayList.add(model);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}