package com.example.voyage.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.voyage.R;
import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.data.Constants;
import com.example.voyage.ui.bookings.RecentBookingActivity;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class VoyageMessagingService extends FirebaseMessagingService {

    private static final String LOG_TAG = VoyageMessagingService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.i(LOG_TAG, "Token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        PreferenceUtilities.saveFcmToken(ApplicationContextProvider.getContext(), token);
        VoyageAuth.getInstance().sendFcmRegistrationToServer();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage
                    .getNotification().getBody());

            createNotification(remoteMessage);
        }

    }

    private void createNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        int notificationId = 0;
        assert notification != null;
        String title = notification.getTitle();
        String content = notification.getBody();
        PendingIntent pendingIntent = getPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                Constants.NEW_BOOKING_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

    }

    private PendingIntent getPendingIntent() {
        // Create an explicit intent for RecentBookingActivity
        Intent intent = new Intent(this, RecentBookingActivity.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        // Get the PendingIntent containing the entire back stack
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = ApplicationContextProvider.getContext()
                    .getString(R.string.voyage_channel_name);

            String description = ApplicationContextProvider.getContext()
                    .getString(R.string.voyage_channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    Constants.NEW_BOOKING_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ApplicationContextProvider.getContext()
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public static String getToken(Context context) {
        return PreferenceUtilities.getPrefFcmToken(context);
    }
}
