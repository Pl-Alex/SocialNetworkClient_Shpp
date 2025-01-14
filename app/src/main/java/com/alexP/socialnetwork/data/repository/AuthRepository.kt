package com.alexP.socialnetwork.data.repository

import com.alexp.datastore.DataStoreProvider
import com.alexp.webapi.models.AuthData
import com.alexp.webapi.models.UserData
import com.alexp.webapi.models.state.ResponseState
import com.alexp.webapi.repository.WebApiRepository
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val webApiRepository: WebApiRepository,
    private val dataStore: DataStoreProvider,
) {

    suspend fun createUser(email: String, password: String): ResponseState<AuthData> {
        return when (val result = webApiRepository.createUser(email, password)) {
            is ResponseState.Success -> {
                val data = result.data
                dataStore.saveAuthData(data.accessToken, data.refreshToken, data.user.id)
                ResponseState.Success(data)
            }

            else -> result
        }
    }

    suspend fun authorize(email: String, password: String): ResponseState<AuthData> {
        return when (val result = webApiRepository.authorize(email, password)) {
            is ResponseState.Success -> {
                val data = result.data
                dataStore.saveAuthData(data.accessToken, data.refreshToken, data.user.id)
                ResponseState.Success(data)
            }

            else -> result
        }
    }

    suspend fun editUser(username: String, phone: String): ResponseState<UserData> {
        val userId = dataStore.getUserId().first()
        val accessToken = dataStore.getAccessToken().first()
        return when (val result = webApiRepository.editUser(username, phone, userId, accessToken)) {
            is ResponseState.Success -> {
                val data = result.data
                ResponseState.Success(data)
            }

            else -> result
        }
    }

}
