package com.alexp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private const val accessTokenKey = "access_token"
private const val refreshTokenKey = "refresh_token"
private const val userIdKey = "user_id"


private const val DATASTORE_NAME = "user_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

class DataStoreProvider(private val context: Context) {

    private suspend fun <T> writeValue(dataStoreKey: Preferences.Key<T>, value: T) {
        context.dataStore.edit { pref ->
            pref[dataStoreKey] = value
        }
    }

    suspend fun setAccessToken(accessToken: String) {
        writeValue(stringPreferencesKey(accessTokenKey), accessToken)
    }

    suspend fun setRefreshToken(refreshToken: String) {
        writeValue(stringPreferencesKey(refreshTokenKey), refreshToken)
    }

    suspend fun setUserId(userid: String) {
        writeValue(stringPreferencesKey(userIdKey), userid)
    }

    fun getAccessToken(): Flow<String> {
        return readString(stringPreferencesKey(accessTokenKey))
    }

    fun getRefreshToken(): Flow<String> {
        return readString(stringPreferencesKey(refreshTokenKey))
    }

    fun getUserId(): Flow<String> {
        return readString(stringPreferencesKey(userIdKey))
    }

    private fun readString(dataStoreKey: Preferences.Key<String>): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: ""
        }
    }

    suspend fun cleanStorage() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }
}