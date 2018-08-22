package com.sergey.domain.interactor

import com.sergey.domain.entity.RouteDomain
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetRoadUseCase @Inject constructor(
        private val serverRepo: ServerRepository
) : UseCase<String, RouteDomain>() {

    override fun buildUseCaseObservable(params: RouteDomain): Observable<String> =
            serverRepo.getRouteOfCar(params).toObservable()
}