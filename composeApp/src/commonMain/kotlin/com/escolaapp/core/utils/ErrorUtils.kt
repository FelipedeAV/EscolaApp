package com.escolaapp.core.utils

import com.escolaapp.core.data.remote.gateway.ApiException

fun Throwable.toUserMessage(): String = when (this) {
    is ApiException -> when (statusCode) {
        401 -> "Sessão expirada. Faça login novamente."
        403 -> "Acesso negado."
        404 -> "Recurso não encontrado."
        409 -> "Conflito: este registro já existe."
        422 -> "Dados inválidos. Verifique as informações."
        in 500..599 -> "Erro interno do servidor. Tente novamente mais tarde."
        else -> message ?: "Erro inesperado ($statusCode)"
    }
    is kotlinx.coroutines.TimeoutCancellationException,
    is io.ktor.client.plugins.HttpRequestTimeoutException ->
        "O servidor não respondeu. Verifique sua conexão e tente novamente."
    else -> {
        val name = this::class.simpleName
        when {
            name == "UnknownHostException" || name == "ConnectException" ->
                "Sem conexão com a internet. Verifique sua rede."
            name == "SocketTimeoutException" ->
                "O servidor não respondeu. Tente novamente."
            else -> "Ocorreu um erro inesperado. Tente novamente."
        }
    }
}
