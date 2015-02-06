package org.autogarden;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.autogarden.dto.Sensor;
import org.autogarden.dto.SensorReading;
import org.autogarden.model.ModelCallback;
import org.autogarden.model.SensorListener;
import org.autogarden.model.SensorModel;

import javax.inject.Inject;

public class SensorDetailActivity extends BaseActivity implements SensorListener {
    public static final String SENSOR_KEY = "SENSOR_KEY";

    @Inject
    DateFormatter dateFormatter;
    @Inject
    SensorModel sensorModel;
    private Sensor sensor;
    private TextView locationView;
    private TextView lastWateringView;
    private TextView lastDataUpdateView;
    private TextView temperatureView;
    private TextView moistureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_detail_activity);
        sensor = getIntent().getExtras().getParcelable(SENSOR_KEY);

        getSupportActionBar().setTitle(sensor.getName());

        locationView = (TextView) findViewById(R.id.device_detail_location);
        lastWateringView = (TextView) findViewById(R.id.device_detail_last_watering);
        lastDataUpdateView = (TextView) findViewById(R.id.device_detail_last_data_update);
        temperatureView = (TextView) findViewById(R.id.device_detail_temperature);
        moistureView = (TextView) findViewById(R.id.device_detail_moisture);

        sensorModel.fetchLatestSensorReading(sensor, new ModelCallback<SensorReading>() {

            @Override
            public void success(SensorReading data) {
//                locationView.setText(sensor.getLocation());
//                lastWateringView.setText(dateFormatter.formatDate(sensor.getLastWatering()));
//                lastDataUpdateView.setText(dateFormatter.formatDate(sensor.getLastDataUpdate()));
                temperatureView.setText(String.valueOf(data.getTemp()));
                moistureView.setText(String.valueOf(data.getMoisture()));
            }

            @Override
            public void fail() {
                Toast.makeText(SensorDetailActivity.this, "Unable to retrieve Current Sensor Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorModel.addSensorListener(sensor, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorModel.removeSensorListener(sensor, this);
    }

    @Override
    public void sensorUpdated(SensorReading sensorReading) {
        temperatureView.setText(String.valueOf(sensorReading.getTemp()));
        moistureView.setText(String.valueOf(sensorReading.getMoisture()));
    }
}
