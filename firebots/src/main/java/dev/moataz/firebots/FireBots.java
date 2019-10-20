package dev.moataz.firebots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import dev.moataz.firebots.messaging.FireBotsDataObject;
import dev.moataz.firebots.messaging.FireBotsMessageBroadCastReceiver;
import dev.moataz.firebots.messaging.FireBotsMessagingInterface;
import dev.moataz.firebots.networking.FirebaseTokenHelper;
import dev.moataz.firebots.networking.SubscribeToFireBots;
import dev.moataz.firebots.notification.FireBotsNotificationClickListenerInterface;
import dev.moataz.firebots.notification.FireBotsNotificationClickReceiver;
import dev.moataz.firebots.util.FireBotsPreferenceManager;

public class FireBots {
    public final static String TAG = "FireBots";
    public static final String BROADCAST_MESSAGE_RECIVED_ACTION = "com.moataz.dev.firebots.action.BROADCAST_MESSAGE_RECIVED_ACTION ";
    public static final String BROADCAST_NOTIFICATION_CLICKED_ACTION = "com.moataz.dev.firebots.action.BROADCAST_NOTIFICATION_CLICKED_ACTION";
    private static FireBots fireBots;
    FireBotsMessagingInterface fireBotsMessagingInterface;
    FireBotsNotificationClickListenerInterface fireBotsNotificationListener;
    FireBotsMessageBroadCastReceiver fireBotsMessageBroadCastReceiver;
    FireBotsNotificationClickReceiver fireBotsNotificationClickReciver;
    private Context context;


    private FireBots(final Context context) {
        this.context = context;

        new FirebaseTokenHelper(new FirebaseTokenHelper.TokenAvilableListiner() {
            @Override
            public void onTokenAvilable(String token) {
                if (!FireBotsPreferenceManager.getInstance(context).getSubscribedToken().equals(token)){
                    FireBotsPreferenceManager.getInstance(context).setSubscribeRequestForToken(token);
                    SubscribeToFireBots.subscribe(context,token);
                }
            }
        });

        fireBotsMessageBroadCastReceiver = new FireBotsMessageBroadCastReceiver(new FireBotsMessagingInterface() {
            @Override
            public void onMessageReceived(FireBotsDataObject message) {
                if (FireBots.this.fireBotsMessagingInterface != null)
                    FireBots.this.fireBotsMessagingInterface.onMessageReceived(message);
                else {
                    for (String name : message.getAll().keySet()) {
                        Log.d(TAG, name + " : " + message.getAll().get(name));
                    }
                }
            }
        });
        fireBotsNotificationClickReciver = new FireBotsNotificationClickReceiver(new FireBotsNotificationClickListenerInterface() {
            @Override
            public void onNotificationClicked(String clickActionDestination) {
                if (FireBots.this.fireBotsNotificationListener != null)
                    FireBots.this.fireBotsNotificationListener.onNotificationClicked(clickActionDestination);
                else {
                    Log.d(TAG, clickActionDestination);
                    startActiviry(context, clickActionDestination);
                }
            }
        });

        registerMyReceiver();
    }

    public static synchronized FireBots init(Context context) {
        if (fireBots == null) fireBots = new FireBots(context);
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

    private void registerMyReceiver() {

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_MESSAGE_RECIVED_ACTION);
            context.registerReceiver(fireBotsMessageBroadCastReceiver, intentFilter);

            intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_NOTIFICATION_CLICKED_ACTION);
            context.registerReceiver(fireBotsNotificationClickReciver, intentFilter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setMessagingInterface(FireBotsMessagingInterface fireBotsMessagingInterface) {
        FireBots.this.fireBotsMessagingInterface = fireBotsMessagingInterface;
    }

    public void setNotificationClickListener(Activity activity) {
        FireBots.this.fireBotsNotificationListener = (FireBotsNotificationClickListenerInterface) activity;

    }




    public static void testRest() {
        //Simple POST

        try {




        }catch (Exception e) {
            e.printStackTrace();
        }
    }



}
