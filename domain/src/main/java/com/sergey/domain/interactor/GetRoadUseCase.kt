package com.sergey.domain.interactor

import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.entity.Routes
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetRoadUseCase @Inject constructor(
        private val serverRepo: ServerRepository
) : UseCase<Routes, AddressDomain>() {

    override fun buildUseCaseObservable(params: AddressDomain): Observable<Routes> =
            serverRepo.getRouteOfCar(params).toObservable()
}