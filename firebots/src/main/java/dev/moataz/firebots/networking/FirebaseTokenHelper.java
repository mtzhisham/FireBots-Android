package dev.moataz.firebots.networking;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static dev.moataz.firebots.FireBots.TAG;

public class FirebaseTokenHelper {

    private TokenReceivedListiner tokenReceivedListiner;

    public FirebaseTokenHelper(TokenReceivedListiner tokenReceivedListiner) {
        this.tokenReceivedListiner = tokenReceivedListiner;
        requestToken();
    }

    private void requestToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        try {
                            tokenReceivedListiner.onTokenReceived(task.getResult().getToken());

                        } catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                });

    }


}
