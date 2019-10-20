package dev.moataz.firebots.messaging;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import dev.moataz.firebots.networking.SubscribeToFireBots;
import dev.moataz.firebots.util.FireBotsPreferenceManager;

import static dev.moataz.firebots.FireBots.BROADCAST_MESSAGE_RECIVED_ACTION;
import static dev.moataz.firebots.messaging.FireBotsMessageBroadCastReceiver.EXTRA_FIREPUSH_DATA_OPJECT;
import static dev.moataz.firebots.notification.FireBotsNotificationManager.createNotification;

public class FireBotsFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        if (data != null) {

            FireBotsDataObject map = new FireBotsDataObject();
            map.putAll(data);
            sendMyBroadCast(map);

            createNotification(data.get("body"), getApplicationContext(), data.get("click_action"));
        }

    }


    private void sendMyBroadCast(FireBotsDataObject fireBotsDataObject) {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BROADCAST_MESSAGE_RECIVED_ACTION);
            broadCastIntent.putExtra(EXTRA_FIREPUSH_DATA_OPJECT, fireBotsDataObject);
            sendBroadcast(broadCastIntent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onNewToken(@NonNull String s) {
        if(!FireBotsPreferenceManager.getInstance(getApplicationContext()).getSubscribedToken().equals(s)
                && !FireBotsPreferenceManager.getInstance(getApplicationContext()).getSubscribeRequestForToken().equals(s)){
            FireBotsPreferenceManager.getInstance(getApplicationContext()).setSubscribeRequestForToken(s);
            SubscribeToFireBots.subscribe(getApplicationContext(),s);
        }

    }

}
