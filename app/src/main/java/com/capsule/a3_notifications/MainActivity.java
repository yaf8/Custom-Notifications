package com.capsule.a3_notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID_1 = 1;
    private static final int NOTIFICATION_ID_2 = 2;
    private static final int NOTIFICATION_ID_3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the notification channel
        createNotificationChannel();

        // Build first notification
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("First Notification")
                .setContentText("This is the first notification");

// Build second notification
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Second Notification")
                .setContentText("This is the second notification");

// Set the color of the notification
        int color = ContextCompat.getColor(this, R.color.notification_color);
        builder2.setColor(color);

// Set the image with color filter
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.capsule_logo_w);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        builder2.setLargeIcon(bitmap);

// Build third notification
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_custom);

// Set the text and image of the notification
        contentView.setTextViewText(R.id.notification_title, "Notification Title");
        contentView.setImageViewResource(R.id.notification_icon, R.drawable.capsule_logo_w);

// Set click listeners for the buttons

        Context context = getApplicationContext();
        Intent acceptIntent = new Intent(context, MyBroadcastReceiver.class);
        acceptIntent.setAction("ACTION_ACCEPT");
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(context, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.notification_accept_button, acceptPendingIntent);
        //contentView.setOnClickPendingIntent(R.id.notification_reject_button, rejectIntent);

// Create the notification builder
        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.capsule_logo)
                .setCustomContentView(contentView)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

// Issue the notification




             createCustomNotification();





// Create the notification manager and send the notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder1.build());
        notificationManager.notify(2, builder2.build());
        notificationManager.notify(3, builder3.build());

    }

    public void createCustomNotification() {
        // Create the Intent for the notification
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);

// Create the PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

// Create the full screen PendingIntent
        PendingIntent fullScreenIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrationPattern = {0, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500, 100, 500};
// Create the NotificationCompat builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.capsule_logo)
                .setContentTitle("New Notification")
                .setContentText("This is a new notification.")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(contentIntent)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true)
                .setVibrate(vibrationPattern)
                .setFullScreenIntent(fullScreenIntent, true);

// Create the notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// Show the notification
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }




        builder.setFullScreenIntent(fullScreenIntent, true);
        notificationManager.notify(4, builder.build());

    }




    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "This is my notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
