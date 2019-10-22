package dev.moataz.firebots.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.atomic.AtomicInteger;

import dev.moataz.firebots.R;

public class FireBotsNotificationManager {
    private static NotificationManager notificationManager;

     public static void createNotification(String aMessage, Context context, String dest) {
        AtomicInteger atomicInteger = new AtomicInteger();

        final int NOTIFY_ID = atomicInteger.incrementAndGet();
        String id = context.getString(R.string.default_notification_channel_id);
        String title = context.getString(R.string.default_notification_channel_title);
        PendingIntent pendingIntent;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        pendingIntent = FireBotsNotificationClickListenerService.getPendingIntent(context, aMessage, dest, NOTIFY_ID);

        NotificationCompat.Builder builder;
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{0, 400, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(context, id);

                builder.setContentTitle(aMessage)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentText(context.getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setVibrate(new long[]{0, 400, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(context, id);

                builder.setContentTitle(aMessage)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentText(context.getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker(aMessage)
                        .setVibrate(new long[]{0, 400, 200, 400})
                ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    builder.setPriority(Notification.PRIORITY_HIGH);
                }
            }
            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);
        }

    }

}
