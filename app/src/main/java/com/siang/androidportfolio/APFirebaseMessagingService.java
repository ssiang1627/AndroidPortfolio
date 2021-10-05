package com.siang.androidportfolio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.siang.androidportfolio.view.NewsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class APFirebaseMessagingService extends FirebaseMessagingService implements LifecycleObserver {
    private static final String TAG = "FCMToken";
    private boolean isAppInForeground;
    private Map<String, String> data;

    public APFirebaseMessagingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onForegroundStart(){
        Log.i(TAG, "App moves to foreground");
        isAppInForeground = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onForegroundStop(){
        Log.i(TAG, "App moves to background");
        isAppInForeground = false;
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        data = remoteMessage.getData();
        Log.i(TAG, "data: " + data);
        NotificationDetail notificationDetail = new NotificationDetail();
        if (data != null){
            notificationDetail.setTitle(data.get("title"));
            notificationDetail.setBody(data.get("body"));
            notificationDetail.setClickAction(data.get("click_action"));
            notificationDetail.setExtraText(data.get("extra_text"));
            notificationDetail.setChannelId(data.get("android_channel_id"));
            notificationDetail.setLargeIconName(data.get("large_icon"));
            notificationDetail.setStyle(data.get("notification_style"));
            notificationDetail.setActionList(Arrays.asList(data.get("action_buttons").split("\\s*,\\s*")));

            switch (notificationDetail.getClickAction()){
                case "OPEN_ACTIVITY_NEWS":
                    sendNotification(notificationDetail, NewsActivity.class);
                    break;
                case "OPEN_DIALOG":
                    if (isAppInForeground){
                        Intent dialogIntent = new Intent(this, DialogActivity.class);
                        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }else{
                        SharedPreferences pref = getSharedPreferences("campaign", MODE_PRIVATE);
                        pref.edit()
                                .putBoolean("DISPLAY", false)
                                .putString("API", "")
                                .apply();
                        sendNotification(notificationDetail, DialogActivity.class);
                    }
            }
        }
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "token" + s);
    }

    private void sendNotification(NotificationDetail notificationDetail, Class targetClass) {
        Intent targetIntent = new Intent(this, targetClass);
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent targetPIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int largeIconId = this.getResources().getIdentifier(notificationDetail.getLargeIconName(), "drawable", this.getPackageName());
        Bitmap iconBitmap = BitmapFactory.decodeResource(getResources(), largeIconId);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, notificationDetail.getChannelId())
                        .setContentTitle(notificationDetail.getTitle())
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(iconBitmap)
                        .setContentText(notificationDetail.getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(targetPIntent);

        switch (notificationDetail.getStyle()){
            case "BIG_TEXT":
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDetail.getExtraText()));
                break;

            case "BIG_PICTURE":
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(iconBitmap));
                break;

            case "INBOX":
                notificationBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(notificationDetail.getExtraText()));
                break;
        }

        for (String action: notificationDetail.getActionList()){
            switch (action) {
                case "HOME":
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    PendingIntent homePIntent = PendingIntent.getActivity(this, 0, homeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.ic_launcher_foreground, getString(R.string.notification_action_home), homePIntent);
                    break;
                case "NEWS":
                    Intent newsIntent = new Intent(this, NewsActivity.class);
                    PendingIntent newsPIntent = PendingIntent.getActivity(this, 0, newsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.addAction(R.drawable.ic_launcher_foreground, getString(R.string.notification_action_news), newsPIntent);
                    break;
            }
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}