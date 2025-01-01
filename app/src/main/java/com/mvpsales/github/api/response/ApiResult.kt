package com.mvpsales.github.api.response

sealed class ApiResult<T>() {
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val error: GenericErrorApiResponse) : ApiResult<T>()
    class Loading<T> : ApiResult<T>()
}