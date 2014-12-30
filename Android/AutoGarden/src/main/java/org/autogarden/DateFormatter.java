package org.autogarden;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mmZ");
        return formatter.format(date);
    }
}
