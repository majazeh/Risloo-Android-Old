package com.majazeh.risloo.ViewModels.Authentication;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.majazeh.risloo.Models.Repositories.Authentication.AuthRepository;

import org.json.JSONException;

public class AuthViewModel extends AndroidViewModel {
    AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void start(String authorized_key) throws JSONException {
        authRepository.start(authorized_key);
    }

    public void auth_theory(String password, String code) throws JSONException {
        authRepository.auth_theory(password, code);
    }

    public void auth_theory(String authorized_key) throws JSONException {
        authRepository.auth(authorized_key);
    }

    public void signIn(String name, String gender, String mobile, String password) throws JSONException {
        authRepository.signIn(name, gender, mobile, password);
    }
}
