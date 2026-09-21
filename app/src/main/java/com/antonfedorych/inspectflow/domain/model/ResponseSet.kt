package com.antonfedorych.inspectflow.domain.model

data class ResponseSet(
    val id: Int,
    val multipleSelection: Boolean,
    val responses: List<Response>,
)
