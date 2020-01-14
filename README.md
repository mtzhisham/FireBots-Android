# FireBots-Android
FireBots is a wrapper library for Firebase for Android.

BushBots Technical Assignment

## Installation

1. In `build.gradle` in project module add either `jcenter()` or `mavenCentral()`
  	```gradle
  	allprojects {
		repositories {
			...
			jcenter() //or mavenCentral()
			....
		}
	}
  	```

2. In `build.gradle` app module app module:
  	```gradle
  	dependencies {
    ...

    implementation 'dev.moataz:firebots:0.0.7'
    ...

    }
  	```


3. recommended to update the following lines in `build.gradle` app module app module:
  	```gradle
  	buildToolsVersion = "29.0.2"
  	compileSdkVersion = 29
	targetSdkVersion = 29
  	```





4.	This Library uses AndroidX  adding this lines in `gradle.properties` is recommended:
  	```gradle
      android.useAndroidX=true
	  android.enableJetifier=true
  	```

## Usage

`MyApplication.java`

```java
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
```
`MainActivity.class`

```java
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
        //send notification manually through library helper method, library won't handel displaying notification if app in
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
```


### Notification:

body could be:

```json
{
  "to": "some_id",
 "data" : {
 "title" : "FireBots",
 "body" : "Awesome",
 "click_action":"com.example.MainActivity" 
 }
}
```
`"click_action":"com.example.MainActivity" //activity to be opened if valid`
