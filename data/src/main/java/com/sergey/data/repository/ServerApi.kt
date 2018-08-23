package com.sergey.data.repository

import com.sergey.domain.entity.Routes
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://maps.googleapis.com/"

interface ServerApi {

    @GET("maps/api/directions/json?")
    fun loadFunds(
            @Query(value = "origin") position: String,
            @Query(value = "destination") destination: String,
            @Query("sensor") sensor: Boolean): Single<Routes>

}