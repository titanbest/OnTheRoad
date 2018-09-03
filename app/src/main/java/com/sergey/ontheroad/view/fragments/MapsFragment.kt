package com.sergey.ontheroad.view.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.sergey.ontheroad.R
import com.sergey.ontheroad.extension.*
import com.sergey.ontheroad.models.GeoSearchResult
import com.sergey.ontheroad.models.ItemMapPosition
import com.sergey.ontheroad.view.base.BaseFragment
import com.sergey.ontheroad.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.concurrent.TimeUnit

class MapsFragment : BaseFragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private var markerCar: Marker? = null
    private var markerDirectory: Marker? = null
    private val targetLocation = Location(LocationManager.GPS_PROVIDER)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMapSettings(savedInstanceState)
        initView()
        initEditAddressView()
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
            if (markerDirectory != null) {
                markerDirectory!!.remove()
            }
            markerDirectory = mMap.draw(activity!!, it.position, R.drawable.ic_marker, it.name)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, BASE_ZOOM))
        }
    }

    private fun moveCarOnTheRoad(car: LatLng?) {
        targetLocation.latitude = car!!.latitude
        targetLocation.longitude = car.longitude
        mMap.animateMarker(markerCar!!, targetLocation)
    }

    private fun drawRoad(pointList: ArrayList<LatLng>?) {
        mMap.paintPolyline(pointList!!)
    }

    @SuppressLint("SetTextI18n")
    private fun setTextDistance(distance: String?) {
        textViewDistance.text = "Distance: $distance"
    }

    @SuppressLint("SetTextI18n")
    private fun setTextDuration(duration: String?) {
        textViewDuration.text = "Duration: $duration"
    }

    private fun initView() {
        buttonMoveCar.setOnClickListener {
            observe(viewModel.getDrawRoad, ::drawRoad)
            observe(viewModel.getFullDistance, ::setTextDistance)
            observe(viewModel.getFullDuration, ::setTextDuration)
            TimeUnit.MILLISECONDS.sleep(1500)
            observe(viewModel.moveCarOnTheMap, ::moveCarOnTheRoad)
        }

        searchEditAddress.setOnClickListener {
            editDirectionAddress.text?.let {
                if (!TextUtils.isEmpty(it)){
                    viewModel.setSearchAddress(activity!!, it.toString())
                    observe(viewModel.getSearchAddress, ::setDirectPosition)
                }
            }
        }
        clearEditAddress.setOnClickListener { editDirectionAddress.setText("") }
    }

    private fun initEditAddressView() {
        editDirectionAddress.threshold = THRESHOLD
        editDirectionAddress.setAdapter(GeoAutoCompleteAdapter(activity!!))

        editDirectionAddress.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
            val result = adapterView.getItemAtPosition(position) as GeoSearchResult
            editDirectionAddress.setText(result.address)
        }

        editDirectionAddress.setOnEditorActionListener(TextView.OnEditorActionListener { text, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                text.text?.let {
                    if (!TextUtils.isEmpty(it)) {
                        viewModel.setSearchAddress(activity!!, it.toString())
                        observe(viewModel.getSearchAddress, ::setDirectPosition)
                        this@MapsFragment.editDirectionAddress.clearFocus()
                    }
                }
                return@OnEditorActionListener false
            }
            false
        })

        editDirectionAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                clearEditAddress.visibility = if (p0!!.isNotEmpty()) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onPause() {
        super.onPause()
        activity!!.finish()
    }

    companion object {
        const val THRESHOLD = 5
        const val MAX_RESULTS = 5
        const val DELAY_TIME = 20L
        const val BASE_ZOOM = 13f
    }
}
