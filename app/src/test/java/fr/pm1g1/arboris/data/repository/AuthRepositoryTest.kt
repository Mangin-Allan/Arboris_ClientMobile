package fr.pm1g1.arboris.data.repository

import fr.pm1g1.arboris.core.network.IAuthApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.model.auth.AuthResponse
import fr.pm1g1.arboris.data.dto.auth.LoginRequest
import fr.pm1g1.arboris.data.dto.auth.RefreshRequest
import fr.pm1g1.arboris.data.dto.auth.RegisterRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private lateinit var apiService: IAuthApiService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        apiService = mockk()
        authRepository = AuthRepository(apiService)
    }


    @Test
    fun login_returns_Success_when_API_call_is_successful() = runTest {
        val expectedResponse = AuthResponse("accessToken", 3600L, "refreshToken")
        coEvery { apiService.login(LoginRequest("test@example.com", "password123")) } returns
            Response.success(expectedResponse)

        val result = authRepository.login("test@example.com", "password123")

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResponse, (result as NetworkResult.Success).data)
        coVerify(exactly = 1) { apiService.login(LoginRequest("test@example.com", "password123")) }
    }

    @Test
    fun login_returns_Error_when_API_returns_401() = runTest {
        coEvery { apiService.login(LoginRequest("test@example.com", "wrongpass")) } returns
            Response.error(401, "Unauthorized".toResponseBody())

        val result = authRepository.login("test@example.com", "wrongpass")

        assertTrue(result is NetworkResult.Error)
        assertEquals(401, (result as NetworkResult.Error).code)
    }

    @Test
    fun login_returns_Error_when_API_returns_500() = runTest {
        coEvery { apiService.login(LoginRequest("test@example.com", "password123")) } returns
            Response.error(500, "Internal Server Error".toResponseBody())

        val result = authRepository.login("test@example.com", "password123")

        assertTrue(result is NetworkResult.Error)
        assertEquals(500, (result as NetworkResult.Error).code)
    }



    @Test
    fun register_returns_Success_when_API_call_is_successful() = runTest {
        coEvery { apiService.register(RegisterRequest("new@example.com", "Passw0rd!")) } returns
            Response.success(Unit)

        val result = authRepository.register("new@example.com", "Passw0rd!")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { apiService.register(RegisterRequest("new@example.com", "Passw0rd!")) }
    }

    @Test
    fun register_returns_Error_when_email_already_taken_400() = runTest {
        coEvery { apiService.register(RegisterRequest("taken@example.com", "Passw0rd!")) } returns
            Response.error(400, "Bad Request".toResponseBody())

        val result = authRepository.register("taken@example.com", "Passw0rd!")

        assertTrue(result is NetworkResult.Error)
        assertEquals(400, (result as NetworkResult.Error).code)
    }



    @Test
    fun refreshToken_returns_Success_with_new_tokens() = runTest {
        val newTokens = AuthResponse("newAccessToken", 3600L, "newRefreshToken")
        coEvery { apiService.refreshToken(RefreshRequest("oldRefreshToken")) } returns
            Response.success(newTokens)

        val result = authRepository.refreshToken("oldRefreshToken")

        assertTrue(result is NetworkResult.Success)
        assertEquals("newAccessToken", (result as NetworkResult.Success).data.accessToken)
        assertEquals("newRefreshToken", result.data.refreshToken)
        coVerify(exactly = 1) { apiService.refreshToken(RefreshRequest("oldRefreshToken")) }
    }

    @Test
    fun refreshToken_returns_Error_when_token_expired_401() = runTest {
        coEvery { apiService.refreshToken(RefreshRequest("expiredToken")) } returns
            Response.error(401, "Unauthorized".toResponseBody())

        val result = authRepository.refreshToken("expiredToken")

        assertTrue(result is NetworkResult.Error)
        assertEquals(401, (result as NetworkResult.Error).code)
    }
}
