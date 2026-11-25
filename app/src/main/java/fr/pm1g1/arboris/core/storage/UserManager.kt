package fr.pm1g1.arboris.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
import fr.pm1g1.arboris.data.model.arboris.Tree
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class UserManager(private val context: Context) {

    private val USER_ID = stringPreferencesKey("userId")
    private val TREES = stringPreferencesKey("trees")

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun saveUser(userDetails: UserDetailsResponse) {
        runBlocking {
            context.userDataStore.edit { preferences ->
                preferences[USER_ID] = userDetails.id
                preferences[TREES] = json.encodeToString(userDetails.trees)
            }
        }
    }

    fun getUserId(): String? {
        return runBlocking {
            context.userDataStore.data.map { preferences ->
                preferences[USER_ID]
            }.first()
        }
    }

    fun getTrees(): List<Tree> {
        val treesJson = runBlocking {
            context.userDataStore.data.map { preferences ->
                preferences[TREES]
            }.first()
        }

        return if (treesJson != null) {
            try {
                json.decodeFromString<List<Tree>>(treesJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun getUserDetails(): UserDetailsResponse? {
        val userId = getUserId()
        val trees = getTrees()

        return if (userId != null) {
            UserDetailsResponse(id = userId, trees = trees)
        } else {
            null
        }
    }

    fun clearUser() {
        runBlocking {
            context.userDataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    fun hasUser(): Boolean {
        return getUserId() != null
    }
}
