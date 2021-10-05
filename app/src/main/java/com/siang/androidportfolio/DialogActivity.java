package com.siang.androidportfolio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("This is the title");
        alertDialog.setMessage("This is the message");
        alertDialog.setIcon(R.drawable.newspaper);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //destroy the activity when cancel the dialog
                finish();
            }
        });
        alertDialog.show();

        SharedPreferences pref = getSharedPreferences("campaign", MODE_PRIVATE);
        pref.edit()
                .putBoolean("DISPLAY", true)
                .apply();
    }
}