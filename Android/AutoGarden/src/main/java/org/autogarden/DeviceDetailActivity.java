package org.autogarden;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class DeviceDetailActivity extends BaseActivity {
    public static final String DEVICE_KEY = "DEVICE_KEY";

    @Inject
    DateFormatter dateFormatter = new DateFormatter();
    private Device device;
    private TextView locationView;
    private TextView lastWateringView;
    private TextView lastDataUpdateView;
    private TextView temperatureView;
    private TextView moistureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_detail_activity);
        device = getIntent().getExtras().getParcelable(DEVICE_KEY);

        locationView = (TextView) findViewById(R.id.device_detail_location);
        lastWateringView = (TextView) findViewById(R.id.device_detail_last_watering);
        lastDataUpdateView = (TextView) findViewById(R.id.device_detail_last_data_update);
        temperatureView = (TextView) findViewById(R.id.device_detail_temperature);
        moistureView = (TextView) findViewById(R.id.device_detail_moisture);

        getSupportActionBar().setTitle(device.getName());
        locationView.setText(device.getLocation());
        lastWateringView.setText(dateFormatter.formatDate(device.getLastWatering()));
        lastDataUpdateView.setText(dateFormatter.formatDate(device.getLastDataUpdate()));
        temperatureView.setText(String.valueOf(device.getLastTemperature()));
        moistureView.setText(String.valueOf(device.getLastMoisture()));
    }
}
