package com.sergey.data.repository

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi {

    @GET("maps/api/directions/json?")
    fun loadFunds(
            @Query(value = "origin") position: String,
            @Query(value = "destination") destination: String,
            @Query("sensor") sensor: Boolean): Single<String>

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}

//fun getUrl(origin: LatLng, dest: LatLng): String {
//    val str_origin = "origin=" + origin.latitude + "," + origin.longitude
//    val str_dest = "destination=" + dest.latitude + "," + dest.longitude
//    val sensor = "sensor=false"
//    val parameters = "$str_origin&$str_dest&$sensor"
//    val output = "json"
//    return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
//}