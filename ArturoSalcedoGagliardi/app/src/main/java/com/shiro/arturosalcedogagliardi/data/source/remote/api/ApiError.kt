package com.shiro.arturosalcedogagliardi.data.source.remote.api

import com.shiro.arturosalcedogagliardi.R

sealed class ApiError(val errorMessage: Int): Exception() {
    class Network(errorMessage: Int = R.string.network_error): ApiError(errorMessage)
    class NotFound(errorMessage: Int = R.string.not_found_error): ApiError(errorMessage)
    class BadRequest(errorMessage: Int = R.string.bad_request_error): ApiError(errorMessage)
    class Timeout(errorMessage: Int = R.string.timeout_error): ApiError(errorMessage)
    class Server(errorMessage: Int = R.string.server_error): ApiError(errorMessage)
    class Unauthorized(errorMessage: Int = R.string.unauthorized_error): ApiError(errorMessage)
    class Unavailable(errorMessage: Int = R.string.unavailable_error): ApiError(errorMessage)
    class Unknown(errorMessage: Int = R.string.unknown_error): ApiError(errorMessage)
}
