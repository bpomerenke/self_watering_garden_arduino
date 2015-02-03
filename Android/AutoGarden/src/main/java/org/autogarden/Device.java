package org.autogarden;

import android.os.Parcel;
import android.os.Parcelable;

import org.autogarden.dto.Sensor;
import org.autogarden.dto.SensorReading;

import java.util.Date;

public class Device implements Parcelable {
    private Sensor sensor;
    private SensorReading latestReading;
    private String location;
    private Date lastWatering;
    private Date lastDataUpdate;

    public Device(Sensor sensor, SensorReading latestReading, String location, Date lastWatering, Date lastDataUpdate) {
        this.sensor = sensor;
        this.latestReading = latestReading;
        this.location = location;
        this.lastWatering = lastWatering;
        this.lastDataUpdate = lastDataUpdate;
    }

    public String getName() {
        return sensor.getName();
    }

    public String getLocation() {
        return location;
    }

    public Date getLastWatering() {
        return lastWatering;
    }

    public Date getLastDataUpdate() {
        return lastDataUpdate;
    }

    public double getLastTemperature() {
        return latestReading.getTemp().doubleValue();
    }

    public int getLastMoisture() {
        return latestReading.getMoisture().intValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sensor, 0);
        dest.writeParcelable(this.latestReading, 0);
        dest.writeString(this.location);
        dest.writeLong(lastWatering != null ? lastWatering.getTime() : -1);
        dest.writeLong(lastDataUpdate != null ? lastDataUpdate.getTime() : -1);
    }

    private Device(Parcel in) {
        this.sensor = in.readParcelable(Sensor.class.getClassLoader());
        this.latestReading = in.readParcelable(SensorReading.class.getClassLoader());
        this.location = in.readString();
        long tmpLastWatering = in.readLong();
        this.lastWatering = tmpLastWatering == -1 ? null : new Date(tmpLastWatering);
        long tmpLastDataUpdate = in.readLong();
        this.lastDataUpdate = tmpLastDataUpdate == -1 ? null : new Date(tmpLastDataUpdate);
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
