package fr.pm1g1.arboris.data.repository

import fr.pm1g1.arboris.core.network.IUserApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse

class UserRepository(
    private val userApiService: IUserApiService,
) {

    suspend fun fetchCurrentUser(): NetworkResult<UserDetailsResponse> {
        return try {
            val response = userApiService.getCurrentUser()
            if (response.isSuccessful == true && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMessage = response.message()
                NetworkResult.Error(Exception(errorMessage), response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
