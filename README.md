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

    implementation 'dev.moataz:firebots:0.0.4'
    ...

    }
  	```


3. recommended to update the following lines in `build.gradle` app module app module:
  	```gradle
  	buildToolsVersion = "29.0.2"
  	compileSdkVersion = 29
	targetSdkVersion = 29
  	```



4. open `android/gradle.properties` and add;
  	```gradle
  	PushBotsAPIKey = "APP_API_KEY"
	PushBotsPlatformCode = "APP_Platform"

  	```


5.	This Library uses AndroidX  adding this lines in `android/gradle.properties` is recommended:
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
        FireBots.init(this);
    }
}
```
`MainActivity.class`

```java
public class MainActivity extends AppCompatActivity implements FireBotsMessagingInterface, FireBotsNotificationClickListenerInterface {

    public final static String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBots.getInstance().setMessagingInterface(this);
        FireBots.getInstance().setNotificationClickListener(this);
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
```
