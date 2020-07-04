package com.majazeh.risloo.Models.Remotes.Apis;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;;

public interface SampleApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("answers")
    Call<ResponseBody> send(@Field("data") ArrayList data);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> auth(@Header("Authorization") String authorization, @Field("callback") String callback, @Query("authorized_key") String authorized_key);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth/theory/{LoginKey}")
    Call<ResponseBody> authTheory(@Header("Authorization") String authorization, @Path("LoginKey") String LoginKey, @Field("callback") String callback, @Query("password") String input, @Query("code") String coe);
}