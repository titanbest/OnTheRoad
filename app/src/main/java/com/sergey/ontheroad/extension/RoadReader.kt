package com.sergey.ontheroad.extension

import com.google.android.gms.maps.model.LatLng
import com.sergey.domain.entity.Step

fun decodePolyline(steps: List<Step>): ArrayList<LatLng> {
    var pointList: List<LatLng>
    val routeList = ArrayList<LatLng>()

    for (i in steps.indices) {
        pointList = decode(steps[i].polyline.points)
        routeList.addAll(pointList)
    }

    return routeList
}

private fun decode(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)

        poly.add(p)
    }

    return poly
}