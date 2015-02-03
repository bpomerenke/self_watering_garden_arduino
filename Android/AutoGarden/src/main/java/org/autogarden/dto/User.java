package org.autogarden.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private String _id;
    private String username;
    private List<String> sensors = new ArrayList<>();

    public User(String username, List<String> sensors) {
        this.username = username;
        this.sensors = sensors;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getSensors() {
        return sensors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.username);
        dest.writeList(this.sensors);
    }

    private User(Parcel in) {
        this._id = in.readString();
        this.username = in.readString();
        in.readList(this.sensors, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
