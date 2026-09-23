package com.antonfedorych.inspectflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val type: String,
    @ColumnInfo(name = "parent_id")
    val parentId: Int?,
    val title: String?,
    val content: String?,
    val src: String?,
    @ColumnInfo(name = "sort_order")
    val sortOrder: Int
)