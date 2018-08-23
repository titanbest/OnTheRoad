package com.sergey.ontheroad.exception

import android.content.Context
import com.sergey.ontheroad.R
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Factory used to create error messages from an Exception as a condition.
 */
object ErrorMessageFactory {

    /**
     * Creates a String representing of error message.
     *
     * @param context   Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return [String] an error message.
     */
    fun create(context: Context, exception: Throwable?): String = when {
        exception is UnknownHostException -> context.getString(R.string.please_check_your_internet_connection)
        exception is ConnectException -> context.getString(R.string.please_check_your_internet_connection)
        exception is HttpException -> {
            try {
                val json = JSONObject(exception.response().errorBody()?.string())
                if (json.has("data")) {
                    json.getString("data")
                } else {
                    context.getString(R.string.server_error)
                }
            } catch (ex: Exception) {
                context.getString(R.string.server_error)
            }
        }
        exception is IOException -> context.getString(R.string.please_check_your_internet_connection)
        exception != null -> "${context.getString(R.string.error)} ${exception.localizedMessage}"
        else -> context.getString(R.string.please_check_your_internet_connection)
    }
}