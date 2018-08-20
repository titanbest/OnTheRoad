package com.sergey.data.mapper

import com.sergey.data.entity.LatLngEntity
import com.sergey.data.entity.RouteEntity
import com.sergey.domain.entity.LatLng
import com.sergey.domain.entity.Route
import com.sergey.domain.mapper.InputMapper
import com.sergey.domain.mapper.OutputMapper

object RouteEntityMapper : OutputMapper<RouteEntity, Route>, InputMapper<Route, RouteEntity> {

    override fun transformToDomain(item: Route): RouteEntity =
            RouteEntity(LatLngEntity(item.startPosition.latitude, item.startPosition.longitude),
                    LatLngEntity(item.endPosition.latitude, item.endPosition.longitude))

    override fun transformFromDomain(item: RouteEntity): Route =
            Route(LatLng(item.startPosition.latitude, item.startPosition.longitude),
                    LatLng(item.endPosition.latitude, item.endPosition.longitude))
}