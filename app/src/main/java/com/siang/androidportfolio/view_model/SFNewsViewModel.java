package com.siang.androidportfolio.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.siang.androidportfolio.model.SFNews;
import com.siang.androidportfolio.repository.SFNewsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SFNewsViewModel extends AndroidViewModel {

    private SFNewsRepository sfNewsRepository;
    private LiveData<SFNews> sfNewsLiveData;
    private LiveData<List<SFNews>> sfNewsListLiveData;

    public SFNewsViewModel(@NonNull @NotNull Application application) {
        super(application);
        sfNewsRepository = new SFNewsRepository();
        sfNewsLiveData = sfNewsRepository.getSFNewsLiveData();
        sfNewsListLiveData = sfNewsRepository.getSFNewsListLiveData();

    }

    public void getSFNewsArticle(long newsId) {
        sfNewsRepository.getSFNewsArticle(newsId);
    }
    public void getSFNewsArticleList(int articlesNumber) {
        sfNewsRepository.getSFNewsArticleList(articlesNumber);
    }
    public LiveData<SFNews> getSFNewsLiveData() {
        return sfNewsLiveData;
    }
    public LiveData<List<SFNews>> getSFNewsListLiveData() {
        return sfNewsListLiveData;
    }

}
