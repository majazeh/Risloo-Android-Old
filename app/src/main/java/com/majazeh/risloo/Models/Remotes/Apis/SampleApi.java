package com.majazeh.risloo.Models.Remotes.Apis;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SampleApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @GET("$/samples/{sample_id}")
    Call<ResponseBody> getSample(@Header("Authorization") String authorization, @Path("sample_id") String sampleID);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @POST("$/samples/{sample_id}/items")
    Call<ResponseBody> send(@Header("Authorization") String authorization, @Path("sample_id") String sampleId, @Header("items") ArrayList<ArrayList<Integer>> items);

}