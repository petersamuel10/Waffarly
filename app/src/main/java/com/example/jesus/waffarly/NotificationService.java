package com.example.jesus.waffarly;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by jesus on 5/24/2018.
 */

public class NotificationService extends FirebaseMessagingService {

    private String title,body,NTitle,NBody;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size()>0) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("message");
        }
        if(remoteMessage.getNotification()!=null)
        {
            NTitle = remoteMessage.getNotification().getTitle();
            NBody = remoteMessage.getNotification().getBody();
        }
        sendNotification(NTitle,NBody);
    }

    private void sendNotification(String NTitle, String NBody) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NTitle)
                .setContentText(NBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
               // .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
