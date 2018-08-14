package com.sergey.ontheroad.view

import com.google.android.gms.maps.model.LatLng

interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

    class LinearFixed : LatLngInterpolator {
        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            val lat = (b.latitude - a.latitude) * fraction + a.latitude
            var lngDelta = b.longitude - a.longitude

            if (Math.abs(lngDelta) > 180) lngDelta -= Math.signum(lngDelta) * 360

            val lng = lngDelta * fraction + a.longitude
            return LatLng(lat, lng)
        }
    }
}