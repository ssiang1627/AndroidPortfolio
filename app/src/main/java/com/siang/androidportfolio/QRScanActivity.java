package com.siang.androidportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private ConstraintLayout layoutPermissionDenied;
    private Button btnSetting;
    private TextView tvResult1;
    private TextView tvResult2;
    private TextView tvResult3;
    private Handler pauseHandler = new Handler();
    private Button btnReset;
    private PermissionHelper permissionHelper = new PermissionHelper(QRScanActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvToolbarTitle.setText("QR Scan");

        scannerView = (ZXingScannerView)findViewById(R.id.zxscan);
        tvResult1 = (TextView)findViewById(R.id.tvResult1);
        tvResult2 = (TextView)findViewById(R.id.tvResult2);
        tvResult3 = (TextView)findViewById(R.id.tvResult3);
        btnReset = (Button)findViewById(R.id.btnReset);
        layoutPermissionDenied = (ConstraintLayout)findViewById(R.id.layout_permissionDenied);
        btnSetting = (Button)findViewById(R.id.btnSetting);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.showSettingPage();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult1.setText("");
                tvResult2.setText("");
                tvResult3.setText("");
                scannerView.resumeCameraPreview(QRScanActivity.this);
            }
        });

        View.OnClickListener tvOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                tv.setText("");
                scannerView.resumeCameraPreview(QRScanActivity.this);
            }
        };
        tvResult1.setOnClickListener(tvOnClickListener);
        tvResult2.setOnClickListener(tvOnClickListener);
        tvResult3.setOnClickListener(tvOnClickListener);

        permissionHelper.requestPermission(Manifest.permission.CAMERA);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)){
            scannerView.setResultHandler(this);
            scannerView.startCamera();
            scannerView.setVisibility(View.VISIBLE);
            layoutPermissionDenied.setVisibility(View.GONE);
        }else{
            scannerView.setVisibility(View.GONE);
            layoutPermissionDenied.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (tvResult1.getText() == ""){
            tvResult1.setText(rawResult.getText());
        }else if (tvResult2.getText() == ""){
            tvResult2.setText(rawResult.getText());
        }else if (tvResult3.getText() == ""){
            tvResult3.setText(rawResult.getText());
            Toast.makeText(QRScanActivity.this,"請點選重置以重新掃描", Toast.LENGTH_SHORT).show();

        }

        if (tvResult1.getText() == "" || tvResult2.getText() == "" || tvResult3.getText() == ""){
            pauseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scannerView.resumeCameraPreview(QRScanActivity.this);
                }
            },500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.CAMERA_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scannerView.setResultHandler(QRScanActivity.this);
                scannerView.startCamera();
                scannerView.setVisibility(View.VISIBLE);
                layoutPermissionDenied.setVisibility(View.GONE);
            } else if (grantResults[0] ==PackageManager.PERMISSION_DENIED){
                permissionHelper.showSettingAlertDialog("Camera");
            }
        }
    }
}