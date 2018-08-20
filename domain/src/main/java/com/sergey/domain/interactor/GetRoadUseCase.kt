package com.sergey.domain.interactor

import com.sergey.domain.entity.LatLngs
import com.sergey.domain.entity.Route
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Observable
import javax.inject.Inject

//class GetRoadUseCase @Inject constructor(private val serverRepo: ServerRepository
//) :
//        UseCase<String, Route>() {
////        UseCase<List<List<LatLngs>>, Route>() {
//
//    override fun buildUseCaseObservable(params: Route): Observable<String> = serverRepo.getRouteOfCar(params).toObservable()
////    override fun buildUseCaseObservable(params: Route): Observable<List<List<LatLngs>>> = serverRepo.getRouteOfCar(params).toObservable()
//}