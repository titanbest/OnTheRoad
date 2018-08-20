package com.sergey.ontheroad.extension

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sergey.ontheroad.utils.GMapUtil

fun GoogleMap.draw(context: Context, location: LatLng, resDrawable: Int, title: String) =
        this.addMarker(MarkerOptions()
                .position(location)
                .title(title)
                .icon(GMapUtil.getBitmapFromVectorDrawable(context, resDrawable)))!!