package com.shiro.arturosalcedogagliardi.helpers.extensions

import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

fun Exception.parseException(): ApiError {
    return when (this) {
        is IOException -> ApiError.Network()
        is HttpException -> {
            when (this.code()) {
                HttpURLConnection.HTTP_NOT_FOUND -> ApiError.NotFound()
                HttpURLConnection.HTTP_BAD_REQUEST -> ApiError.BadRequest()
                HttpURLConnection.HTTP_CLIENT_TIMEOUT -> ApiError.Timeout()
                HttpURLConnection.HTTP_INTERNAL_ERROR -> ApiError.Server()
                HttpURLConnection.HTTP_FORBIDDEN -> ApiError.Unauthorized()
                HttpURLConnection.HTTP_UNAVAILABLE -> ApiError.Unavailable()
                else -> ApiError.Unknown()
            }
        }
        else -> ApiError.Unknown()
    }
}