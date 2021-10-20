package com.simonigh.mojo

import android.app.Application
import com.simonigh.mojo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class MojoTestApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    
        Timber.plant(DebugTree())
    
        startKoin {
            androidLogger()
            androidContext(this@MojoTestApplication)
            modules(appModule)
        }
    }
}