package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Apis.ExplodeApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
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
    private ExplodeApi explodeApi;
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ExplodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;


        sharedPreferences = context.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        explodeApi = RetroGenerator.getRetrofit().create(ExplodeApi.class);
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

    private void explode() {
        try {
            Call<ResponseBody> call = explodeApi.explode();

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());
                JSONObject android = successBody.getJSONObject("android");

                if (Integer.parseInt(android.getString("current")) < Integer.parseInt(android.getString("force"))) {
                    // TODO: Force Update
                } else {
                    // TODO: Normal Update
                }

                JSONObject data = successBody.getJSONObject("user");

                boolean hasAccess = false;


                if (data.has("type") && data.getString("type").equals("admin")) {
                    hasAccess = true;
                } else {
                    JSONArray centers = data.getJSONArray("centers");
                    for (int i = 0; i < centers.length(); i++) {
                        JSONObject jsonObject = centers.getJSONObject(i);
                        JSONObject acceptation = jsonObject.getJSONObject("acceptation");

                        if (acceptation.getString("position").equals("operator") || acceptation.getString("position").equals("manager") || acceptation.getString("position").equals("psychologist")) {
                            hasAccess = true;
                        }
                    }
                }

                if (hasAccess) {
                    editor.putBoolean("hasAccess", true);
                } else {
                    editor.putBoolean("hasAccess", false);
                }

                FileManager.writeObjectToCache(context, new JSONObject().put("data", data.getJSONArray("centers")), "centers", "my");

                if (data.has("id"))
                    editor.putString("userId", data.getString("id"));
                else
                    editor.putString("userId", "");

                if (data.has("name"))
                    editor.putString("name", data.getString("name"));
                else
                    editor.putString("name", "");

                if (data.has("type"))
                    editor.putString("type", data.getString("type"));
                else
                    editor.putString("type", "");

                if (data.has("mobile"))
                    editor.putString("mobile", data.getString("mobile"));
                else
                    editor.putString("mobile", "");

                if (data.has("email"))
                    editor.putString("email", data.getString("email"));
                else
                    editor.putString("email", "");

                if (data.has("gender"))
                    editor.putString("gender", data.getString("gender"));
                else
                    editor.putString("gender", "");

                if (data.has("birthday"))
                    editor.putString("birthday", data.getString("birthday"));
                else
                    editor.putString("birthday", "");

                if (data.has("avatar") && !data.isNull("avatar")) {
                    JSONObject avatar = data.getJSONObject("avatar");
                    JSONObject medium = avatar.getJSONObject("medium");

                    editor.putString("avatar", medium.getString("url"));
                } else {
                    editor.putString("avatar", "");
                }

                editor.apply();


                ExceptionManager.getException(true, bodyResponse.code(), successBody, "explode", "explode");
                ExplodeRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "explode", "explode");
                ExplodeRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "explode");
            ExplodeRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "explode");
            ExplodeRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "explode");
            ExplodeRepository.workState.postValue(0);
        }
    }

}