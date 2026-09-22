package com.antonfedorych.inspectflow.data.remote.dto

import com.squareup.moshi.Json

data class ResponseSetDto(
    val id: Int,
    @Json(name = "multiple_selection")
    val multipleSelection: Boolean,
    val responses: List<ResponseDto>,
)
