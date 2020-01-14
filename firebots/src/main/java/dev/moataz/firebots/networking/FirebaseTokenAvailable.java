package dev.moataz.firebots.networking;

public interface FirebaseTokenAvailable {
    public void onFirebaseTokenAvilable(String token);
    public void onFirebaseTokenUpdated(String token);
}
