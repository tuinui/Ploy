package com.nos.ploy.utils;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import rx.functions.Action1;

/**
 * Created by Saran on 24/12/2559.
 */

public class MyLocationUtils {

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

    public static String getCompleteAddressString(Context context, double lat, double lng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
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

    public static void getLastKnownLocation(Context context, GoogleApiClient googleApiClient, final Action1<Location> onFinishLocation) {
        getLastKnownLocation(context, googleApiClient, false, onFinishLocation);
    }

    public static void getLastKnownLocation(Context context, GoogleApiClient googleApiClient, boolean checkGpsEnable, final Action1<Location> onFinishLocation) {
        if (googleApiClient != null && googleApiClient.isConnected() && LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable()) {
            CURRENT_LOCATION = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            onFinishLocation.call(CURRENT_LOCATION);
        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.NETWORK_PROVIDER;
            if (manager.getLastKnownLocation(provider) == null && CURRENT_LOCATION == null) {
                if (checkGpsEnable) {
                    checkLocationEnabled(context);
                }
                onFinishLocation.call(null);
            } else {
                CURRENT_LOCATION = manager.getLastKnownLocation(provider);
                onFinishLocation.call(CURRENT_LOCATION);
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
