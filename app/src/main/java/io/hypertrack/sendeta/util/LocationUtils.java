package io.hypertrack.sendeta.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by piyush on 20/08/16.
 */
public class LocationUtils {

    public static final int DISTANCE_IN_METERS = 10000;
    public static final long LOCATION_UPDATE_INTERVAL_TIME = 5000;
    public static final long INITIAL_LOCATION_UPDATE_INTERVAL_TIME = 500;

    /**
     * Utility to decide if two locations are same.
     * We use the assumption that if the latitudes and longitudes
     * for the two locations are equal upto five places after the decimal point,
     * then they are same or equal.
     *
     * @param param1 First LatLng parameter
     * @param param2 Second LatLng parameter
     * @return true if the two locations are same, else false
     */
    public static boolean areLocationsSame(LatLng param1, LatLng param2) {
        if (param1 == null || param2 == null)
            return false;

        double multiplier = 100000d;
        double lat1 = ((double) Math.round(param1.latitude * multiplier) / multiplier);
        double lat2 = ((double) Math.round(param2.latitude * multiplier) / multiplier);
        double lng1 = ((double) Math.round(param1.longitude * multiplier) / multiplier);
        double lng2 = ((double) Math.round(param2.longitude * multiplier) / multiplier);

        if (lat1 == lat2 && lng1 == lng2) {
            return true;
        }
        return false;
    }

    public static boolean isLocationEnabled(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            // Find out what the settings say about which providers are enabled
            int mode = Settings.Secure.getInt(
                    contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
            if (mode == Settings.Secure.LOCATION_MODE_OFF) {
                // Location is turned OFF!
                return false;
            } else {
                // Location is turned ON!
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static LatLngBounds getBounds(LatLng latLng, int mDistanceInMeters) {
        double latRadian = Math.toRadians(latLng.latitude);

        double degLatKm = 110.574235;
        double degLongKm = 110.572833 * Math.cos(latRadian);
        double deltaLat = mDistanceInMeters / 1000.0 / degLatKm;
        double deltaLong = mDistanceInMeters / 1000.0 / degLongKm;

        double minLat = latLng.latitude - deltaLat;
        double minLong = latLng.longitude - deltaLong;
        double maxLat = latLng.latitude + deltaLat;
        double maxLong = latLng.longitude + deltaLong;

        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(new LatLng(minLat, minLong));
        b.include(new LatLng(maxLat, maxLong));

        return b.build();
    }
}
