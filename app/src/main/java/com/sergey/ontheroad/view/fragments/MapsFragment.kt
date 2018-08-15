package com.sergey.ontheroad.view.fragments

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Point
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.observe
import com.sergey.ontheroad.extension.viewModel
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.models.Route
import com.sergey.ontheroad.utils.GMapUtil
import com.sergey.ontheroad.utils.LatLngInterpolator
import com.sergey.ontheroad.utils.drawer.DrawMarker
import com.sergey.ontheroad.utils.drawer.DrawRouteMaps
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {
    companion object {
        const val DELAY_TIME = 5000L
        const val BASE_ZOOM = 18f
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var imageCar: Bitmap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageCar = GMapUtil.getBitmapFromVectorDrawable(activity, R.drawable.ic_car)

        setMapSettings(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        activity!!.finish()
    }

    private fun setMapSettings(savedInstanceState: Bundle?) = defaultMap.let {
        it.onCreate(savedInstanceState)
        it.onResume()
        it.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel = viewModel(viewModelFactory) {
            observe(myCar, ::setBasePosition)
            observe(showCarOnTheMap, ::moveCarOnTheRoad)
            observe(drawRoute, ::drawRouteModel)
        }
    }

    private fun setBasePosition(myCar: Car?) {
        myCar?.let {
            marker = mMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(imageCar))
                    .position(it.position)
                    .title(it.name))

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun drawRouteModel(route: Route?){
        DrawRouteMaps.getInstance(context).draw(route!!.startPosition, route.endPosition, mMap)
        DrawMarker.getInstance(context).draw(mMap, route.startPosition, R.drawable.ic_marker, "Start position")
        DrawMarker.getInstance(context).draw(mMap, route.endPosition, R.drawable.ic_marker_finish, "End position")
    }

    private fun moveCarOnTheRoad(car: LatLng?) {
        val targetLocation = Location(LocationManager.GPS_PROVIDER)
        targetLocation.latitude = car!!.latitude
        targetLocation.longitude = car.longitude

        animateMarkerNew(targetLocation)
    }

    private fun animateMarkerNew(destination: Location) {
        val startPosition = marker!!.position
        val endPosition = LatLng(destination.latitude, destination.longitude)

        val latLngInterpolator = LatLngInterpolator.LinearFixed()

        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = DELAY_TIME
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val v = animation.animatedFraction
            val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
            marker!!.position = newPosition
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(newPosition).zoom(BASE_ZOOM).build()))

            marker!!.rotation = GMapUtil.getBearing(startPosition, LatLng(destination.latitude, destination.longitude))
        }

        valueAnimator.start()
    }
}
