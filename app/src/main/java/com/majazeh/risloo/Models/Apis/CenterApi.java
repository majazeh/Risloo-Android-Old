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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CenterApi {

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("centers")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization, @Query("page") int page);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("centers?my=yes")
    Call<ResponseBody> getMy(@Header("Authorization") String authorization, @Query("page") int page);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @POST("centers/{center}/request")
    Call<ResponseBody> request(@Header("Authorization") String authorization, @Path("center") String center);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("centers")
    Call<ResponseBody> create(@Header("Authorization") String authorization, @Body HashMap body);


    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("users?personal_clinic=no")
    Call<ResponseBody> managerIdPersonalClinic(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("users?personal_clinic=yes")
    Call<ResponseBody> managerIdCounselingCenter(@Header("Authorization") String authorization);
}