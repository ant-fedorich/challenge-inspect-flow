package com.antonfedorych.inspectflow.data.remote.dto

import com.squareup.moshi.Json

data class ItemDto(
    val id: Int,
    val type: String,
    val title: String? = null,
    val content: String? = null,
    val src: String? = null,
    val items: List<ItemDto>? = null,
    @Json(name = "response_set")
    val responseSet: ResponseSetDto? = null,
)
