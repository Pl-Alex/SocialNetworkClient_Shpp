package com.alexp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private const val DATASTORE_NAME = "datastore_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

class DataStoreProvider(private val context: Context) {

    private fun <T> readValue(dataStoreKey: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: defaultValue
        }
    }

    suspend fun saveAuthData(accessToken: String, refreshToken: String, userId: Int) {
        context.dataStore.edit { pref ->
            pref[ACCESS_TOKEN_KEY] = accessToken
            pref[REFRESH_TOKEN_KEY] = refreshToken
            pref[USER_ID_KEY] = userId
        }
    }

    fun getAccessToken(): Flow<String> {
        return readValue(ACCESS_TOKEN_KEY, "")
    }

    fun getRefreshToken(): Flow<String> {
        return readValue(REFRESH_TOKEN_KEY, "")
    }

    fun getUserId(): Flow<Int> {
        return readValue(USER_ID_KEY, 0)
    }

    suspend fun cleanDataStore() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }

    companion object{
        private val ACCESS_TOKEN_KEY  = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val USER_ID_KEY  = intPreferencesKey("user_id")
    }
}