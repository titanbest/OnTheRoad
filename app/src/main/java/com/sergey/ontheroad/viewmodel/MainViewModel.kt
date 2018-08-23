package com.sergey.ontheroad.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.sergey.data.entity.Address
import com.sergey.data.entity.SendAddress
import com.sergey.data.mapper.AddressMapper
import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.entity.Routes
import com.sergey.domain.interactor.GetRoadUseCase
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.extension.Flow
import com.sergey.ontheroad.extension.decodePolyline
import com.sergey.ontheroad.view.fragments.MapsFragment.Companion.DELAY_TIME
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(val getRoadFlow: Flow<AddressDomain, Routes, GetRoadUseCase>
) : BaseViewModel() {

    private var listRoute = ArrayList<LatLng>()
    private var counter = 0
    private lateinit var route: Routes
    private val car = Car("BMW I8", LatLng(49.98865707, 36.22775511))

    val basePosition: MutableLiveData<Car> = object : MutableLiveData<Car>() {
        override fun onActive() {
            super.onActive()
            value = car
        }
    }

    val getData: MutableLiveData<ArrayList<LatLng>> = object : MutableLiveData<ArrayList<LatLng>>() {
        override fun onActive() {
            super.onActive()

            getRoadFlow.run(AddressMapper.transformToDomain(SendAddress(
                    car.position,
                    LatLng(49.9827515, 36.23092224)))
            ) {
                route = it
                listRoute.clear()
                listRoute = decodePolyline(it.routes[0].legs[0].steps)
                value = listRoute
            }
        }
    }

    val moveCarOnTheMap: MutableLiveData<LatLng> = object : MutableLiveData<LatLng>() {
        override fun onActive() {
            super.onActive()
            Observable.interval(DELAY_TIME, TimeUnit.MILLISECONDS)
                    .subscribe({
                        postValue(listRoute[counter])
                        if (counter == listRoute.size - 1) counter = 0 else counter++
                    })
        }
    }

    val drawAddress: MutableLiveData<Address> = object : MutableLiveData<Address>() {
        override fun onActive() {
            super.onActive()
            value = Address(
                    listRoute[0],
                    listRoute[listRoute.size - 1],
                    route.routes[0].legs[0].start_address,
                    route.routes[0].legs[0].end_address)
        }
    }
}