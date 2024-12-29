package com.alexP.socialnetwork.presentation.main.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexp.datastore.DataStoreProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyProfileViewModel(
    private val dataStore: DataStoreProvider,
) : ViewModel() {

    private val _myProfileState = MutableStateFlow(MyProfileState())
    val myProfileState get() = _myProfileState.asStateFlow()

    fun cleanStorage() {
        viewModelScope.launch {
            dataStore.cleanStorage()
        }
    }

    companion object {
        fun createFactory(dataStore: DataStoreProvider): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyProfileViewModel(dataStore)
                }
            }
    }

}