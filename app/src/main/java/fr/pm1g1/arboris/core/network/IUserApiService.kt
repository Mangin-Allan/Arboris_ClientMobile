package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
import retrofit2.Response
import retrofit2.http.GET

interface IUserApiService {

    @GET("api/v1/AppUsers/me")
    suspend fun getCurrentUser(): Response<UserDetailsResponse>
}
