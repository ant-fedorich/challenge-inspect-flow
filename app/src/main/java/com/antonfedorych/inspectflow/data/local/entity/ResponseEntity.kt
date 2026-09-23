package com.antonfedorych.inspectflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "responses")
data class ResponseEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "response_set_id")
    val responseSetId: Int,
    val label: String,
    val score: Int?,
)
