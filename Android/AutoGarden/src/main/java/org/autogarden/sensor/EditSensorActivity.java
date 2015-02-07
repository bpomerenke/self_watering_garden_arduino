package org.autogarden.sensor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.autogarden.BaseActivity;
import org.autogarden.R;
import org.autogarden.dto.Sensor;
import org.autogarden.dto.WateringSchedule;
import org.autogarden.model.ModelCallback;
import org.autogarden.model.SensorModel;
import org.autogarden.model.WorkingSensorModel;

import javax.inject.Inject;

public class EditSensorActivity extends BaseActivity {
    @Inject
    WorkingSensorModel workingSensorModel;
    @Inject
    SensorModel sensorModel;
    private EditText startWhenBelow;
    private EditText stopWhenAbove;
    private Sensor sensor;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sensor_activity);

        startWhenBelow = (EditText) findViewById(R.id.edit_start_when_below);
        stopWhenAbove = (EditText) findViewById(R.id.edit_stop_when_above);
        saveButton = (Button) findViewById(R.id.edit_save);

        sensor = workingSensorModel.getSensor();
        getSupportActionBar().setTitle(sensor.getName());
        WateringSchedule wateringSchedule = sensor.getWateringSchedule();
        if (wateringSchedule != null) {
            setIfNotNull(startWhenBelow, wateringSchedule.getStartWhenBelowMoisture());
            setIfNotNull(stopWhenAbove, wateringSchedule.getStopWhenAboveMoisture());
        }

        TextWatcherListener watcher = new TextWatcherListener();
        startWhenBelow.addTextChangedListener(watcher);
        stopWhenAbove.addTextChangedListener(watcher);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WateringSchedule wateringSchedule = sensor.getWateringSchedule();
                if (wateringSchedule == null) {
                    wateringSchedule = new WateringSchedule();
                    sensor.setWateringSchedule(wateringSchedule);
                }
                Integer start = Integer.valueOf(startWhenBelow.getText().toString());
                wateringSchedule.setStartWhenBelowMoisture(start);
                Integer stop = Integer.valueOf(stopWhenAbove.getText().toString());
                wateringSchedule.setStopWhenAboveMoisture(stop);

                sensorModel.updateSensor(sensor, new ModelCallback<Sensor>() {
                    @Override
                    public void success(Sensor sensor) {
                        workingSensorModel.setSensor(sensor);
                        finish();
                    }

                    @Override
                    public void fail() {
                        Toast.makeText(EditSensorActivity.this, "Unable to update sensor", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setIfNotNull(EditText field, Integer value) {
        if (value != null) {
            field.setText(String.valueOf(value));
        }
    }

    private void updateSaveButtonState() {
        try {
            Integer.valueOf(startWhenBelow.getText().toString());
            Integer.valueOf(stopWhenAbove.getText().toString());
            saveButton.setEnabled(true);
        } catch (NumberFormatException ex) {
            saveButton.setEnabled(false);
        }
    }

    private class TextWatcherListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateSaveButtonState();
        }
    }
}
