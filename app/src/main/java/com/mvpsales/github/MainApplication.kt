package com.mvpsales.github

import android.app.Application
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val options = UsercentricsOptions(
            settingsId = BuildConfig.usercentrics_sdk_settingsid,
            defaultLanguage = "en"
        )
        Usercentrics.initialize(this, options)
    }
}