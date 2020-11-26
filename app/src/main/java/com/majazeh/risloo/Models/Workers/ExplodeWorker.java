package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Apis.ExplodeApi;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.ExplodeRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ExplodeWorker extends Worker {

    // Apis
    private final ExplodeApi api;

    // Objects
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public ExplodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        api = RetroGenerator.getRetrofit().create(ExplodeApi.class);

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
                case "explode":
                    explode();
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

    private void explode() {
        try {
            Call<ResponseBody> call = api.explode();

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                JSONObject version = successBody.getJSONObject("version");
                JSONObject android = version.getJSONObject("android");

                if (Integer.parseInt(android.getString("current")) < Integer.parseInt(android.getString("force"))) {
                    // TODO: Force Update
                } else {
                    // TODO: Normal Update
                }

                JSONObject user = successBody.getJSONObject("user");
                JSONArray centers = user.getJSONArray("centers");

                FileManager.writeObjectToCache(context, new JSONObject().put("data", centers), "centers" + "/" + "my");

                boolean hasAccess = false;

                if (user.has("type") && user.getString("type").equals("admin")) {
                    hasAccess = true;
                } else {
                    for (int i = 0; i < centers.length(); i++) {
                        JSONObject center = centers.getJSONObject(i);
                        JSONObject acceptation = center.getJSONObject("acceptation");

                        if (acceptation.getString("position").equals("operator") || acceptation.getString("position").equals("manager") || acceptation.getString("position").equals("psychologist")) {
                            hasAccess = true;
                        }
                    }
                }

                editor.putBoolean("hasAccess", hasAccess);

                if (user.has("id") && !user.isNull("id")) {
                    editor.putString("userId", user.getString("id"));
                } else {
                    editor.putString("userId", "");
                }

                if (user.has("name") && !user.isNull("name")) {
                    editor.putString("name", user.getString("name"));
                } else {
                    editor.putString("name", "");
                }

                if (user.has("type") && !user.isNull("type")) {
                    editor.putString("type", user.getString("type"));
                } else {
                    editor.putString("type", "");
                }

                if (user.has("mobile") && !user.isNull("mobile")) {
                    editor.putString("mobile", user.getString("mobile"));
                } else {
                    editor.putString("mobile", "");
                }

                if (user.has("email") && !user.isNull("email")) {
                    editor.putString("email", user.getString("email"));
                } else {
                    editor.putString("email", "");
                }

                if (user.has("gender") && !user.isNull("gender")) {
                    editor.putString("gender", user.getString("gender"));
                } else {
                    editor.putString("gender", "");
                }

                if (user.has("birthday") && !user.isNull("birthday")) {
                    editor.putString("birthday", user.getString("birthday"));
                } else {
                    editor.putString("birthday", "");
                }

                if (!user.isNull("avatar") && user.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                    JSONObject avatar = user.getJSONObject("avatar");
                    JSONObject medium = avatar.getJSONObject("medium");

                    editor.putString("avatar", medium.getString("url"));
                } else {
                    editor.putString("avatar", "");
                }

                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "explode");
                ExplodeRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "explode");
                ExplodeRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            ExplodeRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            ExplodeRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            ExplodeRepository.workState.postValue(0);
        }
    }

}