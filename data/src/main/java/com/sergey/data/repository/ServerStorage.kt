package com.sergey.data.repository

import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.entity.Routes
import com.sergey.domain.repository.ServerRepository
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

class ServerStorage @Inject constructor(
        private val retrofit: Retrofit,
        private val serverApi: ServerApi
) : ServerRepository {

    override fun getRouteOfCar(address: AddressDomain): Single<Routes> = Single.just(address)
            .flatMap {
                val origin = "${it.startPosition.latitude},${it.startPosition.longitude}"
                val destination = "${it.endPosition.latitude},${it.endPosition.longitude}"
                serverApi.loadFunds(origin, destination, false)
                        .onErrorResumeNext { errorParser(it) }
                        .map {
                            return@map it
                        }
            }

    private fun <T> errorParser(throwable: Throwable): Single<T> {
        return com.sergey.data.utils.errorParser(throwable, retrofit)
    }

    private fun <T> errorMessageParser(throwable: Throwable): Single<T> {
        return com.sergey.data.utils.errorMessageParser(throwable, retrofit)
    }
}