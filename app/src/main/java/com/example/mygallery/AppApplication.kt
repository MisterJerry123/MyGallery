package com.example.mygallery

import android.app.Application
import com.example.mygallery.core.di.appModule
import com.example.mygallery.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AppApplication)
            modules(
                appModule,
                viewModelModule
            )
        }
    }

}