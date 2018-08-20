package com.sergey.domain.entity

data class LatLng(
        val latitude: Double,
        val longitude: Double
)

class LatLngs(list: List<LatLng>):List<LatLng> by list