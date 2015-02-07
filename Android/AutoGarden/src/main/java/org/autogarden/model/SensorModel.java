package org.autogarden.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.autogarden.dto.Sensor;
import org.autogarden.dto.SensorReading;
import org.autogarden.service.GetGsonRequest;
import org.autogarden.service.PutGsonRequest;
import org.autogarden.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SensorModel {
    private final UserModel userModel;
    private final RequestQueue requestQueue;
    private final PollRunnable pollRunnable = new PollRunnable();
    private Map<Sensor, List<SensorListener>> listeners = new HashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject
    public SensorModel(UserModel userModel, RequestQueue requestQueue) {
        this.userModel = userModel;
        this.requestQueue = requestQueue;
        scheduler.scheduleWithFixedDelay(pollRunnable, 5, 5, TimeUnit.SECONDS);
    }

    public void fetchSensors(final ModelCallback<Sensor[]> callback) {
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
                        Log.e(SensorModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        callback.fail();
                    }
                });
        requestQueue.add(request);
    }

    public void updateSensor(final Sensor sensor, final ModelCallback<Sensor> callback) {
        PutGsonRequest<Void> request = new PutGsonRequest<>(Service.URL + "sensor/" + sensor.get_id(), sensor, Void.class, new Response.Listener<Void>() {
            @Override
            public void onResponse(Void response) {
                fetchSensor(sensor.get_id(), callback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(SensorModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                callback.fail();
            }
        });
        requestQueue.add(request);
    }

    private void fetchSensor(String sensorId, final ModelCallback<Sensor> callback) {
        GetGsonRequest<Sensor> request = new GetGsonRequest<>(Service.URL + "sensor/" + sensorId, Sensor.class,
                new Response.Listener<Sensor>() {
                    @Override
                    public void onResponse(Sensor response) {
                        callback.success(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(SensorModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
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
                        if (response.length > 0) {
                            callback.success(response[0]);
                        } else {
                            callback.success(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(SensorModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                        callback.fail();
                    }
                });
        requestQueue.add(request);
    }

    public void addSensorListener(Sensor sensor, SensorListener sensorListener) {
        synchronized (listeners) {
            List<SensorListener> sensorListeners = listeners.get(sensor);
            if (sensorListeners == null) {
                sensorListeners = new ArrayList<>();
                sensorListeners.add(sensorListener);
                listeners.put(sensor, sensorListeners);
            }
        }
    }

    public void removeSensorListener(Sensor sensor, SensorListener sensorListener) {
        synchronized (listeners) {
            List<SensorListener> sensorListeners = listeners.get(sensor);
            if (sensorListeners != null) {
                sensorListeners.remove(sensorListener);
                if (sensorListeners.size() == 0) {
                    listeners.remove(sensor);
                }
            }
        }
    }

    private void fireSensorUpdated(Sensor sensor, SensorReading sensorReading) {
        List<SensorListener> toNotifyListeners = new ArrayList<>();
        synchronized (listeners) {
            List<SensorListener> sensorListeners = listeners.get(sensor);
            if (sensorListeners != null) {
                toNotifyListeners.addAll(sensorListeners);
            }
        }
        for (SensorListener sensorListener : toNotifyListeners) {
            sensorListener.sensorUpdated(sensorReading);
        }
    }

    private class PollRunnable implements Runnable {
        @Override
        public void run() {
            Log.e(PollRunnable.class.getSimpleName(), "Start PollRunnable");
            List<Sensor> sensorsToPoll;
            synchronized (listeners) {
                sensorsToPoll = new ArrayList<>(listeners.keySet());
            }
            Log.e(PollRunnable.class.getSimpleName(), "Sensors to poll " + sensorsToPoll.size());
            for (final Sensor sensor : sensorsToPoll) {
                GetGsonRequest<SensorReading[]> request = new GetGsonRequest<>(Service.URL + "sensor/" + sensor.get_id() + "/sensorReading", SensorReading[].class,
                        new Response.Listener<SensorReading[]>() {
                            @Override
                            public void onResponse(SensorReading[] response) {
                                if (response.length > 0) {
                                    SensorReading sensorReading = response[0];
                                    Log.e(PollRunnable.class.getSimpleName(), "Sensor response " + sensor.getDisplayName() + " " + sensorReading);
                                    fireSensorUpdated(sensor, sensorReading);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(SensorModel.class.getSimpleName(), "VolleyError " + error, error.getCause());
                            }
                        });
                requestQueue.add(request);
            }
        }
    }
}
