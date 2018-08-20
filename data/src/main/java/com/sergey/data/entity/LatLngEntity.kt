package com.sergey.data.entity

import com.google.gson.annotations.SerializedName

class LatLngEntity(
        @SerializedName("lat") val latitude: Double,
        @SerializedName("lng") val longitude: Double
)