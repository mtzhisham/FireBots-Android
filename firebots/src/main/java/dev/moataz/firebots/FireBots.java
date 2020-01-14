package dev.moataz.firebots;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.ref.WeakReference;

import dev.moataz.firebots.messaging.FireBotsDataObject;
import dev.moataz.firebots.messaging.FireBotsFirebaseMessagingService;
import dev.moataz.firebots.messaging.FireBotsMessageBroadCastReceiver;
import dev.moataz.firebots.messaging.FireBotsMessagingInterface;
import dev.moataz.firebots.networking.FirebaseTokenAvailable;
import dev.moataz.firebots.networking.FirebaseTokenHelper;
import dev.moataz.firebots.networking.TokenReceivedListiner;
import dev.moataz.firebots.notification.FireBotsNotificationClickListenerInterface;
import dev.moataz.firebots.notification.FireBotsNotificationClickReceiver;
import dev.moataz.firebots.util.FireBotsPreferenceManager;

import static dev.moataz.firebots.notification.FireBotsNotificationManager.createNotification;

public class FireBots {
    public final static String TAG = "FireBots";
    public static final String BROADCAST_MESSAGE_RECIVED_ACTION = "com.moataz.dev.firebots.action.BROADCAST_MESSAGE_RECIVED_ACTION ";
    public static final String BROADCAST_NOTIFICATION_CLICKED_ACTION = "com.moataz.dev.firebots.action.BROADCAST_NOTIFICATION_CLICKED_ACTION";
    private static FireBots fireBots;
    private FireBotsMessagingInterface fireBotsMessagingInterface;
    private FireBotsNotificationClickListenerInterface fireBotsNotificationListener;
    private FireBotsMessageBroadCastReceiver fireBotsMessageBroadCastReceiver;
    private FireBotsNotificationClickReceiver fireBotsNotificationClickReciver;
    private WeakReference<Context> contextWeakReference;
    private boolean canHandleNotifications = false;


    private FireBots(final Context context, final FirebaseTokenAvailable firebaseTokenAvailable) {
        this.contextWeakReference = new WeakReference<>(context);

        new FirebaseTokenHelper(new TokenReceivedListiner() {
            @Override
            public void onTokenReceived(String token) {
                if (!FireBotsPreferenceManager.getInstance(context).getSubscribedToken().equals(token)){
                    FireBotsPreferenceManager.getInstance(context).setSubscribeRequestForToken(token);
                    firebaseTokenAvailable.onFirebaseTokenAvilable(token);
                }
            }
        });

        //mimics the default Firebase behaviour, if app is in background/killed a notification will be displayed
        //if tha app in foreground/there is an active instance running the user is responsible for showing it
        fireBotsMessageBroadCastReceiver = new FireBotsMessageBroadCastReceiver(new FireBotsMessagingInterface() {
            @Override
            public void onMessageReceived(FireBotsDataObject message) {
                if (fireBots.fireBotsMessagingInterface != null) {
                    fireBots.fireBotsMessagingInterface.onMessageReceived(message);
                }
                if (canHandleNotifications)
                createNotification(message.get("body"), contextWeakReference.get(), message.get("click_action"));
            }
        });
        fireBotsNotificationClickReciver = new FireBotsNotificationClickReceiver(new FireBotsNotificationClickListenerInterface() {
            @Override
            public void onNotificationClicked(String clickActionDestination) {
                if (fireBots.fireBotsNotificationListener != null)
                    fireBots.fireBotsNotificationListener.onNotificationClicked(clickActionDestination);
                else {
                    Log.d(TAG, clickActionDestination);
                    startActiviry(context, clickActionDestination);
                }
            }
        });

        FireBotsFirebaseMessagingService.setTokenAvailableListener(firebaseTokenAvailable);
        registeReceivers();
    }


    public void setCanHandleNotifications(boolean canHandleNotifications) {
        this.canHandleNotifications = canHandleNotifications;
    }

    public static synchronized FireBots init(Context context, FirebaseTokenAvailable firebaseTokenAvailable ) {
        if (fireBots == null) fireBots = new FireBots(context, firebaseTokenAvailable);
        return fireBots;
    }

    public static synchronized FireBots getInstance() {
        return fireBots;
    }

    private void startActiviry(Context context, String activityName) {
        try {
            Intent notificationIntent = new Intent(context, Class.forName(activityName));
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notificationIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registeReceivers() {

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_MESSAGE_RECIVED_ACTION);
            contextWeakReference.get().registerReceiver(fireBotsMessageBroadCastReceiver, intentFilter);

            intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_NOTIFICATION_CLICKED_ACTION);
            contextWeakReference.get().registerReceiver(fireBotsNotificationClickReciver, intentFilter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setMessagingInterface(FireBotsMessagingInterface fireBotsMessagingInterface) {
        if (fireBots != null)
        fireBots.fireBotsMessagingInterface = fireBotsMessagingInterface;
        else Log.e(TAG, "FireBots is not initialized did you forget to call init()");
    }

    public void setNotificationClickListener(FireBotsNotificationClickListenerInterface fireBotsNotificationListener) {
        if (fireBots != null)
        fireBots.fireBotsNotificationListener = fireBotsNotificationListener;
        else Log.e(TAG, "FireBots is not initialized did you forget to call init()");

    }


}
