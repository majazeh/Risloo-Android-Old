package com.majazeh.risloo.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Models.Repositories.Authentication.AuthController;
import com.majazeh.risloo.Models.Repositories.Authentication.AuthRepository;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthViewModel extends AndroidViewModel {

    private AuthRepository repository;

    public AuthViewModel(@NonNull Application application) {
        super(application);

        repository = new AuthRepository(application);
    }


    public void auth_theory(String password, String code) throws JSONException {
        repository.auth_theory(password, code);
    }

    public void auth(String authorized_key) throws JSONException {
        repository.auth(authorized_key);
        Log.e( "auth: ", String.valueOf(AuthController.workState.getValue()));
    }

    public void signIn(String name, String gender, String mobile, String password) throws JSONException {
        repository.signIn(name, gender, mobile, password);
    }

    public JSONObject getStep() {
        return repository.getStep(AuthController.theory);
    }

}