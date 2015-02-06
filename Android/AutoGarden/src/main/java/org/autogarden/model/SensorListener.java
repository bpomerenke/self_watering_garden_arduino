package org.autogarden.model;

import org.autogarden.dto.SensorReading;

public interface SensorListener {
    void sensorUpdated(SensorReading sensorReading);
}
