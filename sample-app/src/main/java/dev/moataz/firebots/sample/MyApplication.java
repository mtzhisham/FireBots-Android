package dev.moataz.firebots.sample;

import android.app.Application;
import android.util.Log;

import dev.moataz.firebots.FireBots;
import dev.moataz.firebots.networking.FirebaseTokenAvailable;

public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the library
        FireBots.init(this, new FirebaseTokenAvailable() {
            @Override
            public void onFirebaseTokenAvilable(String token) {
                Log.d("MyApplication", "onFirebaseTokenAvilable: " + token);

            }
            @Override
            public void onFirebaseTokenUpdated(String token) {
                Log.d("MyApplication", "onFirebaseTokenUpdated: " + token);

            }
        }).setCanHandleNotifications(true);
    }
}
