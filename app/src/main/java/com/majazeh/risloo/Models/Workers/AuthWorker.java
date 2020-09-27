package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Apis.AuthApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AuthWorker extends Worker {

    // Apis
    private AuthApi authApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        authApi = RetroGenerator.getRetrofit().create(AuthApi.class);

        sharedPreferences = context.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "auth":
                    auth();
                    break;
                case "authTheory":
                    authTheory();
                    break;
                case "register":
                    register();
                    break;
                case "verification":
                    verification();
                    break;
                case "recovery":
                    recovery();
                    break;
                case "me":
                    me();
                    break;
                case "edit":
                    edit();
                    break;
                case "logOut":
                    logOut();
                    break;
                case "setAvatar":
                    setAvatar();
                    break;
            }
        }

        return Result.success();
    }

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }

    private void auth() {
        try {
            Call<ResponseBody> call = authApi.auth(token(), AuthRepository.callback, AuthRepository.authorizedKey);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "auth", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "auth", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void authTheory() {
        try {
            Call<ResponseBody> call = authApi.authTheory(token(), AuthRepository.key, AuthRepository.callback, AuthRepository.password, AuthRepository.code);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "authTheory", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "authTheory", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void register() {
        try {
            Call<ResponseBody> call = authApi.register(AuthRepository.name, AuthRepository.mobile, AuthRepository.gender, AuthRepository.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "register", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "register", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = authApi.verification(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "verification", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "verification", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = authApi.recovery(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "recovery", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "recovery", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = authApi.me(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());
                JSONObject data = successBody.getJSONObject("data");

                boolean access = false;

                if (data.has("type") && data.getString("type").equals("admin")) {
                    access = true;
                } else {
                    JSONArray centers = data.getJSONArray("centers");
                    for (int i = 0; i < centers.length(); i++) {
                        JSONObject jsonObject = centers.getJSONObject(i);
                        JSONObject acceptation = jsonObject.getJSONObject("acceptation");
                        if (acceptation.getString("position").equals("operator") || acceptation.getString("position").equals("manager") || acceptation.getString("position").equals("psychologist")) {
                            access = true;
                        }
                    }
                }

                if (access) {
                    editor.putString("access", "true");
                } else {
                    editor.putString("access", "false");
                }

                FileManager.writeObjectToCache(context, new JSONObject().put("data", data.getJSONArray("centers")), "centers", "my");
                    editor.putString("userId", data.getString("id"));
                if (data.has("name"))
                    editor.putString("name", data.getString("name"));
                else
                    editor.putString("name", "null");

                if (data.has("type"))
                    editor.putString("type", data.getString("type"));
                else
                    editor.putString("type", "null");

                if (data.has("mobile"))
                    editor.putString("mobile", data.getString("mobile"));
                else
                    editor.putString("mobile", "null");

                if (data.has("email"))
                    editor.putString("email", data.getString("email"));
                else
                    editor.putString("email", "null");

                if (data.has("gender"))
                    editor.putString("gender", data.getString("gender"));
                else
                    editor.putString("gender", "null");

                if (data.has("birthday"))
                    editor.putString("birthday", data.getString("birthday"));
                else
                    editor.putString("birthday", "null");

                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "me", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "me", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void edit() {
        try {
            Call<ResponseBody> call = authApi.edit(token(), AuthRepository.name, AuthRepository.gender, AuthRepository.birthday);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.putString("name", AuthRepository.name);
                editor.putString("gender", AuthRepository.gender);
                editor.putString("birthday", AuthRepository.birthday);
                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "edit", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "edit", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void logOut() {
        try {
            Call<ResponseBody> call = authApi.logOut(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.remove("token");
                editor.remove("name");
                editor.remove("type");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("gender");
                editor.remove("birthday");
                editor.remove("avatar");
                editor.remove("userId");
                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "logOut", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "logOut", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void setAvatar() {
        try {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), new File(context.getCacheDir(),"profile"));
            Call<ResponseBody> call = authApi.setAvatar(token(), sharedPreferences.getString("userId", ""), MultipartBody.Part.createFormData("upload", new File(context.getCacheDir(),"profile").getName(), reqFile));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "setAvatar", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "setAvatar", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

}