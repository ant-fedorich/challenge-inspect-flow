package com.antonfedorych.inspectflow.data.repository.inspection

import com.antonfedorych.inspectflow.data.local.InspectionDAO
import com.antonfedorych.inspectflow.data.mapper.toDomainTree
import com.antonfedorych.inspectflow.data.mapper.toEntity
import com.antonfedorych.inspectflow.data.remote.ApiService
import com.antonfedorych.inspectflow.domain.model.Item
import com.antonfedorych.inspectflow.domain.model.state.DataResult
import com.antonfedorych.inspectflow.domain.repository.InspectionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class InspectionRepositoryImpl(
    private val apiService: ApiService,
    private val dao: InspectionDAO,
) : InspectionRepository {
    override fun observeInspection(): Flow<List<Item>> =
        dao.observeItems().map { it.toDomainTree() }

    override fun refreshInspection(): Flow<DataResult<Unit>> = flow {
        emit(DataResult.Loading) //Brief pause, so loading data from network is visible (gist responds instantly).
        delay(1.seconds)
        try {
            val result = apiService.loadInspectionList()
            val entityList = result.toEntity()
            dao.insertInspectionTransaction(
                entityList.items,
                entityList.responseSets,
                entityList.responses
            )
            emit(DataResult.Success(Unit))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(DataResult.Failure(toErrorMessage(e)))
        }
    }
    
    private fun toErrorMessage(e: Exception): String = when (e) {
        is IOException -> "Unable to connect. Check your network and try again."
        is HttpException -> "Unable to load data. Pull to refresh to try again."
        else -> "Something went wrong. Please try again."
    }
}
