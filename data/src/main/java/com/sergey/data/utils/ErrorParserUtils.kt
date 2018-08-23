package com.sergey.data.utils

import com.sergey.data.entity.ErrorsEntity
import com.sergey.data.entity.ErrorsMessageEntity
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Retrofit

fun <T> errorParser(throwable: Throwable, retrofit: Retrofit): Single<T> {
    if (throwable is HttpException) {
        val errorConverter = retrofit.responseBodyConverter<ErrorsEntity>(
                ErrorsEntity::class.java, arrayOfNulls(0))
        // Convert the error body into our Error type.
        try {
            val error = errorConverter
                    .convert(throwable.response().errorBody()!!)
            // Here you have two options, one is report this pojo back as error (onError() will be called),

            var errorMessage = ""
            error.errors.forEach { list ->
                list.value.forEach {
                    errorMessage += "$it "
                }
            }
            return Single.error(Throwable(errorMessage))
        } catch (e2: Exception) {
            return Single.error(throwable)
        }

    }
    // if not the kind we're interested in, then just report the same error to onError()
    return Single.error(throwable)
}


fun <T> errorMessageParser(throwable: Throwable, retrofit: Retrofit): Single<T> {
    if (throwable is HttpException) {

        val errorConverter = retrofit.responseBodyConverter<ErrorsMessageEntity>(
                ErrorsMessageEntity::class.java, arrayOfNulls(0))
        // Convert the error body into our Error type.
        try {
            val error = errorConverter.convert(throwable.response().errorBody()!!)
            // Here you have two options, one is report this pojo back as error (onError() will be called),

            return Single.error(Throwable(error.message))
        } catch (e2: Exception) {
            return Single.error(throwable)
        }

    }
    // if not the kind we're interested in, then just report the same error to onError()
    return Single.error(throwable)
}
