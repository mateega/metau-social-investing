package com.example.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title =  message.getNotification().getTitle();
        String body =  message.getNotification().getBody();
        String userId = message.getData().get("user");

        final String CHANNEL_ID = "NOTIFICATIONS";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MyNotification", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (!userId.equals(currentUserId)) {
            // create the notification object
            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true);
            // send the notification through the channel to be displayed
            NotificationManagerCompat.from(this).notify(1, notification.build());
        }
    }
}