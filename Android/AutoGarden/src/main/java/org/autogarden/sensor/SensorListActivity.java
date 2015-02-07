package org.autogarden.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.autogarden.BaseActivity;
import org.autogarden.R;
import org.autogarden.dto.Sensor;
import org.autogarden.model.ModelCallback;
import org.autogarden.model.SensorModel;
import org.autogarden.model.WorkingSensorModel;

import javax.inject.Inject;


public class SensorListActivity extends BaseActivity {

    @Inject
    SensorListAdapter sensorListAdapter;
    @Inject
    SensorModel sensorModel;
    @Inject
    WorkingSensorModel workingSensorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_activity);
        ListView listView = (ListView) findViewById(R.id.device_list);
        listView.setAdapter(sensorListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor selectedSensor = sensorListAdapter.getItem(position);
                workingSensorModel.setSensor(selectedSensor);
                startActivity(new Intent(SensorListActivity.this, SensorDetailActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        sensorModel.fetchSensors(new ModelCallback<Sensor[]>() {
            @Override
            public void success(Sensor[] data) {
                sensorListAdapter.clear();
                for (Sensor sensor : data) {
                    sensorListAdapter.add(sensor);
                }
            }

            @Override
            public void fail() {
                Toast.makeText(SensorListActivity.this, "Unable to retrieve devices", Toast.LENGTH_LONG).show();
            }
        });
    }
}
