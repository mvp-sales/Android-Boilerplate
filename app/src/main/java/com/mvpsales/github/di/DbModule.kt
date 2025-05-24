package com.mvpsales.github.di

import android.content.Context
import androidx.room.Room
import com.mvpsales.github.db.AppDatabase
import com.mvpsales.github.db.ArticlesDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> {
        provideRoomDatabase(androidContext())
    }
    single<ArticlesDao> {
        provideUserDao(get())
    }
}

private fun provideRoomDatabase(context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "news-db"
).build()

private fun provideUserDao(appDatabase: AppDatabase) = appDatabase.articlesDao()