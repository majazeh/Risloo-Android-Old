package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthRepository extends MainRepository {

    private AuthController controller;

    public AuthRepository(Application application) {
        super(application);

        controller = new AuthController(application);
    }

    public void auth(String authorizedKey) throws JSONException {
        controller.authorizedKey = authorizedKey;
        controller.work = "auth";
        controller.workState.setValue(-1);
        controller.workManager("auth");
    }

    public void authTheory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.work = "authTheory";
        controller.workState.setValue(-1);
        controller.workManager("authTheory");
    }

    public void register(String name, String mobile, String gender, String password) throws JSONException {
        controller.name = name;
        controller.mobile = mobile;
        controller.gender = gender;
        controller.password = password;
        controller.work = "register";
        controller.workState.setValue(-1);
        controller.workManager("register");
    }

    public void verification() throws JSONException {
        controller.work = "verification";
        controller.workState.setValue(-1);
        controller.workManager("verification");
    }

    public void forgetPassword() throws JSONException {
        controller.work = "forgetPassword";
        controller.workState.setValue(-1);
        controller.workManager("forgetPassword");
    }

    public JSONObject getStep(String theory) {
        JSONObject items = new JSONObject();
        switch (theory){
            case "auth":
                items = serialItems();
                break;
            case "password":
                items = passwordItems();
                break;
            case "mobileCode":
                items = pinItems();
                break;
        } return items;
    }

    private JSONObject serialItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("title", application.getApplicationContext().getResources().getString(R.string.AuthSerialTitle));
            authItems.put("description", application.getApplicationContext().getResources().getString(R.string.AuthSerialDescription));
            authItems.put("hint", application.getApplicationContext().getResources().getString(R.string.AuthSerialHint));
            authItems.put("button", application.getApplicationContext().getResources().getString(R.string.AuthSerialButton));
            authItems.put("link", application.getApplicationContext().getResources().getString(R.string.AuthSerialLink));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authItems;
    }

    private JSONObject passwordItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("title", application.getApplicationContext().getResources().getString(R.string.AuthPasswordTitle));
            authItems.put("description", application.getApplicationContext().getResources().getString(R.string.AuthPasswordDescription));
            authItems.put("hint", application.getApplicationContext().getResources().getString(R.string.AuthPasswordHint));
            authItems.put("button", application.getApplicationContext().getResources().getString(R.string.AuthPasswordButton));
            authItems.put("link", application.getApplicationContext().getResources().getString(R.string.AuthPasswordLink));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authItems;
    }

    private JSONObject pinItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("title", application.getApplicationContext().getResources().getString(R.string.AuthPinTitle));
            authItems.put("description", application.getApplicationContext().getResources().getString(R.string.AuthPinDescription));
            authItems.put("hint", application.getApplicationContext().getResources().getString(R.string.AuthPinHint));
            authItems.put("button", application.getApplicationContext().getResources().getString(R.string.AuthPinButton));
            authItems.put("link", application.getApplicationContext().getResources().getString(R.string.AuthPinLink));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authItems;
    }

}