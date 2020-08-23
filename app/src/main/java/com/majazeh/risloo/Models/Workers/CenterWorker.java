package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Apis.CenterApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CenterWorker extends Worker {

    // Apis
    private CenterApi centerApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CenterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        centerApi = RetroGenerator.getRetrofit().create(CenterApi.class);

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
                case "getAll":
                    getAll();
                    break;
                case "getMy":
                    getMy();
                    break;
                case "request":
                    request();
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

    public void getAll() {
        try {
            Call<ResponseBody> call = centerApi.getAll(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                FileManager.writeObjectToCache(context, successBody, "centers", "all");

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "all", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "all", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }
    }

    public void getMy() {
        try {
            Call<ResponseBody> call = centerApi.getMy(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                FileManager.writeObjectToCache(context, successBody, "centers", "my");

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "my", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "my", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }
    }

    private void request() {
        try {
            Call<ResponseBody> call = centerApi.request(token(), CenterRepository.clinicId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject response = successBody.getJSONObject("data");
                JSONObject acceptation = response.getJSONObject("acceptation");
                JSONObject data = FileManager.readObjectFromCache(context, "centers", "all");
                JSONArray items = data.getJSONArray("data");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = (JSONObject) items.get(i);
                    if (response.getString("id").equals(item.getString("id"))) {
                        items.put(i, response);
                        break;
                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", items);
                FileManager.writeObjectToCache(context, jsonObject, "centers", "all");
                if (acceptation.isNull("kicked_at")) {
                    if (!acceptation.isNull("accepted_at")) {
                       JSONObject data2 = FileManager.readObjectFromCache(context, "centers", "my");
                       JSONArray jsonArray = data2.getJSONArray("data");
                       jsonArray.put(response);
                       JSONObject json = new JSONObject();
                       json.put("data", jsonArray);
                       FileManager.writeObjectToCache(context, json, "centers", "my");
                    }
                }


                ExceptionManager.getException(bodyResponse.code(), successBody, true, "request", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "request", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }
    }

}