package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.IEventLabelService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.GenericTypeDetailDto
import fr.pm1g1.arboris.data.dto.GenericTypeSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams

class EventLabelRepository(
    private val context: Context,
    private val apiService: IEventLabelService
) {

    suspend fun getAll(queryParams: QueryParams): NetworkResult<PaginResult<GenericTypeDetailDto>> {
        return try {
            val result = apiService.getAll(queryParams.toMap())
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getEventLabel(id: Int): NetworkResult<GenericTypeDetailDto> {
        return try {
            val result = apiService.getById(id)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun updateEventLabel(id: Int, event: GenericTypeSendDto): NetworkResult<GenericTypeDetailDto> {
        return try {
            val result = apiService.update(id, event)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun createEventLabel(event: GenericTypeSendDto): NetworkResult<GenericTypeDetailDto> {
        return try {
            val result = apiService.create(event)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}