package com.sergey.data.repository

import com.sergey.domain.entity.RouteDomain
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Single
import javax.inject.Inject

class ServerStorage @Inject constructor(
        private val serverApi: ServerApi
) : ServerRepository {

    override fun getRouteOfCar(route: RouteDomain): Single<String> {
        val origin = "${route.startPosition.latitude},${route.startPosition.longitude}"
        val destination = "${route.endPosition.latitude},${route.endPosition.longitude}"
        return serverApi.loadFunds(origin, destination, false)
    }
}