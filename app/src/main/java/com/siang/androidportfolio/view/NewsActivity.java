package com.siang.androidportfolio.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.siang.androidportfolio.R;
import com.siang.androidportfolio.adapter.ArticleAdapter;
import com.siang.androidportfolio.model.Article;
import com.siang.androidportfolio.view_model.ArticleViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private LinearLayoutManager layoutManager;
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    ArticleViewModel articleViewModel;
    private ArticleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        init();

        getArticles();
    }

    private void getArticles() {
        articleViewModel.getDashBoardNewsResponseLiveData().observe(this, articleResponse -> {
            if (articleResponse != null && articleResponse.getArticles() != null && !articleResponse.getArticles().isEmpty()) {
                progressBar.setVisibility(View.GONE);
                List<Article> articleList = articleResponse.getArticles();
                articleArrayList.addAll(articleList);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void init() {
        progressBar = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.rvNews);
        layoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ArticleAdapter(NewsActivity.this, articleArrayList);
        recyclerView.setAdapter(adapter);

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
    }
}