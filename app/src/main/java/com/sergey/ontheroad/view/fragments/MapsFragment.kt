package com.sergey.ontheroad.view.fragments

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.*
import com.sergey.ontheroad.models.ItemMapPosition
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.concurrent.TimeUnit

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var markerCar: Marker? = null
    private var markerDirectory: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapSettings(savedInstanceState)
        initView()
    }

    private fun setMapSettings(savedInstanceState: Bundle?) = defaultMap.let {
        it.onCreate(savedInstanceState)
        it.onResume()
        it.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel = viewModel(viewModelFactory) {
            observe(basePosition, ::setBasePosition)
        }
    }

    private fun setBasePosition(myItemMapPosition: ItemMapPosition?) {
        myItemMapPosition?.let {
            markerCar = mMap.draw(activity!!, it.position, R.drawable.ic_car, it.name)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun setDirectPosition(myItemMapPosition: ItemMapPosition?) {
        editDirectionAddress.isFocusable = false
        myItemMapPosition?.let {
            markerDirectory = mMap.draw(activity!!, it.position, R.drawable.ic_marker, it.name)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun moveCarOnTheRoad(car: LatLng?) {
        val targetLocation = Location(LocationManager.GPS_PROVIDER)
        targetLocation.latitude = car!!.latitude
        targetLocation.longitude = car.longitude

        mMap.animateMarker(markerCar!!, targetLocation)
    }

    private fun showData(pointList: ArrayList<LatLng>?) {
        mMap.paintPolyline(pointList!!)
    }

    private fun initView() {
        editDirectionAddress.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                observe(viewModel.getSearchAddress, ::setDirectPosition)
                editDirectionAddress.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Observable.just(p0)
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { !it.isEmpty() }
                        .subscribe({
                            viewModel.setSearchAddress(activity!!, p0!!)
                        })

                return true
            }
        })

        

        buttonMoveCar.setOnClickListener {
            observe(viewModel.getData, ::showData)
            observe(viewModel.moveCarOnTheMap, ::moveCarOnTheRoad)
        }
    }

    override fun onPause() {
        super.onPause()
        activity!!.finish()
    }

    companion object {
        const val DELAY_TIME = 1000L
        const val BASE_ZOOM = 13f
    }
}
