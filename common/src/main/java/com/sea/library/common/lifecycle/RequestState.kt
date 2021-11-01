package com.sea.library.common.lifecycle

sealed class RequestState<out T> {

    object Loading : RequestState<Nothing>()

    data class Success<out T>(val data: T ? ) : RequestState<T>()

    data class Error(val code: String ? = null, val error: Throwable ? = null) : RequestState<Nothing>()

}