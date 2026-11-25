package fr.pm1g1.arboris.core.storage

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import fr.pm1g1.arboris.data.model.auth.AuthResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class TokenManagerTest {

    private lateinit var tokenManager: TokenManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        tokenManager = TokenManager(context)
        tokenManager.clearTokens()
    }

    // ─── saveTokens ───────────────────────────────────────────────────────────

    @Test
    fun saveTokens_stores_accessToken_correctly() {
        tokenManager.saveTokens(AuthResponse("myAccessToken", 3600L, "myRefreshToken"))
        assertEquals("myAccessToken", tokenManager.getAccessToken())
    }

    @Test
    fun saveTokens_stores_refreshToken_correctly() {
        tokenManager.saveTokens(AuthResponse("myAccessToken", 3600L, "myRefreshToken"))
        assertEquals("myRefreshToken", tokenManager.getRefreshToken())
    }

    @Test
    fun saveTokens_stores_expiration_timestamp_correctly() {
        val beforeSave = System.currentTimeMillis()
        tokenManager.saveTokens(AuthResponse("accessToken", 3600L, "refreshToken"))
        val afterSave = System.currentTimeMillis()

        val tokens = tokenManager.getTokens()
        assertNotNull(tokens)
        val expectedMin = beforeSave + 3600L * 1000
        val expectedMax = afterSave + 3600L * 1000
        assertTrue(tokens!!.expireIn!! in expectedMin..expectedMax)
    }

    @Test
    fun saveTokens_overwrites_previously_saved_tokens() {
        tokenManager.saveTokens(AuthResponse("oldAccess", 3600L, "oldRefresh"))
        tokenManager.saveTokens(AuthResponse("newAccess", 7200L, "newRefresh"))

        assertEquals("newAccess", tokenManager.getAccessToken())
        assertEquals("newRefresh", tokenManager.getRefreshToken())
    }


    @Test
    fun getTokens_returns_null_when_no_tokens_saved() {
        assertNull(tokenManager.getTokens())
    }

    @Test
    fun getTokens_returns_Tokens_object_when_tokens_are_saved() {
        tokenManager.saveTokens(AuthResponse("accessToken", 3600L, "refreshToken"))

        val tokens = tokenManager.getTokens()

        assertNotNull(tokens)
        assertEquals("accessToken", tokens!!.accessToken)
        assertEquals("refreshToken", tokens.refreshToken)
    }


    @Test
    fun clearTokens_removes_accessToken() {
        tokenManager.saveTokens(AuthResponse("accessToken", 3600L, "refreshToken"))
        tokenManager.clearTokens()
        assertNull(tokenManager.getAccessToken())
    }

    @Test
    fun clearTokens_removes_refreshToken() {
        tokenManager.saveTokens(AuthResponse("accessToken", 3600L, "refreshToken"))
        tokenManager.clearTokens()
        assertNull(tokenManager.getRefreshToken())
    }

    @Test
    fun clearTokens_makes_getTokens_return_null() {
        tokenManager.saveTokens(AuthResponse("accessToken", 3600L, "refreshToken"))
        tokenManager.clearTokens()
        assertNull(tokenManager.getTokens())
    }


    @Test
    fun getAccessToken_returns_null_when_no_tokens_saved() {
        assertNull(tokenManager.getAccessToken())
    }

    @Test
    fun getRefreshToken_returns_null_when_no_tokens_saved() {
        assertNull(tokenManager.getRefreshToken())
    }
}
