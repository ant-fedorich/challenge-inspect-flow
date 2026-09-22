package com.antonfedorych.inspectflow.domain.model.state

sealed interface DataResult<out T> {
    data class Success<T>(val value: T) : DataResult<T>
    data class Failure(
        val message: String,
        val cause: Throwable? = null,
    ) : DataResult<Nothing>
}