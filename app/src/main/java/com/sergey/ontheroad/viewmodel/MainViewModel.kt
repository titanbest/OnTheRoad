package com.sergey.ontheroad.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.models.Route
import com.sergey.ontheroad.view.fragments.MapsFragment.Companion.DELAY_TIME
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    private var positionList = ArrayList<LatLng>()
    private var counter = 0

    val myCar: MutableLiveData<Car> = object : MutableLiveData<Car>() {
        override fun onActive() {
            super.onActive()
            value = Car("BMW I8", LatLng(49.98865707, 36.22775511))
        }
    }

    val showCarOnTheMap: MutableLiveData<LatLng> = object : MutableLiveData<LatLng>() {
        override fun onActive() {
            super.onActive()
            setList()
            Observable.interval(DELAY_TIME, TimeUnit.MILLISECONDS)
                    .subscribe({
                        postValue(positionList[counter])
                        if (counter == positionList.size - 1) counter = 0 else counter++
                    })
        }
    }

    val drawRoute: MutableLiveData<Route> = object : MutableLiveData<Route>() {
        override fun onActive() {
            super.onActive()
            value = Route(positionList[0], positionList[positionList.size - 1])
        }
    }

    private fun setList() {
        positionList.add(LatLng(49.98865707, 36.22775511))
        positionList.add(LatLng(49.98854888, 36.22655826))
        positionList.add(LatLng(49.98840057, 36.22479873))
        positionList.add(LatLng(49.98827732, 36.22329704))
        positionList.add(LatLng(49.98820489, 36.22223489))
        positionList.add(LatLng(49.98805659, 36.22051828))
        positionList.add(LatLng(49.98791862, 36.21892163))
        positionList.add(LatLng(49.98767773, 36.21608789))
        positionList.add(LatLng(49.98907012, 36.21586758))
        positionList.add(LatLng(49.99023095, 36.21566497))
        positionList.add(LatLng(49.99096901, 36.21552549))
        positionList.add(LatLng(49.99120186, 36.21394827))
        positionList.add(LatLng(49.99056382, 36.21391608))
        positionList.add(LatLng(49.99050519, 36.21422185))
    }
}