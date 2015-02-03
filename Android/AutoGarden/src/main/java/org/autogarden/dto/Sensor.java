package org.autogarden.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Sensor implements Parcelable {
    private String _id;
    private String name;
    private WateringSchedule wateringSchedule;

    public Sensor() {
    }

    public Sensor(String _id, String name, WateringSchedule wateringSchedule) {
        this._id = _id;
        this.name = name;
        this.wateringSchedule = wateringSchedule;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public WateringSchedule getWateringSchedule() {
        return wateringSchedule;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.name);
        dest.writeParcelable(this.wateringSchedule, flags);
    }

    private Sensor(Parcel in) {
        this._id = in.readString();
        this.name = in.readString();
        this.wateringSchedule = in.readParcelable(WateringSchedule.class.getClassLoader());
    }

    public static final Parcelable.Creator<Sensor> CREATOR = new Parcelable.Creator<Sensor>() {
        public Sensor createFromParcel(Parcel source) {
            return new Sensor(source);
        }

        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };
}
