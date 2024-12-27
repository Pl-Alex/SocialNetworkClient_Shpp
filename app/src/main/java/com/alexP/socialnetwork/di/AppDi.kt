package com.alexP.socialnetwork.di

import com.alexP.socialnetwork.BuildConfig
import com.alexP.socialnetwork.ui.auth.signUp.SignUpViewModel
import com.alexP.socialnetwork.ui.auth.signUpExtended.SignUpExtendedViewModel
import com.alexp.datastore.DataStoreProvider
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<String>(qualifier = named("BASE_URL")) { BuildConfig.BASE_URL }

    viewModel<SignUpViewModel>{
        SignUpViewModel(
            dataStore = DataStoreProvider(get()),
            mainRepository = get()
        )
    }

    viewModel<SignUpExtendedViewModel>{
        SignUpExtendedViewModel(
            dataStore = DataStoreProvider(get()),
            mainRepository = get()
        )
    }

}