package com.sergey.ontheroad.extension

import android.util.Log
import java.util.concurrent.ThreadLocalRandom

fun <T : Any> random(min: T, max: T): Any = when (min) {
    is Int -> ThreadLocalRandom.current().nextInt(min as Int, max as Int + 1)
    is Double -> ThreadLocalRandom.current().nextDouble(min as Double, max as Double + 1)
    else -> Log.d("ERROR", "Invalid variable type")
}