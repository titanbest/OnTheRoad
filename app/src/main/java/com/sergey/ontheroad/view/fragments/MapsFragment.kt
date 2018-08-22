package com.sergey.ontheroad.view.fragments

import android.animation.ValueAnimator
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.sergey.data.entity.Route
import com.sergey.domain.entity.LatLngDomain
import com.sergey.domain.entity.RouteDomain
import com.sergey.domain.repository.ServerRepository
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.draw
import com.sergey.ontheroad.extension.observe
import com.sergey.ontheroad.extension.viewModel
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.utils.GMapUtil
import com.sergey.ontheroad.utils.LatLngInterpolator
import com.sergey.ontheroad.utils.drawer.DrawRoute
import com.sergey.ontheroad.utils.readRound
import com.sergey.ontheroad.utils.sendAddress
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_maps.*
import javax.inject.Inject

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {
    companion object {
        const val DELAY_TIME = 5000L
        const val BASE_ZOOM = 16f
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
//            observe(getStringRoad, ::setStringToLog)
        }

        setStringToLog()
    }

    private fun setStringToLog() {

    }

    private fun setBasePosition(myCar: Car?) {
        myCar?.let {
            marker = mMap.draw(activity!!, it.position, R.drawable.ic_car, it.name)

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun drawRouteModel(route: Route?) {
//        DrawRoute(mMap).execute(GMapUtil.getUrl(route!!.startPosition, route.endPosition))
        Single.just(GMapUtil.getUrl(route!!.startPosition, route.endPosition))
                .map { sendAddress(it) }
                .map { mMap.readRound(it) }
                .subscribe()

        mMap.draw(activity!!, route.startPosition, R.drawable.ic_marker, "Start position")
        mMap.draw(activity!!, route.endPosition, R.drawable.ic_marker_finish, "End position")
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
