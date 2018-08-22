package com.sergey.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.sergey.data.entity.Route
import com.sergey.domain.entity.LatLngDomain
import com.sergey.domain.entity.RouteDomain
import com.sergey.domain.mapper.InputMapper
import com.sergey.domain.mapper.OutputMapper

object RouteEntityMapper : OutputMapper<RouteDomain, Route>, InputMapper<Route, RouteDomain> {

    override fun transformFromDomain(item: RouteDomain): Route =
            Route(LatLng(
                    item.startPosition.latitude,
                    item.startPosition.longitude),
                    LatLng(
                            item.endPosition.latitude,
                            item.endPosition.longitude))

    override fun transformToDomain(item: Route): RouteDomain =
            RouteDomain(LatLngDomain(
                    item.startPosition.latitude,
                    item.startPosition.longitude),
                    LatLngDomain(
                            item.endPosition.latitude,
                            item.endPosition.longitude))
}