package org.autogarden.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.autogarden.dto.Sensor;
import org.autogarden.dto.SensorReading;
import org.autogarden.service.GetGsonRequest;
import org.autogarden.service.Service;

import javax.inject.Inject;

public class DeviceModel {
    private final UserModel userModel;
    private final RequestQueue requestQueue;

    @Inject
    public DeviceModel(UserModel userModel, RequestQueue requestQueue) {
        this.userModel = userModel;
        this.requestQueue = requestQueue;
    }

    public void fetchDevices(final ModelCallback<Sensor[]> callback) {
        GetGsonRequest<Sensor[]> request = new GetGsonRequest<>(Service.URL + "sensor?forUser=" + userModel.getUserName(), Sensor[].class,
                new Response.Listener<Sensor[]>() {
                    @Override
                    public void onResponse(Sensor[] response) {
                        callback.success(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(DeviceModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        callback.fail();
                    }
                });
        requestQueue.add(request);
    }

    public void fetchLatestSensorReading(Sensor sensor, final ModelCallback<SensorReading> callback) {
        GetGsonRequest<SensorReading[]> request = new GetGsonRequest<>(Service.URL + "sensor/" + sensor.get_id() + "/sensorReading", SensorReading[].class,
                new Response.Listener<SensorReading[]>() {
                    @Override
                    public void onResponse(SensorReading[] response) {
                        callback.success(response[0]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(DeviceModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        callback.fail();
                    }
                });
        requestQueue.add(request);
    }
}
