package dev.moataz.firebots.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.moataz.firebots.util.FireBotsPreferenceManager;

import static dev.moataz.firebots.FireBots.TAG;

public class RestTask extends AsyncTask<String, Void, Boolean> {

    private WeakReference<Context> contextWeakReference;
    static void runTask(Context context, String url, String pushBotsAppID, String fireBaseToken, String platform) {
        if (!TextUtils.isEmpty(url)&&!TextUtils.isEmpty(pushBotsAppID)&&!TextUtils.isEmpty(fireBaseToken)&&!TextUtils.isEmpty(platform)) {
            RestTask task = new RestTask(context);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            task.executeOnExecutor(executorService, url, pushBotsAppID, fireBaseToken, platform);

        }
    }

    private RestTask(Context context) {
        this.contextWeakReference = new WeakReference<>(context) ;
    }

    private String paramFireBaseToken;

    @Override
    protected Boolean doInBackground(String... strings) {



        String urlString =  strings[0];
        String headerPushbotsAppID =  strings[1];
        paramFireBaseToken =  strings[2];
        String paramPlatform =  strings[3];

        URL url = null;
        HttpURLConnection connection = null;
        int responseCode =0;
        String responseMessage ="";
        RetryStrategy retryStrategy = new RetryStrategy();
        while (retryStrategy.shouldRetry()) {
            try {
                url = new URL(urlString);

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("x-pushbots-appid", headerPushbotsAppID);
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);
                connection.setReadTimeout(9000);
                connection.setConnectTimeout(10000);



                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("platform", Integer.parseInt(paramPlatform));
                jsonParam.put("token", paramFireBaseToken);
                wr.writeBytes(jsonParam.toString());
                wr.flush();
                wr.close();

                Log.d(TAG, connection.getResponseCode() + " : " + connection.getResponseMessage());
                responseCode = connection.getResponseCode();

                break;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                retryStrategy.errorOccured();
            } catch (ProtocolException e) {
                e.printStackTrace();
                retryStrategy.errorOccured();
            } catch (IOException e) {
                e.printStackTrace();
                retryStrategy.errorOccured();
            } catch (JSONException e) {
                e.printStackTrace();
                retryStrategy.errorOccured();
            } catch (Exception e) {
                e.printStackTrace();
                retryStrategy.errorOccured();
            }
            finally {
                if (connection != null)
                    connection.disconnect();
            }
        }

        return responseCode >= 200 && responseCode < 300;
    }


    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if (isSuccess){
            FireBotsPreferenceManager.getInstance(contextWeakReference.get()).setSubscribedToken(paramFireBaseToken);
            FireBotsPreferenceManager.getInstance(contextWeakReference.get()).setSubscribeRequestForToken("");
            Log.d(TAG,"subscribe: 3" );


        }

    }



}