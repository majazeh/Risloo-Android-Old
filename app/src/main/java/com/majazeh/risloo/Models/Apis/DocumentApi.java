package com.majazeh.risloo.Models.Apis;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DocumentApi {
    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("documents")
    Call<ResponseBody> documents(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("documents/{document_id}")
    Call<ResponseBody> docStatus(@Header("Authorization") String authorization, @Path("document_id") String documentId, @Body HashMap body);

}