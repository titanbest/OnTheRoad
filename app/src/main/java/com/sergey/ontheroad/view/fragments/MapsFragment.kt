package com.sergey.ontheroad.view.fragments

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_maps.*

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
        editDirectionAddress.setOnEditorActionListener(TextView.OnEditorActionListener { text, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.d("LOG", "HI :: ${text.text}")
                val name = text.text.toString()
                viewModel.setSearchAddress(activity!!, name)
                this@MapsFragment.editDirectionAddress.clearFocus()

                observe(viewModel.getSearchAddress, ::setDirectPosition)
                return@OnEditorActionListener false
            }
            false
        })

        val list = emptyArray<ItemMapPosition>()
        editDirectionAddress.threshold = 3
        val adapterCountries = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, list)

        editDirectionAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val arrayList = getStreetList(context!!, p0.toString())

                for (i in arrayList.indices) {
                    list[i] = ItemMapPosition(arrayList[i].thoroughfare + ", " + arrayList[i].subThoroughfare, LatLng(arrayList[i].latitude, arrayList[i].longitude))
                }

                adapterCountries.notifyDataSetChanged()
                editDirectionAddress.setAdapter(adapterCountries)

//                Observable.just(p0)
//                        .debounce(1, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .filter { !it.isEmpty() }
//                        .subscribe({
//                            viewModel.setSearchAddress(activity!!, p0.toString())
//                        })
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                observe(viewModel.getSearchAddress, ::setDirectPosition)
//                editDirectionAddress.clearFocus()
//                return true
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                Observable.just(p0)
//                        .debounce(1, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .filter { !it.isEmpty() }
//                        .subscribe({
//                            viewModel.setSearchAddress(activity!!, p0!!)
//                        })
//
//                return true
//            }
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
