package com.majazeh.risloo.Models.Repositories.Authentication;

import android.app.Application;
import android.util.Log;

import com.majazeh.risloo.Models.Repositories.MainRepository;
import com.majazeh.risloo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthRepository extends MainRepository {
    private AuthController controller;

    public AuthRepository(Application application) {
        super(application);
        controller = new AuthController(application);
    }

    public void auth(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.work = "auth";
        controller.auth();
    }

    public void auth_theory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.work = "authTheory";
        controller.auth_theory();
        checkState();
    }

    public void signIn(String name, String gender, String mobile, String password) throws JSONException {
        controller.name = name;
        controller.gender = gender;
        controller.mobile = mobile;
        controller.password = password;
        controller.work = "signIn";
        controller.signIn();
        checkState();
    }

    public void start(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.auth();
        checkState();
    }

    public void checkState() {
        controller.workState().observeForever(integer -> {
            //auth
            if (controller.work.equals("auth")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                                break;
                        }
                    } else if (!controller.callback.equals("")) {
                        // TODO: go to auth with another authorized_key
                    } else {
                        // TODO: open panel
                        controller.workState().removeObservers(() -> null);
                    }
                } else if (controller.workState().getValue() == 0) {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                } else {

                }
                //auth theory
            } else if (controller.work.equals("authTheory")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                        }
                    } else {
                        // TODO: open panel
                        controller.workState().removeObservers(() -> null);
                    }
                } else {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                }
                //signIn
            } else if (controller.work.equals("signIn")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                        }
                    }
                } else {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                }
            }
        });
    }

    public JSONObject getStep(String step) {
        JSONObject items = new JSONObject();

        switch (step){
            case "serial":
                items = serialItems();
                break;
            case "password":
                items = passwordItems();
                break;
            case "mobile":
                items = mobileItems();
                break;
            case "pin":
                items = pinItems();
                break;
        } return items;
    }

    private JSONObject serialItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("step", "serial");
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
            authItems.put("step", "password");
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

    private JSONObject mobileItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("step", "mobile");
            authItems.put("title", application.getApplicationContext().getResources().getString(R.string.AuthMobileTitle));
            authItems.put("description", application.getApplicationContext().getResources().getString(R.string.AuthMobileDescription));
            authItems.put("hint", application.getApplicationContext().getResources().getString(R.string.AuthMobileHint));
            authItems.put("button", application.getApplicationContext().getResources().getString(R.string.AuthMobileButton));
            authItems.put("link", application.getApplicationContext().getResources().getString(R.string.AuthMobileLink));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authItems;
    }

    private JSONObject pinItems() {
        JSONObject authItems = new JSONObject();
        try {
            authItems.put("step", "pin");
            authItems.put("title", application.getApplicationContext().getResources().getString(R.string.AuthPinTitle));
            authItems.put("description", application.getApplicationContext().getResources().getString(R.string.AuthPinDescription));
            authItems.put("hint", application.getApplicationContext().getResources().getString(R.string.AuthPinHint));
            authItems.put("button", application.getApplicationContext().getResources().getString(R.string.AuthPinButton));
            authItems.put("link", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return authItems;
    }

}