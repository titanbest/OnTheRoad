package com.sergey.data.entity

import com.google.android.gms.maps.model.LatLng

class Address(
        val startPosition: LatLng,
        val endPosition: LatLng,
        val startTitle: String,
        val endTitle: String
)