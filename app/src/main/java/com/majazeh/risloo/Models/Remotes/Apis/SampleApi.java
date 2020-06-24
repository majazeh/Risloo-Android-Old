package com.majazeh.risloo.Models.Remotes.Apis;

import com.majazeh.risloo.Entities.Sample.Sample;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SampleApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("answers")
    Call<Sample> insert(@Field("data") ArrayList sending_data);

}