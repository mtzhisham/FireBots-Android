package dev.moataz.firebots.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import dev.moataz.firebots.BuildConfig;
import dev.moataz.firebots.R;
import dev.moataz.firebots.util.FireBotsPreferenceManager;

import static dev.moataz.firebots.FireBots.TAG;

public class PushBotsAPIConsumer {
    public static synchronized void subscribe(Context context, String fireBaseToken){
//        if (isNetworkConnected(context)) {

            if (!FireBotsPreferenceManager.getInstance(context).getSubscribedToken().equals(FireBotsPreferenceManager.getInstance(context).getSubscribeRequestForToken())&&
                    !fireBaseToken.equals(FireBotsPreferenceManager.getInstance(context).getSubscribedToken())
            ){

//                RestTask.runTask(context,context.getString(R.string.postUrl), BuildConfig.PushBotsAPIKey
//                        , fireBaseToken, BuildConfig.PushBotsPlatformCode);




                Log.d(TAG,"subscribe: " + fireBaseToken );



            }


//        }
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }return false;
    }

}
