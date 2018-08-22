package com.sergey.domain.repository

import com.sergey.domain.entity.RouteDomain
import io.reactivex.Single

interface ServerRepository {

    fun getRouteOfCar(route: RouteDomain): Single<String>
}