package com.asu1.network

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

sealed interface NetworkError {
    data class Http(val code: Int, val body: String?) : NetworkError
    data class Network(val msg: String?) : NetworkError
    data class Timeout(val msg: String?) : NetworkError
    data class UnknownHost(val msg: String?) : NetworkError
    data class Serialization(val msg: String?) : NetworkError
    data class EmptyBody(val msg: String?) : NetworkError
    data class Unexpected(val msg: String?) : NetworkError
}

fun <T> retrofit2.Response<T>.requireSuccess(): T {
    if (!isSuccessful) throw HttpException(this)
    return body() ?: throw NoSuchElementException("Empty body")
}

inline fun <T> runApi(block: () -> T): Result<T> =
    runCatching(block).fold(
        onSuccess = { Result.success(it) },
        onFailure = { e -> mapToResultFailure(e) }
    )

fun <T> mapToResultFailure(e: Throwable): Result<T> = when (e) {
    is CancellationException -> throw e
    is HttpException -> {
        val err = e.response()?.errorBody()?.string()
        Result.failure(RuntimeException(NetworkError.Http(e.code(), err).toString()))
    }
    is SocketTimeoutException -> Result.failure(RuntimeException(
        NetworkError.Timeout(e.message).toString()))
    is UnknownHostException   -> Result.failure(RuntimeException(
        NetworkError.UnknownHost(e.message).toString()))
    is IOException            -> Result.failure(RuntimeException(
        NetworkError.Network(e.message).toString()))
    is SerializationException -> Result.failure(RuntimeException(
        NetworkError.Serialization(e.message).toString()))
    is NoSuchElementException -> Result.failure(RuntimeException(
        NetworkError.EmptyBody(e.message).toString()))
    else                      -> Result.failure(RuntimeException(
        NetworkError.Unexpected(e.message).toString()))
}
