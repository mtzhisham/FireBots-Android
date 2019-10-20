package dev.moataz.firebots.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FireBotsMessageBroadCastReceiver extends BroadcastReceiver {

    public static final String EXTRA_FIREPUSH_DATA_OPJECT = "com.moataz.dev.firebots.EXTRA.FIREBOTS_DATA_OPJECT";
    FireBotsMessagingInterface myFireBotsMessagingInterface;

    public FireBotsMessageBroadCastReceiver(FireBotsMessagingInterface firebotsMessagingInterface) {
        myFireBotsMessagingInterface = firebotsMessagingInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            if (myFireBotsMessagingInterface != null) {
                myFireBotsMessagingInterface.onMessageReceived((FireBotsDataObject) intent.getParcelableExtra(EXTRA_FIREPUSH_DATA_OPJECT));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
