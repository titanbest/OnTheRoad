package com.sergey.ontheroad.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.sergey.data.entity.SendAddress
import com.sergey.data.mapper.AddressMapper
import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.entity.Routes
import com.sergey.domain.interactor.GetRoadUseCase
import com.sergey.ontheroad.extension.Flow
import com.sergey.ontheroad.extension.decodePolyline
import com.sergey.ontheroad.extension.getStreet
import com.sergey.ontheroad.models.ItemMapPosition
import com.sergey.ontheroad.view.fragments.MapsFragment.Companion.DELAY_TIME
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
        val getRoadFlow: Flow<AddressDomain, Routes, GetRoadUseCase>
) : BaseViewModel() {

    private var polyListRoute = ArrayList<LatLng>()
    private var counter = 0
    private lateinit var route: Routes
    private val car = ItemMapPosition("Zonda Pagani", LatLng(49.98865707, 36.22775511))
    private var directPosition: ItemMapPosition? = null
    private var fullDistance: String = ""
    private var fullDuration: String = ""

    val basePosition: MutableLiveData<ItemMapPosition> = object : MutableLiveData<ItemMapPosition>() {
        override fun onActive() {
            super.onActive()
            value = car
        }
    }

    val getFullDistance: MutableLiveData<String> = object : MutableLiveData<String>() {
        override fun onActive() {
            super.onActive()
            value = fullDistance
        }
    }

    val getFullDuration: MutableLiveData<String> = object : MutableLiveData<String>() {
        override fun onActive() {
            super.onActive()
            value = fullDuration
        }
    }

    val getDrawRoad: MutableLiveData<ArrayList<LatLng>> = object : MutableLiveData<ArrayList<LatLng>>() {
        override fun onActive() {
            super.onActive()

            directPosition?.let {
                getRoadFlow.run(AddressMapper.transformToDomain(SendAddress(
                        car.position,
                        directPosition!!.position))
                ) {
                    route = it
                    polyListRoute.clear()
                    polyListRoute = decodePolyline(it.routes[0].legs[0].steps)
                    getFullDistance.value = it.routes[0].legs[0].distance.text
                    getFullDuration.value = it.routes[0].legs[0].duration.text
                    value = polyListRoute
                }
            }
        }
    }

    val moveCarOnTheMap: MutableLiveData<LatLng> = object : MutableLiveData<LatLng>() {
        override fun onActive() {
            super.onActive()
            Observable.interval(DELAY_TIME, TimeUnit.MILLISECONDS)
                    .subscribe({
                        postValue(polyListRoute[counter])
                        if (counter == polyListRoute.size - 1) {
                            counter = 0
                        } else {
                            counter++
                        }
                    })
        }
    }

    val getSearchAddress: MutableLiveData<ItemMapPosition> = object : MutableLiveData<ItemMapPosition>() {
        override fun onActive() {
            super.onActive()
            value = directPosition
        }
    }

    fun setSearchAddress(context: Context, p0: String?) =
        getStreet(context, p0!!)?.let {
            it.subscribe { t1 ->
                directPosition = t1
                getSearchAddress.value = t1
            }
        }
}

//  Харьков, полтавский шлях 134
//  Харьков, проспект Юбилейный, 38
//  Michurina Street, 42, Kirovograd