package com.siang.androidportfolio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.siang.androidportfolio.view.NewsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMToken";
    private Map<String, String> data;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        data = remoteMessage.getData();
        Log.i(TAG, "data: " + data);

        if (remoteMessage.getNotification() != null){
            Log.i(TAG,"title: " + remoteMessage.getNotification().getTitle());
            Log.i(TAG,"body: " + remoteMessage.getNotification().getBody());
            Log.i(TAG, "click_action: " + remoteMessage.getNotification().getClickAction());
            String clickAction = remoteMessage.getNotification().getClickAction();
            String headlineTitle = data.get("headline");
            Log.i(TAG, "headline title: " + headlineTitle);
            if (clickAction.equals("OPEN_ACTIVITY_NEWS")) {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), NewsActivity.class, headlineTitle);
            }

        }
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "token" + s);
    }

    private void sendNotification(String messageTitle,String messageBody, Class targetClass, String bigText) {
        Intent intent = new Intent(this, targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.notification_channel_id_default);
        String channelName = getString(R.string.notification_channel_name_default);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap newsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newspaper);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(messageTitle)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setLargeIcon(newsBitmap)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        // big text style
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));

        // big picture style
//        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(newsBitmap));

        // inbox style
//        notificationBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(inboxText));


        Intent homeIntent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent homePIntent = PendingIntent.getActivity(this, 0, homeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_launcher_foreground, getString(R.string.notification_action_home), homePIntent);
        notificationBuilder.addAction(R.drawable.ic_launcher_foreground, getString(R.string.notification_action_news), pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}