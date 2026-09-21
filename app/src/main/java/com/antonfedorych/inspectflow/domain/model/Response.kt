package com.antonfedorych.inspectflow.domain.model

data class Response(
    val id: Int,
    val label: String,
    val score: Int?,
)
