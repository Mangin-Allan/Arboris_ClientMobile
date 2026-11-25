package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.PersonDetailDto
import fr.pm1g1.arboris.data.dto.PersonSendDto
import fr.pm1g1.arboris.data.dto.RelationSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface IPersonService {
    @GET("api/v1/persons")
    suspend fun getAll(
        @QueryMap queryParams: Map<String, String>
    ): Response<PaginResult<PersonCardDto>>

    @GET("api/v1/persons/{id}")
    suspend fun getById(@Path("id") id: Int): Response<PersonDetailDto>

    @POST("api/v1/persons")
    suspend fun create(@Body request: PersonSendDto): Response<PersonDetailDto>

    @PUT("api/v1/persons/{id}")
    suspend fun update(@Path("id") id: Int, @Body request: PersonSendDto): Response<PersonDetailDto>

    @DELETE("api/v1/persons/{id}")
    suspend fun delete(@Path("id") id: Int): Response<Unit>

    @POST("api/v1/persons/{id}/relations")
    suspend fun addRelation(
        @Path("id") id: Int,
        @Body request: RelationSendDto
    ): Response<Unit>

    @HTTP(method = "DELETE", path = "api/v1/persons/{id}/relations/{targetId}", hasBody = true)
    suspend fun removeRelation(
        @Path("id") id: Int,
        @Path("targetId") targetId: Int,
        @Body request: RelationSendDto
    ): Response<Unit>
}
