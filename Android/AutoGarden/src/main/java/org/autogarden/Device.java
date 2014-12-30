package org.autogarden;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Device implements Parcelable {
    private String name;
    private String location;
    private Date lastWatering;
    private Date lastDataUpdate;
    private double lastTemperature;
    private int lastMoisture;

    public Device(String name, String location, Date lastWatering, Date lastDataUpdate, double lastTemperature, int lastMoisture) {
        this.name = name;
        this.location = location;
        this.lastWatering = lastWatering;
        this.lastDataUpdate = lastDataUpdate;
        this.lastTemperature = lastTemperature;
        this.lastMoisture = lastMoisture;
    }

    public String getName() {
        return name;
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
        return lastTemperature;
    }

    public int getLastMoisture() {
        return lastMoisture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeLong(lastWatering != null ? lastWatering.getTime() : -1);
        dest.writeLong(lastDataUpdate != null ? lastDataUpdate.getTime() : -1);
        dest.writeDouble(this.lastTemperature);
        dest.writeInt(this.lastMoisture);
    }

    private Device(Parcel in) {
        this.name = in.readString();
        this.location = in.readString();
        long tmpLastWatering = in.readLong();
        this.lastWatering = tmpLastWatering == -1 ? null : new Date(tmpLastWatering);
        long tmpLastDataUpdate = in.readLong();
        this.lastDataUpdate = tmpLastDataUpdate == -1 ? null : new Date(tmpLastDataUpdate);
        this.lastTemperature = in.readDouble();
        this.lastMoisture = in.readInt();
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
