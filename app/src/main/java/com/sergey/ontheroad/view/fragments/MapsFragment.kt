package com.sergey.ontheroad.view.fragments

import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.sergey.data.entity.Address
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.*
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*
import java.io.IOException

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapSettings(savedInstanceState)
        initView()
    }

    private fun initView() {
        toolbar.inflateMenu(R.menu.main_menu)
        buttonMoveCar.setOnClickListener {
            observe(viewModel.moveCarOnTheMap, ::moveCarOnTheRoad)
            observe(viewModel.drawAddress, ::drawRouteAddress)
        }
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
            observe(getData, ::showData)
        }
    }

    private fun setBasePosition(myCar: Car?) {
        myCar?.let {
            marker = mMap.draw(activity!!, it.position, R.drawable.ic_car, it.name)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun drawRouteAddress(address: Address?) {
        mMap.draw(activity!!, address!!.startPosition, R.drawable.ic_marker, splitString(address.startTitle))
        mMap.draw(activity!!, address.endPosition, R.drawable.ic_marker, splitString(address.endTitle))
    }

    private fun moveCarOnTheRoad(car: LatLng?) {
        val targetLocation = Location(LocationManager.GPS_PROVIDER)
        targetLocation.latitude = car!!.latitude
        targetLocation.longitude = car.longitude

        mMap.animateMarker(marker!!, targetLocation)
    }

    private fun showData(pointList: ArrayList<LatLng>?) {
        mMap.paintPolyline(pointList!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main_menu, menu)

        val item = menu!!.findItem(R.id.menuSearch)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val addressList: List<android.location.Address>

                if (!TextUtils.isEmpty(p0)) {
                    val geocoder = Geocoder(activity!!)
                    try {
                        addressList = geocoder.getFromLocationName(p0, 1)

                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        Log.d("DLOG", "Coordinates: $latLng")

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }


                    return true
                } else return false
            }
        })
    }

    override fun onPause() {
        super.onPause()
        activity!!.finish()
    }

    companion object {
        const val DELAY_TIME = 1000L
        const val BASE_ZOOM = 15f
    }
}
