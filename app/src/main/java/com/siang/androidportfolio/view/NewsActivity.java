package com.siang.androidportfolio.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.siang.androidportfolio.R;
import com.siang.androidportfolio.adapter.ArticleAdapter;
import com.siang.androidportfolio.adapter.SFNewsAdapter;
import com.siang.androidportfolio.model.Article;
import com.siang.androidportfolio.model.SFNews;
import com.siang.androidportfolio.view_model.ArticleViewModel;
import com.siang.androidportfolio.view_model.SFNewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private LinearLayoutManager layoutManager;
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private ArrayList<SFNews> sfNewsArrayList = new ArrayList<>();
    ArticleViewModel articleViewModel;
    SFNewsViewModel sfNewsViewModel;
    private ArticleAdapter articleAdapter;
    private SFNewsAdapter sfNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        init();

        getArticles();
        getSFNewsList();
        sfNewsViewModel.getSFNewsArticleList(10);
    }

    private void getArticles() {
        articleViewModel.getDashBoardNewsResponseLiveData().observe(this, articleResponse -> {
            if (articleResponse != null && articleResponse.getArticles() != null && !articleResponse.getArticles().isEmpty()) {
                progressBar.setVisibility(View.GONE);
                List<Article> articleList = articleResponse.getArticles();
                articleArrayList.addAll(articleList);
                articleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getSFNewsList() {
        Log.i("FCMToken", "getSFNewsList");
        sfNewsViewModel.getSFNewsListLiveData().observe(this, sfNewsList -> {
            if (sfNewsList != null && !sfNewsList.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                sfNewsArrayList.addAll(sfNewsList);
                sfNewsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void init() {
        progressBar = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.rvNews);
        layoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        articleAdapter = new ArticleAdapter(NewsActivity.this, articleArrayList);
        sfNewsAdapter = new SFNewsAdapter(NewsActivity.this, sfNewsArrayList);
        recyclerView.setAdapter(sfNewsAdapter);

        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        sfNewsViewModel = new ViewModelProvider(this).get(SFNewsViewModel.class);
    }
}