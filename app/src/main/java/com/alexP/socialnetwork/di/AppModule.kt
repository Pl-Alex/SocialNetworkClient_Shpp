package com.alexP.socialnetwork.di

import com.alexP.socialnetwork.BuildConfig
import com.alexP.socialnetwork.ui.auth.singUp.SingUpViewModel
import com.alexP.socialnetwork.ui.auth.singUpExtended.SingUpExtendedViewModel
import com.alexp.datastore.DataStoreProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<String>(qualifier = named("BASE_URL")) { BuildConfig.BASE_URL }

    viewModel<SingUpViewModel> {
        SingUpViewModel(
            dataStore = DataStoreProvider(androidContext()),
            mainRepository = get()
        )
    }
    viewModel<SingUpExtendedViewModel> {
        SingUpExtendedViewModel(
            dataStore = DataStoreProvider(androidContext()),
            mainRepository = get()
        )
    }
}