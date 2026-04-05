package com.mvpsales.github.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleNewsEntity::class, NewsSourceEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
    abstract fun sourcesDao(): NewsSourcesDao
}