package fr.pm1g1.arboris.data.repository

import fr.pm1g1.arboris.core.network.IAuthApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.model.auth.AuthResponse
import fr.pm1g1.arboris.data.dto.auth.LoginRequest
import fr.pm1g1.arboris.data.dto.auth.RefreshRequest
import fr.pm1g1.arboris.data.dto.auth.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: IAuthApiService,
) {

    suspend fun login(email: String, password: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful == true && response.body() != null) {
                val authResponse = response.body()!!
                NetworkResult.Success(authResponse)
            } else {
                val errorMessage = response.message()
                NetworkResult.Error(Exception(errorMessage), response.code())
            }

        }
    }

    suspend fun register(email: String, password: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val response = apiService.register(RegisterRequest(email, password))
            if (response.isSuccessful == true) {
                NetworkResult.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: response.message()
                NetworkResult.Error(Exception(errorMessage), response.code())
            }
        }
    }

    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiService.refreshToken(RefreshRequest(refreshToken))
            if (response.isSuccessful == true && response.body() != null) {
                val newAuthResponse = response.body()!!
                NetworkResult.Success(newAuthResponse)
            } else {
                val errorMessage = response.message()
                NetworkResult.Error(Exception(errorMessage), response.code())
            }
        }
    }
}
