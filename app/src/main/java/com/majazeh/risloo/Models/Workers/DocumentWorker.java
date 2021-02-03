package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.majazeh.risloo.Models.Apis.DocumentApi;
import com.majazeh.risloo.Models.Repositories.DocumentRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DocumentWorker extends Worker {

    // Apis
    private final DocumentApi api;

    // Objects
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public DocumentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        api = RetroGenerator.getRetrofit().create(DocumentApi.class);

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
                case "send":
                    send();
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

    private void getAll() {
        try {
            Call<ResponseBody> call = api.documents(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.getJSONArray("data").length() != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        FileManager.deletePageFromCache(context, "documents");
                    }
                    FileManager.writeObjectToCache(context, successBody, "documents");

                    JSONObject jsonObject = FileManager.readObjectFromCache(context, "documents");
                    JSONArray data;
                    try {
                        data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                            JSONArray jsonArray = successBody.getJSONArray("data");
                            data.put(jsonArray.getJSONObject(i));
                        }
                        jsonObject.put("data", data);
                        FileManager.writeObjectToCache(context, jsonObject, "documents");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    FileManager.deletePageFromCache(context, "documents");
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "documents");
                DocumentRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "documents");
                DocumentRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            DocumentRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            DocumentRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            DocumentRepository.workState.postValue(0);
        }
    }

    private void send() {
        File attachment = new File(DocumentRepository.fileAttachment);

        AndroidNetworking.upload("https://bapi.risloo.ir/api/documents")
                .addHeaders("Authorization", token())
                .addMultipartFile("attachment", attachment)
                .addMultipartParameter("title", DocumentRepository.fileTitle)
                .addMultipartParameter("description", DocumentRepository.fileDescription)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject successBody = new JSONObject(response.toString());

                            FileManager.deleteFolderFromCache(context, "documents");

                            ExceptionGenerator.getException(true, 200, successBody, "send");
                            DocumentRepository.workState.postValue(1);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionGenerator.getException(false, 0, null, "JSONException");
                            DocumentRepository.workState.postValue(0);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorBody = new JSONObject(error.getErrorBody());

                            ExceptionGenerator.getException(true, error.getErrorCode(), errorBody, "send");
                            DocumentRepository.workState.postValue(0);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionGenerator.getException(false, 0, null, "JSONException");
                            DocumentRepository.workState.postValue(0);
                        }
                    }

                });
    }

}