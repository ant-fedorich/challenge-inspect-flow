package com.antonfedorych.inspectflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity

@Database(
    entities = [
        ItemEntity::class,
        ResponseSetEntity::class,
        ResponseEntity::class,
    ],
    version = 1,
)
abstract class InspectionDatabase : RoomDatabase() {
    abstract fun inspectionDao(): InspectionDAO
}