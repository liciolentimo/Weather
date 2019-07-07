package com.lentimosystems.licio.weather.Constants;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
    public static final String APP_ID = "492160633aea4cb7334b9ef4c3cef64c";
    public static Location currentLocation = null;
    public static String convertUnixToDate(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }
    public static String convertUnixToHour(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }
}
