package com.sergey.domain.repository

import com.sergey.domain.entity.LatLngs
import com.sergey.domain.entity.Route
import io.reactivex.Single

interface ServerRepository {

//    fun getRouteOfCar(route: Route): Single<List<List<LatLngs>>>
    fun getRouteOfCar(route: Route): Single<String>
}