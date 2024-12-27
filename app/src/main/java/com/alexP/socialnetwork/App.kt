package com.alexP.socialnetwork

import android.app.Application
import com.alexP.socialnetwork.di.appModule
import com.alexp.contactsprovider.ContactsProvider
import com.alexp.webapi.di.webApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    val contactService = ContactsProvider()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, webApiModule))

        }
    }
}