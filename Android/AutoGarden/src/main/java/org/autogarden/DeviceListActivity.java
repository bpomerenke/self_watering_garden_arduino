package org.autogarden;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;


public class DeviceListActivity extends BaseActivity {

    @Inject
    DeviceListAdapter deviceListAdapter;
    @Inject
    DeviceModel deviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_activity);
        ListView listView = (ListView) findViewById(R.id.device_list);
        listView.setAdapter(deviceListAdapter);
        deviceModel.fetchDevices(new ModelCallback<List<Device>>() {
            @Override
            public void success(List<Device> data) {
                deviceListAdapter.clear();
                for (Device device : data) {
                    deviceListAdapter.add(device);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device selectedDevice = deviceListAdapter.getItem(position);
                Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                intent.putExtra(DeviceDetailActivity.DEVICE_KEY, selectedDevice);
                startActivity(intent);
            }
        });
    }
}
