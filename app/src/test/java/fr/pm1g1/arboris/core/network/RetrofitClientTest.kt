package fr.pm1g1.arboris.core.network

import android.content.Context
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class RetrofitClientTest {

    @Before
    fun resetRetrofitClient() {
        // Étant un Singleton (object), on doit reset via réflexion pour isoler les tests
        val retrofitField: Field = RetrofitClient::class.java.getDeclaredField("retrofit")
        retrofitField.isAccessible = true
        retrofitField.set(null, null)

        val authRetrofitField: Field = RetrofitClient::class.java.getDeclaredField("authRetrofit")
        authRetrofitField.isAccessible = true
        authRetrofitField.set(null, null)
    }

    @Test
    fun `instance throws exception when not initialized`() {
        assertThrows(IllegalStateException::class.java) {
            RetrofitClient.instance
        }
    }

    @Test
    fun `authInstance throws exception when not initialized`() {
        assertThrows(IllegalStateException::class.java) {
            RetrofitClient.authInstance
        }
    }

    @Test
    fun `initialize creates instances correctly`() {
        val mockContext = mockk<Context>(relaxed = true)
        
        RetrofitClient.initialize(mockContext)
        
        assertNotNull(RetrofitClient.instance)
        assertNotNull(RetrofitClient.authInstance)
    }

    @Test
    fun `initialize only runs once`() {
        val mockContext = mockk<Context>(relaxed = true)
        
        RetrofitClient.initialize(mockContext)
        val firstInstance = RetrofitClient.instance
        
        RetrofitClient.initialize(mockContext)
        val secondInstance = RetrofitClient.instance
        
        assert(firstInstance === secondInstance)
    }
}
