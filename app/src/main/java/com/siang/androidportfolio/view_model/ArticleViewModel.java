package com.siang.androidportfolio.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.siang.androidportfolio.repository.ArticleRepository;
import com.siang.androidportfolio.response.ArticleResponse;

public class ArticleViewModel extends AndroidViewModel {
    private ArticleRepository articleRepository;
    private LiveData<ArticleResponse> articleResponseLiveData;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        articleRepository = new ArticleRepository();
        this.articleResponseLiveData = articleRepository.getDashBoardNews();
    }

    public LiveData<ArticleResponse> getDashBoardNewsResponseLiveData(){
        return articleResponseLiveData;
    }
}
