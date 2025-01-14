package com.alexP.socialnetwork.di

import com.alexP.socialnetwork.BuildConfig
import com.alexP.socialnetwork.data.repository.AuthRepository
import com.alexP.socialnetwork.presentation.auth.logIn.LogInViewModel
import com.alexP.socialnetwork.presentation.auth.signUp.SignUpViewModel
import com.alexP.socialnetwork.presentation.auth.signUpExtended.SignUpExtendedViewModel
import com.alexp.datastore.DataStoreProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<String>(qualifier = named("BASE_URL")) { BuildConfig.BASE_URL }

    viewModel<SignUpViewModel> {
        SignUpViewModel(
            authRepository = get()
        )
    }

    viewModel<SignUpExtendedViewModel> {
        SignUpExtendedViewModel(
            authRepository = get()
        )
    }

    viewModel<LogInViewModel> {
        LogInViewModel(
            authRepository = get()
        )
    }

    factory<AuthRepository> { AuthRepository(get(), DataStoreProvider(androidContext())) }

}