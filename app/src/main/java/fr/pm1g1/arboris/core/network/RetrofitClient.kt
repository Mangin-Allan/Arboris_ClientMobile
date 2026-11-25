package fr.pm1g1.arboris.core.network

import android.content.Context
import fr.pm1g1.arboris.BuildConfig
import fr.pm1g1.arboris.core.storage.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    // Client sans AuthInterceptor, pour les endpoints login, register et refresh
    private var authRetrofit: Retrofit? = null

    val instance: Retrofit
        get() =
            retrofit
                ?: throw IllegalStateException(
                    "Le client retrofit n'as pas ete initializer"
                )

    val authInstance: Retrofit
        get() =
            authRetrofit
                ?: throw IllegalStateException(
                    "Le client retrofit n'as pas ete initializer"
                )

    fun initialize(context: Context) {
        if (retrofit == null) {
            val tokenManager = TokenManager(context)
            val authInterceptor = AuthInterceptor(tokenManager)

            val loggingInterceptor =
                HttpLoggingInterceptor { message -> android.util.Log.d("API", message) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            // Client avec AuthInterceptor
            val client =
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build()

            // Client sans AuthInterceptor
            val unauthClient =
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(
                        json.asConverterFactory("application/json".toMediaType())
                    )
                    .build()

            authRetrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(unauthClient)
                    .addConverterFactory(
                        json.asConverterFactory("application/json".toMediaType())
                    )
                    .build()
        }
    }
}
