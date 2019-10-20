package dev.moataz.firebots.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Map;

import dev.moataz.firebots.FireBots;
import dev.moataz.firebots.messaging.FireBotsDataObject;
import dev.moataz.firebots.messaging.FireBotsMessagingInterface;
import dev.moataz.firebots.notification.FireBotsNotificationClickListenerInterface;

public class MainActivity extends AppCompatActivity implements FireBotsMessagingInterface, FireBotsNotificationClickListenerInterface {

    public final static String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBots.getInstance().setMessagingInterface(this);
        FireBots.getInstance().setNotificationClickListener(this);
        FireBots.testRest();
    }

    @Override
    public void onMessageReceived(FireBotsDataObject message) {
        Map<String, String> data = message.getAll();
        for (String name : data.keySet()){
            Log.d(TAG, name + " : " + data.get(name));
        }

    }


    @Override
    public void onNotificationClicked(String clickActionDestination) {
        Log.d(TAG,"ACTION: " +  clickActionDestination);
    }

}
