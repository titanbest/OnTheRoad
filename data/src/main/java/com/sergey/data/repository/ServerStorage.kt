package com.sergey.data.repository

import com.sergey.data.mapper.RouteEntityMapper
import com.sergey.domain.entity.LatLngs
import com.sergey.domain.entity.Route
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Single
import javax.inject.Inject

class ServerStorage @Inject constructor(private val serverApi: ServerApi) : ServerRepository {

//    override fun getRouteOfCar(route: Route): Single<List<List<LatLngs>>> {
//        val routeEntity = RouteEntityMapper.transformToDomain(route)
//        return apiEndpoints.loadFunds(routeEntity.startPosition, routeEntity.endPosition, false)
//    }

    override fun getRouteOfCar(route: Route): Single<String> {
        val routeEntity = RouteEntityMapper.transformToDomain(route)
        return serverApi.loadFunds(routeEntity.startPosition, routeEntity.endPosition, false)
    }
}