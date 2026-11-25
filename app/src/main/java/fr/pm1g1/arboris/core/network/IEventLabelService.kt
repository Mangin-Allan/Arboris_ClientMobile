package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.GenericTypeDetailDto
import fr.pm1g1.arboris.data.dto.GenericTypeSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface IEventLabelService {

    @GET("api/v1/Events/types")
    suspend fun getAll(@QueryMap queryParams: Map<String, String>): Response<PaginResult<GenericTypeDetailDto>>

    @GET("api/v1/Events/types/{id}")
    suspend fun getById(@Path("id") id: Int): Response<GenericTypeDetailDto>

    @POST("api/v1/Events/types")
    suspend fun create(@Body request: GenericTypeSendDto): Response<GenericTypeDetailDto>

    @PUT("api/v1/Events/types/{id}")
    suspend fun update(@Path("id") id: Int, @Body request: GenericTypeSendDto): Response<GenericTypeDetailDto>

}
