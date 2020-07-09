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
    @POST("$/samples/{Sample_id}/items")
    Call<ResponseBody> send(@Path("Sample_id") String sample_id,@Path("items") ArrayList<ArrayList<Integer>> items);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @POST("auth/verification")
    Call<ResponseBody> verification(@Query("mobile") String mobile);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> auth(@Header("Authorization") String authorization, @Field("callback") String callback, @Query("authorized_key") String authorized_key);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth/theory/{LoginKey}")
    Call<ResponseBody> authTheory(@Header("Authorization") String authorization, @Path("LoginKey") String LoginKey, @Field("callback") String callback, @Query("password") String input, @Query("code") String coe);

    @POST("register")
    Call<ResponseBody> signIn(@Query("name") String name, @Query("gender") String gender, @Query("mobile") String mobile, @Query("password") String password);

    @GET("explode")
    Call<ResponseBody> explode();


}