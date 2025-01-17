package com.vodimobile

import android.app.Application
import com.vodimobile.di.androidModule
import android.content.Context
import com.vodimobile.di.carModule
import com.vodimobile.di.crmModule
import com.vodimobile.di.hashModule
import com.vodimobile.di.repositoryModule
import com.vodimobile.di.supabaseModule
import com.vodimobile.di.validatorModule
import com.vodimobile.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    companion object {
        lateinit var INSTANCE: Context
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin {
            androidContext(this@App)
            androidLogger(level = Level.INFO) //Change it on release build to Level.NONE
            modules(
                viewModelModule,
                validatorModule,
                androidModule,
                repositoryModule,
                carModule,
                crmModule,
                supabaseModule,
                hashModule
            )
        }
    }
}
