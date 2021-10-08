package com.siang.androidportfolio.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.siang.androidportfolio.model.SFNews;
import com.siang.androidportfolio.repository.SFNewsRepository;

import org.jetbrains.annotations.NotNull;

public class SFNewsViewModel extends AndroidViewModel {

    private SFNewsRepository sfNewsRepository;
    private LiveData<SFNews> sfNewsLiveData;

    public SFNewsViewModel(@NonNull @NotNull Application application) {
        super(application);
        sfNewsRepository = new SFNewsRepository();
        sfNewsLiveData = sfNewsRepository.getSFNewsLiveData();
    }

    public void getSFNewsArticle(long newsId) {
        sfNewsRepository.getSFNewsArticle(newsId);
    }

    public LiveData<SFNews> getSfNewsLiveData() {
        return sfNewsLiveData;
    }
}
