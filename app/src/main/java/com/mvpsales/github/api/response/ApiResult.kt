package com.mvpsales.github.api.response

sealed class ApiResult<T>(
    val data: T? = null,
    val error: GenericErrorApiResponse? = null
) {
    class Success<T>(response: T) : ApiResult<T>(data = response)
    class Error<T>(error: GenericErrorApiResponse) : ApiResult<T>(error = error)
    class Loading<T> : ApiResult<T>()
}