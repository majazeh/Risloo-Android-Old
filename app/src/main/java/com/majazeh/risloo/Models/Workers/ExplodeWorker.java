package com.majazeh.risloo.Models.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Apis.ExplodeApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.ExplodeRepository;

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

    public ExplodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

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