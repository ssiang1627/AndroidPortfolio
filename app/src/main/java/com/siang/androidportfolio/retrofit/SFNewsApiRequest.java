package com.siang.androidportfolio.retrofit;

import com.siang.androidportfolio.model.SFNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SFNewsApiRequest {

    @GET("articles/{id}")
    Call<SFNews> getSFNewsArticle(
            @Path("id") long newsId
    );

    @GET("articles")
    Call<List<SFNews>> getSFNewsArticleList(
            @Query("_limit") int articlesNumber
    );
}
