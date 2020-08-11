package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Controllers.RelationshipController;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Remotes.Apis.RelationshipApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RelationshipWorker extends Worker {

    // Apis
    private RelationshipApi relationshipApi;

    // Managers
    private FileManager manager;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public RelationshipWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        relationshipApi = RetroGenerator.getRetrofit().create(RelationshipApi.class);

        manager = new FileManager();

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
            Call<ResponseBody> call = relationshipApi.getAll(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                manager.writeToCache(context, successBody, "relationships", "all");

                RelationshipController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                RelationshipController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                RelationshipController.exception = errorBody.getString("message_text");
                RelationshipController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        }
    }

    public void getMy() {
        try {
            Call<ResponseBody> call = relationshipApi.getMy(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                manager.writeToCache(context, successBody, "relationships", "my");

                RelationshipController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                RelationshipController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                RelationshipController.exception = errorBody.getString("message_text");
                RelationshipController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        }
    }

    private void request() {
        try {
            Call<ResponseBody> call = relationshipApi.request(token(), RelationshipController.clinicId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                RelationshipController.exception = "درخواست شما با موفقیت ارسال شد";
                RelationshipController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                RelationshipController.exception = errorBody.getString("message_text");
                RelationshipController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            RelationshipController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            RelationshipController.workState.postValue(0);
        }
    }

}