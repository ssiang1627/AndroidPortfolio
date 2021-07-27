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

public final class PermissionHelper{
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;

    private static final String TAG = "PermissionHelper";
    private int permissionDeniedTimes = 0;

    private PermissionHelper() { }

    public static void requestPermission(Activity activity, String permission) {
        Log.d(TAG, "requestPermission: "+ permission);
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, getPermissionCode(permission));
        }
    }

    public static boolean isPermissionGranted(Context context, String permission){
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED ? true : false);
    }

    public static void showSettingAlertDialog(Context context, String permissionTitle){
        new AlertDialog.Builder(context)
                .setTitle("Permission Denied")
                .setMessage("Please click \"Setting\" button to grant permission for "+ permissionTitle)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSettingPage(context);
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

    public static void showSettingPage(Context context){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    public static int getPermissionCode(String permission){
        switch (permission){
            case Manifest.permission.CAMERA:
                return CAMERA_REQUEST_CODE;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                return READ_EXTERNAL_STORAGE_REQUEST_CODE;
        }
        return -1;
    }
}

