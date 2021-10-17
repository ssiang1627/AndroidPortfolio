package com.siang.androidportfolio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.siang.androidportfolio.model.SFNews;
import com.siang.androidportfolio.view_model.SFNewsViewModel;

public class DialogActivity extends AppCompatActivity {
    private SFNewsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("campaign", MODE_PRIVATE);

        Intent intent = getIntent();
        long campaignNewsId = intent.getLongExtra("CAMPAIGN_NEWS_ID",0);

        viewModel = new ViewModelProvider(this).get(SFNewsViewModel.class);
        viewModel.getSFNewsLiveData().observe(this, new Observer<SFNews>() {
            @Override
            public void onChanged(SFNews sfNews) {
                Log.i("FCMToken", sfNews.toString());
                if (sfNews != null){
                    showDialog(sfNews.getTitle(), sfNews.getSummary());
                    pref.edit()
                            .putBoolean("DISPLAY", true)
                            .apply();
                }
            }
        });

        if (campaignNewsId != 0){
            viewModel.getSFNewsArticle(campaignNewsId);
        }
    }

    private void showDialog(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.newspaper);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //destroy the activity when cancel the dialog
                finish();
            }
        });
        alertDialog.show();
    }
}