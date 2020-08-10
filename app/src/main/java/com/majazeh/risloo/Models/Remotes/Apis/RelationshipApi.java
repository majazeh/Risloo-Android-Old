package com.majazeh.risloo.Models.Remotes.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RelationshipApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @GET("relationships")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @GET("relationships?my=1")
    Call<ResponseBody> getMy(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @POST("relationships/request")
    Call<ResponseBody> request(@Header("Authorization") String authorization, @Field("clinic_id") String clinicId);

}