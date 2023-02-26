package com.shiro.arturosalcedogagliardi.helpers.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

inline fun <reified T : Any> Any.cast(): T {
    return this as T
}

suspend fun <T> Any.callResult(
    call: suspend () -> Response<T>
): Result<T?> {
    return try {
        withContext(Dispatchers.IO) {
            val response = call()
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(response.code().parseErrorCode())
            }
        }
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}