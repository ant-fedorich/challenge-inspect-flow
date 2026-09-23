package com.antonfedorych.inspectflow.domain.model.state

sealed interface DataResult<out T> {
    data object Loading : DataResult<Nothing>
    data class Success<T>(val value: T) : DataResult<T>
    data class Failure(
        val message: String,
        val cause: Throwable? = null,
    ) : DataResult<Nothing>
}