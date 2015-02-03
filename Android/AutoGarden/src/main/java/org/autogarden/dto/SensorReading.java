package org.autogarden.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class SensorReading implements Parcelable {
    private String _id;
    private String sensorId;
    private String takenAt;
    private BigDecimal temp;
    private Integer moisture;

    public SensorReading(String _id, String sensorId, String takenAt, BigDecimal temp, Integer moisture) {
        this._id = _id;
        this.sensorId = sensorId;
        this.takenAt = takenAt;
        this.temp = temp;
        this.moisture = moisture;
    }

    public String get_id() {
        return _id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getTakenAt() {
        return takenAt;
    }

    public BigDecimal getTemp() {
        return temp;
    }

    public Integer getMoisture() {
        return moisture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.sensorId);
        dest.writeString(this.takenAt);
        dest.writeSerializable(this.temp);
        dest.writeValue(this.moisture);
    }

    private SensorReading(Parcel in) {
        this._id = in.readString();
        this.sensorId = in.readString();
        this.takenAt = in.readString();
        this.temp = (BigDecimal) in.readSerializable();
        this.moisture = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<SensorReading> CREATOR = new Parcelable.Creator<SensorReading>() {
        public SensorReading createFromParcel(Parcel source) {
            return new SensorReading(source);
        }

        public SensorReading[] newArray(int size) {
            return new SensorReading[size];
        }
    };
}
