package com.antonfedorych.inspectflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionDAO {
    @Query("SELECT * FROM items ORDER BY sort_order")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM response_sets")
    fun getAllResponseSets(): Flow<List<ResponseSetEntity>>

    @Query("SELECT * FROM responses")
    fun getAllResponses(): Flow<List<ResponseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponseSets(sets: List<ResponseSetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<ResponseEntity>)

    //Without Transaction, Flow combine() could emit several times on several Inserts
    @Transaction
    suspend fun insertInspectionTransaction(
        items: List<ItemEntity>,
        sets: List<ResponseSetEntity>,
        responses: List<ResponseEntity>
    ) {
        insertItems(items)
        insertResponseSets(sets)
        insertResponses(responses)
    }
}
