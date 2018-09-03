package com.sergey.ontheroad.extension

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.sergey.ontheroad.models.ItemMapPosition
import com.sergey.ontheroad.utils.LatLngInterpolator
import com.sergey.ontheroad.view.fragments.MapsFragment
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

fun getStreetList(context: Context, p0:String): Maybe<ArrayList<String>>? = Single.just(p0)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { Geocoder(context).getFromLocationName(p0, 5) }
        .filter { it.isNotEmpty()}
        .map {
            val list = ArrayList<String>()
            for (i in it.indices) {
                val element = it[i]
                val sb = StringBuffer()

                sb.append(element.getAddressLine(0)).append("\n")
                for (j in 1 until element.maxAddressLineIndex) {
                    sb.append(element.getAddressLine(j)).append(", ")
                }
                val lm = StringBuilder(sb.substring(0, sb.length - 2))
                list.add(i, lm.toString())
            }
            return@map list
        }
        .doOnError { Log.d("DLOG", it.message) }

fun getStreet(context: Context, p0: String): Maybe<ItemMapPosition>? = Single.just(p0)
        .map { Geocoder(context).getFromLocationName(p0, 1) }
        .filter { it.isNotEmpty()}
        .map {

            val address = it[0]
            val sb = StringBuilder()
            if (!TextUtils.isEmpty(address.thoroughfare)) {
                sb.append(address.thoroughfare)

                if (!TextUtils.isEmpty(address.subThoroughfare)) {
                    sb.append(", " + address.subThoroughfare)
                }
                if (!TextUtils.isEmpty(address.locality)) {
                    sb.append(", " + address.locality)
                }
            }

            ItemMapPosition(sb.toString(), LatLng(address.latitude, address.longitude))
        }
        .doOnError { Log.d("DLOG", it.message) }


