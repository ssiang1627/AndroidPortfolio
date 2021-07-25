package com.siang.androidportfolio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class PermissionHelper{
    private Context context;
    public static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "PermissionHelper";
    private int permissionDeniedTimes = 0;

    public PermissionHelper(Context context)
    {
        this.context = context;
    }

    public void requestPermission(String permission) {
        Log.d(TAG, "requestPermission: "+ permission);
        Activity activity = (Activity) context;
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, getPermissionCode(permission));
        }
    }

    public boolean isPermissionGranted(String permission){
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED ? true : false);
    }

    public void showSettingAlertDialog(String permissionTitle){
        new AlertDialog.Builder(context)
                .setTitle("Permission Denied")
                .setMessage("Please click \"Setting\" button to grant permission for "+ permissionTitle)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSettingPage();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "cancel pressed");
                    }
                })
                .show();
    }

    public void showSettingPage(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    public int getPermissionCode(String permission){
        switch (permission){
            case Manifest.permission.CAMERA:
                return CAMERA_REQUEST_CODE;
        }
        return -1;
    }
}

