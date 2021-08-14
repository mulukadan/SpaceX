package com.example.spacex.API;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * by Mulu Kadan on 13/08/2021.
 */

public interface Service {
    //Post Request & Get Response AS String
    @POST(" ")
    Call<String>postRequest(@Body RequestBody Body);

    //Get getRequestResponse AS String
    @GET(" ")
    Call<String> getRequestResponse();
}
