package com.sergey.ontheroad.view.fragments

import com.google.android.gms.maps.model.LatLng

interface IMapsFragment {
    fun setBasePosition()
    fun moveCarOnTheRoad(car: LatLng)
}