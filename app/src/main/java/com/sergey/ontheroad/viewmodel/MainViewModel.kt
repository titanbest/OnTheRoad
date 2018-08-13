package com.sergey.ontheroad.viewmodel

import android.os.Handler
import com.google.android.gms.maps.model.LatLng
import com.sergey.ontheroad.models.Car
import com.sergey.ontheroad.view.fragments.IMapsFragment
import com.sergey.ontheroad.view.fragments.MapsFragment.Companion.DELAY_TIME
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel(){
    private lateinit var activity: IMapsFragment

    fun getMyCar(): Car = Car("BMW I8", LatLng(49.98846416, 36.22633136))

    fun getMoveOnRoute() {
        val positionList = ArrayList<LatLng>()

        positionList.add(LatLng(49.98846416, 36.22633136))
        positionList.add(LatLng(49.98811926, 36.22216858))
        positionList.add(LatLng(49.98780195, 36.21796287))
        positionList.add(LatLng(49.98758121, 36.21504463))
        positionList.add(LatLng(49.98728234, 36.21246622))
        positionList.add(LatLng(49.98609584, 36.21272371))
        positionList.add(LatLng(49.98537832, 36.21650959))
        positionList.add(LatLng(49.98477125, 36.21719624))
        positionList.add(LatLng(49.98554388, 36.21929909))
        positionList.add(LatLng(49.98655104, 36.22207786))
        positionList.add(LatLng(49.98732365, 36.22456695))
        positionList.add(LatLng(49.98755818, 36.22627283))
        positionList.add(LatLng(49.98848253, 36.22653032))

        val handler = Handler()

        Timer().schedule(object : TimerTask() {
            var currentPt = 0
            override fun run() {
                handler.post {
                    if (currentPt < positionList.size) {
                        activity.moveCarOnTheRoad(positionList[currentPt])
                        currentPt++
                        handler.postDelayed(this, DELAY_TIME)
                    } else {
                        currentPt = 0
                    }
                }
            }
        }, DELAY_TIME)
    }

    fun setMapsUi(activity: IMapsFragment) {
        this.activity = activity
    }
}