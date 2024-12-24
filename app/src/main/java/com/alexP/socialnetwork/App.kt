package com.alexP.socialnetwork

import android.app.Application
import com.alexP.socialnetwork.di.appModule
import com.alexp.contactsprovider.ContactsProvider
import com.alexp.webapi.di.webApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    val contactService = ContactsProvider()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(listOf(appModule, webApiModule))
        }
    }
}