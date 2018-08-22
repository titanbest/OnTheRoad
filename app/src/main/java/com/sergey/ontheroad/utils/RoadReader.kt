package com.sergey.ontheroad.utils

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

fun sendAddress(strUrl: String): List<List<HashMap<String, String>>> {
    var backString = ""
    val url = URL(strUrl)
    val urlConnection = url.openConnection() as HttpURLConnection
    urlConnection.connect()

    try {
        urlConnection.inputStream.use { iStream ->
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuilder()

            if (!TextUtils.isEmpty(br.readLine())) {
                sb.append(br.readLine())
            }

            backString = sb.toString()

            br.close()
        }
    } catch (e: Exception) {
        Log.d("Exception", e.toString())
    } finally {
        urlConnection.disconnect()
    }
    return RouteParser().parse(JSONObject(backString))
}

fun GoogleMap.readRound(result: List<List<HashMap<String, String>>>) {
    val lineOptions = PolylineOptions()
    val points = ArrayList<LatLng>()

    for (i in result.indices) {
        val path = result[i]
        for (j in path.indices) {
            points.add(LatLng(
                    java.lang.Double.parseDouble(path[j]["lat"]),
                    java.lang.Double.parseDouble(path[j]["lng"])))
        }
        lineOptions.addAll(points)
        lineOptions.width(6f)
    }

    lineOptions.color(Color.RED)
    this.addPolyline(lineOptions)
}