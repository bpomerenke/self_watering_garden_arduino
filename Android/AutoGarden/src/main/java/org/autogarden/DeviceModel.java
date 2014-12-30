package org.autogarden;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class DeviceModel {
    private final List<Device> devices;

    @Inject
    public DeviceModel() {
        devices = new ArrayList<>();
        devices.add(new Device("My Garden 1", "Location A", new Date(), new Date(), 76.8, 200));
        devices.add(new Device("My Garden 2", "Location B", new Date(), new Date(), 80.4, 400));
    }

    public void fetchDevices(ModelCallback<List<Device>> callback) {
        callback.success(devices);
    }
}
