package com.mvpsales.github.di

import android.content.Context
import androidx.room.Room
import com.mvpsales.github.db.AppDatabase
import com.mvpsales.github.db.UsercentricsDataTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Usercentrics"
        )
        .createFromAsset("database/usercentrics.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideDataTypesDao(appDatabase: AppDatabase): UsercentricsDataTypeDao =
        appDatabase.dataTypesDao
}