package dev.moataz.firebots.util;

import android.content.Context;
import android.content.SharedPreferences;

public class FireBotsPreferenceManager {
    private static final String PREF_NAME = "com.moataz.dev.firebots.FIREBOTS_PREF";
    private static final String KEY_SubscribedToken = "ccom.moataz.dev.firebots.SubscribedToken";
    private static final String KEY_SubscribedSucess = "ccom.moataz.dev.firebots.SubscribedSucess";
    private static final String KEY_SubscribeRequest = "ccom.moataz.dev.firebots.SubscribeRequest";

    private static FireBotsPreferenceManager fireBotsPreferenceManager;
    private SharedPreferences pref;


    private FireBotsPreferenceManager(Context context) {
        if (pref == null) pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FireBotsPreferenceManager getInstance(Context context) {

        if (fireBotsPreferenceManager == null)
            fireBotsPreferenceManager = new FireBotsPreferenceManager(context);

        return fireBotsPreferenceManager;
    }

    public String getSubscribedToken() {
        return pref.getString(KEY_SubscribedToken, "");
    }

    public void setSubscribedToken(String value) {
        pref.edit()
                .putString(KEY_SubscribedToken, value)
                .apply();
    }

    public String getSubscribeRequestForToken() {
        return pref.getString(KEY_SubscribeRequest, "");
    }

    public void setSubscribeRequestForToken(String value) {
        pref.edit()
                .putString(KEY_SubscribeRequest, value)
                .apply();
    }


}
