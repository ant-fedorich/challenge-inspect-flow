package com.antonfedorych.inspectflow.data.remote

import com.antonfedorych.inspectflow.data.remote.dto.ItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("lumiform-android-test.json")
    suspend fun loadInspectionList(): List<ItemDto>
}