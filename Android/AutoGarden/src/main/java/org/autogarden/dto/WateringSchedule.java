package org.autogarden.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class WateringSchedule implements Parcelable {
    private String _id;
    private Integer startWhenBelowMoisture;
    private Integer stopWhenAboveMoisture;

    public WateringSchedule(String _id, Integer startWhenBelowMoisture, Integer stopWhenAboveMoisture) {
        this._id = _id;
        this.startWhenBelowMoisture = startWhenBelowMoisture;
        this.stopWhenAboveMoisture = stopWhenAboveMoisture;
    }

    public String get_id() {
        return _id;
    }

    public Integer getStartWhenBelowMoisture() {
        return startWhenBelowMoisture;
    }

    public Integer getStopWhenAboveMoisture() {
        return stopWhenAboveMoisture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeValue(this.startWhenBelowMoisture);
        dest.writeValue(this.stopWhenAboveMoisture);
    }

    private WateringSchedule(Parcel in) {
        this._id = in.readString();
        this.startWhenBelowMoisture = (Integer) in.readValue(Integer.class.getClassLoader());
        this.stopWhenAboveMoisture = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<WateringSchedule> CREATOR = new Parcelable.Creator<WateringSchedule>() {
        public WateringSchedule createFromParcel(Parcel source) {
            return new WateringSchedule(source);
        }

        public WateringSchedule[] newArray(int size) {
            return new WateringSchedule[size];
        }
    };
}
