package fr.pm1g1.arboris.data.repository

import fr.pm1g1.arboris.core.network.IUserApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
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
class UserRepositoryTest {

    private lateinit var apiService: IUserApiService
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        apiService = mockk()
        userRepository = UserRepository(apiService)
    }

    @Test
    fun fetchCurrentUser_returns_Success_when_API_call_is_successful() = runTest {
        // Arrange
        // We will mock the dependencies needed for UserDetailsResponse 
        // Assuming it's a simple class with id and email or similar based on typical usages
        // If not, we might need a dummy object, we can intercept any issues. Let's mock it using reflections or a data mock.
        val expectedResponse = mockk<UserDetailsResponse>()
        val response = Response.success(expectedResponse)

        coEvery { apiService.getCurrentUser() } returns response

        // Act
        val result = userRepository.fetchCurrentUser()

        // Assert
        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResponse, (result as NetworkResult.Success).data)
        coVerify(exactly = 1) { apiService.getCurrentUser() }
    }

    @Test
    fun fetchCurrentUser_returns_Error_when_API_call_fails() = runTest {
        // Arrange
        val response = Response.error<UserDetailsResponse>(404, "Not Found".toResponseBody())
        coEvery { apiService.getCurrentUser() } returns response

        // Act
        val result = userRepository.fetchCurrentUser()

        // Assert
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
        coVerify(exactly = 1) { apiService.getCurrentUser() }
    }

    @Test
    fun fetchCurrentUser_returns_Error_when_exception_is_thrown() = runTest {
        // Arrange
        val exception = RuntimeException("Network failure")
        coEvery { apiService.getCurrentUser() } throws exception

        // Act
        val result = userRepository.fetchCurrentUser()

        // Assert
        assertTrue(result is NetworkResult.Error)
        assertEquals(exception, (result as NetworkResult.Error).exception)
        coVerify(exactly = 1) { apiService.getCurrentUser() }
    }
}

