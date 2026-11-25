package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.EventDetailDto
import fr.pm1g1.arboris.data.dto.EventSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface IEventService {
    @GET("api/v1/Events")
    suspend fun getAll(
        @QueryMap queryParams: Map<String, String>
    ): Response<PaginResult<EventDetailDto>>

    @GET("api/v1/Events/{id}")
    suspend fun getById(@Path("id") id: Int): Response<EventDetailDto>

    @POST("api/v1/Events")
    suspend fun create(@Body request: EventSendDto): Response<EventDetailDto>

    @PUT("api/v1/Events/{id}")
    suspend fun update(@Path("id") id: Int, @Body request: EventSendDto): Response<EventDetailDto>
}
