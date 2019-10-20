package dev.moataz.firebots.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FireBotsNotificationClickReceiver extends BroadcastReceiver {

    public static final String EXTRA_CLICK_ACTION_DESTINATION = "com.moataz.dev.firebots.action.CLICK_ACTION_DESTINATION";
    FireBotsNotificationClickListenerInterface myfireBotsNotificationListener;

    public FireBotsNotificationClickReceiver(FireBotsNotificationClickListenerInterface fireBotsNotificationListener) {
        myfireBotsNotificationListener = fireBotsNotificationListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (myfireBotsNotificationListener != null)
            myfireBotsNotificationListener.onNotificationClicked(intent.getStringExtra(EXTRA_CLICK_ACTION_DESTINATION));
    }
}
