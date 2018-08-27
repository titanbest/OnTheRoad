package com.sergey.ontheroad.extension

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.support.v4.content.ContextCompat
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.sergey.ontheroad.models.ItemMapPosition
import com.sergey.ontheroad.utils.LatLngInterpolator
import com.sergey.ontheroad.view.fragments.MapsFragment

fun GoogleMap.draw(context: Context, location: LatLng, resDrawable: Int, title: String) =
        this.addMarker(MarkerOptions()
                .position(location)
                .title(title)
                .icon(getBitmapFromVectorDrawable(context, resDrawable)))!!

fun GoogleMap.paintPolyline(list: ArrayList<LatLng>) {
    val lineOptions = PolylineOptions()
    lineOptions.color(Color.RED)
    lineOptions.addAll(list)
    lineOptions.width(6f)
    this.addPolyline(lineOptions)
}

fun GoogleMap.animateMarker(marker: Marker, destination: Location) {
    val startPosition = marker.position
    val endPosition = LatLng(destination.latitude, destination.longitude)

    val latLngInterpolator = LatLngInterpolator.LinearFixed()

    val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
    valueAnimator.duration = MapsFragment.DELAY_TIME
    valueAnimator.interpolator = LinearInterpolator()
    valueAnimator.addUpdateListener { animation ->
        val v = animation.animatedFraction
        val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
        marker.position = newPosition
        this.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(newPosition).zoom(MapsFragment.BASE_ZOOM).build()))

        marker.rotation = getBearing(startPosition, LatLng(destination.latitude, destination.longitude))
    }

    valueAnimator.start()
}

fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(context, drawableId)!!
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun splitString(str: String): String = str.substring(0, str.indexOf(','))

fun getBearing(begin: LatLng, end: LatLng): Float {
    val lat = Math.abs(begin.latitude - end.latitude)
    val lng = Math.abs(begin.longitude - end.longitude)

    if (begin.latitude < end.latitude && begin.longitude < end.longitude)
        return Math.toDegrees(Math.atan(lng / lat)).toFloat()
    else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
        return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
    else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
        return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
    else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
        return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()

    return -1f
}

fun getStreet(context: Context, p0: String): ItemMapPosition? {
    val addressList: List<android.location.Address>
    val geocoder = Geocoder(context)
    if (!p0.isEmpty()) {
        addressList = geocoder.getFromLocationName(p0, 1)
        if (!addressList.isEmpty()) {
            val address = addressList[0]
            return ItemMapPosition(address.thoroughfare + ", " + address.subThoroughfare, LatLng(address.latitude, address.longitude))
        } else return null
    } else return null
}

