package com.example.spacex.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * by Mulu Kadan on 13/08/2021.
 */

public class Client {
    static String username = "mulukadan";
    static String api_key = "nnkLxSWOtXA2lKfOdLlWg6J7d8pJeCuirMTEJs4E9VrjHQhlGP";
    static String sender = "SMARTLINK";


    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();


    //Get Company Info
    public static  Retrofit getInfo(String Url){
       Retrofit retrofit = null;
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
