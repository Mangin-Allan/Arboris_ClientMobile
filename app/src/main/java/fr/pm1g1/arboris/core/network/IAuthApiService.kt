package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.model.auth.AuthResponse
import fr.pm1g1.arboris.data.dto.auth.LoginRequest
import fr.pm1g1.arboris.data.dto.auth.RefreshRequest
import fr.pm1g1.arboris.data.dto.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthApiService {

    @POST("authentication/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @POST("authentication/login")
    suspend fun login(
            @Body request: LoginRequest,
    ): Response<AuthResponse>

    @POST("authentication/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): Response<AuthResponse>
}
