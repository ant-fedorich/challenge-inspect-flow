package com.antonfedorych.inspectflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.antonfedorych.inspectflow.data.local.entity.ItemEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseEntity
import com.antonfedorych.inspectflow.data.local.entity.ResponseSetEntity
import com.antonfedorych.inspectflow.data.local.relation.ItemWithResponseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionDAO {
    // 📦 One read: items plus each choice's response set and responses.
    @Transaction
    @Query("SELECT * FROM items ORDER BY sort_order")
    fun observeItems(): Flow<List<ItemWithResponseSet>>

    @Insert
    suspend fun insertItems(items: List<ItemEntity>)

    @Insert
    suspend fun insertResponseSets(sets: List<ResponseSetEntity>)

    @Insert
    suspend fun insertResponses(responses: List<ResponseEntity>)

    @Query("DELETE FROM responses")
    suspend fun clearResponses()

    @Query("DELETE FROM response_sets")
    suspend fun clearResponseSets()

    @Query("DELETE FROM items")
    suspend fun clearItems()

    // 🧹 One transaction: drop the previous snapshot, then insert the new one.
    //    Room notifies Flow observers only after commit.
    @Transaction
    suspend fun insertInspectionTransaction(
        items: List<ItemEntity>,
        sets: List<ResponseSetEntity>,
        responses: List<ResponseEntity>
    ) {
        clearResponses()
        clearResponseSets()
        clearItems()
        insertItems(items)
        insertResponseSets(sets)
        insertResponses(responses)
    }
}
