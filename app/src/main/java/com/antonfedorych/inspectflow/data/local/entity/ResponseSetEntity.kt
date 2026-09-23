package com.antonfedorych.inspectflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "response_sets")
data class ResponseSetEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "item_id")
    val itemId: Int,
    @ColumnInfo(name = "multiple_selection")
    val multipleSelection: Boolean,
)
