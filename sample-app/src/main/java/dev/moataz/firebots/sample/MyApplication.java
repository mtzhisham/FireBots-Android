package dev.moataz.firebots.sample;

import android.app.Application;

import dev.moataz.firebots.FireBots;

public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the library
        FireBots.init(this);
    }
}
