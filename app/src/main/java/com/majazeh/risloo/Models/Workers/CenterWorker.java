package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Controllers.CenterController;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Remotes.Apis.CenterApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;

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

    // Managers
    private FileManager manager;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CenterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        centerApi = RetroGenerator.getRetrofit().create(CenterApi.class);

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
            Call<ResponseBody> call = centerApi.getAll(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                manager.writeToCache(context, successBody, "centers", "all");

                CenterController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                CenterController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                CenterController.exception = errorBody.getString("message_text");
                CenterController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        }
    }

    public void getMy() {
        try {
            Call<ResponseBody> call = centerApi.getMy(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                manager.writeToCache(context, successBody, "centers", "my");

                CenterController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                CenterController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                CenterController.exception = errorBody.getString("message_text");
                CenterController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        }
    }

    private void request() {
        try {
            Call<ResponseBody> call = centerApi.request(token(), CenterController.clinicId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                CenterController.exception = "درخواست شما با موفقیت ارسال شد";
                CenterController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                CenterController.exception = errorBody.getString("message_text");
                CenterController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            CenterController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            CenterController.workState.postValue(0);
        }
    }

}