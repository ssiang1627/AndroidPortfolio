package com.siang.androidportfolio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback {
    private Context context;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "PermissionHelper";
    private int permissionDeniedTimes = 0;

    public PermissionHelper(Context context)
    {
        this.context = context;
    }

    public void requestPermission() {
        Log.d(TAG, "requestPermission");
        Activity activity = (Activity) context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        Log.d(TAG, "onRequestPermission");
        if (requestCode == CAMERA_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                permissionDeniedTimes +=1;
                if (permissionDeniedTimes <=1){
                    Toast.makeText(context,"You must accept this permission", Toast.LENGTH_SHORT).show();
                    requestPermission();
                }else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(intent);
                }
                Log.d("TAG","helper times:" + permissionDeniedTimes);

            }
        }
    }
}

