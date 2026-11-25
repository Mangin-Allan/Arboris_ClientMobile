package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.core.storage.TokenManager
import fr.pm1g1.arboris.data.model.arboris.Tokens
import fr.pm1g1.arboris.data.model.auth.AuthResponse
import io.mockk.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var tokenManager: TokenManager
    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var chain: Interceptor.Chain

    @Before
    fun setUp() {
        tokenManager = mockk(relaxed = true)
        authInterceptor = AuthInterceptor(tokenManager)
        chain = mockk()
    }

    @Test
    fun `intercept adds Authorization header when tokens are present`() {
        // Condition normale
        val tokens = Tokens(accessToken = "valid_access_token", refreshToken = "valid_refresh_token", expireIn = System.currentTimeMillis() + 100000)
        every { tokenManager.getTokens() } returns tokens
        
        val request = Request.Builder().url("https://test.com").build()
        every { chain.request() } returns request
        val response = mockk<Response>()
        every { chain.proceed(any()) } returns response

        authInterceptor.intercept(chain)

        val requestSlot = slot<Request>()
        verify { chain.proceed(capture(requestSlot)) }
        assertEquals("Bearer valid_access_token", requestSlot.captured.header("Authorization"))
    }

    @Test
    fun `intercept does not add Authorization header when tokens are null`() {
        // Condition limite : pas de session
        every { tokenManager.getTokens() } returns null
        
        val request = Request.Builder().url("https://test.com").build()
        every { chain.request() } returns request
        val response = mockk<Response>()
        every { chain.proceed(any()) } returns response

        authInterceptor.intercept(chain)

        val requestSlot = slot<Request>()
        verify { chain.proceed(capture(requestSlot)) }
        assertEquals(null, requestSlot.captured.header("Authorization"))
    }

//    @Test
//    fun `intercept proceed even if token refresh fails`() {
//        // Condition d'erreur : token expiré et refresh échoue
//        val expiredTokens = Tokens(accessToken = "expired", refreshToken = "refresh", expireIn = System.currentTimeMillis() - 1000)
//        every { tokenManager.getTokens() } returns expiredTokens
//
//        // Mocking RetrofitClient or internal calls in Interceptor is complex without DI,
//        // but the current implementation handles refresh failure by proceeding anyway.
//
//        val request = Request.Builder().url("https://test.com").build()
//        every { chain.request() } returns request
//        val response = mockk<Response>()
//        every { chain.proceed(any()) } returns response
//
//        authInterceptor.intercept(chain)
//
//        verify { chain.proceed(any()) }
//    }
}
