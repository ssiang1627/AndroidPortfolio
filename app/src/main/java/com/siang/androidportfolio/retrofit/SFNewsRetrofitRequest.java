package com.siang.androidportfolio.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.siang.androidportfolio.constants.AppConstant.SPACEFLIGHT_NEWS_BASE_URL;

public class SFNewsRetrofitRequest {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SPACEFLIGHT_NEWS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
