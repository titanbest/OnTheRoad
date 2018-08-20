package com.sergey.data.repository

import com.sergey.data.entity.LatLngEntity
import com.sergey.domain.entity.LatLngs
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApi{

    @GET("maps/api/directions/json?")
    fun loadFunds(
            @Query("origin") startPosition: LatLngEntity,
            @Query("destination") endPosition: LatLngEntity,
            @Query("sensor") serialize:Boolean):
            Single<String>
//            Single<List<List<LatLngs>>>



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