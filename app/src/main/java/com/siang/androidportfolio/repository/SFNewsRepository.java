package com.siang.androidportfolio.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.siang.androidportfolio.model.SFNews;
import com.siang.androidportfolio.response.ArticleResponse;
import com.siang.androidportfolio.retrofit.SFNewsApiRequest;
import com.siang.androidportfolio.retrofit.SFNewsRetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SFNewsRepository {
    private final SFNewsApiRequest apiRequest;
    private MutableLiveData<SFNews> sfNewsMutableLiveData;

    public SFNewsRepository() {
        apiRequest = SFNewsRetrofitRequest.getRetrofitInstance().create(SFNewsApiRequest.class);
        sfNewsMutableLiveData = new MutableLiveData<>();
    }

    public void getSFNewsArticle(long newsId) {
        apiRequest.getSFNewsArticle(newsId)
                .enqueue(new Callback<SFNews>() {
                    @Override
                    public void onResponse(Call<SFNews> call, Response<SFNews> response) {
                        if (response.body() != null){
                            sfNewsMutableLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<SFNews> call, Throwable throwable) {
                        sfNewsMutableLiveData.setValue(null);
                    }
                });
    }

    public LiveData<SFNews> getSFNewsLiveData() {
        return sfNewsMutableLiveData;
    }
}
