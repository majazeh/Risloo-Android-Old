package com.majazeh.risloo.Models.Remotes.Apis;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SampleApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("$/samples/{Sample_id}/items")
    Call<ResponseBody> send(@Path("Sample_id") String sampleId,@Path("items") ArrayList<ArrayList<Integer>> items);

}