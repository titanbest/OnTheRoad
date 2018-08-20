package com.sergey.data.entity

import com.google.gson.annotations.SerializedName

class RouteEntity(
        @SerializedName("origin") val startPosition: LatLngEntity,
        @SerializedName("destination") val endPosition: LatLngEntity
)