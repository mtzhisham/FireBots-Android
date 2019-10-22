package dev.moataz.firebots.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Map;

import dev.moataz.firebots.FireBots;
import dev.moataz.firebots.messaging.FireBotsDataObject;
import dev.moataz.firebots.messaging.FireBotsMessagingInterface;
import dev.moataz.firebots.notification.FireBotsNotificationClickListenerInterface;
import dev.moataz.firebots.notification.FireBotsNotificationManager;

public class MainActivity extends AppCompatActivity implements FireBotsMessagingInterface
        , FireBotsNotificationClickListenerInterface {

    public final static String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //register message listener
        FireBots.getInstance().setMessagingInterface(this);
        //register notification click listener
        FireBots.getInstance().setNotificationClickListener(this);

    }

    /**
     * Called when message received
     * @param message a (key,value) map object containing notification message data
     */
    @Override
    public void onMessageReceived(FireBotsDataObject message) {
        Map<String, String> data = message.getAll();
        for (String name : data.keySet()){
            Log.d(TAG, name + " : " + data.get(name));
        }
        //send notification library won't handel displaying notification if app in
        // foreground/not killed
        FireBotsNotificationManager.createNotification(message.get("body"),
                MainActivity.this, message.get("click_action"));
    }

    /**
     *
     * @param clickActionDestination if the notification body contains a "click_action"  you can
     *  catch it here and app is not killed/background
     *  if the value a of "click_action" valid full class name like
     *  "dev.moataz.firebots.sample.MainActivity"  this Activity will be started
     */
    @Override
    public void onNotificationClicked(String clickActionDestination) {
        Log.d(TAG,"ACTION: " +  clickActionDestination);
    }

}
