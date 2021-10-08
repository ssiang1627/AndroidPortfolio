package com.siang.androidportfolio.retrofit;

import com.siang.androidportfolio.model.SFNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SFNewsApiRequest {

    @GET("articles/{id}")
    Call<SFNews> getSFNewsArticle(
            @Path("id") long newsId
    );
}
