package com.escolaapp.core.utils

import com.escolaapp.core.data.remote.gateway.ApiException
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.i18n.PtStrings

fun Throwable.toUserMessage(strings: AppStrings = PtStrings): String = when (this) {
    is ApiException -> when (statusCode) {
        401 -> strings.errors.sessionExpired
        403 -> strings.errors.accessDenied
        404 -> strings.errors.notFound
        409 -> strings.errors.conflict
        422 -> strings.errors.invalidData
        in 500..599 -> strings.errors.serverError
        else -> message ?: strings.errors.unexpectedErrorWithCode(statusCode)
    }
    is kotlinx.coroutines.TimeoutCancellationException,
    is io.ktor.client.plugins.HttpRequestTimeoutException ->
        strings.errors.serverNotResponding
    else -> {
        val name = this::class.simpleName
        when {
            name == "UnknownHostException" || name == "ConnectException" ->
                strings.errors.noInternet
            name == "SocketTimeoutException" ->
                strings.errors.timeout
            else -> strings.errors.unexpectedError
        }
    }
}
