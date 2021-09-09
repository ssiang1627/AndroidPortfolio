package com.siang.androidportfolio.retrofit;

import com.siang.androidportfolio.response.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.siang.androidportfolio.constants.AppConstant.API_KEY;

public interface ApiRequest {

    @GET("top-headlines?country=us&apiKey="+API_KEY)
    Call<ArticleResponse> getTopHeadlines();
}
