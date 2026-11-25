package fr.pm1g1.arboris.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import fr.pm1g1.arboris.data.model.arboris.Tokens
import fr.pm1g1.arboris.data.model.auth.AuthResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN = stringPreferencesKey("accessToken")
    private val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    private val EXPIRES_AT = longPreferencesKey("expiresAt")

    fun saveTokens(tokens: AuthResponse) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = tokens.accessToken
                preferences[REFRESH_TOKEN] = tokens.refreshToken
                preferences[EXPIRES_AT] = System.currentTimeMillis() + tokens.expiresIn * 1000L
            }
        }
     }

    fun getAccessToken(): String? {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[ACCESS_TOKEN]
            }.first()
        }
    }

    fun getRefreshToken(): String? {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[REFRESH_TOKEN]
            }.first()
        }
    }

    fun clearTokens() {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    fun getTokens(): Tokens? {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                val access = preferences[ACCESS_TOKEN]
                val refresh = preferences[REFRESH_TOKEN]
                val expiresAt = preferences[EXPIRES_AT] ?: 0L

                if (access != null && refresh != null) {
                    Tokens(access, refresh, expiresAt)
                } else {
                    null
                }
            }.first()
        }
    }
}
