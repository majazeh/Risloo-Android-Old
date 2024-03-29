package com.majazeh.risloo.Models.Apis;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SampleApi {

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("$/samples")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization, @Query("page") int page, @Query("scale") String scale, @Query("room") String room, @Query("status") String status);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("$/samples/{sample_id}")
    Call<ResponseBody> getSingle(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("$/samples/{sample_id}/items")
    Call<ResponseBody> send(@Header("Authorization") String authorization, @Path("sample_id") String sampleId, @Body Object body);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("$/samples")
    Call<ResponseBody> create(@Header("Authorization") String authorization, @Body HashMap body);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @PUT("$/samples/{sample_id}/close")
    Call<ResponseBody> close(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("$/samples/{sample_id}/scoring")
    Call<ResponseBody> score(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("$")
    Call<ResponseBody> getScales(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms?my_management=1")
    Call<ResponseBody> getRooms(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("cases")
    Call<ResponseBody> getCases(@Header("Authorization") String authorization, @Query("room") String room, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms/{room_id}/users?status=accepted")
    Call<ResponseBody> getReferences(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("$/samples/{sample_id}")
    Call<ResponseBody> getGeneral(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("$/samples/{sample_id}/scoring")
    Call<ResponseBody> getScore(@Header("Authorization") String authorization, @Path("sample_id") String sampleId);


}