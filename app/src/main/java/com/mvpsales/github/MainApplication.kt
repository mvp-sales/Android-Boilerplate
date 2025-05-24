package com.mvpsales.github

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.mvpsales.github.di.commonModule
import com.mvpsales.github.di.dbModule
import com.mvpsales.github.di.networkModule
import com.mvpsales.github.di.uiModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(commonModule, dbModule, networkModule, uiModule)
        }
    }
}