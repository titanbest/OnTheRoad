package com.sergey.ontheroad.view.fragments

import android.animation.ValueAnimator
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
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
import com.sergey.ontheroad.utils.GMapUtil
import com.sergey.ontheroad.view.LatLngInterpolator
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback, IMapsFragment {
    companion object {
        const val DELAY_TIME = 2000L
        const val BASE_ZOOM = 17f
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var imageCar: Bitmap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageCar = GMapUtil.getBitmapFromVectorDrawable(activity, R.drawable.ic_car)

        viewModel = ViewModelProviders.of(activity!!)[MainViewModel::class.java]
        viewModel.setMapsUi(this)

        defaultMap.onCreate(savedInstanceState)
        defaultMap.onResume()
        defaultMap.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setBasePosition()
    }

    override fun setBasePosition() {
        val myCar = viewModel.getMyCar()

        marker = mMap.addMarker(MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(imageCar))
                .position(myCar.position)
                .title(myCar.name))

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCar.position, BASE_ZOOM))

        viewModel.getMoveOnRoute()
    }

    override fun moveCarOnTheRoad(car: LatLng) {
        val targetLocation = Location(LocationManager.GPS_PROVIDER)
        targetLocation.latitude = car.latitude
        targetLocation.longitude = car.longitude

        animateMarkerNew(targetLocation, marker!!)
    }

    private fun animateMarkerNew(destination: Location, marker: Marker) {
        val startPosition = marker.position
        val endPosition = LatLng(destination.latitude, destination.longitude)

        val latLngInterpolator = LatLngInterpolator.LinearFixed()

        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = DELAY_TIME
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val v = animation.animatedFraction
            val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
            marker.position = newPosition
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(newPosition).zoom(BASE_ZOOM).build()))

            marker.rotation = GMapUtil.getBearing(startPosition, LatLng(destination.latitude, destination.longitude))
        }

        valueAnimator.start()
    }
}
