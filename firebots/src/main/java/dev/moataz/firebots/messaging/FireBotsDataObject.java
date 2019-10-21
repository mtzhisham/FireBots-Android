package dev.moataz.firebots.messaging;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class FireBotsDataObject implements Parcelable {
    public static final Parcelable.Creator<FireBotsDataObject> CREATOR = new Parcelable.Creator<FireBotsDataObject>() {
        @Override
        public FireBotsDataObject createFromParcel(Parcel in) {
            return new FireBotsDataObject(in);
        }

        @Override
        public FireBotsDataObject[] newArray(int size) {
            return new FireBotsDataObject[size];
        }
    };
    private HashMap<String, String> data;

    FireBotsDataObject() {
        data = new HashMap<>();
    }


    private FireBotsDataObject(Parcel in) {
        data = new HashMap<>();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(data.size());
        for (String s : data.keySet()) {
            dest.writeString(s);
            dest.writeString(data.get(s));
        }
    }

    private void readFromParcel(Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            data.put(in.readString(), in.readString());
        }
    }

    public String get(String key) {
        return data.get(key);
    }

    public HashMap<String, String> getAll() {
        return data;
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    void putAll(Map<String, String> map) {
        for (String s : map.keySet()) {
            data.put(s, map.get(s));
        }
    }


}
