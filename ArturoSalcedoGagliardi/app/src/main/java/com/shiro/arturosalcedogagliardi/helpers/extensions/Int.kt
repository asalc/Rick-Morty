package com.shiro.arturosalcedogagliardi.helpers.extensions

import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.helpers.Constants

fun Int.parseErrorCode(): ApiError =
    when (this) {
        Constants.BAD_REQUEST_CODE -> ApiError.BadRequest()
        Constants.UNAUTHORIZED_CODE,
        Constants.FORBIDDEN_CODE -> ApiError.Unauthorized()
        Constants.NOT_FOUND_CODE -> ApiError.NotFound()
        else -> ApiError.Unknown()
    }