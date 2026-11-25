package fr.pm1g1.arboris.core.network

import android.content.Context
import fr.pm1g1.arboris.core.storage.TokenManager
import fr.pm1g1.arboris.data.dto.auth.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var tokens = tokenManager.getTokens()

        // Si il n'y a pas de token, on continue sans ajouter l'auth
        if (tokens == null) {
            return chain.proceed(originalRequest)
        }

        // Vérifier si le token a expiré et le rafraîchir si nécessaire
        val expireIn = tokens.expireIn
        if (expireIn != null && System.currentTimeMillis() >= expireIn) {
            val authApiService = RetrofitClient.authInstance.create(IAuthApiService::class.java)
            
            val refreshResult = runBlocking { 
                try {
                    val fallbackResponse = authApiService.refreshToken(RefreshRequest(tokens!!.refreshToken!!))
                    if (fallbackResponse.isSuccessful == true && fallbackResponse.body() != null) {
                        tokenManager.saveTokens(fallbackResponse.body()!!)
                        true
                    } else {
                        false
                    }
                } catch (e: Exception) {
                    false
                }
            }

            // Recharger les tokens après le refresh
            if (refreshResult == true) {
                tokens = tokenManager.getTokens()

                if (tokens == null) {
                    return chain.proceed(originalRequest)
                }
            } else {
                // Si le refresh échoue, on continue avec l'ancien token (qui est expiré)
            }
        }

        val authenticatedRequest =
                originalRequest
                        .newBuilder()
                        .header("Authorization", "Bearer ${tokens.accessToken}")
                        .build()

        return chain.proceed(authenticatedRequest)
    }
}
