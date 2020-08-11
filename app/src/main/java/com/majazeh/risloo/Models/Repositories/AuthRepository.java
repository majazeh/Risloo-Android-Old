package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controllers.AuthController;
import com.majazeh.risloo.Models.Items.AuthItems;

import org.json.JSONException;

import java.util.ArrayList;

public class AuthRepository extends MainRepository {

    // Controllers
    private AuthController controller;

    // Items
    private AuthItems authItems;

    // Objects
    private SharedPreferences sharedPreferences;

    public AuthRepository(Application application) throws JSONException {
        super(application);

        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        controller = new AuthController(application);
        authItems = new AuthItems(application, sharedPreferences);
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

    public void recovery(String mobile) throws JSONException {
        controller.mobile = mobile;
        controller.work = "recovery";
        controller.workState.setValue(-1);
        controller.workManager("recovery");
    }

    public void me() throws JSONException {
        controller.work = "me";
        controller.workState.setValue(-1);
        controller.workManager("me");
    }

    public void edit(String name, String gender, String birthday) throws JSONException {
        controller.name = name;
        controller.gender = gender;
        controller.birthday = birthday;
        controller.work = "edit";
        controller.workState.setValue(-1);
        controller.workManager("edit");
    }

    public void logOut() throws JSONException {
        controller.work = "logOut";
        controller.workState.setValue(-1);
        controller.workManager("logOut");
    }

    public ArrayList<Model> getAll() {
        return authItems.items();
    }

    public String getAvatar() {
        return authItems.avatar();
    }

    public String getName() {
        return authItems.name();
    }

    public String getType() {
        return authItems.type();
    }

}