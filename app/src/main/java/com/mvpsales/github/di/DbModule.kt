package com.mvpsales.github.di

import android.content.Context
import androidx.room.Room
import com.mvpsales.github.db.AppDatabase
import com.mvpsales.github.db.ArticlesDao
import com.mvpsales.github.db.MIGRATION_1_2
import com.mvpsales.github.db.NewsSourcesDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> {
        provideRoomDatabase(androidContext())
    }
    single<ArticlesDao> {
        provideUserDao(get())
    }
    single<NewsSourcesDao> {
        provideSourcesDao(get())
    }
}

private fun provideRoomDatabase(context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "news-db"
).addMigrations(MIGRATION_1_2).build()

private fun provideUserDao(appDatabase: AppDatabase) = appDatabase.articlesDao()
private fun provideSourcesDao(appDatabase: AppDatabase) = appDatabase.sourcesDao()