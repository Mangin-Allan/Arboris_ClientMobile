package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.IEventService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.EventDetailDto
import fr.pm1g1.arboris.data.dto.EventSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams

class EventRepository(
    private val context: Context,
    private val apiService: IEventService
) {
    suspend fun getAll(queryParams: QueryParams): NetworkResult<PaginResult<EventDetailDto>> {
        return try {
            val result = apiService.getAll(queryParams.toMap())
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(context.getString(R.string.fetching_error, result.code(), result.message())),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getEvent(id: Int): NetworkResult<EventDetailDto> {
        return try {
            val result = apiService.getById(id)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(context.getString(R.string.fetching_error, result.code(), result.message())),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun updateEvent(id: Int, event: EventSendDto): NetworkResult<EventDetailDto> {
        return try {
            val result = apiService.update(id, event)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(context.getString(R.string.fetching_error, result.code(), result.message())),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun createEvent(event: EventSendDto): NetworkResult<EventDetailDto> {
        return try {
            val result = apiService.create(event)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(context.getString(R.string.fetching_error, result.code(), result.message())),
                    result.code()
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
