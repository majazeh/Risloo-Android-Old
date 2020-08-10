package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controller.RelationshipController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RelationshipRepository extends MainRepository {

    // Controllers
    private RelationshipController controller;

    public RelationshipRepository(Application application) throws JSONException {
        super(application);

        controller = new RelationshipController(application);
    }

    public RelationshipRepository() {

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
        JSONObject jsonObject = readSampleFromCache("relationships");
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
        JSONObject jsonObject = readSampleFromCache("myRelationships");
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












    public boolean writeReservationToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "relationships/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject readSampleFromCache(String fileName) {
        JSONObject jsonObject = null;
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), "relationship/" + fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                jsonObject = new JSONObject((String) ois.readObject());
                ois.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

}