package dev.moataz.firebots.notification;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static dev.moataz.firebots.FireBots.BROADCAST_NOTIFICATION_CLICKED_ACTION;
import static dev.moataz.firebots.notification.FireBotsNotificationClickReceiver.EXTRA_CLICK_ACTION_DESTINATION;

public class FireBotsNotificationClickListenerService extends IntentService {

    private static final String ACTION_CLICK = "com.moataz.dev.firepush.action.CLICK";


    private static final String EXTRA_MESSAGE = "com.moataz.dev.firepush.extra.MESSAGE";
    private static final String EXTRA_CLICK_ACTION = "com.moataz.dev.firepush.extra.CLICK_ACTION";

    public FireBotsNotificationClickListenerService() {
        super("FirePushNotificationClickListenerService");
    }

    public static PendingIntent getPendingIntent(Context context, String message, String clickAction, int code) {
        Intent intent;
        intent = new Intent(context, FireBotsNotificationClickListenerService.class);
        intent.setAction(ACTION_CLICK);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_CLICK_ACTION, clickAction);
        return PendingIntent.getService(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CLICK.equals(action)) {
                final String message = intent.getStringExtra(EXTRA_MESSAGE);
                final String clickAction = intent.getStringExtra(EXTRA_CLICK_ACTION);

                sendMyBroadCast(clickAction);

            }
        }
    }

    private void sendMyBroadCast(String destination) {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BROADCAST_NOTIFICATION_CLICKED_ACTION);
            broadCastIntent.putExtra(EXTRA_CLICK_ACTION_DESTINATION, destination);
            sendBroadcast(broadCastIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
