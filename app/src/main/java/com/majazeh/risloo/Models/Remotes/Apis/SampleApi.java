package com.majazeh.risloo.Models.Remotes.Apis;

import org.json.JSONArray;

import java.util.ArrayList;

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

public interface SampleApi {

    @Headers({"content-type: application/json"})
    @GET("$/samples/{sample_id}")
    Call<ResponseBody> get(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/json"})
    @POST("$/samples/{sample_id}/items")
    Call<ResponseBody> send(@Header("Authorization") String authorization, @Path("sample_id") String sampleId, @Body Object body);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("$/samples/{sample_id}/close")
    Call<ResponseBody> close(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("$/samples/{sample_id}/items")
    Call<ResponseBody> prerequisite(@Header("Authorization") String authorization, @Path("sample_id") String sampleId, @Field("prerequisite") Object items);

}