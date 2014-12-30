package org.autogarden;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import javax.inject.Inject;

public class DeviceListAdapter extends ArrayAdapter<Device> {

    private DateFormatter dateFormatter;

    @Inject
    public DeviceListAdapter(Context context, DateFormatter dateFormatter) {
        super(context, 0);
        this.dateFormatter = dateFormatter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.device_row, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        Device item = getItem(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.nameView.setText(item.getName());
        holder.lastWateringView.setText(dateFormatter.formatDate(item.getLastWatering()));
        holder.lastDataUpdateView.setText(dateFormatter.formatDate(item.getLastDataUpdate()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView nameView;
        public TextView lastWateringView;
        public TextView lastDataUpdateView;

        private ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.device_row_name);
            lastWateringView = (TextView) view.findViewById(R.id.device_row_last_watering);
            lastDataUpdateView = (TextView) view.findViewById(R.id.device_row_last_update);
        }
    }
}
