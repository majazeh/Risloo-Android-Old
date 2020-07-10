package com.majazeh.risloo.Models.Remotes.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExplodeApi {

    @GET("explode")
    Call<ResponseBody> explode();

}
