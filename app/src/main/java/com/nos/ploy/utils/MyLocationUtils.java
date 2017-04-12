package com.nos.ploy.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Saran on 24/12/2559.
 */

public class MyLocationUtils {

    public static LatLng DEFAULT_LATLNG = new LatLng(48.858951, 2.293986);
    public static Location CURRENT_LOCATION;

    public static Address getGetCoder(Context context, LatLng latLng) {
        Geocoder geoCoder = new Geocoder(context, Locale.US);
        try {
            List<Address> addresses = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (null != addresses && !addresses.isEmpty()) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStaticMapsUrl(LatLng latLng) {
        double lat = DEFAULT_LATLNG.latitude;
        double lng = DEFAULT_LATLNG.longitude;
        if (null != latLng) {
            lat = latLng.latitude;
            lng = latLng.longitude;
        }
        return "http://maps.googleapis.com/maps/api/staticmap?" +
                "markers=" + lat + "," + lng +
                "&zoom=17" +
                "&size=640x360" +
                "&scale=2" +
                "&maptype=roadmap" +
                "&sensor=false";
    }

    public static String getEmptyMaps() {
//        double lat = DEFAULT_LATLNG.latitude;
//        double lng = DEFAULT_LATLNG.longitude;
//        if (null != latLng) {
//            lat = latLng.latitude;
//            lng = latLng.longitude;
//        }
        return "http://maps.googleapis.com/maps/api/staticmap?" +
                "center=-1.0,-1.0" +
                "&zoom=17" +
                "&size=640x360" +
                "&scale=2" +
                "&maptype=roadmap" +
                "&sensor=false";
    }

    public static String getDistance(LatLng current, LatLng destination) {
        Location l1 = new Location("One");
        l1.setLatitude(current.latitude);
        l1.setLongitude(current.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(destination.latitude);
        l2.setLongitude(destination.longitude);

        float distance = l1.distanceTo(l2);
//        String dist = distance + " M";
        String dist = String.format(Locale.getDefault(), "%.0f", Math.ceil(distance)) + " m";
        if (distance > 1000.0f) {
            distance = distance / 1000.0f;
            dist = String.format(Locale.getDefault(), "%.1f", Math.ceil(distance)) + " km";
        }
        return dist;
    }

    public static String getDistanceFromCurrentLocation(Context context, GoogleApiClient googleApiClient, LatLng destination) {
        Location location = getLastKnownLocation(context, googleApiClient);
        LatLng latLng = null;
        if (null != location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            latLng = new LatLng(0, 0);
        }

        return getDistance(latLng, destination);
    }

    public static String getCompleteAddressString(Context context, double lat, double lng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    String addresLineI = returnedAddress.getAddressLine(i);
                    if (!TextUtils.isEmpty(addresLineI)) {
                        strReturnedAddress.append(addresLineI).append("\n");
                    }
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd.trim();
    }

//    private static AlertDialog DIALOG_LOCATION_SETTINGS;

//    public static void getLastKnownLocation(Context context, GoogleApiClient googleApiClient, final Action1<Location> onFinishLocation, boolean forceUpdate) {
//        if (googleApiClient != null && googleApiClient.isConnected() && LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable()) {
//            onFinishLocation.call(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
//        } else {
//            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_FINE);
//            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            String provider = manager.getBestProvider(criteria, false);
//            manager.requestSingleUpdate(provider, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    CURRENT_LOCATION = location;
//                    if (null != CURRENT_LOCATION) {
//                        onFinishLocation.call(CURRENT_LOCATION);
//                    }
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//                }
//            }, null);
//            CURRENT_LOCATION = manager.getLastKnownLocation(provider);
//            if (null != CURRENT_LOCATION) {
//                onFinishLocation.call(CURRENT_LOCATION);
//            }
//        }
//    }

    public static Location getLastKnownLocation(Context context, GoogleApiClient googleApiClient) {
        return getLastKnownLocation(context, googleApiClient, false);
    }

    public static Location getLastKnownLocation(Context context, GoogleApiClient googleApiClient, boolean checkGpsEnable) {
        if (googleApiClient != null && googleApiClient.isConnected() && LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable()) {
            if (null != LocationServices.FusedLocationApi.getLastLocation(googleApiClient)) {
                CURRENT_LOCATION = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }

            return CURRENT_LOCATION;
        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.NETWORK_PROVIDER;
            if (manager.getLastKnownLocation(provider) == null && CURRENT_LOCATION == null) {
                if (checkGpsEnable) {
                    checkLocationEnabled(context);
                }
                return CURRENT_LOCATION;
            } else {
                if (null != manager.getLastKnownLocation(provider)) {
                    CURRENT_LOCATION = manager.getLastKnownLocation(provider);
                }
                return CURRENT_LOCATION;
            }
        }
    }


    public static boolean locationProviderEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        return gps_enabled || network_enabled;
    }

    public static void checkLocationEnabled(Context context) {
        if (!locationProviderEnabled(context)) {
            PopupMenuUtils.showDialogLocationSettings(context, null);
        }
    }


}
