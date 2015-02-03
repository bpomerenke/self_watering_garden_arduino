package org.autogarden;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.autogarden.addsensor.AddSensorActivity;
import org.autogarden.dto.Sensor;
import org.autogarden.model.DeviceModel;
import org.autogarden.model.ModelCallback;

import javax.inject.Inject;


public class SensorListActivity extends BaseActivity {

    @Inject
    SensorListAdapter sensorListAdapter;
    @Inject
    DeviceModel deviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_activity);
        ListView listView = (ListView) findViewById(R.id.device_list);
        listView.setAdapter(sensorListAdapter);
        deviceModel.fetchDevices(new ModelCallback<Sensor[]>() {
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sensor selectedSensor = sensorListAdapter.getItem(position);
                Intent intent = new Intent(SensorListActivity.this, SensorDetailActivity.class);
                intent.putExtra(SensorDetailActivity.SENSOR_KEY, selectedSensor);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_sensor) {
            startActivity(new Intent(this, AddSensorActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
