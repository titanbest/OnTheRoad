package com.sergey.domain.repository

import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.entity.Routes
import io.reactivex.Single

interface ServerRepository {

    fun getRouteOfCar(address: AddressDomain): Single<Routes>
}