package com.sergey.ontheroad.utils.drawer;

import com.google.android.gms.maps.model.LatLng;

public class FetchUrl {
    public static String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        return "https://maps.googleapis.com/maps/api/directions/json?" + str_origin + "&" + str_dest + "&sensor=false";
    }
}

//https://maps.googleapis.com/maps/api/directions/json?origin=49.98865707,36.22775511&destination=49.99050519,36.21422185&sensor=false